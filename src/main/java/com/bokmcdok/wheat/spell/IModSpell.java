package com.bokmcdok.wheat.spell;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
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

    /**
     * Get the casting time of the spell (defaults to 6 seconds).
     * @return The casting time.
     */
    default int getCastingTime() {
        return 60;
    }

    /**
     * Get the duration of the spell.
     * @return The duration of the spell.
     */
    default int getDuration() {
        return 0;
    }

    /**
     * Get the range of the spell.
     * @return The range of the spell.
     */
    default int getRange() {
        return 0;
    }

    /**
     * Get the sound made when an entity starts casting the spell.
     * @return A sound event.
     */
    default SoundEvent getPrepareSound() {
        return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
    }

    /**
     * Get the sound made when the spell is successfully cast.
     * @return A sound event.
     */
    default SoundEvent getCastSound() {
        return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
    }

    /**
     * Get the sound made when a spell fails to cast.
     * @return A sound event.
     */
    default SoundEvent getFailSound() {
        return SoundEvents.ENTITY_BLAZE_SHOOT;
    }
}
