package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.villagers.miner.behavior.MinerActivity;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.gasharaujo.towntopiafix.CompatibleTools;
import java.util.Set;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MinerActivity.class, remap = false)
abstract class MinerActivityMixin {
    @ModifyReturnValue(method = "isOre", at = @At("RETURN"))
    private static boolean recognizeModdedOre(boolean recognized, BlockState state) {
        return recognized || state.is(Tags.Blocks.ORES);
    }

    @Inject(method = "allowedPickaxes", at = @At("RETURN"), cancellable = true)
    private static void addModdedPickaxes(CallbackInfoReturnable<Set<Item>> result) {
        result.setReturnValue(CompatibleTools.extendTiers(result.getReturnValue(), ItemTags.PICKAXES));
    }
}
