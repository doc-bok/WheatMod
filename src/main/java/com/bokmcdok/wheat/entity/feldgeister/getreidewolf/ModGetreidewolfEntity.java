package com.bokmcdok.wheat.entity.feldgeister.getreidewolf;

import com.bokmcdok.wheat.ai.goals.ModDiseaseFarmGoal;
import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.block.ModCropsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Predicate;

public class ModGetreidewolfEntity extends MonsterEntity {
    public static final Predicate<LivingEntity> IS_CHILD = (entity) -> {
        return entity.isChild();
    };

    private float mHeadRotationCourse;
    private float mHeadRotationCourseOld;
    private boolean mIsWet;
    private boolean mIsShaking;
    private float mTimeWolfIsShaking;
    private float mPrevTimeWolfIsShaking;

    /**
     * Construction
     * @param type The type of this entity.
     * @param world The current world.
     */
    public ModGetreidewolfEntity(EntityType<? extends ModGetreidewolfEntity> type, World world) {
        super(type, world);
    }

    /**
     * Getreidewolves will shake like normal wolves if they get wet.
     */
    @Override
    public void livingTick() {
        super.livingTick();
        if(world.isRemote && isWet() && !mIsShaking && !hasPath() && onGround) {
            mIsShaking = true;
            mTimeWolfIsShaking = 0.0f;
            mPrevTimeWolfIsShaking = 0.0f;
            world.setEntityState(this, (byte)8);
        }

        for(int l = 0; l < 4; ++l) {
            int i = MathHelper.floor(this.posX + (double)((float)(l % 2 * 2 - 1) * 0.25F));
            int j = MathHelper.floor(this.posY);
            int k = MathHelper.floor(this.posZ + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));
            BlockPos blockPosition = new BlockPos(i, j, k);
            BlockState blockState = world.getBlockState(blockPosition);
            Block block = blockState.getBlock();
            if (ModBlockUtils.WHEAT.contains(block) && block instanceof ModCropsBlock) {
                ((ModCropsBlock)block).diseaseCrop(world, blockPosition, blockState);
            }
        }
    }

    /**
     * Handle getreidewolf getting wet and shaking.
     */
    @Override
    public void tick() {
        super.tick();
        if (isAlive()) {
            mHeadRotationCourseOld = mHeadRotationCourse;
            mHeadRotationCourse += 0.0f - mHeadRotationCourse * 0.04f;
        }

        if (isInWaterRainOrBubbleColumn()) {
            mIsWet = true;
            mIsShaking = false;
            mTimeWolfIsShaking = 0.0f;
            mPrevTimeWolfIsShaking = 0.0f;
        } else if (mIsShaking) {
            if (mTimeWolfIsShaking == 0.0f) {
                playSound(SoundEvents.ENTITY_WOLF_SHAKE, getSoundPitch(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }

            mPrevTimeWolfIsShaking = mTimeWolfIsShaking;
            mTimeWolfIsShaking += 0.05f;
            if (mPrevTimeWolfIsShaking >= 2.0f) {
                mIsWet = false;
                mIsShaking = false;
                mPrevTimeWolfIsShaking = 0.0f;
                mTimeWolfIsShaking = 0.0f;
            }

            if (mTimeWolfIsShaking > 0.4f) {
                float minY = (float) getBoundingBox().minY;
                int sin = (int) (MathHelper.sin((mTimeWolfIsShaking - 0.4f) * (float) Math.PI) * 7.0f);
                Vec3d motion = getMotion();

                for (int i = 0; i < sin; ++i) {
                    float f1 = (rand.nextFloat() * 2.0f - 1.0f) - getWidth() * 0.5f;
                    float f2 = (rand.nextFloat() * 2.0f - 1.0f) - getWidth() * 0.5f;
                    world.addParticle(ParticleTypes.SPLASH, posX + f1, minY + 0.8f, posZ + f2, motion.x, motion.y, motion.z);
                }
            }
        }
    }

    /**
     * Reset everything when the getreidewolf dies.
     * @param source
     */
    @Override
    public void onDeath(DamageSource source) {
        mIsWet = false;
        mIsShaking = false;
        mPrevTimeWolfIsShaking = 0.0f;
        mTimeWolfIsShaking = 0.0f;
        super.onDeath(source);
    }

    /**
     * Check to see if the getreidewolf is wet.
     * @return TRUE if the getreidewolf is wet.
     */
    @OnlyIn(Dist.CLIENT)
    public boolean getIsWet() {
        return mIsWet;
    }

    /**
     * Get the shading to apply when the wolf is wet.
     * @param partialTicks The time delta.
     * @return The shading to apply.
     */
    @OnlyIn(Dist.CLIENT)
    public float getShadingWhileWet(float partialTicks) {
        return 0.75f + MathHelper.lerp(partialTicks, mPrevTimeWolfIsShaking, mTimeWolfIsShaking) * 0.125f;
    }

    /**
     * Get the angle of a body part while shaking.
     * @param partialTicks The time delta.
     * @param modifier The modifier to the angle.
     * @return The angle of the body part.
     */
    @OnlyIn(Dist.CLIENT)
    public float getShakeAngle(float partialTicks, float modifier) {
        float result = (MathHelper.lerp(partialTicks, mPrevTimeWolfIsShaking, mTimeWolfIsShaking) + modifier) / 1.8f;
        MathHelper.clamp(result, 0.0f, 1.0f);
        return MathHelper.sin(result * (float)Math.PI) * MathHelper.sin(result * (float)Math.PI * 11.0f) * 0.15f * (float)Math.PI;
    }

    /**
     * Get the angle of the head when interested in something.
     * @param partialTicks The time delta.
     * @return The angle of the head.
     */
    @OnlyIn(Dist.CLIENT)
    public float getInterestedAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, mHeadRotationCourseOld, mHeadRotationCourse) * 0.15f * (float)Math.PI;
    }

    /**
     * Getreidewolf attacks cause nausea.
     * @param target The target of the attack.
     * @return TRUE if the attack was successful.
     */
    @Override
    public boolean attackEntityAsMob(Entity target) {
        boolean result = target.attackEntityFrom(DamageSource.causeMobDamage(this), (float)getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());

        if (result) {
            if (target instanceof LivingEntity) {
                ((LivingEntity) target).addPotionEffect(new EffectInstance(Effects.NAUSEA, 200));
            }
        }

        return  result;
    }

    /**
     * Handle the setStatus call from a server.
     * @param id The status ID.
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 8) {
            mIsShaking = true;
            mTimeWolfIsShaking = 0.0f;
            mPrevTimeWolfIsShaking = 0.0f;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    /**
     * Get the tail rotation.
     * @return The same as an angry wolf.
     */
    @OnlyIn(Dist.CLIENT)
    public float getTailRotation() {
        return 1.5393804f;
    }

    /**
     * We can only ever have one getreidewolf per chunk.
     * @return Always 1.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    /**
     * Check if the entity can spawn.
     * @param entity The entity.
     * @param world The current world.
     * @param reason The spawn reason.
     * @param position The block's position.
     * @param random The random number generator.
     * @return TRUE if a widowbird can spawn here.
     */
    public static boolean canSpawn(EntityType<ModGetreidewolfEntity> entity, IWorld world, SpawnReason reason, BlockPos position, Random random) {
        return world.getCurrentMoonPhaseFactor() > 0.9f && MonsterEntity.func_223325_c(entity, world, reason, position, random);
    }

    /**
     * Get the height of the entity's eyes.
     * @param pose The current pose.
     * @param size The size of the entity.
     * @return The height of the entity's eyes.
     */
    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return size.height * 0.8f;
    }

    /**
     * Register the behaviours of the getreidewolf.
     */
    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4f));
        goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0d, true));
        goalSelector.addGoal(5, new ModDiseaseFarmGoal(this, ModBlockUtils.WHEAT));
        goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0d));
        goalSelector.addGoal(10, new LookRandomlyGoal(this));

        targetSelector.addGoal(3, new HurtByTargetGoal((this)));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, 10, false, false, IS_CHILD));
    }

    /**
     * Getreidewolves have more health and do more damage than a wolf.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3d);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(22.0d);
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0d);
    }

    /**
     * Play a step sound.
     * @param position The current block position.
     * @param state The current block state.
     */
    @Override
    protected void playStepSound(BlockPos position, BlockState state) {
        playSound(SoundEvents.ENTITY_WOLF_STEP, 0.3f, 0.5f);
    }

    /**
     * Get the ambient sound.
     * @return The ambient sound of the entity.
     */
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WOLF_GROWL;
    }

    /**
     * Get the hurt sound.
     * @param source The source of the damage.
     * @return The sound to play when this entity is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WOLF_HURT;
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
