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
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class ModCornsnakeEntity extends ModNestingEntity {

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
        goalSelector.addGoal(1, new PanicGoal(this, 2.2D));
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
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3d);
        getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0d);
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
     * Get the speed of the entity.
     * @return The speed at which the entity moves.
     */
    private double getSpeed() {
        return getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
    }
}
