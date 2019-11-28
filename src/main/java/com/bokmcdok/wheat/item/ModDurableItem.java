package com.bokmcdok.wheat.item;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModDurableItem extends Item {

    public ModDurableItem(Item.Properties properties) {
        super(properties);
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
            if (super.hasContainerItem()) {
                return super.getContainerItem(itemStack);
            }

            container.setCount(0);
        }

        return container;
    }
}
