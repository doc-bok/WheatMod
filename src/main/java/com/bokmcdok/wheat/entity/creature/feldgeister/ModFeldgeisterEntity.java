package com.bokmcdok.wheat.entity.creature.feldgeister;

import com.bokmcdok.wheat.ai.goals.ModFindFarmGoal;
import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.block.ModCropsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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

import java.util.Random;
import java.util.function.Predicate;

public class ModFeldgeisterEntity extends MonsterEntity {
    protected static final DataParameter<Boolean> FED = EntityDataManager.createKey(ModFeldgeisterEntity.class, DataSerializers.BOOLEAN);
    protected static final Predicate<LivingEntity> IS_CHILD = (entity) -> entity.isChild();
    protected Goal mAttackGoal;

    /**
     * Store NBT data so that status is maintained between saves.
     * @param data The NBT data.
     */
    @Override
    public void writeAdditional(CompoundNBT data) {
        super.writeAdditional(data);
        data.putBoolean("IsFed", getIsFed());
    }

    /**
     * Read NBT data so that status is maintained between saves.
     * @param data The NBT data.
     */
    @Override
    public void readAdditional(CompoundNBT data) {
        super.readAdditional(data);
        setIsFed(data.getBoolean("IsFed"));
    }

    /**
     * Construction
     * @param type The type of this entity.
     * @param world The current world.
     */
    protected ModFeldgeisterEntity(EntityType<? extends ModFeldgeisterEntity> type, World world) {
        super(type, world);
    }

    /**
     * Register data to the data manager.
     */
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(FED, false);
    }

    /**
     * Check whether or not the getreidewolf has been fed.
     * @return TRUE if the getreidewolf has been fed.
     */
    protected boolean getIsFed() {
        return dataManager.get(FED);
    }

    /**
     * Set whether or not the getreidewolf has been fed.
     * @param value TRUE if the getreidewolf has been fed.
     */
    protected void setIsFed(boolean value) {
        dataManager.set(FED, value);
        world.setEntityState(this, (byte)18);
    }

    /**
     * Play enderman teleport sound when removed.
     */
    @Override
    public void remove() {
        super.remove();

        world.playSound(null, prevPosX, prevPosY, prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, getSoundCategory(), 1.0F, 1.0F);
        playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

        if (world.isRemote) {
            Vec3d position = getPositionVec();
            for (int i = 0; i < 50; ++i) {
                world.addParticle(ParticleTypes.PORTAL,
                        position.x + (rand.nextDouble() - 0.5D) * (double) getWidth(),
                        position.y + rand.nextDouble() * (double) getHeight() - 0.25D,
                        position.z + (rand.nextDouble() - 0.5D) * (double) getWidth(),
                        (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(),
                        (rand.nextDouble() - 0.5D) * 2.0D);
            }
        }
    }

    /**
     * Show love particles
     * @param status The status update from the server.
     */
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte status) {
        if (status == 18) {
            Vec3d position = getPositionVec();
            for(int i = 0; i < 7; ++i) {
                double x = rand.nextGaussian() * 0.02D;
                double y = rand.nextGaussian() * 0.02D;
                double z = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.HEART,
                        position.x + (double)(rand.nextFloat() * getWidth() * 2.0F) - (double)getWidth(),
                        position.y + 0.5D + (double)(rand.nextFloat() * getHeight()),
                        position.z + (double)(rand.nextFloat() * getWidth() * 2.0F) - (double)getWidth(), x, y, z);
            }
        } else {
            super.handleStatusUpdate(status);
        }
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
    public static <T extends ModFeldgeisterEntity> boolean canSpawn(EntityType<T> entity, IWorld world, SpawnReason reason, BlockPos position, Random random) {
        return MonsterEntity.func_223325_c(entity, world, reason, position, random);
    }

    /**
     * Feldgeister attacks cause nausea.
     * @param target The target of the attack.
     * @return TRUE if the attack was successful.
     */
    @Override
    public boolean attackEntityAsMob(Entity target) {
        boolean result = target.attackEntityFrom(DamageSource.causeMobDamage(this), (float)getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());

        if (result && target instanceof LivingEntity) {
            ((LivingEntity) target).addPotionEffect(new EffectInstance(Effects.NAUSEA, 200));
        }

        return result;
    }

    /**
     * Register the behaviours of the getreidewolf.
     */
    @Override
    protected void registerGoals() {
        mAttackGoal = new MeleeAttackGoal(this, 1.0d, true);

        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(5, mAttackGoal);
        goalSelector.addGoal(8, new ModFindFarmGoal(this, ModBlockUtils.WHEAT, 1.0d, 16, 1));
        goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0d));
        goalSelector.addGoal(10, new LookRandomlyGoal(this));

        targetSelector.addGoal(3, new HurtByTargetGoal((this)));
    }

    /**
     * Cause disease in any crops this entity is touching. If the feldgeister has been fed will instead pollinate the
     * crops.
     */
    protected void affectTouchedCrops() {
        Vec3d position = getPositionVec();
        for(int l = 0; l < 4; ++l) {
            int i = MathHelper.floor(position.x + (double)((float)(l % 2 * 2 - 1) * 0.25F));
            int j = MathHelper.floor(position.y);
            int k = MathHelper.floor(position.z + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));
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
     * Get the speed of the feldgeister.
     * @return The actual speed of the feldgeister.
     */
    protected double getSpeed() {
        return getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
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
