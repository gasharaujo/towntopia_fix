package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.villagers.farmer.FarmerShearsUtils;
import dev.gasharaujo.towntopiafix.CompatibleTools;
import java.util.Set;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.alex.test.village.villagers.farmer.behavior.GetShearsActivity$1", remap = false)
abstract class FarmerShearsRequirementMixin {
    @Inject(method = "allowedTools", at = @At("RETURN"), cancellable = true)
    private void addModdedShears(CallbackInfoReturnable<Set<Item>> result) {
        result.setReturnValue(CompatibleTools.extendTag(result.getReturnValue(), FarmerShearsUtils.FARMER_SHEARS));
    }
}
