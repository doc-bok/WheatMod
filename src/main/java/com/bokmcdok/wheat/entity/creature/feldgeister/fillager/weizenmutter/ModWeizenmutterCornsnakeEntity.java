package com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter;

import com.bokmcdok.wheat.ai.goals.ModFindFarmGoal;
import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.entity.creature.animal.cornsnake.ModCornsnakeEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;

public class ModWeizenmutterCornsnakeEntity extends ModCornsnakeEntity {

    /**
     * Construction
     *
     * @param type  The type of the entity
     * @param world The current world
     */
    public ModWeizenmutterCornsnakeEntity(EntityType<? extends ModWeizenmutterCornsnakeEntity> type, World world) {
        super(type, world);
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
        CornsnakeData cornsnakeData;
        if (data instanceof CornsnakeData) {
            cornsnakeData = (CornsnakeData)data;
        } else {

            cornsnakeData = new CornsnakeData(3);
        }

        setType(3);

        return super.onInitialSpawn(world, difficulty, reason, cornsnakeData, nbt);
    }

    /**
     * Weizenmutters are similar to witches.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(26.0D);
    }

    /**
     * Register the behaviours of the getreidewolf.
     */
    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4f));
        goalSelector.addGoal(5, new MeleeAttackGoal(this, getSpeed(), true));
        goalSelector.addGoal(8, new ModFindFarmGoal(this, ModBlockUtils.WHEAT, getSpeed(), 16, 1));
        goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0d));
        goalSelector.addGoal(10, new LookRandomlyGoal(this));

        targetSelector.addGoal(3, new HurtByTargetGoal((this)));
    }

    /**
     * Get the speed of the feldgeister.
     * @return The actual speed of the feldgeister.
     */
    protected double getSpeed() {
        return getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
    }
}
