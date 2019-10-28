package com.bokmcdok.wheat.item;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ModItem extends Item {

    public ModItem(ItemGroup itemGroup, String registryName) {
        super(new Item.Properties().group(itemGroup));
        setRegistryName(WheatMod.MOD_ID, registryName);
    }
}
