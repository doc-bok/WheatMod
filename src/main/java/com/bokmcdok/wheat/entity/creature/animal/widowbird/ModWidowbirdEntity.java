package com.bokmcdok.wheat.entity.creature.animal.widowbird;

import com.bokmcdok.wheat.ai.goals.ModCreateNestGoal;
import com.bokmcdok.wheat.ai.goals.ModMateGoal;
import com.bokmcdok.wheat.ai.goals.ModNestingGoal;
import com.bokmcdok.wheat.ai.goals.ModRaidFarmGoal;
import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.entity.ModEntityUtils;
import com.bokmcdok.wheat.entity.creature.ModFlappingController;
import com.bokmcdok.wheat.entity.creature.animal.ModNestingEntity;
import com.bokmcdok.wheat.entity.creature.animal.butterfly.ModButterflyEntity;
import com.bokmcdok.wheat.item.ModItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class ModWidowbirdEntity extends ModNestingEntity implements IFlyingAnimal {
    private final ModFlappingController mFlappingController;

    /**
     * Construction
     * @param type The type of this entity.
     * @param world The current world.
     */
    public ModWidowbirdEntity(EntityType<? extends ModWidowbirdEntity> type, World world) {
        super(type, world);
        moveController = new FlyingMovementController(this, 10, false);
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
     * Widowbirds can be bred with seeds/grains.
     * @param item The item to check.
     * @return TRUE if the item is a seed/grain.
     */
    @Override
    public boolean isBreedingItem(ItemStack item) {
        return ModItemUtils.SEED_ITEMS.test(item);
    }

    /**
     * Check if the entity can spawn.
     * @param widowbird The widowbird entity.
     * @param world The current world.
     * @param reason The spawn reason.
     * @param position The block's position.
     * @param random The random number generator.
     * @return TRUE if a widowbird can spawn here.
     */
    public static boolean canSpawn(EntityType<ModWidowbirdEntity> widowbird, IWorld world, SpawnReason reason, BlockPos position, Random random) {
        Block block = world.getBlockState(position.down()).getBlock();
        return (block.isIn(BlockTags.LEAVES) ||
                block == Blocks.GRASS_BLOCK ||
                block instanceof LogBlock ||
                block == Blocks.AIR) &&
                world.getNeighborAwareLightSubtracted(position, 0) > 8;
    }

    /**
     * Widowbirds cannot fall.
     * @param distance The distance fell.
     * @param damageMultiplier The damage multiplier from the fall.
     */
    @Override
    public boolean func_225503_b_(float distance, float damageMultiplier) {
        return false;
    }

    /**
     * Creates a baby widowbird.
     * @param ageableEntity
     * @return The new child entity.
     */
    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageableEntity) {
        return ModEntityUtils.widowbird.create(world);
    }

    /**
     * Get the category for the widowbird's sounds.
     * @return A neutral category.
     */
    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
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
     * Get the ambient sound
     * TODO: Add custom sounds for widowbird.
     * @return The ambient sound event.
     */
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PARROT_AMBIENT;
    }

    /**
     * Get the hurt sound
     * TODO: Add custom sounds for widowbird.
     * @return The hurt sound event.
     */
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_PARROT_HURT;
    }

    /**
     * Get the death sound
     * TODO: Add custom sounds for widowbird.
     * @return The death sound event.
     */
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PARROT_DEATH;
    }

    /**
     * Play the step sound
     * TODO: Add custom sounds for widowbird.
     */
    @Override
    protected void playStepSound(BlockPos position, BlockState blockState) {
        playSound(SoundEvents.ENTITY_PARROT_STEP, 0.15f, 1.0f);
    }

    /**
     * Play the fly sound.
     * TODO: Add custom sounds for widowbird.
     * @param volume The volume to play it at.
     * @return The updated volume.
     */
    @Override
    protected float playFlySound(float volume) {
        playSound(SoundEvents.ENTITY_PARROT_FLY, 0.15f, 1.0f);
        return  volume + mFlappingController.getFlapSpeed() * 0.5f;
    }

    /**
     * Widowbirds make a flying sound.
     * @return Always TRUE.
     */
    @Override
    protected boolean makeFlySound() {
        return true;
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
     * Widowbirds can be bred with seeds and attack insects.
     */
    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new PanicGoal(this, 1.25d));
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(1, new ModMateGoal(this, getFlyingSpeed()));
        goalSelector.addGoal(1, new ModCreateNestGoal(this, ModBlockUtils.widowbird_nest, getFlyingSpeed(), 16, 8));
        goalSelector.addGoal(1, new ModNestingGoal(this, ModBlockUtils.widowbird_nest, getFlyingSpeed(), 16, 8));
        goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0d));
        goalSelector.addGoal(5, new ModRaidFarmGoal(this, ModBlockUtils.WHEAT, getFlyingSpeed(), 16, 8));
        goalSelector.addGoal(9, new OcelotAttackGoal(this));
        goalSelector.addGoal(12, new LookAtGoal(this, ModButterflyEntity.class, 8.0f));

        targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, ModButterflyEntity.class, 10, false, false, null));
    }

    /**
     *  Widowbirds have three hearts of health, can fly fast, but walk slow.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0d);
        getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.4d);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2d);
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0d);
    }

    /**
     * Create a flying navigator for widowbirds.
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
     * Get the height of the widowbird's eyes.
     * @param pose The current pose.
     * @param size The size of the widowbird.
     * @return The height of the eyes.
     */
    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return size.height * 0.6f;
    }

    /**
     * Get the bird's flying speed.
     * @return The speed at which the widowbird flies.
     */
    private double getFlyingSpeed() {
        return getAttribute(SharedMonsterAttributes.FLYING_SPEED).getValue();
    }
}
