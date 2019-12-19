package com.bokmcdok.wheat.block;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ModCropProperties {
    private ResourceLocation mDiseaseCrop = null;
    private ResourceLocation mSeed = null;
    private List<ModCropMutation> mMutations = new ArrayList<>();
    private int mDiseaseResistance = 0;
    private boolean mWild = false;

    public void disease(ResourceLocation disease) {
        mDiseaseCrop = disease;
    }

    public void seed(ResourceLocation seed) {
        mSeed = seed;
    }

    public void addMutation(ResourceLocation mutation, ResourceLocation required, int weight) {
        mMutations.add(new ModCropMutation(mutation, required, weight));
    }

    public void diseaseResistance(int resistance) {
        mDiseaseResistance = resistance;
    }

    public void wild(boolean value) { mWild = value; }

    public ResourceLocation getDiseaseCrop() {
        return mDiseaseCrop;
    }

    public ResourceLocation getSeed() {
        return mSeed;
    }

    public List<ModCropMutation> getMutations() {
        return mMutations;
    }

    public int getDiseaseResistance() {
        return (int)Math.pow(2, mDiseaseResistance);
    }

    public boolean getWild() {
        return mWild;
    }
}
