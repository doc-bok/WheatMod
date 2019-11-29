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

}
