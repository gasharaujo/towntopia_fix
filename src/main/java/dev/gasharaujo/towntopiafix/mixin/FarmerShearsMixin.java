package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.villagers.farmer.FarmerShearsUtils;
import dev.gasharaujo.towntopiafix.CompatibleTools;
import java.util.Set;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FarmerShearsUtils.class, remap = false)
abstract class FarmerShearsMixin {
    @Redirect(method = {"findUsableShearsSlot", "findBestUsableShearsSlot"}, at = @At(value = "FIELD",
        target = "Lcom/alex/test/village/villagers/farmer/FarmerShearsUtils;ALLOWED_SHEARS:Ljava/util/Set;"), require = 2)
    private static Set<Item> useTaggedShears() {
        return CompatibleTools.extendTag(FarmerShearsUtils.ALLOWED_SHEARS, FarmerShearsUtils.FARMER_SHEARS);
    }
}
