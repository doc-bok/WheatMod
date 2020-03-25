package com.bokmcdok.wheat.entity.creature.animal.mouse;

import com.bokmcdok.wheat.ai.behaviour.IUsesTags;
import com.bokmcdok.wheat.ai.goals.ModBreedGoal;
import com.bokmcdok.wheat.ai.goals.ModRaidFarmGoal;
import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.entity.ModEntityRegistrar;
import com.bokmcdok.wheat.entity.creature.animal.cornsnake.ModCornsnakeEntity;
import com.bokmcdok.wheat.tag.ModTagRegistrar;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;

public class ModMouseEntity extends AnimalEntity implements IUsesTags {
    private ModTagRegistrar mTagRegistrar;

    /**
     * Construction
     * @param type The type of the entity
     * @param world The current world
     */
    public ModMouseEntity(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
    }

    /**
     * Register the AI behaviours of the entity.
     */
    @Override
    protected void registerGoals() {
        Set<Block> blocksToRaid = mTagRegistrar.getBlockTag("docwheat:crop").getBlocks();
        blocksToRaid.add(ModBlockUtils.seeded_mouse_trap);

        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, getSpeed()));
        goalSelector.addGoal(2, new ModBreedGoal(this, getSpeed()));
        goalSelector.addGoal(3, new TemptGoal(this, getSpeed(), Ingredient.fromItems(Items.CARROT, Items.GOLDEN_CARROT, Blocks.DANDELION), false));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, getSpeed(), getSpeed()));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, WolfEntity.class, 10.0F, getSpeed(), getSpeed()));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, MonsterEntity.class, 4.0F, getSpeed(), getSpeed()));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, CatEntity.class, 4.0F, getSpeed(), getSpeed()));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, VillagerEntity.class, 4.0F, getSpeed(), getSpeed()));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, ModCornsnakeEntity.class, 4.0F, getSpeed(), getSpeed()));
        goalSelector.addGoal(5, new ModRaidFarmGoal(this, blocksToRaid, getSpeed(), 16, 1));
        goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, getSpeed()));
        goalSelector.addGoal(11, new LookAtGoal(this, PlayerEntity.class, 10.0F));
    }

    /**
     * Create a child version of the entity
     * @param ageable The entity to create a child for.
     * @return The child entity.
     */
    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return ModEntityRegistrar.field_mouse.create(world);
    }

    /**
     * Register the attributes of the entity.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0d);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6d);
    }

    /**
     * Get the entity's ambient sound.
     * @return The ambient sound to play.
     */
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_RABBIT_AMBIENT;
    }

    /**
     * Get the entity's hurt sound.
     * @param damageSource The source of the damage.
     * @return The damage sound to play.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_RABBIT_HURT;
    }

    /**
     * Get the entity's death sound.
     * @return The sound to play on death.
     */
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_RABBIT_DEATH;
    }

    /**
     * Get the sound category for this creature.
     * @return The sound category for the entity.
     */
    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
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
    public static boolean canSpawn(EntityType<ModMouseEntity> entity, IWorld world, SpawnReason reason, BlockPos position, Random random) {
        Block block = world.getBlockState(position.down()).getBlock();
        return block == Blocks.GRASS_BLOCK && world.getNeighborAwareLightSubtracted(position, 0) > 8;
    }

    /**
     * Give access to tags
     * @param tagRegistrar The tag registrar to use.
     */
    @Override
    public void setTagRegistrar(ModTagRegistrar tagRegistrar) {
        mTagRegistrar = tagRegistrar;
    }

    /**
     * Get the speed of the mouse.
     * @return The speed at which the mouse moves.
     */
    private double getSpeed() {
        return getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
    }
}
