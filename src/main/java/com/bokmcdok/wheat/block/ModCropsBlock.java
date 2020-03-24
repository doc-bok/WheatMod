package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.supplier.ModTagSupplier;
import com.bokmcdok.wheat.tag.ModTag;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.LazyValue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class ModCropsBlock extends CropsBlock implements IModBlock {
    private static final String MUSHROOM = "mushroom";
    private static final LazyValue<ModTag> MUSHROOM_TAG = new LazyValue<>(new ModTagSupplier(WheatMod.MOD_ID, MUSHROOM));

    private ModBlockImpl mImpl;
    private ModCropProperties mCropProperties;

    public ModCropsBlock(ModBlockImpl.ModBlockProperties properties) {
        super(properties.asBlockProperties());
        mImpl = new ModBlockImpl(properties);
        mCropProperties = mImpl.getCropProperties();
    }

    @Override
    public IBlockColor getColor() {
        return mImpl.getColor();
    }

    /**
     * Get the render type's name.
     * @return The name of the render type.
     */
    @Override
    public String getRenderType() {
        return mImpl.getRenderType();
    }

    @Override
    public int getFlammability() {
        return mImpl.getFlammability();
    }

    @Override
    public int getFireEncouragement() {
        return mImpl.getFireEncouragement();
    }

    /**
     * Main update - checks for disease and mutations
     * @param state The current block state
     * @param world The world the block is in
     * @param pos The position of the block
     * @param random The current RNG
     */
    @Override
    public void func_225534_a_(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int oldAge = getAge(state);
        super.func_225534_a_(state, world, pos, random);

        //  If a crop is generated in the air it will be destroyed during the tick update and replaced with an air
        //  block. In this case we don't want to check for disease/mutation as the crop no longer exists and it will
        //  cause Minecraft to crash when it tries to get the Age property.
        BlockState newState = world.getBlockState(pos);
        if (newState.getBlock() != Blocks.AIR) {
            int newAge = newState.get(getAgeProperty());
            checkForDisease(world, pos, random, oldAge, newAge);
            checkForMutation(world, pos, random, oldAge, newAge, 15);
        }
    }

    /**
     * When grown using bonemeal there is zero chance of disease and a higher chance of mutation.
     * @param worldIn The world the block is in
     * @param pos The position of the block
     * @param state The current block state
     */
    @Override
    public void grow(World worldIn, BlockPos pos, BlockState state) {
        int oldAge = getAge(state);
        super.grow(worldIn, pos, state);
        BlockState newState = worldIn.getBlockState(pos);
        if (newState.getBlock() != Blocks.AIR) {
            int newAge = newState.get(getAgeProperty());
            checkForMutation(worldIn, pos, worldIn.getRandom(), oldAge, newAge, 10);
        }
    }

    /**
     * Turns the crop into a diseased crop.
     * @param world The current world.
     * @param position The position of the crop.
     * @param state The state of the crop.
     */
    public void diseaseCrop(World world, BlockPos position, BlockState state) {
        Block disease = mCropProperties.getDiseaseCrop();
        if (disease != null) {
            world.setBlockState(position, disease.getDefaultState().with(getAgeProperty(), getAge(state)));
        }
    }

    /**
     * Get the implementation.
     * @return The implementation object.
     */
    @Override
    public ModBlockImpl getImpl() {
        return mImpl;
    }

    /**
     * Allow wild wheat to also generate and grow on grass blocks.
     * @param state The block state of the ground
     * @param world The world the block is in
     * @param position The position of the block
     * @return TRUE if the crop can grow
     */
    @Override
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
                        Block required = mutation.getRequired();
                        if (required != null) {
                            if (!ModBlockUtils.isBlockPresent(world, position, required, 2)) {
                                return;
                            }
                        }

                        if (random.nextInt(rarity) < 1) {
                            Block mutated = mutation.getMutation();
                            if (mutated != null) {
                                world.setBlockState(position, mutated.getDefaultState().with(getAgeProperty(), newAge));
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkForDisease(World world, BlockPos position, Random random, int oldAge, int newAge) {
        if (oldAge != newAge) {
            Block disease = mCropProperties.getDiseaseCrop();
            if (disease != null) {
                int diseaseResistance = mCropProperties.getDiseaseResistance();

                int diseasedCrops = getTotalBlocksPresent(world, position, disease, 1);
                if (diseasedCrops > 1) {
                    diseaseResistance /= (int) Math.pow(2, diseasedCrops);
                }

                diseaseResistance -= 10 * getTotalBlocksPresent(world, position, MUSHROOM_TAG.getValue().getBlocks(), 1);
                diseaseResistance -= getTotalBlocksPresent(world, position, this, 1);

                if (diseaseResistance <= 1 || random.nextInt(diseaseResistance) < 1) {
                    world.setBlockState(position, disease.getDefaultState().with(getAgeProperty(), newAge));
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
        return mCropProperties.getSeed();
    }
}
