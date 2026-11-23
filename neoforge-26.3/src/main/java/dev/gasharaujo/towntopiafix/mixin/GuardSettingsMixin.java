package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.villagers.guard.settings.GuardSettings;
import dev.gasharaujo.towntopiafix.CompatibleTools;
import java.util.Set;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GuardSettings.class, remap = false)
abstract class GuardSettingsMixin {
    @Inject(method = "getAllowedSwords", at = @At("RETURN"), cancellable = true)
    private void addModdedTools(CallbackInfoReturnable<Set<Item>> result) {
        result.setReturnValue(CompatibleTools.extendTiers(result.getReturnValue(), ItemTags.SWORDS));
    }
}
