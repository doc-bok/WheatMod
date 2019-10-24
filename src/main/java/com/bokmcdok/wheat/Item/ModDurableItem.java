package com.bokmcdok.wheat.Item;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModDurableItem extends Item {

    public ModDurableItem(ItemGroup itemGroup, int maxDamage, String registryName) {
        super(new Item.Properties().group(itemGroup).maxDamage(maxDamage).setNoRepair());
        setRegistryName(WheatMod.MOD_ID, registryName);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack container = itemStack.copy();
        container.setDamage(itemStack.getDamage() + 1);
        if (container.getDamage() >= container.getMaxDamage()) {
            container.setCount(0);
        }

        return container;
    }
}
