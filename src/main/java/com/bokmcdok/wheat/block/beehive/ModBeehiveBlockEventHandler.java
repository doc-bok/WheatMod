package com.bokmcdok.wheat.block.beehive;

import com.bokmcdok.wheat.tag.ModTag;
import com.bokmcdok.wheat.tag.ModTagDataManager;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

public class ModBeehiveBlockEventHandler {
    private static final VoxelShape CAMPFIRE_COLLISION_BOX = Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private final ModTagDataManager mBlockTagDataManager;

    /**
     * Construction
     * @param blockTagDataManager The data manager for block tags.
     */
    public ModBeehiveBlockEventHandler(ModTagDataManager blockTagDataManager) {
        mBlockTagDataManager = blockTagDataManager;
    }

    /**
     * Handle the beehive being used. Intercepted so we can use different blocks as campfires.
     * @param event The event data.
     * @param world The current world.
     * @param position The block's position.
     * @param state The block state.
     * @param block The block.
     */
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event, World world, BlockPos position, BlockState state, Block block) {
        PlayerEntity player = event.getPlayer();
        Hand hand = event.getHand();
        ItemStack heldItem = player.getHeldItem(hand);
        ItemStack copy = heldItem.copy();
        int honeyLevel = state.get(BlockStateProperties.field_227036_ao_);
        boolean actionSucceeded = false;
        if (honeyLevel >= 5) {
            if (heldItem.getItem() == Items.SHEARS) {
                world.playSound(player, player.func_226277_ct_(), player.func_226278_cu_(), player.func_226281_cx_(), SoundEvents.field_226133_ah_, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                BeehiveBlock.func_226878_a_(world, position);
                heldItem.damageItem(1, player, (x) -> {
                    x.sendBreakAnimation(hand);
                });
                actionSucceeded = true;
            } else if (heldItem.getItem() == Items.GLASS_BOTTLE) {
                heldItem.shrink(1);
                world.playSound(player, player.func_226277_ct_(), player.func_226278_cu_(), player.func_226281_cx_(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                if (heldItem.isEmpty()) {
                    player.setHeldItem(hand, new ItemStack(Items.field_226638_pX_));
                } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.field_226638_pX_))) {
                    player.dropItem(new ItemStack(Items.field_226638_pX_), false);
                }

                actionSucceeded = true;
            }

            if (actionSucceeded) {
                if (!isCampfireBelow(world, position, 5)) {
                    if (containsBees(world, position)) {
                        releaseBees(world, position);
                    }

                    ((BeehiveBlock)block).func_226877_a_(world, state, position, player, BeehiveTileEntity.State.EMERGENCY);
                } else {
                    ((BeehiveBlock)block).func_226876_a_(world, state, position);
                    if (player instanceof ServerPlayerEntity) {
                        CriteriaTriggers.field_229863_J_.func_226695_a_((ServerPlayerEntity) player, position, copy);
                    }
                }

                event.setCanceled(true);
                event.setCancellationResult(ActionResultType.SUCCESS);
            }
        }
    }

    /**
     * Check if there is a campfire below this block.
     * @param world The current world.
     * @param position The block's position.
     * @param distance The distance down to check.
     * @return TRUE if there is a campfire below.
     */
    private boolean isCampfireBelow(World world, BlockPos position, int distance) {
        for(int i = 1; i <= distance; ++i) {
            BlockPos downPosition = position.down(i);
            BlockState downBlock = world.getBlockState(downPosition);
            if (isCampfire(downBlock)) {
                return true;
            }

            boolean colliding = VoxelShapes.compare(CAMPFIRE_COLLISION_BOX, downBlock.getCollisionShape(world, position, ISelectionContext.dummy()), IBooleanFunction.AND);
            if (colliding) {
                BlockState lowerBlock = world.getBlockState(downPosition.down());
                return isCampfire(lowerBlock);
            }
        }

        return false;
    }

    /**
     * Check if this block is a campfire.
     * @param state The block state.
     * @return TRUE if the block is a campfire.
     */
    private boolean isCampfire(BlockState state) {
        ModTag tag = mBlockTagDataManager.getEntry("docwheat:campfire");
        Block block = state.getBlock();
        ResourceLocation registryName = block.getRegistryName();
        return tag.contains(registryName);
    }

    /**
     * Check if the block contains bees.
     * @param world The current world.
     * @param position The position of the block.
     * @return TRUE if the block contains bees.
     */
    private boolean containsBees(World world, BlockPos position) {
        TileEntity tileEntity = world.getTileEntity(position);
        if (tileEntity instanceof BeehiveTileEntity) {
            BeehiveTileEntity beehiveTileEntity = (BeehiveTileEntity)tileEntity;
            return !beehiveTileEntity.func_226969_f_();
        } else {
            return false;
        }
    }

    /**
     * Releases all bees from the specified block.
     * @param world The current world.
     * @param position The position of the block.
     */
    private void releaseBees(World world, BlockPos position) {
        List<BeeEntity> entities = world.getEntitiesWithinAABB(BeeEntity.class, (new AxisAlignedBB(position)).grow(8.0D, 6.0D, 8.0D));
        if (!entities.isEmpty()) {
            List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, (new AxisAlignedBB(position)).grow(8.0D, 6.0D, 8.0D));
            int i = players.size();

            for(BeeEntity beeEntity : entities) {
                if (beeEntity.getAttackTarget() == null) {
                    beeEntity.func_226391_a_(players.get(world.rand.nextInt(i)));
                }
            }
        }
    }
}
