package com.bokmcdok.wheat.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
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

            IForgeRegistry<EntityType<?>> entityRegistry = RegistryManager.ACTIVE.getRegistry(GameData.ENTITIES);
            EntityType<?> entityType = entityRegistry.getValue(nestProperties.getEntityToSpawn());
            for (int i = 0; i < count; i++) {
                AgeableEntity child = (AgeableEntity)entityType.create(world);
                child.setGrowingAge(-24000);
                child.setLocationAndAngles(position.getX(), position.getY() + 1, position.getZ(), 0.0f, 0.0f);
                world.addEntity(child);
            }
        }
    }
}
