package com.bokmcdok.wheat.spell;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IModSpell {

    /**
     * Handle casting the spell.
     * @param world The current world.
     * @param entity The entity casting the spell.
     * @return TRUE if the spell was successfully cast.
     */
    boolean cast(World world, Entity entity);

    /**
     * Check if the spell has a vocal component.
     * @return TRUE if the spell has a vocal component.
     */
    default boolean getHasVocalComponent() {
        return true;
    }

    /**
     * Check if the spell has a somatic component.
     * @return TRUE if the spell has a somatic component.
     */
    default boolean getHasSomaticComponent() {
        return true;
    }

    /**
     * Get the material component of the spell, if any.
     * @return The material component.
     */
    default ItemStack getMaterialComponent() {
        return ItemStack.EMPTY;
    }

    /**
     * Get the focus of the spell, if any.
     * @return The focus item.
     */
    default ItemStack getFocus() {
        return ItemStack.EMPTY;
    }
}
