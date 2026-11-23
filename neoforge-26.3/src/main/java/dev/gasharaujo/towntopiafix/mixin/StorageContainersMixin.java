package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.building.StorageContainers;
import com.alex.test.village.building.StorageInventory;
import dev.gasharaujo.towntopiafix.CompatibleBlocks;
import dev.gasharaujo.towntopiafix.storage.NetworkStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StorageContainers.class, remap = false)
abstract class StorageContainersMixin {
    @Inject(method = "isStorageBlock(Lnet/minecraft/world/level/block/Block;)Z", at = @At("RETURN"), cancellable = true)
    private static void recognizeCompatibleStorage(Block block, CallbackInfoReturnable<Boolean> result) {
        if (!result.getReturnValue()
            && (CompatibleBlocks.isTaggedStorageItem(block) || NetworkStorage.isInterfaceBlock(block))) {
            result.setReturnValue(true);
        }
    }

    @Inject(method = "inventory", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"),
        cancellable = true)
    private static void accessStorageNetwork(Level level, BlockPos pos, CallbackInfoReturnable<StorageInventory> result) {
        var inventory = NetworkStorage.at(level, pos);
        if (inventory != null) {
            result.setReturnValue(inventory);
        }
    }
}
