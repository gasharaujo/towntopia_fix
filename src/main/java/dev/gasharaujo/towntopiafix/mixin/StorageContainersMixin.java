package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.building.StorageContainers;
import dev.gasharaujo.towntopiafix.CompatibleBlocks;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StorageContainers.class, remap = false)
abstract class StorageContainersMixin {
    @Inject(method = "isStorageBlock(Lnet/minecraft/world/level/block/Block;)Z", at = @At("RETURN"), cancellable = true)
    private static void recognizeStorageItemTag(Block block, CallbackInfoReturnable<Boolean> result) {
        if (!result.getReturnValue() && CompatibleBlocks.isTaggedStorageItem(block)) {
            result.setReturnValue(true);
        }
    }
}
