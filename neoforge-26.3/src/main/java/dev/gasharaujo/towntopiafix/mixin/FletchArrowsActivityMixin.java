package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.brain.Activity;
import com.alex.test.village.brain.CraftingStation;
import com.alex.test.village.villagers.archer.behavior.FletchArrowsActivity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.gasharaujo.towntopiafix.CompatibleBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = FletchArrowsActivity.class, remap = false)
abstract class FletchArrowsActivityMixin {
    @Shadow
    private CraftingStation table;

    @ModifyExpressionValue(method = "workAnimation", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean recognizeCraftingTable(boolean recognized, Activity.Context context) {
        return recognized || CompatibleBlocks.isCraftingTable(context.level().getBlockState(table.table()).getBlock());
    }
}
