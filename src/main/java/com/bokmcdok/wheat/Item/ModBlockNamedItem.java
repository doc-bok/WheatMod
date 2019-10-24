package com.bokmcdok.wheat.Item;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.Block;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ModBlockNamedItem extends BlockNamedItem {

    public ModBlockNamedItem(Block block, ItemGroup itemGroup, String registryName) {
        super(block, new Item.Properties().group(itemGroup));
        setRegistryName(WheatMod.MOD_ID, registryName);
    }
}
