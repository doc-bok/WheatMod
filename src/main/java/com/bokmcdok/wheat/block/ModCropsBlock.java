package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

public class ModCropsBlock extends CropsBlock {

    private final Item mSeed;

    /**
     * Construction
     * @param seed  The seed that is used to grow this crop.
     * @param registryName The registry name of this crop.
     */
    public ModCropsBlock(Item seed, String registryName)
    {
        super(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.CROP));
        setRegistryName(WheatMod.MOD_ID, registryName);

        mSeed = seed;
    }

    /**
     * Get the type of seed used to grow this block.
     * @return The seed item used to grow this crop.
     */
    protected IItemProvider getSeedsItem() { return mSeed; }
}
