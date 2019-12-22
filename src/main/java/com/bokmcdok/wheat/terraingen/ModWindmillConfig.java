package com.bokmcdok.wheat.terraingen;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class ModWindmillConfig implements IFeatureConfig {
    public final int mRarity;

    public ModWindmillConfig(int rarity) {
        mRarity = rarity;
    }

    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        T key = ops.createString("rarity");
        T value = ops.createDouble(mRarity);
        T map = ops.createMap(ImmutableMap.of(key, value));
        return new Dynamic<>(ops, map);
    }

    public static <T> ModWindmillConfig deserialize(Dynamic<T> ops) {
        int rarity = ops.get("rarity").asInt(1);
        return new ModWindmillConfig(rarity);
    }
}
