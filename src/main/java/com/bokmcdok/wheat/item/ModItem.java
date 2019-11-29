package com.bokmcdok.wheat.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ModItem extends Item {

    public ModItem(Item.Properties properties) {
        super(properties);
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete. This is used for food that is contained in a bowl, for example.
     * @param stack The item stack to check.
     * @param world The current world.
     * @param entityLiving The entity that owns the item.
     * @return The item to replace the current one with.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
        if (isFood() && hasContainerItem(stack)) {
            super.onItemUseFinish(stack, world, entityLiving);
            return getContainerItem(stack);
        }

        return super.onItemUseFinish(stack, world, entityLiving);
    }

    /**
     * Always returns TRUE. This allows us to return a more damaged item on each use.
     * @param stack The item stack to check.
     * @return Always TRUE.
     */
    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return getMaxDamage(stack) > 0 || super.hasContainerItem(stack);
    }

    /**
     * Get the container item. This will return a more damaged item on each use. If the item is destroyed it will return
     * an empty item, unless the item has an actual container that will be returned instead.
     * @param stack The item stack to check.
     * @return The item to replace the current item with.
     */
    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        if (getMaxDamage(stack) > 0) {
            ItemStack container = stack.copy();
            container.setDamage(stack.getDamage() + 1);
            if (container.getDamage() >= container.getMaxDamage()) {
                if (super.hasContainerItem()) {
                    return super.getContainerItem(stack);
                }

                container.setCount(0);
            }

            return container;
        }

        return super.getContainerItem(stack);
    }
}
