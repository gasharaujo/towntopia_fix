package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.building.KitchenBuilding;
import com.alex.test.village.building.StorageBuilding;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.gasharaujo.towntopiafix.CompatibleBlocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(value = {KitchenBuilding.class, StorageBuilding.class}, remap = false)
abstract class BuildingCraftingMixin {
    // Keep the original block check available for TownKit's stand-in rules.
    @ModifyExpressionValue(method = "scanFunctionalBlocks",
        slice = @Slice(from = @At(value = "FIELD",
            target = "Lnet/minecraft/world/level/block/Blocks;CRAFTING_TABLE:Lnet/minecraft/world/level/block/Block;")),
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z",
            ordinal = 0))
    private boolean recognizeCraftingTable(boolean recognized, @Local BlockState state) {
        return recognized || CompatibleBlocks.isCraftingTable(state.getBlock());
    }
}
