package com.bokmcdok.wheat.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public class ModEffectManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String EFFECTS_FOLDER = "effects";
    private Map<String, EffectInstance> mEffects = ImmutableMap.of();

    public EffectInstance getEffect(String effectName) {
        return mEffects.get(effectName);
    }

    public void loadEffects(IResourceManager resourceManager) {
        ModJsonLoader jsonLoader = new ModJsonLoader();
        Map<ResourceLocation, JsonObject> itemResources = jsonLoader.loadJsonResources(resourceManager, EFFECTS_FOLDER);

        mEffects = Maps.newHashMap();

        for(Map.Entry<ResourceLocation, JsonObject> entry : itemResources.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_")) { continue; }

            try {
                EffectInstance effect = deserializeEffect(entry.getValue());
                if (effect == null) {
                    LOGGER.info("Skipping loading effect {} as it's serializer returned null", resourceLocation);
                    continue;
                }

                mEffects.put(resourceLocation.getPath().substring(1), effect);

            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading item {}", resourceLocation, exception);
            }
        }

        LOGGER.info("Loaded {} effect", mEffects.values().size());
    }

    private EffectInstance deserializeEffect(JsonObject json) {
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
