package dev.gasharaujo.towntopiafix;

import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public final class RoomFurniture {
    private static final TagKey<Block> FURNITURE = TagKey.create(
        Registries.BLOCK, Identifier.parse("gashs_towntopia_fix:room_furniture"));

    private RoomFurniture() {
    }

    public static Set<BlockPos> besideFloor(Level level, Set<BlockPos> floor) {
        Set<BlockPos> furniture = new LinkedHashSet<>();
        for (BlockPos tile : floor) {
            addFurniture(level, tile, furniture);
            addFurniture(level, tile.above(), furniture);
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos candidate = tile.relative(direction);
                BlockPos behind = candidate.relative(direction);
                if (floor.contains(behind) || backsFurniture(level, behind)) {
                    addFurniture(level, candidate, furniture);
                    addFurniture(level, candidate.above(), furniture);
                }
            }
        }
        return furniture;
    }

    private static boolean backsFurniture(Level level, BlockPos pos) {
        if (!level.isLoaded(pos)) {
            return false;
        }
        var state = level.getBlockState(pos);
        return state.is(FURNITURE) || state.isCollisionShapeFullBlock(level, pos);
    }

    private static void addFurniture(Level level, BlockPos pos, Set<BlockPos> furniture) {
        if (level.isLoaded(pos) && level.getBlockState(pos).is(FURNITURE)) {
            furniture.add(pos);
        }
    }
}
