package com.bokmcdok.wheat.entity.creature.feldgeister.weizenvogel;

import com.bokmcdok.wheat.entity.creature.ModFlappingController;
import com.bokmcdok.wheat.entity.creature.feldgeister.ModFeldgeisterEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ModWeizenvogelEntity extends ModFeldgeisterEntity implements IFlyingAnimal {
    private static final Predicate<LivingEntity> IS_FARMER = (entity) -> {
        if (entity instanceof VillagerEntity) {
            return ((VillagerEntity) entity).getVillagerData().getProfession() == VillagerProfession.FARMER;
        }

        return false;
    };

    private final ModFlappingController mFlappingController;

    /**
     * Construction
     * @param type The type of this entity.
     * @param world The current world.
     */
    public ModWeizenvogelEntity(EntityType<? extends ModWeizenvogelEntity> type, World world) {
        super(type, world);
        moveController = new FlyingMovementController(this, 10, false);
        mFlappingController = new ModFlappingController(this);
    }

    /**
     * Tick the entity - update the flapping animation.s
     */
    @Override
    public void livingTick() {
        super.livingTick();
        mFlappingController.livingTick();
    }

    /**
     * Weizenvogel cannot fall.
     * @param distance The distance fell.
     * @param damageMultiplier The damage multiplier from the fall.
     */
    @Override
    public boolean func_225503_b_(float distance, float damageMultiplier) {
        return false;
    }

    /**
     * Widowbirds can be pushed.
     * @return Always TRUE.
     */
    @Override
    public boolean canBePushed() {
        return true;
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
    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return size.height * 0.6f;
    }

    /**
     * Weizenvogels attack farmers and cattle.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, VillagerEntity.class, 10, false, false, IS_FARMER));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, CowEntity.class, 10, false, false, null));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, MooshroomEntity.class, 10, false, false, null));
    }

    /**
     * Weizenvogel are slightly stronger than Widowbirds and can attack.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0d);
        getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.5d);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3d);
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0d);
    }

    /**
     * Play a step sound.
     * @param volume how loud the sound is.
     */
    @Override
    protected float playFlySound(float volume) {
        playSound(SoundEvents.ENTITY_PARROT_FLY, 0.15f, 1.0f);
        return  volume + mFlappingController.getFlapSpeed() * 0.5f;
    }

    /**
     * Play the step sound
     */
    @Override
    protected void playStepSound(BlockPos position, BlockState blockState) {
        playSound(SoundEvents.ENTITY_PARROT_STEP, 0.15f, 1.0f);
    }

    /**
     * Weizenvogel make a flying sound.
     * @return Always TRUE.
     */
    @Override
    protected boolean makeFlySound() {
        return true;
    }

    /**
     * Get the ambient sound.
     * @return The ambient sound of the entity.
     */
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PARROT_AMBIENT;
    }

    /**
     * Get the hurt sound.
     * @param source The source of the damage.
     * @return The sound to play when this entity is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PARROT_HURT;
    }

    /**
     * Get the death sound.
     * @return The sound to play when the entity dies.
     */
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER;
    }

    /**
     * Get the pitch to play sounds at.
     * @return The pitch of the sounds.
     */
    @Override
    protected float getSoundPitch() {
        return (rand.nextFloat() - rand.nextFloat()) * 0.2f + 1.0f;
    }

    /**
     * Widowbirds cannot fall.
     * @param y The current y-position.
     * @param onGround Is this on the ground.
     * @param state The block state.
     * @param position The block position.
     */
    @Override
    protected void updateFallState(double y, boolean onGround, BlockState state, BlockPos position) {
        //no-op
    }

    /**
     * Create a flying navigator for weizenvogel.
     * @param world The current world.
     * @return A flying path navigator instance.
     */
    @Override
    protected PathNavigator createNavigator(World world) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, world);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanSwim(true);
        flyingpathnavigator.setCanEnterDoors(true);
        return flyingpathnavigator;
    }

    /**
     * Weizenvogel use their flying speed primarily.
     * @return The flying speed.
     */
    @Override
    protected double getSpeed() {
        return getAttribute(SharedMonsterAttributes.FLYING_SPEED).getValue();
    }
}
