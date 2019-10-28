package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.Block;
import net.minecraft.block.HayBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class BaleBlock extends HayBlock {
    public BaleBlock(MaterialColor color, String registryName) {
        super(Block.Properties.create(Material.ORGANIC, color).hardnessAndResistance(0.5f).sound(SoundType.PLANT));
        setRegistryName(WheatMod.MOD_ID, registryName);
    }
}
