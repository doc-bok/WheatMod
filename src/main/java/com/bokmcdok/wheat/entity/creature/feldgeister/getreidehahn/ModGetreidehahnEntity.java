package com.bokmcdok.wheat.entity.creature.feldgeister.getreidehahn;

import com.bokmcdok.wheat.entity.creature.ModFlappingController;
import com.bokmcdok.wheat.entity.creature.feldgeister.ModFeldgeisterEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ModGetreidehahnEntity extends ModFeldgeisterEntity {
    private final ModFlappingController mFlappingController;

    /**
     * Construction
     * @param type  The type of this entity.
     * @param world The current world.
     */
    public ModGetreidehahnEntity(EntityType<? extends ModFeldgeisterEntity> type, World world) {
        super(type, world);
        mFlappingController = new ModFlappingController(this);
    }

    /**
     * Tick the entity - update the flapping animations.
     */
    @Override
    public void livingTick() {
        super.livingTick();
        mFlappingController.livingTick();
    }

    /**
     * Make an attack sound.
     * @param target The target of the attack.
     * @return TRUE if the attack was successful.
     */
    @Override
    public boolean attackEntityAsMob(Entity target) {
        boolean result = target.attackEntityFrom(DamageSource.causeMobDamage(this), (float)getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());
        playSound(SoundEvents.ENTITY_CHICKEN_AMBIENT, 1.0F, 1.0F);
        return result;
    }

    /**
     * Get the flapping controller for the entity.
     * @return An instance of a flapping controller
     */
    public ModFlappingController getFlappingController() {
        return mFlappingController;
    }

    /**
     * Get the height of the entity's eyes.
     * @param pose The current pose.
     * @param size The size of the entity.
     * @return The height of the entity's eyes.
     */
    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return size.height * 0.92F;
    }

    /**
     * Register the behaviours of the entity.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();

        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, 10, false, false, IS_CHILD));
    }

    /**
     * Heukatze have more health and do more damage than a cat.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3d);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0d);
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0d);
    }

    /**
     * Get the ambient sound.
     * @return The ambient sound of the entity.
     */
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    /**
     * Get the hurt sound.
     * @param source The source of the damage.
     * @return The sound to play when this entity is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    /**
     * Get the death sound.
     * @return The sound to play when the entity dies.
     */
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER;
    }
}
