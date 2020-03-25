package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.entity.tile.ModCampfireTileEntity;
import com.bokmcdok.wheat.tag.ModTag;
import com.bokmcdok.wheat.tag.ModTagRegistrar;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class ModCampfireBlock extends CampfireBlock implements IModBlock {
    protected final ModBlockImpl mImpl;
    private final ModTagRegistrar mTagRegistrar;

    /**
     * Construction
     * @param properties The block's properties.
     */
    public ModCampfireBlock(ModTagRegistrar tagRegistrar, ModBlockImpl.ModBlockProperties properties) {
        super(properties.asBlockProperties());
        mTagRegistrar = tagRegistrar;
        mImpl = new ModBlockImpl(properties);
    }

    /**
     * Get the initial block state on placement.
     * @param context The context in which the item is being used.
     * @return The new block state.
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IWorld world = context.getWorld();
        BlockPos position = context.getPos();
        boolean isInWater = world.getFluidState(position).getFluid() == Fluids.WATER;
        return getDefaultState()
                .with(WATERLOGGED, isInWater)
                .with(SIGNAL_FIRE, isHayBlock(world.getBlockState(position.down())))
                .with(LIT, !isInWater)
                .with(FACING, context.getPlacementHorizontalFacing());
    }

    /**
     * Update the block state after placement.
     * @param state The current block state.
     * @param facing The current facing of the block.
     * @param facingState The block state of the block this one is facing.
     * @param world The current world.
     * @param position The position of the block.
     * @param facingPosition The position of the block this one is facing.
     * @return The updated block state.
     */
    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos position, BlockPos facingPosition) {
        if (state.get(WATERLOGGED)) {
            world.getPendingFluidTicks().scheduleTick(position, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return facing == Direction.DOWN ? state.with(SIGNAL_FIRE, isHayBlock(facingState)) : super.updatePostPlacement(state, facing, facingState, world, position, facingPosition);
    }

    /**
     * Create a new tile entity for this block.
     * @param world The current world.
     * @return A new tile entity.
     */
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new ModCampfireTileEntity();
    }

    /**
     * Interact with the campfire - place meat on it for cooking.
     * @param state The block's state.
     * @param world The current world.
     * @param position The position of the world.
     * @param player The player interacting with the block.
     * @param hand The hand that is interacting.
     * @param blockRayTraceResult The result of the block raytrace.
     * @return The result of the action.
     */
    @Override
    public ActionResultType func_225533_a_(BlockState state, World world, BlockPos position, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        if (state.get(LIT)) {
            TileEntity tileentity = world.getTileEntity(position);
            if (tileentity instanceof ModCampfireTileEntity) {
                ModCampfireTileEntity ModCampfireTileEntity = (ModCampfireTileEntity)tileentity;
                ItemStack itemstack = player.getHeldItem(hand);
                Optional<CampfireCookingRecipe> optional = ModCampfireTileEntity.findMatchingRecipe(itemstack);
                if (optional.isPresent()) {
                    if (!world.isRemote && ModCampfireTileEntity.addItem(player.abilities.isCreativeMode ? itemstack.copy() : itemstack, optional.get().getCookTime())) {
                        player.addStat(Stats.INTERACT_WITH_CAMPFIRE);
                        return ActionResultType.SUCCESS;
                    }

                    return ActionResultType.CONSUME;
                }
            }
        }

        return ActionResultType.PASS;
    }

    /**
     * Drop any inventory items if the block is replaced.
     * @param state The block's state.
     * @param world The current world.
     * @param position The position of the block.
     * @param newState The new block state.
     * @param isMoving Is the block moving?
     */
    @Override
    public void onReplaced(BlockState state, World world, BlockPos position, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = world.getTileEntity(position);
            if (tileentity instanceof ModCampfireTileEntity) {
                InventoryHelper.dropItems(world, position, ((ModCampfireTileEntity)tileentity).getInventory());
            }

            super.onReplaced(state, world, position, newState, isMoving);
        }
    }

    /**
     * Handle fluids going over the campfire.
     * @param world The current world.
     * @param position The position of the block.
     * @param state The current block state.
     * @param fluidState The new fluid state.
     * @return TRUE if the block state changes.
     */
    @Override
    public boolean receiveFluid(IWorld world, BlockPos position, BlockState state, IFluidState fluidState) {
        if (!state.get(BlockStateProperties.WATERLOGGED) && fluidState.getFluid() == Fluids.WATER) {
            boolean flag = state.get(LIT);
            if (flag) {
                if (world.isRemote()) {
                    for(int i = 0; i < 20; ++i) {
                        spawnSmokeParticles(world.getWorld(), position, state.get(SIGNAL_FIRE), true);
                    }
                } else {
                    world.playSound((PlayerEntity)null, position, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                TileEntity tileentity = world.getTileEntity(position);
                if (tileentity instanceof ModCampfireTileEntity) {
                    ((ModCampfireTileEntity)tileentity).dropAllItems();
                }
            }

            world.setBlockState(position, state.with(WATERLOGGED, Boolean.TRUE).with(LIT, Boolean.FALSE), 3);
            world.getPendingFluidTicks().scheduleTick(position, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the block color.
     * @return The color of the block.
     */
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

    /**
     * Get the flammability of the block.
     * @return How flammable the block is.
     */
    @Override
    public int getFlammability() {
        return mImpl.getFlammability();
    }

    /**
     * Get how much fire is encouraged by the block.
     * @return The fire encouragement.
     */
    @Override
    public int getFireEncouragement() {
        return mImpl.getFlammability();
    }

    @Override
    public ModBlockImpl getImpl() {
        return mImpl;
    }

    /**
     * Test if this block is a hay/straw bale.
     * @param state The block state to check.
     * @return TRUE if this block is a hay/straw bale.
     */
    private boolean isHayBlock(BlockState state) {
        ModTag tag = mTagRegistrar.getBlockTag("docwheat:hay_bale");
        return tag.containsBlock(state.getBlock());
    }
}
