package com.bokmcdok.wheat.spell;

import com.bokmcdok.wheat.supplier.ModEntityTypeSupplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;


public class ModTruePolymorphOtherSpell extends ModSpell {
    private final LazyValue<EntityType<?>> mPolymorphTo;

    /**
     * Construction
     * @param polymorphTo The registry name of the entity to polymorph to.
     */
    public ModTruePolymorphOtherSpell(String polymorphTo) {
        Supplier supplier = new ModEntityTypeSupplier(polymorphTo);
        mPolymorphTo = new LazyValue<>(supplier);
    }

    /**
     * Convert the target to polymorph entity.
     * @param caster The caster of the spell.
     * @param target The target entity.
     * @return TRUE if the spell was successfully cast.
     */
    @Override
    public boolean cast(LivingEntity caster, Entity target) {
        if (caster.getDistanceSq(target) > getRangeSquared()) {
            return false;
        }

        LivingEntity converted = (LivingEntity)mPolymorphTo.getValue().create(caster.world);
        converted.copyLocationAndAnglesFrom(target);
        target.remove();
        if (target.hasCustomName()) {
            converted.setCustomName(target.getCustomName());
            converted.setCustomNameVisible(target.isCustomNameVisible());
        }

        converted.setInvulnerable(target.isInvulnerable());
        caster.world.addEntity(converted);

        return true;
    }

    /**
     * Get the material components of the spell.
     * TODO: Needs gum arabic (acacia tree sap), drop of mercury (cinnabar ore), wisp of smoke (glass bottle used on campfire)
     * @return A list of ingredients.
     */
    @Override
    public Ingredient getMaterialComponent() {
        return Ingredient.EMPTY;
    }

    /**
     * Get the level of the spell.
     * @return This is a level 9 spell.
     */
    @Override
    public int getLevel() {
        return 9;
    }

    /**
     * Get the range of the spell.
     * @return This is a close range spell.
     */
    @Override
    public double getRange() {
        return 2.0d;
    }
}
