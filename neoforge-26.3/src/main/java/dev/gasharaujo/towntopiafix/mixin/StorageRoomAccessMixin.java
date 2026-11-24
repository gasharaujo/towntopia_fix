package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.building.StorageContainers;
import com.alex.test.village.building.StorageRoomAccess;
import dev.gasharaujo.towntopiafix.storage.NetworkStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = StorageRoomAccess.class, remap = false)
abstract class StorageRoomAccessMixin {
    @Redirect(method = "forContainer", at = @At(value = "INVOKE",
        target = "Lcom/alex/test/village/building/StorageContainers;isStorageBlock(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private static boolean recognizeInterfacePart(BlockState state, ServerLevel level, BlockPos pos) {
        return StorageContainers.isStorageBlock(state) || NetworkStorage.isInterfacePart(level, pos, state.getBlock());
    }
}
