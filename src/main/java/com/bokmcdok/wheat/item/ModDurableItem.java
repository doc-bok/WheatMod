package com.bokmcdok.wheat.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModDurableItem extends Item {

    /**
     * Construction
     * @param properties The item's properties.
     */
    public ModDurableItem(Item.Properties properties) {
        super(properties);
    }

    /**
     * Always returns TRUE. This allows us to return a more damaged item on each use.
     * @param stack The item stack to check.
     * @return Always TRUE.
     */
    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    /**
     * Get the container item. This will return a more damaged item on each use. If the item is destroyed it will return
     * an empty item, unless the item has an actual container that will be returned instead.
     * @param itemStack The item stack to check.
     * @return The item to replace the current item with.
     */
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
