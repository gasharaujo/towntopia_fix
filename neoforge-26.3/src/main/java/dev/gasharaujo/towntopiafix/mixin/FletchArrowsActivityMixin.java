package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.villagers.archer.behavior.FletchArrowsActivity;
import dev.gasharaujo.towntopiafix.CompatibleBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FletchArrowsActivity.class, remap = false)
abstract class FletchArrowsActivityMixin {
    @Redirect(method = "workAnimation", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean recognizeCraftingTable(BlockState state, Block expected) {
        return CompatibleBlocks.matchesStation(state, expected);
    }
}
