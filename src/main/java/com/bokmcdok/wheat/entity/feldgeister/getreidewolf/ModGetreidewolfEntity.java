package com.bokmcdok.wheat.entity.feldgeister.getreidewolf;

import com.bokmcdok.wheat.ai.goals.ModDiseaseFarmGoal;
import com.bokmcdok.wheat.ai.goals.ModRangedAttackGoal;
import com.bokmcdok.wheat.ai.goals.ModNocturnalGoal;
import com.bokmcdok.wheat.ai.goals.ModPollinateGoal;
import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.block.ModCropsBlock;
import com.bokmcdok.wheat.entity.feldgeister.ModFeldgeisterEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import net.minecraft.block.CropsBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ModGetreidewolfEntity extends ModFeldgeisterEntity implements IRangedAttackMob {

    private Goal mDiseaseCropsGoal;
    private float mHeadRotationCourse;
    private float mHeadRotationCourseOld;
    private float mTimeWolfIsShaking;
    private float mPrevTimeWolfIsShaking;
    private boolean mIsWet;
    private boolean mIsShaking;

    /**
     * Construction
     * @param type The type of this entity.
     * @param world The current world.
     */
    public ModGetreidewolfEntity(EntityType<? extends ModGetreidewolfEntity> type, World world) {
        super(type, world);
    }

    /**
     * Getreidewolves will shake like normal wolves if they get wet. They will also cause disease in any wheat they
     * touch.
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
            int i = MathHelper.floor(posX + (double)((float)(l % 2 * 2 - 1) * 0.25F));
            int j = MathHelper.floor(posY);
            int k = MathHelper.floor(posZ + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));
            BlockPos blockPosition = new BlockPos(i, j, k);
            BlockState blockState = world.getBlockState(blockPosition);
            Block block = blockState.getBlock();
            if (ModBlockUtils.WHEAT.contains(block) && block instanceof ModCropsBlock) {
                if (getIsFed()) {
                    CropsBlock crop = (CropsBlock)block;
                    Integer integer = blockState.get(CropsBlock.AGE);
                    if (integer < crop.getMaxAge()) {
                        world.setBlockState(blockPosition, blockState.with(CropsBlock.AGE, Integer.valueOf(integer + 1)), 2);
                        world.playEvent(2001, blockPosition, Block.getStateId(blockState));
                    }
                } else {
                    ((ModCropsBlock) block).diseaseCrop(world, blockPosition, blockState);
                }
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
     * @return TRUE if the entity can spawn here.
     */
    public static boolean canGetreideWolfSpawn(EntityType<ModGetreidewolfEntity> entity, IWorld world, SpawnReason reason, BlockPos position, Random random) {
        return world.getCurrentMoonPhaseFactor() > 0.9f && ModFeldgeisterEntity.canSpawn(entity, world, reason, position, random);
    }

    /**
     * Launch a howl attack
     * @param target The target to attack.
     * @param v unused.
     */
    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float v) {
        //  Play the howl
        world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_WOLF_HOWL, getSoundCategory(), 1.0F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F);

        //  Get the bounding box
        AxisAlignedBB boundingBox = getBoundingBox();
        boundingBox = boundingBox.grow(5.0d);

        //  Apply effects to all entities in AoE
        List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, boundingBox);
        for (LivingEntity i : entities) {
            if (i != this) {
                boolean result = i.attackEntityFrom(DamageSource.causeMobDamage(this), (float)getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());

                if (result) {

                    i.addPotionEffect(new EffectInstance(Effects.NAUSEA, 200));

                    Vec3d direction = i.getPositionVector().subtract(getPositionVector()).normalize();
                    float rotation = (float)Math.atan(direction.x/direction.z);
                    i.knockBack(this, 0.5f,
                            MathHelper.sin(rotation),
                            (-MathHelper.cos(rotation)));
                }
            }
        }
    }

    /**
     * If a getreidewolf is fed meat they will start to pollinate crops instead of diseasing them.
     * @param player The player interacting with the entity.
     * @param hand The player's hand.
     * @return TRUE if the interaction is handled.
     */
    @Override
    protected boolean processInteract(PlayerEntity player, Hand hand) {
        if (!getIsFed()) {
            ItemStack itemStack = player.getHeldItem(hand);
            Item item = itemStack.getItem();
            if (item.getFood().isMeat()) {
                if (!player.abilities.isCreativeMode) {
                    itemStack.shrink(1);
                }

                setIsFed(true);
            }

            return true;
        }

        return super.processInteract(player, hand);
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
        mDiseaseCropsGoal = new ModDiseaseFarmGoal(this, ModBlockUtils.WHEAT);

        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(3, new ModNocturnalGoal(this));
        goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4f));
        goalSelector.addGoal(5, new ModRangedAttackGoal(this, 0.3d, 200, 3.0f));
        goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0d, true));
        goalSelector.addGoal(5, mDiseaseCropsGoal);
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

    /**
     * Set whether or not the getreidewolf has been fed.
     * @param value TRUE if the getreidewolf has been fed.
     */
    protected void setIsFed(boolean value) {
        super.setIsFed(value);

        if (value) {
            goalSelector.removeGoal(mDiseaseCropsGoal);
            goalSelector.addGoal(5, new ModPollinateGoal(this));
        }
    }
}
