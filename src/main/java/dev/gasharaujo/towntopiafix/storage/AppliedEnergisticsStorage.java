package dev.gasharaujo.towntopiafix.storage;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.IActionSource;
import appeng.api.parts.IPartHost;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.StorageHelper;
import appeng.blockentity.misc.InterfaceBlockEntity;
import appeng.parts.misc.InterfacePart;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

final class AppliedEnergisticsStorage extends NetworkInventory<AEItemKey> {
    private static final Map<IGrid, Slots<AEItemKey>> NETWORK_SLOTS = new WeakHashMap<>();

    private final BlockEntity blockEntity;
    private final IActionHost host;
    private final IGrid grid;
    private final IActionSource source;

    private AppliedEnergisticsStorage(ServerLevel level, BlockEntity blockEntity, IActionHost host, IGrid grid) {
        super(level, grid == null ? blockEntity.getBlockPos() : grid,
            grid == null ? null : NETWORK_SLOTS.computeIfAbsent(grid, unused -> new Slots<>()));
        this.blockEntity = blockEntity;
        this.host = host;
        this.grid = grid;
        this.source = IActionSource.ofMachine(host);
    }

    static AppliedEnergisticsStorage at(ServerLevel level, BlockPos pos) {
        var blockEntity = level.getBlockEntity(pos);
        IActionHost host = blockEntity instanceof InterfaceBlockEntity fullBlock ? fullBlock : findPart(blockEntity);
        return host == null ? null : new AppliedEnergisticsStorage(level, blockEntity, host, activeGrid(host));
    }

    static boolean hasInterfacePart(BlockEntity blockEntity) {
        return findPart(blockEntity) != null;
    }

    private static InterfacePart findPart(BlockEntity blockEntity) {
        if (!(blockEntity instanceof IPartHost cable)) {
            return null;
        }
        InterfacePart offline = null;
        for (Direction side : Direction.values()) {
            if (cable.getPart(side) instanceof InterfacePart part) {
                if (activeGrid(part) != null) {
                    return part;
                }
                offline = part;
            }
        }
        return offline;
    }

    private static IGrid activeGrid(IActionHost host) {
        var node = host.getActionableNode();
        return node != null && node.isActive() ? node.getGrid() : null;
    }

    @Override
    protected boolean isOnline() {
        return grid != null && !blockEntity.isRemoved() && grid == activeGrid(host);
    }

    @Override
    protected Object2LongMap<AEItemKey> readItems() {
        var items = new Object2LongOpenHashMap<AEItemKey>();
        for (var entry : grid.getStorageService().getInventory().getAvailableStacks()) {
            if (entry.getKey() instanceof AEItemKey item && entry.getLongValue() > 0) {
                items.put(item, entry.getLongValue());
            }
        }
        return items;
    }

    @Override
    protected AEItemKey fromStack(ItemStack stack) {
        return AEItemKey.of(stack);
    }

    @Override
    protected ItemStack toStack(AEItemKey item, int amount) {
        return item.toStack(amount);
    }

    @Override
    protected long extract(AEItemKey item, int amount) {
        return StorageHelper.poweredExtraction(grid.getEnergyService(), grid.getStorageService().getInventory(),
            item, amount, source);
    }

    @Override
    protected long insert(AEItemKey item, int amount, boolean simulate) {
        return StorageHelper.poweredInsert(grid.getEnergyService(), grid.getStorageService().getInventory(),
            item, amount, source, simulate ? Actionable.SIMULATE : Actionable.MODULATE);
    }
}
