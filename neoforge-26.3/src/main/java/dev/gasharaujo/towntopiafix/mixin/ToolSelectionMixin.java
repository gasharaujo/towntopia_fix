package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.villagers.tools.ToolSelectionHelper;
import dev.gasharaujo.towntopiafix.CompatibleTools;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ToolSelectionHelper.class, remap = false)
abstract class ToolSelectionMixin {
    @Inject(method = "rank", at = @At("RETURN"), cancellable = true)
    private static void rankModdedTools(Item item, CallbackInfoReturnable<Integer> result) {
        if (result.getReturnValue() == 0 && CompatibleTools.isModded(item)) {
            var holder = item.builtInRegistryHolder();
            if (holder.is(ItemTags.AXES) || holder.is(ItemTags.HOES) || holder.is(ItemTags.PICKAXES)
                || holder.is(ItemTags.SHOVELS) || holder.is(ItemTags.SWORDS)) {
                result.setReturnValue(CompatibleTools.moddedRank(item));
            }
        }
    }
}
