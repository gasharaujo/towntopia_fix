package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.building.KitchenBuilding;
import com.alex.test.village.building.StorageBuilding;
import dev.gasharaujo.towntopiafix.CompatibleBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {KitchenBuilding.class, StorageBuilding.class}, remap = false)
abstract class BuildingCraftingMixin {
    @Redirect(method = "scanFunctionalBlocks", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean recognizeCraftingTable(BlockState state, Block expected) {
        return CompatibleBlocks.matchesStation(state, expected);
    }
}
