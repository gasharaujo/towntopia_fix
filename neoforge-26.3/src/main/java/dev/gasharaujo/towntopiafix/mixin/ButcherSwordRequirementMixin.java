package dev.gasharaujo.towntopiafix.mixin;

import dev.gasharaujo.towntopiafix.CompatibleTools;
import java.util.Set;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.alex.test.village.villagers.butcher.ButcherSword$1", remap = false)
abstract class ButcherSwordRequirementMixin {
    @Inject(method = "allowedTools", at = @At("RETURN"), cancellable = true)
    private void addModdedSwords(CallbackInfoReturnable<Set<Item>> result) {
        result.setReturnValue(CompatibleTools.extendTag(result.getReturnValue(), ItemTags.SWORDS));
    }
}
