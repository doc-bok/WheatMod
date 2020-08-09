package com.bokmcdok.wheat.entity.creature.animal.butterfly;

import com.bokmcdok.wheat.ai.goals.ModAttractToLightGoal;
import com.bokmcdok.wheat.ai.goals.ModDiurnalGoal;
import com.bokmcdok.wheat.ai.goals.ModNocturnalGoal;
import com.bokmcdok.wheat.ai.goals.ModPollinateGoal;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class ModButterflyEntity extends CreatureEntity {
    private static int MAX_VARIETIES = 2;

    private static final DataParameter<Boolean> IS_BUTTERFLY = EntityDataManager.createKey(ModButterflyEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> VARIETY = EntityDataManager.createKey(ModButterflyEntity.class, DataSerializers.VARINT);
    private BlockPos mSpawnPosition;

    /**
     * Create a new entity.
     * @param type The type of the entity.
     * @param world The current world.
     */
    public ModButterflyEntity(EntityType<? extends ModButterflyEntity> type, World world) {
        super(type, world);
    }

    /**
     * Butterflies are too small to push.
     * @return Always FALSE.
     */
    @Override
    public boolean canBePushed() {
        return false;
    }

    /**
     * Update the butterfly's speed.
     */
    @Override
    public void tick() {
        super.tick();
        setMotion(getMotion().mul(1.0d, 0.6d, 1.0d));
    }

    /**
     * Butterflies don't trigger pressure plates.
     * @return Always FALSE.
     */
    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return false;
    }

    /**
     * Handle attacks.
     * @param source The source of the attack.
     * @param amount The amount of damage caused.
     * @return Whether the attack was successful.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        } else {
            return super.attackEntityFrom(source, amount);
        }
    }

    /**
     * Check if the entity can spawn.
     * @param entity The entity to spawn.
     * @param world The current world.
     * @param reason The reason the entity is spawning.
     * @param position The position to spawn in.
     * @param random The random number generator to use.
     * @return TRUE if the entity can spawn.
     */
    public static boolean canSpawn(EntityType<ModButterflyEntity> entity, IWorld world, SpawnReason reason, BlockPos position, Random random) {
        int light = world.getLight(position);
        if (random.nextBoolean()) {
            return false;
        }

        if (world.getWorld().isDaytime()) {
            if (position.getY() < world.getSeaLevel()) {
                return false;
            } else {
                return light >= 4 && MobEntity.canSpawnOn(entity, world, reason, position, random);
            }
        } else {
            return light < random.nextInt(4) && MobEntity.canSpawnOn(entity, world, reason, position, random);
        }
    }

    /**
     * Get wether or not this entity is a butterfly.
     * @return TRUE if this is a butterfly, false if it is a moth.
     */
    public boolean getIsButterfly() {
        return dataManager.get(IS_BUTTERFLY);
    }

    /**
     * Get the variety of butterfly.
     * @return The index of the texture to use for the butterfly.
     */
    public int getVariety() {
        return dataManager.get(VARIETY);
    }

    /**
     * On spawn check for day/night to determine if this is a moth or a butterfly.
     * @param world The current world.
     * @param difficulty The difficulty setting.
     * @param reason The reason for the spawn.
     * @param data The spawn data.
     * @param nbt NBT tags.
     * @return The updated spawn data.
     */
    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        setIsButterfly(world.getWorld().isDaytime());
        setVariety(rand.nextInt(MAX_VARIETIES));
        return super.onInitialSpawn(world, difficulty, reason, data, nbt);
    }

    /**
     * Leashes can't be used on butterflies.
     * @param player The player.
     * @return Always FALSE.
     */
    @Override
    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

    /**
     * Store NBT data so that status is maintained between saves.
     * @param data The NBT data.
     */
    @Override
    public void writeAdditional(CompoundNBT data) {
        super.writeAdditional(data);
        data.putBoolean("IsButterfly", getIsButterfly());
        data.putInt("Variety", getVariety());
    }

    /**
     * Read NBT data so that status is maintained between saves.
     * @param data The NBT data.
     */
    @Override
    public void readAdditional(CompoundNBT data) {
        super.readAdditional(data);
        setIsButterfly(data.getBoolean("IsButterfly"));
        setVariety(data.getInt("Variety"));
    }

    /**
     * Butterflies are silent.
     * @return No volume.
     */
    @Override
    protected float getSoundVolume() {
        return 0.0f;
    }

    /**
     * Disable collision detection.
     * @param other The other entity.
     */
    @Override
    protected void collideWithEntity(Entity other) {
        //no-op
    }

    /**
     * Disable collision detection.
     */
    @Override
    protected void collideWithNearbyEntities() {
        //no-op
    }

    /**
     * Set the health to 3 hearts.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0d);
        getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.7d);
    }

    /**
     * Update the butterfly's movement.
     */
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (mSpawnPosition != null && (!world.isAirBlock(mSpawnPosition) || mSpawnPosition.getY() < 1)) {
            mSpawnPosition = null;
        }

        Vec3d position = getPositionVec();
        if (mSpawnPosition == null || rand.nextInt(30) == 0 || mSpawnPosition.withinDistance(getPositionVec(), 2.0d)) {
            mSpawnPosition = new BlockPos(
                    position.x + (double)rand.nextInt(7) - (double)rand.nextInt(7),
                    position.y + (double)rand.nextInt(6) - 2.0d,
                    position.z + (double)rand.nextInt(7) - (double)rand.nextInt(7));
        }

        double x = (double)mSpawnPosition.getX() + 0.5D - position.x;
        double y = (double)mSpawnPosition.getY() + 0.1D - position.y;
        double z = (double)mSpawnPosition.getZ() + 0.5D - position.z;
        Vec3d motion = getMotion();
        Vec3d newMotion = motion.add(
                (Math.signum(x) * 0.5D - motion.x) * 0.10000000149011612D,
                (Math.signum(y) * 0.699999988079071D - motion.y) * 0.10000000149011612D,
                (Math.signum(z) * 0.5D - motion.z) * 0.10000000149011612D);
        setMotion(newMotion);
        float facing = (float)(MathHelper.atan2(newMotion.z, newMotion.x) * 57.2957763671875D) - 90.0F;
        float yawDelta = MathHelper.wrapDegrees(facing - rotationYaw);
        moveForward = 0.5F;
        rotationYaw += yawDelta;
    }

    /**
     * Butterflies can't walk.
     * @return Always FALSE.
     */
    @Override
    protected boolean func_225502_at_() {
        return false;
    }

    /**
     * Butterflies can't fall.
     * @param distance The distance fell.
     * @param damageMultiplier The damage multiplier from the fall.
     */
    @Override
    public boolean func_225503_b_(float distance, float damageMultiplier) {
        return false;
    }

    /**
     * Butterflies can't fall.
     * @param y The current y position of the entity.
     * @param onGround Is the entity on the ground.
     * @param state The block that was landed on.
     * @param position The position of the block.
     */
    @Override
    protected void updateFallState(double y, boolean onGround, BlockState state, BlockPos position) {
        //no-op
    }

    /**
     * Get the eye height of the entity.
     * @param pose The current pose.
     * @param size The size of the entity.
     * @return The height of the entity's eyes.
     */
    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return size.height / 2.0f;
    }

    /**
     * Register the butterfly/moth flag.
     */
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(IS_BUTTERFLY, true);
        dataManager.register(VARIETY, 0);
    }

    /**
     * Register the pollinate goal.
     */
    @Override
    protected void registerGoals() {
        goalSelector.addGoal(5, new ModPollinateGoal(this, getFlyingSpeed(), 16, 8));
    }

    /**
     * Set whether or not this is a butterfly.
     * @param isButterfly TRUE if this is a butterfly, FALSE if this is a moth.
     */
    private void setIsButterfly(boolean isButterfly) {
        if (isButterfly) {
            goalSelector.addGoal(3, new ModDiurnalGoal(this));
        } else {
            goalSelector.addGoal(3, new ModNocturnalGoal(this));
            goalSelector.addGoal(4, new ModAttractToLightGoal(this, getFlyingSpeed(), 16, 8));
        }

        dataManager.set(IS_BUTTERFLY, isButterfly);
    }

    /**
     * Set the variety of this butterfly.
     * @param variety The variety of this butterfly.
     */
    private void setVariety(int variety) {
        dataManager.set(VARIETY, variety);
    }

    private double getFlyingSpeed() {
        return getAttribute(SharedMonsterAttributes.FLYING_SPEED).getValue();
    }
}
