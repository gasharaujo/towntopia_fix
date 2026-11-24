package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.NavNode;
import dev.gasharaujo.towntopiafix.CompatibleBlocks;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = NavNode.class, remap = false)
abstract class NavNodeMixin {
    @Shadow @Final public Block surfaceBlock;
    @Shadow @Final public boolean waterDrop;

    @Inject(method = "isAvoidedSurface", at = @At("RETURN"), cancellable = true)
    private void avoidModdedTables(CallbackInfoReturnable<Boolean> result) {
        if (!result.getReturnValue() && !waterDrop && surfaceBlock != null && CompatibleBlocks.isCraftingTable(surfaceBlock)) {
            result.setReturnValue(true);
        }
    }
}
