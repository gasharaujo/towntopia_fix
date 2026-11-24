package dev.gasharaujo.towntopiafix;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class StorageConfig {
    static final ModConfigSpec SPEC;
    private static final ModConfigSpec.BooleanValue REFINED_STORAGE;
    private static final ModConfigSpec.BooleanValue APPLIED_ENERGISTICS;

    static {
        var builder = new ModConfigSpec.Builder();
        builder.push("storage");
        REFINED_STORAGE = builder
            .comment("Let TownTopia villagers access the item network of an active Refined Storage Interface.")
            .define("refinedStorage", false);
        APPLIED_ENERGISTICS = builder
            .comment("Let TownTopia villagers access the item network of an active ME Interface.")
            .define("appliedEnergistics", false);
        builder.pop();
        SPEC = builder.build();
    }

    private StorageConfig() {
    }

    public static boolean refinedStorageEnabled() {
        return SPEC.isLoaded() && REFINED_STORAGE.get();
    }

    public static boolean appliedEnergisticsEnabled() {
        return SPEC.isLoaded() && APPLIED_ENERGISTICS.get();
    }
}
