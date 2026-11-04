package dev.gasharaujo.towntopiafix.mixin;

import com.alex.test.village.villagers.farmer.crops.CropDefinition;
import com.alex.test.village.villagers.farmer.crops.CropDefinitionRegistry;
import dev.gasharaujo.towntopiafix.CompatibleCrops;
import java.util.Collection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = CropDefinitionRegistry.class, remap = false)
abstract class CropDefinitionRegistryMixin {
    @ModifyArg(method = "install", at = @At(value = "INVOKE",
        target = "Ljava/util/List;copyOf(Ljava/util/Collection;)Ljava/util/List;"))
    private static Collection<CropDefinition> includeModdedCrops(Collection<CropDefinition> parsed) {
        return CompatibleCrops.addFarmlandCrops(parsed);
    }
}
