package com.bokmcdok.wheat.data;

import com.google.gson.JsonObject;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public class ModEffectManager extends ModDataManager<EffectInstance> {
    private static final String EFFECTS_FOLDER = "effects";

    /**
     * Loads all effect JSON files from the effects folder.
     */
    public void loadEffects() {
        loadDataEntries(EFFECTS_FOLDER);
    }

    /**
     * Converts a single JSON file to an effect instance.
     * @param json The JSON data from the file.
     * @return A new instance of the effect.
     */
    protected EffectInstance deserialize(ResourceLocation location, JsonObject json) {
        String effectType = JSONUtils.getString(json, "effect");
        ResourceLocation effectKey = new ResourceLocation(effectType);
        Optional<Effect> effect = Registry.EFFECTS.getValue(effectKey);
        if (effect.isPresent()) {
            int duration = 0;
            int amplifier = 0;
            boolean ambient = false;
            boolean showParticles = true;
            boolean showIcon = true;

            if (JSONUtils.hasField(json, "duration")) {
                duration = JSONUtils.getInt(json, "duration");
            }

            if (JSONUtils.hasField(json, "amplifier")) {
                amplifier = JSONUtils.getInt(json, "amplifier");
            }

            if (JSONUtils.hasField(json, "ambient")) {
                ambient = JSONUtils.getBoolean(json, "ambient");
            }

            if (JSONUtils.hasField(json, "show_particles")) {
                showParticles = JSONUtils.getBoolean(json, "show_particles");
                showIcon = showParticles;
            }

            if (JSONUtils.hasField(json, "show_icon")) {
                showIcon = JSONUtils.getBoolean(json, "show_icon");
            }

            return new EffectInstance(effect.get(), duration, amplifier, ambient, showParticles, showIcon);
        }

        LOGGER.error("Effect could not be loaded");
        return null;
    }
}
