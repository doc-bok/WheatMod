package com.bokmcdok.wheat.entity.creature.animal.cornsnake;

import com.bokmcdok.wheat.ai.goals.ModCreateNestGoal;
import com.bokmcdok.wheat.ai.goals.ModMateGoal;
import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.entity.ModEntityUtils;
import com.bokmcdok.wheat.entity.creature.animal.ModNestingEntity;
import com.bokmcdok.wheat.entity.creature.animal.mouse.ModMouseEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.Random;

public class ModCornsnakeEntity extends ModNestingEntity {
    private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(ModCornsnakeEntity.class, DataSerializers.VARINT);

    /**
     * Construction
     * @param type The type of the entity
     * @param world The current world
     */
    public ModCornsnakeEntity(EntityType<? extends ModCornsnakeEntity> type, World world) {
        super(type, world);
    }

    /**
     * Register the AI behaviours of the entity.
     */
    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, getSpeed()));
        goalSelector.addGoal(1, new ModMateGoal(this, getSpeed()));
        goalSelector.addGoal(1, new ModCreateNestGoal(this, ModBlockUtils.cornsnake_egg, getSpeed(), 16, 8));
        goalSelector.addGoal(5, new MeleeAttackGoal(this, getSpeed(), true));
        goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
        goalSelector.addGoal(11, new LookAtGoal(this, ModMouseEntity.class, 10.0F));

        targetSelector.addGoal(3, new HurtByTargetGoal((this)));
        targetSelector.addGoal(4,new NearestAttackableTargetGoal<>(this, ModMouseEntity.class, 10, false, false, null));
    }

    /**
     * Create a child version of the entity
     * @param ageable The entity to create a child for.
     * @return The child entity.
     */
    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return ModEntityUtils.cornsnake.create(world);
    }

    /**
     * Register the attributes of the entity.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0d);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45d);
        getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0d);
    }

    /**
     * Handle the snake's type on spawning.
     * @param world The current world.
     * @param difficulty The current difficulty.
     * @param reason The reason for spawning.
     * @param data The spawn data.
     * @param nbt The NBT data.
     * @return The new living entity data for the entity.
     */
    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        int type = 0;
        CornsnakeData cornsnakeData;
        if (data instanceof CornsnakeData) {
            cornsnakeData = (CornsnakeData)data;
            type = cornsnakeData.mType;
        } else {
            Biome biome = world.func_226691_t_(new BlockPos(this));
            if (biome.getCategory() == Biome.Category.FOREST) {
                type = 1;
            }

            cornsnakeData = new CornsnakeData(type);
        }

        setType(type);

        return super.onInitialSpawn(world, difficulty, reason, cornsnakeData, nbt);
    }

    /**
     * If the snake eats a mouse they will be ready to breed.
     * @param entity The entity the snake just killed.
     */
    @Override
    public void onKillEntity(LivingEntity entity) {
        super.onKillEntity(entity);
        if (entity instanceof ModMouseEntity) {
            if (isChild()) {
                addGrowth(3);
            } else {
                setInLove(600);
            }
        }
    }

    /**
     * Get the index of the texture to use.
     * @return The index based on the type of snake this is.
     */
    public int getTextureIndex() {
        return dataManager.get(TYPE);
    }

    /**
     * Method to determine if the entity can spawn.
     * @param entity The entity to try and spawn.
     * @param world The current world.
     * @param reason The reason for spawning.
     * @param position The position to spawn at.
     * @param random The random number generator.
     * @return TRUE if the entity can spawn.
     */
    public static boolean canSpawn(EntityType<ModCornsnakeEntity> entity, IWorld world, SpawnReason reason, BlockPos position, Random random) {
        Block block = world.getBlockState(position.down()).getBlock();
        return block == Blocks.GRASS_BLOCK;
    }

    /**
     * Register the default snake type.
     */
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(TYPE, 0);
    }

    /**
     * Set the type of the snake.
     * @param type The type of snake to use.
     */
    private void setType(int type) {
        dataManager.set(TYPE, type);
    }

    /**
     * Get the speed of the entity.
     * @return The speed at which the entity moves.
     */
    private double getSpeed() {
        return getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
    }

    /**
     * Class to store snake-specific data.
     */
    private static class CornsnakeData extends AgeableEntity.AgeableData {
        private final int mType;

        /**
         * Construction
         * @param type The type of this snake.
         */
        public CornsnakeData(int type) {
            mType = type;
        }

        /**
         * Get the type of this snake.
         * @return The type of this snake.
         */
        public int getType() {
            return mType;
        }
    }
}
