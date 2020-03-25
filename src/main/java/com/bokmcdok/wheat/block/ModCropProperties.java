package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.supplier.ModBlockSupplier;
import com.bokmcdok.wheat.supplier.ModItemSupplier;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ModCropProperties {
    private LazyValue<Block> mDiseaseCrop = null;
    private LazyValue<Item> mSeed = null;
    private List<ModCropMutation> mMutations = new ArrayList<>();
    private int mDiseaseResistance = 0;
    private boolean mWild = false;

    /**
     * This crop can be diseased.
     * @param disease The block to replace the crop with if it becomes diseased.
     */
    public void disease(ResourceLocation disease) {
        mDiseaseCrop = new LazyValue<>(new ModBlockSupplier(disease));
    }

    /**
     * The seed used to grow this crop.
     * @param seed The location of the seed item.
     */
    public void seed(ResourceLocation seed) {
        mSeed = new LazyValue<>(new ModItemSupplier(seed));
    }

    /**
     * Add a potential mutation to the crop.
     * @param mutation The crop that will be mutated into.
     * @param required The other crop required for the mutation.
     * @param weight The weight of this specific mutation.
     */
    public void addMutation(ResourceLocation mutation, ResourceLocation required, int weight) {
        mMutations.add(new ModCropMutation(mutation, required, weight));
    }

    /**
     * The crop's disease resistance.
     * @param resistance
     */
    public void diseaseResistance(int resistance) {
        mDiseaseResistance = resistance;
    }

    /**
     * Whether or not the crop is wild.
     * @param value
     */
    public void wild(boolean value) { mWild = value; }

    /**
     * Get the diseased version of the crop.
     * @return The block to use.
     */
    public Block getDiseaseCrop() {
        return mDiseaseCrop != null ? mDiseaseCrop.getValue() : null;
    }

    /**
     * The seed for this crop.
     * @return The seed item location.
     */
    public Item getSeed() {
        return mSeed.getValue();
    }

    /**
     * Get the mutations for this crop.
     * @return The list of mutations.
     */
    public List<ModCropMutation> getMutations() {
        return mMutations;
    }

    /**
     * Get the crop's disease resistance.
     * @return The crop's resistance to disease.
     */
    public int getDiseaseResistance() {
        return (int)Math.pow(2, mDiseaseResistance);
    }

    /**
     * Is this crop wild?
     * @return TRUE if the crop is wild.
     */
    public boolean getWild() {
        return mWild;
    }
}
