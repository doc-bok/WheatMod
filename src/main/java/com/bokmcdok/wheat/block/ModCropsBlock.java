package com.bokmcdok.wheat.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class ModCropsBlock extends CropsBlock implements IModBlock {
    private ModBlockImpl mImpl;
    private ModCropProperties mCropProperties;

    public ModCropsBlock(ModBlockImpl.ModBlockProperties properties) {
        super(properties.asBlockProperties());
        mImpl = new ModBlockImpl(properties);
        mCropProperties = mImpl.getCropProperties();
    }

    public IBlockColor getColor() {
        return mImpl.getColor();
    }

    public int getFlammability() {
        return mImpl.getFlammability();
    }

    public int getFireEncouragement() {
        return mImpl.getFireEncouragement();
    }

    /**
     * Main update - checks for disease and mutations
     * @param state The current block state
     * @param worldIn The world the block is in
     * @param pos The position of the block
     * @param random The current RNG
     */
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        int oldAge = getAge(state);
        super.tick(state, worldIn, pos, random);

        //  If a crop is generated in the air it will be destroyed during the tick update and replaced with an air
        //  block. In this case we don't want to check for disease/mutation as the crop no longer exists and it will
        //  cause Minecraft to crash when it tries to get the Age property.
        BlockState newState = worldIn.getBlockState(pos);
        if (newState.getBlock() != Blocks.AIR) {
            int newAge = newState.get(getAgeProperty());
            checkForDisease(worldIn, pos, random, oldAge, newAge);
            checkForMutation(worldIn, pos, random, oldAge, newAge, 15);
        }
    }

    /**
     * When grown using bonemeal there is zero chance of disease and a higher chance of mutation.
     * @param worldIn The world the block is in
     * @param random The current RNG
     * @param pos The position of the block
     * @param state The current block state
     */
    public void grow(World worldIn, Random random, BlockPos pos, BlockState state) {
        int oldAge = getAge(state);
        super.grow(worldIn, random, pos, state);
        BlockState newState = worldIn.getBlockState(pos);
        if (newState.getBlock() != Blocks.AIR) {
            int newAge = newState.get(getAgeProperty());
            checkForMutation(worldIn, pos, random, oldAge, newAge, 10);
        }
    }

    /**
     * Turns the crop into a diseased crop.
     * @param world The current world.
     * @param position The position of the crop.
     * @param state The state of the crop.
     */
    public void diseaseCrop(World world, BlockPos position, BlockState state) {
        ResourceLocation disease = mCropProperties.getDiseaseCrop();
        if (disease != null) {
            Optional<Block> diseaseBlock = Registry.BLOCK.getValue(disease);
            world.setBlockState(position, diseaseBlock.get().getDefaultState().with(getAgeProperty(), getAge(state)));
        }
    }

    /**
     * Allow wild wheat to also generate and grow on grass blocks.
     * @param state The block state of the ground
     * @param world The world the block is in
     * @param position The position of the block
     * @return TRUE if the crop can grow
     */
    protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos position)
    {
        if (mCropProperties.getWild()) {
            return state.getBlock() == Blocks.FARMLAND ||
                    state.getBlock() == Blocks.GRASS_BLOCK;
        }

        return  super.isValidGround(state, world, position);
    }

    private void checkForMutation(World world, BlockPos position, Random random, int oldAge, int newAge, int rarity) {
        if (oldAge <= 2 && newAge >= 3) {
            BlockPos groundPos = position.down();
            if (world.getBlockState(groundPos).getBlock() == Blocks.FARMLAND) {
                List<ModCropMutation> mutations = mCropProperties.getMutations();
                if (mutations.size() > 0) {

                    //  Check for a random mutation
                    int totalWeight = 0;
                    for (int i = 0; i < mutations.size(); i++) {
                        totalWeight += mutations.get(i).getWeight();
                    }

                    int decision = random.nextInt(totalWeight);

                    totalWeight = 0;
                    ModCropMutation mutation = null;
                    for (int i = 0; i < mutations.size(); i++) {
                        totalWeight += mutations.get(i).getWeight();
                        if (decision < totalWeight) {
                            mutation = mutations.get(i);
                            break;
                        }
                    }

                    if (mutation != null) {
                        if (mutation.getRequired() != null) {
                            Optional<Block> required = Registry.BLOCK.getValue(mutation.getRequired());
                            if (required.isPresent() && !ModBlockUtils.isBlockPresent(world, position, required.get(), 2)) {
                                return;
                            }
                        }

                        if (random.nextInt(rarity) < 1) {
                            Optional<Block> mutated = Registry.BLOCK.getValue(mutation.getMutation());
                            if (mutated.isPresent()) {
                                world.setBlockState(position, mutated.get().getDefaultState().with(getAgeProperty(), newAge));
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkForDisease(World world, BlockPos position, Random random, int oldAge, int newAge) {
        if (oldAge != newAge) {
            ResourceLocation disease = mCropProperties.getDiseaseCrop();
            if (disease != null) {
                Optional<Block> diseaseBlock = Registry.BLOCK.getValue(disease);
                if (diseaseBlock.isPresent()) {
                    int diseaseResistance = mCropProperties.getDiseaseResistance();

                    int diseasedCrops = getTotalBlocksPresent(world, position, diseaseBlock.get(), 1);
                    if (diseasedCrops > 1) {
                        diseaseResistance /= (int) Math.pow(2, diseasedCrops);
                    }

                    diseaseResistance -= 10 * getTotalBlocksPresent(world, position, ModBlockUtils.MUSHROOMS, 1);
                    diseaseResistance -= getTotalBlocksPresent(world, position, this, 1);

                    //diseaseResistance *= newAge;

                    if (diseaseResistance <= 1 || random.nextInt(diseaseResistance) < 1) {
                        world.setBlockState(position, diseaseBlock.get().getDefaultState().with(getAgeProperty(), newAge));
                    }
                }
            }
        }
    }

    private int getTotalBlocksPresent(World world, BlockPos position, Block block, int radius) {
        int total = 0;
        for (int x = (radius * -1); x < radius + 1; x++) {
            for (int z = (radius * -1); z < radius + 1; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }

                BlockPos posToCheck = position.add(x, 0, z);
                if(world.getBlockState(posToCheck).getBlock() == block) {
                    ++total;
                }
            }
        }

        return total;
    }

    private int getTotalBlocksPresent(World world, BlockPos position, Set<Block> blocks, int radius) {
        int total = 0;
        for (int x = (radius * -1); x < radius + 1; x++) {
            for (int z = (radius * -1); z < radius + 1; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }

                BlockPos posToCheck = position.add(x, 0, z);
                if(blocks.contains(world.getBlockState(posToCheck).getBlock())) {
                    ++total;
                }
            }
        }

        return total;
    }

    /**
     * Get the type of seed used to grow this block.
     * @return The seed item used to grow this crop.
     */
    protected  IItemProvider getSeedsItem() {
        ResourceLocation location = mCropProperties.getSeed();
        if (location != null) {
            return Registry.ITEM.getOrDefault(location);
        }

        return null;
    }
}
