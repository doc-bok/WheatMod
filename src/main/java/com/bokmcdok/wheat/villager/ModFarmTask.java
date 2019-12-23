package com.bokmcdok.wheat.villager;

import com.bokmcdok.wheat.villager.VillagerUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class ModFarmTask extends Task<VillagerEntity> {
    @Nullable
    private BlockPos mPosition;
    private boolean mIsFarmItemInInventory;
    private boolean mHasInventorySpace;
    private long mCooldown;
    private int mDuration;
    private final List<BlockPos> mFarmableBlocks = Lists.newArrayList();

    public ModFarmTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleStatus.VALUE_PRESENT));
    }

    protected boolean shouldExecute(ServerWorld worldIn, VillagerEntity owner) {
        if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(worldIn, owner)) {
            return false;
        } else if (owner.getVillagerData().getProfession() != VillagerProfession.FARMER) {
            return false;
        } else {
            mIsFarmItemInInventory = VillagerUtils.isFarmItemInInventory(owner);
            mHasInventorySpace = mIsFarmItemInInventory;

            if (!mHasInventorySpace) {
                Inventory inventory = owner.func_213715_ed(); // getInventory
                int i = inventory.getSizeInventory();

                for (int j = 0; j < i; ++j) {
                    ItemStack itemstack = inventory.getStackInSlot(j);
                    if (itemstack.isEmpty()) {
                        mHasInventorySpace = true;
                        break;
                    }
                }
            }

            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(owner.posX, owner.posY, owner.posZ);
            mFarmableBlocks.clear();

            for (int i1 = -1; i1 <= 1; ++i1) {
                for (int k = -1; k <= 1; ++k) {
                    for (int l = -1; l <= 1; ++l) {
                        blockpos$mutableblockpos.setPos(owner.posX + (double) i1, owner.posY + (double) k, owner.posZ + (double) l);
                        if (isFarmableBlock(blockpos$mutableblockpos, worldIn)) {
                            mFarmableBlocks.add(new BlockPos(blockpos$mutableblockpos));
                        }
                    }
                }
            }

            mPosition = getRandomFarmableBlock(worldIn);
            return (mIsFarmItemInInventory || mHasInventorySpace) && mPosition != null;
        }
    }

    @Nullable
    private BlockPos getRandomFarmableBlock(ServerWorld world) {
        return mFarmableBlocks.isEmpty() ? null : mFarmableBlocks.get(world.getRandom().nextInt(mFarmableBlocks.size()));
    }

    private boolean isFarmableBlock(BlockPos position, ServerWorld world) {
        BlockState blockstate = world.getBlockState(position);
        Block block = blockstate.getBlock();
        Block block1 = world.getBlockState(position.down()).getBlock();
        return block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(blockstate) && mHasInventorySpace ||
               blockstate.isAir() && block1 instanceof FarmlandBlock && mIsFarmItemInInventory;
    }

    protected void startExecuting(ServerWorld worldIn, VillagerEntity entityIn, long gameTimeIn) {
        if (gameTimeIn > mCooldown && mPosition != null) {
            entityIn.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosWrapper(mPosition));
            entityIn.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosWrapper(mPosition), 0.5F, 1));
        }

    }

    protected void resetTask(ServerWorld worldIn, VillagerEntity entityIn, long gameTimeIn) {
        entityIn.getBrain().removeMemory(MemoryModuleType.LOOK_TARGET);
        entityIn.getBrain().removeMemory(MemoryModuleType.WALK_TARGET);
        mDuration = 0;
        mCooldown = gameTimeIn + 40L;
    }

    protected void updateTask(ServerWorld worldIn, VillagerEntity owner, long gameTime) {
        if (mPosition != null && gameTime > mCooldown) {
            BlockState blockstate = worldIn.getBlockState(mPosition);
            Block block = blockstate.getBlock();
            Block block1 = worldIn.getBlockState(mPosition.down()).getBlock();
            if (block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(blockstate) && mHasInventorySpace) {
                worldIn.destroyBlock(mPosition, true);
            }

            if (blockstate.isAir() && block1 instanceof FarmlandBlock && mIsFarmItemInInventory) {
                Inventory inventory = owner.func_213715_ed();

                for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                    ItemStack itemstack = inventory.getStackInSlot(i);
                    if (!itemstack.isEmpty()) {
                        Block cropBlock = VillagerUtils.getCropBlock(itemstack.getItem());
                        if (cropBlock != null) {
                            worldIn.setBlockState(mPosition, cropBlock.getDefaultState(), 3);
                            worldIn.playSound((PlayerEntity) null, (double) mPosition.getX(), (double) mPosition.getY(), (double) mPosition.getZ(), SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                            }

                            break;
                        }
                    }
                }
            }

            if (block instanceof CropsBlock && !((CropsBlock) block).isMaxAge(blockstate)) {
                mFarmableBlocks.remove(mPosition);
                mPosition = getRandomFarmableBlock(worldIn);
                if (mPosition != null) {
                    mCooldown = gameTime + 20L;
                    owner.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosWrapper(mPosition), 0.5F, 1));
                    owner.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosWrapper(mPosition));
                }
            }
        }

        ++mDuration;
    }

    protected boolean shouldContinueExecuting(ServerWorld worldIn, VillagerEntity entityIn, long gameTimeIn) {
        return mDuration < 200;
    }
}
