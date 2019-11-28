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
     * the Item before the action is complete.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
        if (isFood() && hasContainerItem(stack)) {
            super.onItemUseFinish(stack, world, entityLiving);
            return getContainerItem(stack);
        }

        return super.onItemUseFinish(stack, world, entityLiving);
    }

}
