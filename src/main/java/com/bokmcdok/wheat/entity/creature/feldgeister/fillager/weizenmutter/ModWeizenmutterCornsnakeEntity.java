package com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.ai.goals.ModCastSpellOnSelfGoal;
import com.bokmcdok.wheat.entity.creature.animal.cornsnake.ModCornsnakeEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

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
     * Method to determine if the entity can spawn.
     * @param entity The entity to try and spawn.
     * @param world The current world.
     * @param reason The reason for spawning.
     * @param position The position to spawn at.
     * @param random The random number generator.
     * @return TRUE if the entity can spawn.
     */
    public static boolean canWeizenmutterSpawn(EntityType<ModWeizenmutterCornsnakeEntity> entity, IWorld world, SpawnReason reason, BlockPos position, Random random) {
        return true;
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
     * Register the behaviours of the weizenmutter.
     */
    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(3, new ModCastSpellOnSelfGoal(this, WheatMod.SPELL_REGISTRAR.getSpell("true_polymorph_weizenmutter"), (caster, target) -> caster.getHealth() / caster.getMaxHealth() >= 0.5f));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, getSpeed(), getSpeed()));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, WolfEntity.class, 10.0F, getSpeed(), getSpeed()));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, VillagerEntity.class, 4.0F, getSpeed(), getSpeed()));
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
