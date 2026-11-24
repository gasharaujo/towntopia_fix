package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.villagers.miner.MinerBalance;
import dev.gasharaujo.towntopiafix.CompatibleTools;
import net.minecraft.world.item.Item;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MinerBalance.class, remap = false)
abstract class MinerBalanceMixin {
    @Inject(method = "toolSpeed", at = @At("RETURN"), cancellable = true)
    private static void useModdedPickaxeSpeed(Item item, CallbackInfoReturnable<Float> result) {
        var tool = item.getDefaultInstance().get(DataComponents.TOOL);
        if (CompatibleTools.isModded(item) && tool != null) {
            result.setReturnValue(Math.clamp(tool.getMiningSpeed(Blocks.STONE.defaultBlockState()), 2.0F, 30.0F));
        }
    }

    @Inject(method = "baseSwings", at = @At("RETURN"), cancellable = true)
    private static void useModdedPickaxeSwings(Item item, CallbackInfoReturnable<Integer> result) {
        if (CompatibleTools.isModded(item)) {
            int rank = CompatibleTools.moddedRank(item);
            result.setReturnValue(rank == 2 ? 1 : Math.min(5, 7 - rank));
        }
    }
}
