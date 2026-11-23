package dev.gasharaujo.towntopiafix;

import com.alex.test.village.villagers.tools.ToolSelectionHelper;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

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
        var stack = item.getDefaultInstance();
        var repairable = stack.get(DataComponents.REPAIRABLE);
        if (repairable != null && BuiltInRegistries.ITEM.get(ItemTags.GOLD_TOOL_MATERIALS)
            .map(materials -> materials.stream().anyMatch(repairable.items()::contains)).orElse(false)) {
            return 2;
        }
        int durability = stack.getMaxDamage();
        if (durability >= ToolMaterial.NETHERITE.durability()) {
            return 6;
        }
        if (durability >= ToolMaterial.DIAMOND.durability()) {
            return 5;
        }
        if (durability >= ToolMaterial.IRON.durability()) {
            return 4;
        }
        if (durability >= ToolMaterial.STONE.durability()) {
            return 3;
        }
        return 1;
    }
}
