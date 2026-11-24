package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.villagers.farmer.settings.FarmerSettings;
import dev.gasharaujo.towntopiafix.CompatibleTools;
import java.util.Set;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FarmerSettings.class, remap = false)
abstract class FarmerSettingsMixin {
    @Inject(method = "getAllowedHoes", at = @At("RETURN"), cancellable = true)
    private void addModdedHoes(CallbackInfoReturnable<Set<Item>> result) {
        result.setReturnValue(CompatibleTools.extendTiers(result.getReturnValue(), ItemTags.HOES));
    }

    @Inject(method = "getAllowedAxes", at = @At("RETURN"), cancellable = true)
    private void addModdedAxes(CallbackInfoReturnable<Set<Item>> result) {
        result.setReturnValue(CompatibleTools.extendTiers(result.getReturnValue(), ItemTags.AXES));
    }
}
