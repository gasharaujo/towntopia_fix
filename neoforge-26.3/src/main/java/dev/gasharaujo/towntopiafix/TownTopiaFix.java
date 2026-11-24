package dev.gasharaujo.towntopiafix;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod("gashs_towntopia_fix")
public final class TownTopiaFix {
    public TownTopiaFix(ModContainer container) {
        container.registerConfig(ModConfig.Type.SYNCED, StorageConfig.SPEC);
    }
}
