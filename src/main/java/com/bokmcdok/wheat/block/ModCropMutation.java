package com.bokmcdok.wheat.block;

import net.minecraft.util.ResourceLocation;

public class ModCropMutation {
    private ResourceLocation mMutation;
    private ResourceLocation mRequired;
    private int mWeight;

    public ModCropMutation(ResourceLocation mutation) {
        this(mutation, null, 1);
    }

    public ModCropMutation(ResourceLocation mutation, ResourceLocation required) {
        this(mutation, required, 1);
    }

    public ModCropMutation(ResourceLocation mutation, int weight) {
        this(mutation, null, weight);
    }

    public ModCropMutation(ResourceLocation mutation, ResourceLocation required, int weight) {
        mMutation = mutation;
        mRequired = required;
        mWeight = weight;
    }

    public ResourceLocation getMutation() {
        return mMutation;
    }

    public ResourceLocation getRequired() {
        return mRequired;
    }

    public int getWeight() {
        return mWeight;
    }
}
