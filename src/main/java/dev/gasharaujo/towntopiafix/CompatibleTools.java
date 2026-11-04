package dev.gasharaujo.towntopiafix;

import com.alex.test.village.villagers.tools.ToolSelectionHelper;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;

public final class CompatibleTools {
    private CompatibleTools() {
    }

    public static Set<Item> extendTiers(Set<Item> allowed, TagKey<Item> tag) {
        if (allowed.isEmpty()) {
            return allowed;
        }
        var ranks = new boolean[7];
        for (Item item : allowed) {
            ranks[ToolSelectionHelper.rank(item)] = true;
        }
        Set<Item> extended = new LinkedHashSet<>(allowed);
        BuiltInRegistries.ITEM.getTagOrEmpty(tag).forEach(holder -> {
            Item item = holder.value();
            if (isModded(item) && ranks[moddedRank(item)]) {
                extended.add(item);
            }
        });
        return extended;
    }

    public static Set<Item> extendTag(Set<Item> allowed, TagKey<Item> tag) {
        if (allowed.isEmpty()) {
            return allowed;
        }
        Set<Item> extended = new LinkedHashSet<>(allowed);
        BuiltInRegistries.ITEM.getTagOrEmpty(tag).forEach(holder -> extended.add(holder.value()));
        return extended;
    }

    public static boolean isModded(Item item) {
        return !BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("minecraft");
    }

    public static int moddedRank(Item item) {
        Tier tier = item instanceof TieredItem tool ? tool.getTier() : null;
        if (tier == Tiers.GOLD) {
            return 2;
        }
        int durability = tier == null ? item.getDefaultInstance().getMaxDamage() : tier.getUses();
        if (durability >= Tiers.NETHERITE.getUses()) {
            return 6;
        }
        if (durability >= Tiers.DIAMOND.getUses()) {
            return 5;
        }
        if (durability >= Tiers.IRON.getUses()) {
            return 4;
        }
        if (durability >= Tiers.STONE.getUses()) {
            return 3;
        }
        return 1;
    }
}
