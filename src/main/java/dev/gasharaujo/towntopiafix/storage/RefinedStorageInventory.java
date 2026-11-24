package dev.gasharaujo.towntopiafix.storage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.impl.node.iface.InterfaceNetworkNode;
import com.refinedmods.refinedstorage.api.network.node.NetworkNodeActor;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.common.iface.InterfaceBlockEntity;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

final class RefinedStorageInventory extends NetworkInventory<ItemResource> {
    private static final Map<Network, Slots<ItemResource>> NETWORK_SLOTS = new WeakHashMap<>();

    private final InterfaceBlockEntity blockEntity;
    private final InterfaceNetworkNode node;
    private final Network network;
    private final StorageNetworkComponent storage;
    private final NetworkNodeActor actor;

    private RefinedStorageInventory(ServerLevel level, InterfaceBlockEntity blockEntity, InterfaceNetworkNode node) {
        this(level, blockEntity, node, node.getNetwork());
    }

    private RefinedStorageInventory(ServerLevel level, InterfaceBlockEntity blockEntity,
                                    InterfaceNetworkNode node, Network network) {
        super(level, network == null ? blockEntity.getBlockPos() : network,
            network == null ? null : NETWORK_SLOTS.computeIfAbsent(network, unused -> new Slots<>()));
        this.blockEntity = blockEntity;
        this.node = node;
        this.network = network;
        this.storage = network == null ? null : network.getComponent(StorageNetworkComponent.class);
        this.actor = new NetworkNodeActor(node);
    }

    static RefinedStorageInventory at(ServerLevel level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof InterfaceBlockEntity blockEntity) {
            for (var container : blockEntity.getContainerProvider().getContainers()) {
                if (!container.isRemoved() && container.getNode() instanceof InterfaceNetworkNode node) {
                    return new RefinedStorageInventory(level, blockEntity, node);
                }
            }
        }
        return null;
    }

    @Override
    protected boolean isOnline() {
        return storage != null && !blockEntity.isRemoved() && node.isActive() && network == node.getNetwork();
    }

    @Override
    protected Object2LongMap<ItemResource> readItems() {
        var items = new Object2LongOpenHashMap<ItemResource>();
        for (var entry : storage.getAll()) {
            if (entry.resource() instanceof ItemResource item && entry.amount() > 0) {
                items.put(item, entry.amount());
            }
        }
        return items;
    }

    @Override
    protected ItemResource fromStack(ItemStack stack) {
        return ItemResource.ofItemStack(stack);
    }

    @Override
    protected ItemStack toStack(ItemResource item, int amount) {
        return item.toItemStack(amount);
    }

    @Override
    protected long extract(ItemResource item, int amount) {
        return storage.extract(item, amount, Action.EXECUTE, actor);
    }

    @Override
    protected long insert(ItemResource item, int amount, boolean simulate) {
        return storage.insert(item, amount, simulate ? Action.SIMULATE : Action.EXECUTE, actor);
    }
}
