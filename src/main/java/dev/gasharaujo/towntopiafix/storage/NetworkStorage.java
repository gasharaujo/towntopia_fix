package dev.gasharaujo.towntopiafix.storage;

import com.alex.test.village.building.StorageInventory;
import dev.gasharaujo.towntopiafix.StorageConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;

public final class NetworkStorage {
    private static final ResourceLocation RS_INTERFACE = ResourceLocation.parse("refinedstorage:interface");
    private static final ResourceLocation ME_INTERFACE = ResourceLocation.parse("ae2:interface");
    private static final ResourceLocation ME_CABLE_BUS = ResourceLocation.parse("ae2:cable_bus");

    private NetworkStorage() {
    }

    public static boolean isInterfaceBlock(Block block) {
        var id = BuiltInRegistries.BLOCK.getKey(block);
        return (RS_INTERFACE.equals(id) && refinedStorageEnabled())
            || (ME_INTERFACE.equals(id) && appliedEnergisticsEnabled());
    }

    public static boolean isInterfacePart(ServerLevel level, BlockPos pos, Block block) {
        return appliedEnergisticsEnabled() && ME_CABLE_BUS.equals(BuiltInRegistries.BLOCK.getKey(block))
            && level.isLoaded(pos) && AppliedEnergisticsStorage.hasInterfacePart(level.getBlockEntity(pos));
    }

    public static StorageInventory at(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)
            || (!StorageConfig.refinedStorageEnabled() && !StorageConfig.appliedEnergisticsEnabled())
            || !level.isLoaded(pos)) {
            return null;
        }

        var id = BuiltInRegistries.BLOCK.getKey(level.getBlockState(pos).getBlock());
        if (RS_INTERFACE.equals(id) && refinedStorageEnabled()) {
            return RefinedStorageInventory.at(serverLevel, pos);
        }
        if ((ME_INTERFACE.equals(id) || ME_CABLE_BUS.equals(id)) && appliedEnergisticsEnabled()) {
            return AppliedEnergisticsStorage.at(serverLevel, pos);
        }
        return null;
    }

    private static boolean refinedStorageEnabled() {
        return StorageConfig.refinedStorageEnabled() && ModList.get().isLoaded("refinedstorage");
    }

    private static boolean appliedEnergisticsEnabled() {
        return StorageConfig.appliedEnergisticsEnabled() && ModList.get().isLoaded("ae2");
    }
}
