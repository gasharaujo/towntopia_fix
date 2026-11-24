package dev.gasharaujo.towntopiafix.mixin;

import dev.gasharaujo.towntopiafix.RoomFurniture;
import java.util.List;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.alex.test.ui.screen.RoomSchematicView", remap = false)
abstract class RoomSchematicViewMixin {
    @Shadow
    abstract void addFurniture(ClientLevel world, BlockPos pos, int color, String label);

    @Inject(method = "addShell(Lnet/minecraft/client/multiplayer/ClientLevel;Ljava/util/Set;Ljava/util/Set;Ljava/util/List;Lnet/minecraft/core/BlockPos;Ljava/lang/String;)V",
        at = @At("HEAD"))
    private void drawModdedFurniture(ClientLevel world, Set<BlockPos> floor, Set<BlockPos> inside,
                                     List<BlockPos> doors, BlockPos doorTarget, String doorLabel, CallbackInfo info) {
        if (world == null) {
            return;
        }
        for (BlockPos pos : RoomFurniture.besideFloor(world, floor)) {
            if ((floor.contains(pos) || !inside.contains(pos)) && !floor.contains(pos.above())) {
                inside.add(pos);
                addFurniture(world, pos, 0xFF887260, "");
            }
        }
    }
}
