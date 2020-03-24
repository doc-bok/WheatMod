package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.supplier.ModBlockSupplier;
import net.minecraft.block.Block;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;

public class ModCropMutation {
    private LazyValue<Block> mMutation;
    private LazyValue<Block> mRequired;
    private int mWeight;

    /**
     * Construction
     * @param mutation The crop that can be mutated to.
     * @param required The crops required for the mutation.
     * @param weight The likelihood this mutation will be chosen.
     */
    public ModCropMutation(ResourceLocation mutation, ResourceLocation required, int weight) {
        mMutation = new LazyValue<>(new ModBlockSupplier(mutation));
        mRequired = new LazyValue<>(new ModBlockSupplier(required));
        mWeight = weight;
    }

    /**
     * Get the crop to be mutated into.
     * @return The mutated crop block.
     */
    public Block getMutation() {
        return mMutation.getValue();
    }

    /**
     * Get the other crop required for this mutation.
     * @return The other crop block required.
     */
    public Block getRequired() {
        return mRequired.getValue();
    }

    /**
     * Get the chance this mutation is chosen.
     * @return The weight.
     */
    public int getWeight() {
        return mWeight;
    }
}
