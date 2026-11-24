package dev.gasharaujo.towntopiafix;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.EnderChestBlock;

public final class CompatibleBlocks {
    private static final TagKey<Block> CRAFTING_TABLES = TagKey.create(
        Registries.BLOCK, ResourceLocation.parse("gashs_towntopia_fix:crafting_tables"));
    private static final TagKey<Item> ITEM_CRAFTING_TABLES = commonItemTag("crafting_tables");
    private static final TagKey<Item> ITEM_WORKBENCHES = commonItemTag("workbenches");
    private static final TagKey<Item> ITEM_CHESTS = commonItemTag("chests");
    private static final TagKey<Item> ITEM_BARRELS = commonItemTag("barrels");
    private static final TagKey<Item> ITEM_ENDER_CHESTS = commonItemTag("chests/ender");

    private CompatibleBlocks() {
    }

    public static boolean isCraftingTable(Block block) {
        if (block instanceof CraftingTableBlock || block.builtInRegistryHolder().is(CRAFTING_TABLES)) {
            return true;
        }
        var item = block.asItem().builtInRegistryHolder();
        return item.is(ITEM_CRAFTING_TABLES) || item.is(ITEM_WORKBENCHES);
    }

    public static boolean isTaggedStorageItem(Block block) {
        var item = block.asItem().builtInRegistryHolder();
        return !(block instanceof EnderChestBlock) && !item.is(ITEM_ENDER_CHESTS)
            && (item.is(ITEM_CHESTS) || item.is(ITEM_BARRELS));
    }

    private static TagKey<Item> commonItemTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }
}
