package com.bokmcdok.wheat.entity.creature.feldgeister.haferbock;

import com.bokmcdok.wheat.ai.goals.ModMoveToBlockGoal;
import com.bokmcdok.wheat.entity.creature.feldgeister.ModFeldgeisterEntity;
import com.bokmcdok.wheat.supplier.ModBlockSupplier;
import com.bokmcdok.wheat.tag.ModTagRegistrar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;

public class ModHaferbockEntity extends ModFeldgeisterEntity {
    private static LazyValue<Block> WILD_EINKORN = new LazyValue<>(new ModBlockSupplier("docwheat:wild_einkorn"));
    private static LazyValue<Block> WILD_EMMER = new LazyValue<>(new ModBlockSupplier("docwheat:wild_emmer"));
    private static LazyValue<Block> EINKORN = new LazyValue<>(new ModBlockSupplier("docwheat:einkorn"));
    private static LazyValue<Block> EMMER = new LazyValue<>(new ModBlockSupplier("docwheat:emmer"));
    private static LazyValue<Block> COMMON_WHEAT = new LazyValue<>(new ModBlockSupplier("docwheat:common_wheat"));
    private static LazyValue<Block> DURUM = new LazyValue<>(new ModBlockSupplier("docwheat:durum"));
    private static LazyValue<Block> SPELT = new LazyValue<>(new ModBlockSupplier("docwheat:spelt"));

    private ModTagRegistrar mTagRegistrar;
    private boolean mCried;

    /**
     * Construction
     * @param type  The type of this entity.
     * @param world The current world.
     */
    public ModHaferbockEntity(EntityType<? extends ModHaferbockEntity> type, World world) {
        super(type, world);
    }

    /**
     * Haferbocks cause disease in any wheat they touch. Every time it makes a sound it will pollinate nearby crops.
     * During thunderstorms haferbocks will plant wheat.
     */
    @Override
    public void livingTick() {
        super.livingTick();

        if (world.isThundering()) {
            plantWheat();
        } else {
            affectTouchedCrops();
        }

        if (mCried) {
            mCried = false;
            pollinateNearbyCrops();
        }
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
     * Get the height of the entity's eyes.
     * @param pose The current pose.
     * @param size The size of the entity.
     * @return The height of the entity's eyes.
     */
    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return 1.3f;
    }

    /**
     * Register the behaviours of the haferbock. Haferbock will look through windows and target children.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(5, new ModMoveToBlockGoal(this, "docwheat:glass_pane", getSpeed(), 16, 1));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, 10, false, false, IS_CHILD));
    }

    /**
     * Play a step sound.
     *
     * @param position The current block position.
     * @param state    The current block state.
     */
    @Override
    protected void playStepSound(BlockPos position, BlockState state) {
        playSound(SoundEvents.ENTITY_COW_STEP, 0.3f, 0.5f);
    }

    /**
     * Get the ambient sound.
     *
     * @return The ambient sound of the entity.
     */
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        mCried = true;
        return SoundEvents.ENTITY_COW_AMBIENT;
    }

    /**
     * Get the hurt sound.
     * @param source The source of the damage.
     * @return The sound to play when this entity is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_COW_DEATH;
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
     * Haferbocks have more health and can do some damage.
     */
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0d);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224d);
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0f);
    }

    /**
     * Plants wheat on any block the haferbock touches.
     */
    private void plantWheat() {
        Vec3d position = getPositionVec();
        for (int l = 0; l < 4; ++l) {
            int i = MathHelper.floor(position.x + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
            int j = MathHelper.floor(position.y);
            int k = MathHelper.floor(position.z + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
            BlockPos blockPosition = new BlockPos(i, j, k);

            Biome.Category biome = world.func_226691_t_(blockPosition).getCategory();
            if (biome == Biome.Category.PLAINS ||
                biome == Biome.Category.FOREST ||
                biome == Biome.Category.RIVER ||
                biome == Biome.Category.SWAMP) {

                BlockState blockState = world.getBlockState(blockPosition);
                boolean isEinkorn = biome == Biome.Category.PLAINS || biome == Biome.Category.FOREST;
                if (blockState.isAir(world, blockPosition)) {
                    plantWheat(blockPosition, isEinkorn ? WILD_EINKORN.getValue() : WILD_EMMER.getValue());
                } else if (blockState.getBlock() == Blocks.FARMLAND) {
                    plantWheat(blockPosition.up(), getWheatToPlant(isEinkorn));
                }
            }
        }
    }

    /**
     * Get the wheat to plant based on biome type.
     * @param isEinkorn      TRUE if we are in a biome that grows wild einkorn.
     * @return The block type to create.
     */
    private Block getWheatToPlant(boolean isEinkorn) {
        int random = world.getRandom().nextInt(12);
        if (random < 2) {
            return SPELT.getValue();
        } else if (random < 6) {
            return isEinkorn ? EINKORN.getValue() : EMMER.getValue();
        } else {
            return isEinkorn ? COMMON_WHEAT.getValue() : DURUM.getValue();
        }
    }

    /**
     * Update the block state with the new wheat type.
     *
     * @param position The position to update.
     * @param wheat    The wheat block to place.
     */
    private void plantWheat(BlockPos position, Block wheat) {
        if (wheat != null) {
            world.setBlockState(position, wheat.getDefaultState().with(CropsBlock.AGE, 0));
        }
    }

    /**
     * Finds nearby crops and pollinates them.
     */
    private void pollinateNearbyCrops() {
        Vec3d position = getPositionVec();
        for (int x = -2; x < 2 + 1; x++) {
            for (int y = -2; y < 2; y++) {
                for (int z = -2; z < 2; z++) {
                    attemptPollinate(new BlockPos(position.x + x, position.y + y, position.z + z));
                }
            }
        }
    }

    /**
     * Attempt to pollinate a block at a specified position.
     * @param position The position to check.
     */
    private void attemptPollinate(BlockPos position) {
        BlockState state = world.getBlockState(position);
        Block block = state.getBlock();
        if (block instanceof CropsBlock) {
            CropsBlock crop = (CropsBlock) block;
            int age = state.get(crop.getAgeProperty());
            if (age < crop.getMaxAge()) {
                world.setBlockState(position, state.with(crop.getAgeProperty(), age + 1), 2);
                world.playEvent(2001, position, Block.getStateId(state));
            }
        }
    }
}
