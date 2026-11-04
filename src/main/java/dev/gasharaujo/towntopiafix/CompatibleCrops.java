package dev.gasharaujo.towntopiafix;

import com.alex.test.village.villagers.farmer.crops.BlockMatcher;
import com.alex.test.village.villagers.farmer.crops.CropDefinition;
import com.alex.test.village.villagers.farmer.crops.CropHarvestStrategy;
import com.alex.test.village.villagers.farmer.crops.CropMaturity;
import com.alex.test.village.villagers.farmer.crops.CropPlantingStrategy;
import com.alex.test.village.villagers.farmer.crops.HarvestTool;
import com.alex.test.village.villagers.farmer.crops.ItemMatcher;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.CropBlock;

public final class CompatibleCrops {
    private CompatibleCrops() {
    }

    public static Collection<CropDefinition> addFarmlandCrops(Collection<CropDefinition> parsed) {
        var definitions = new ArrayList<>(parsed);
        for (var item : BuiltInRegistries.ITEM) {
            if (!(item instanceof BlockItem seed) || !(seed.getBlock() instanceof CropBlock crop)) {
                continue;
            }
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(crop);
            if (blockId.getNamespace().equals("minecraft") || definitions.stream().anyMatch(
                definition -> definition.matchesCropBlock(crop) || definition.matchesPlantingItem(seed))) {
                continue;
            }
            var id = ResourceLocation.fromNamespaceAndPath(
                "gashs_towntopia_fix", "auto/" + blockId.getNamespace() + "/" + blockId.getPath());
            if (definitions.stream().anyMatch(definition -> definition.id().equals(id))) {
                continue;
            }
            var definition = new CropDefinition(id, new ItemMatcher.Single(seed), new BlockMatcher.Single(crop),
                new CropMaturity.AgeProperty(), new CropPlantingStrategy.Farmland(),
                new CropHarvestStrategy.BreakAndReplant(), HarvestTool.NONE);
            definitions.add(definition);
        }
        LogUtils.getLogger().info("Gash's TownTopia Fix detected {} modded farmland crops",
            definitions.size() - parsed.size());
        return definitions;
    }
}
