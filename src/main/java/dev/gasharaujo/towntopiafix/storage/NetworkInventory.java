package dev.gasharaujo.towntopiafix.storage;

import com.alex.test.village.building.StorageInventory;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

abstract class NetworkInventory<K> implements StorageInventory {
    private final ServerLevel level;
    private final Object key;
    private final Slots<K> slots;

    NetworkInventory(ServerLevel level, Object key, Slots<K> slots) {
        this.level = level;
        this.key = key;
        this.slots = slots;
    }

    @Override
    public final Object key() {
        return key;
    }

    @Override
    public final int size() {
        return isOnline() ? currentSlots().keys.size() : 0;
    }

    @Override
    public final int emptySlots() {
        // TownTopia uses this value to rank deposit destinations. Insertion checks actual capacity.
        return isOnline() ? 1 : 0;
    }

    @Override
    public final ItemStack peek(int slot) {
        if (!isOnline()) {
            return ItemStack.EMPTY;
        }
        var current = currentSlots();
        K item = current.at(slot);
        long amount = item == null ? 0 : current.amounts.getLong(item);
        return amount > 0 ? toStack(item, (int) Math.min(amount, Integer.MAX_VALUE)) : ItemStack.EMPTY;
    }

    @Override
    public final ItemStack take(int slot, int amount) {
        if (amount <= 0 || !isOnline()) {
            return ItemStack.EMPTY;
        }
        var current = currentSlots();
        K item = current.at(slot);
        if (item == null || current.amounts.getLong(item) <= 0) {
            return ItemStack.EMPTY;
        }
        int extracted = (int) extract(item, amount);
        current.record(item, -extracted);
        return extracted > 0 ? toStack(item, extracted) : ItemStack.EMPTY;
    }

    @Override
    public final ItemStack insert(ItemStack stack, boolean simulate) {
        if (stack.isEmpty() || !isOnline()) {
            return stack;
        }
        K item = fromStack(stack);
        int inserted = (int) insert(item, stack.getCount(), simulate);
        if (inserted == 0) {
            return stack;
        }
        if (!simulate && slots.tick == level.getGameTime()) {
            slots.record(item, inserted);
        }
        return stack.copyWithCount(stack.getCount() - inserted);
    }

    private Slots<K> currentSlots() {
        slots.refresh(level.getGameTime(), this::readItems);
        return slots;
    }

    protected abstract boolean isOnline();

    protected abstract Object2LongMap<K> readItems();

    protected abstract K fromStack(ItemStack stack);

    protected abstract ItemStack toStack(K item, int amount);

    protected abstract long extract(K item, int amount);

    protected abstract long insert(K item, int amount, boolean simulate);

    static final class Slots<K> {
        private final List<K> keys = new ArrayList<>();
        private Object2LongMap<K> amounts = new Object2LongOpenHashMap<>();
        private long tick = Long.MIN_VALUE;

        private K at(int slot) {
            return slot >= 0 && slot < keys.size() ? keys.get(slot) : null;
        }

        private void refresh(long gameTime, Supplier<Object2LongMap<K>> readItems) {
            if (tick == gameTime) {
                return;
            }
            amounts = readItems.get();
            var unassigned = new ObjectOpenHashSet<>(amounts.keySet());
            // Villagers can refer to a tool slot again after choosing it.
            keys.replaceAll(item -> item != null && unassigned.remove(item) ? item : null);
            var added = unassigned.iterator();
            for (int slot = 0; slot < keys.size() && added.hasNext(); slot++) {
                if (keys.get(slot) == null) {
                    keys.set(slot, added.next());
                }
            }
            while (added.hasNext()) {
                keys.add(added.next());
            }
            while (!keys.isEmpty() && keys.getLast() == null) {
                keys.removeLast();
            }
            tick = gameTime;
        }

        private void record(K item, int change) {
            long previous = amounts.getLong(item);
            if (!amounts.containsKey(item)) {
                keys.add(item);
            }
            amounts.put(item, Math.max(0L, previous + change));
        }
    }
}
