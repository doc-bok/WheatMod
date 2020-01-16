package com.bokmcdok.wheat.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Random;

public class ModNestBlock extends ModBlock {

    /**
     * Construction
     * @param properties The properties for the block.
     */
    public ModNestBlock(ModBlockImpl.ModBlockProperties properties) {
        super(properties);
    }

    /**
     * Tick update - check for babies spawning.
     * @param state The current block statxe
     * @param world The world the block is in
     * @param position The position of the block
     * @param random The current RNG
     */
    @Override
    public void func_225534_a_(BlockState state, ServerWorld world, BlockPos position, Random random) {
        super.func_225534_a_(state, world, position, random);

        ModNestProperties nestProperties = mImpl.getNestProperties();
        if (random.nextInt((int)(25.0F / nestProperties.getSpawnChance()) + 1) == 0) {
            world.setBlockState(position, Blocks.AIR.getDefaultState(), 2);
            world.playEvent(2001, position, Block.getStateId(state));

            //  Spawn babies
            int minimum = nestProperties.getMinimum();
            int maximum = nestProperties.getMaximum();
            int count = minimum;
            if (minimum != maximum) {
                count += random.nextInt(maximum - minimum + 1);
            }

            EntityType<?> entityType = getEntityType();
            for (int i = 0; i < count; i++) {
                AgeableEntity child = (AgeableEntity)entityType.create(world);
                if (child != null) {
                    child.setGrowingAge(-24000);
                    child.setLocationAndAngles(position.getX(), position.getY() + 1, position.getZ(), 0.0f, 0.0f);
                    world.addEntity(child);
                }
            }
        }
    }

    /**
     * Called when an entity walks over the nest.
     * @param world The current world.
     * @param position The position of the nest.
     * @param entity The entity that is walking over the nest.
     */
    @Override
    public void onEntityWalk(World world, BlockPos position, Entity entity) {
        tryTrample(world, position, entity, 100);
        super.onEntityWalk(world, position, entity);
    }

    /**
     * Called when an entity falls on the nest.
     * @param world The current world.
     * @param position The position of the nest.
     * @param entity The entity that fell on the nest.
     * @param fallDistance The distance fell.
     */
    @Override
    public void onFallenUpon(World world, BlockPos position, Entity entity, float fallDistance) {
        if (!(entity instanceof ZombieEntity)) {
            tryTrample(world, position, entity, 3);
        }
        super.onFallenUpon(world, position, entity, fallDistance);
    }

    /**
     * Try and trample the nest.
     * @param world The current world.
     * @param position The position of the nest.
     * @param entity The entity trampling.
     * @param destructProbability The chance that the nest is destroyed.
     */
    private void tryTrample(World world, BlockPos position, Entity entity, int destructProbability) {
        if (!canTrample(world, entity)) {
            super.onEntityWalk(world, position, entity);
        } else if (!world.isRemote && world.rand.nextInt(destructProbability) == 0) {
            world.destroyBlock(position, false);
        }
    }

    /**
     * Check if an entity can trample the nest.
     * @param world The current world.
     * @param trampler The entity trampling.
     * @return TRUE if the nest can be trampled.
     */
    private boolean canTrample(World world, Entity trampler) {
        if (trampler.getType() == getEntityType()) {
            return false;
        } else {
            return !(trampler instanceof LivingEntity) ||
                    trampler instanceof PlayerEntity ||
                    net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(world, trampler);
        }
    }

    /**
     * Get the entity type spawned by this nest.
     * @return The type of entity to spawn.
     */
    private EntityType<?> getEntityType() {
        IForgeRegistry<EntityType<?>> entityRegistry = RegistryManager.ACTIVE.getRegistry(GameData.ENTITIES);
        return entityRegistry.getValue(mImpl.getNestProperties().getEntityToSpawn());
    }
}
