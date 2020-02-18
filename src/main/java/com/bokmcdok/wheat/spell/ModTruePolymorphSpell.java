package com.bokmcdok.wheat.spell;

import com.bokmcdok.wheat.supplier.ModEntityTypeSupplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.world.World;

import java.util.function.Supplier;


public class ModTruePolymorphSpell extends ModSpell {
    private final LazyValue<EntityType<?>> mPolymorphTo;

    /**
     * Construction
     * @param polymorphTo The registry name of the entity to polymorph to.
     */
    public ModTruePolymorphSpell(String polymorphTo) {
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

        World world = caster.world;

        LivingEntity from = (LivingEntity)target;

        LivingEntity converted = (LivingEntity)mPolymorphTo.getValue().create(world);
        converted.copyLocationAndAnglesFrom(target);

        float healthRatio = from.getHealth() / from.getMaxHealth();
        converted.setHealth(converted.getMaxHealth() * healthRatio);

        target.remove();
        if (target.hasCustomName()) {
            converted.setCustomName(target.getCustomName());
            converted.setCustomNameVisible(target.isCustomNameVisible());
        }

        converted.setInvulnerable(target.isInvulnerable());

        if (converted instanceof MobEntity) {
            MobEntity convertedMob = (MobEntity)converted;
            convertedMob.onInitialSpawn(world, world.getDifficultyForLocation(converted.getPosition()), SpawnReason.CONVERSION, null, null);

            if (from instanceof MobEntity) {
                convertedMob.setAttackTarget(((MobEntity)from).getAttackTarget());
            }
        }
        caster.world.addEntity(converted);

        return true;
    }

    /**
     * Get the material components of the spell.
     * TODO: Needs gum arabic (acacia tree sap), drop of mercury (from cinnabar ore), wisp of smoke (glass bottle used on/above campfire)
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
