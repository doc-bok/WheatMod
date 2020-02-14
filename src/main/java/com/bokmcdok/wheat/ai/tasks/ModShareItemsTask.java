package com.bokmcdok.wheat.ai.tasks;

import com.bokmcdok.wheat.entity.creature.villager.ModVillagerItems;
import com.bokmcdok.wheat.entity.creature.villager.food.ModVillagerFood;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;

import java.util.Set;

public class ModShareItemsTask extends Task<VillagerEntity> {
    private final ModVillagerFood mVillagerFood;
    private final ModVillagerItems mVillagerItems;
    private final MemoryModuleType<LivingEntity> mTargetMemory;
    private Set<Item> mShareableItems = ImmutableSet.of();

    /**
     * Construction
     * @param villagerFood Provides functions for handling villager food.
     * @param villagerItems Provides functions for handling villager items.
     * @param targetMemory The memory module for the interaction target.
     */
    public ModShareItemsTask(ModVillagerFood villagerFood, ModVillagerItems villagerItems, MemoryModuleType<LivingEntity> targetMemory) {
        super(ImmutableMap.of(targetMemory, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_MOBS, MemoryModuleStatus.VALUE_PRESENT));
        mVillagerFood = villagerFood;
        mVillagerItems = villagerItems;
        mTargetMemory = targetMemory;
    }

    /**
     * Execute if a villager is the current interaction target.
     * @param world The current world.
     * @param villager The villager.
     * @return TRUE if the task should execute.
     */
    @Override
    protected boolean shouldExecute(ServerWorld world, VillagerEntity villager) {
        return BrainUtil.isCorrectVisibleType(villager.getBrain(), mTargetMemory, EntityType.VILLAGER);
    }

    /**
     * Continue executing if a villager is the current interaction target.
     * @param world The current world.
     * @param villager The villager.
     * @return TRUE if the task should execute.
     */
    @Override
    protected boolean shouldContinueExecuting(ServerWorld world, VillagerEntity villager, long gameTime) {
        return shouldExecute(world, villager);
    }

    /**
     * Approach the target and get shareable items.
     * @param world The current world.
     * @param villager The villager.
     * @param gameTime The current game time.
     */
    @Override
    protected void startExecuting(ServerWorld world, VillagerEntity villager, long gameTime) {
        VillagerEntity interactionTarget = (VillagerEntity)villager.getBrain().getMemory(mTargetMemory).get();
        BrainUtil.lookApproachEachOther(villager, interactionTarget);
        mShareableItems = mVillagerItems.getSharableItems(villager, interactionTarget);
    }

    /**
     * If the villagers are close enough then share items.
     * @param world The current world.
     * @param villager The villager.
     * @param gameTime The current game time.
     */
    @Override
    protected void updateTask(ServerWorld world, VillagerEntity villager, long gameTime) {
        VillagerEntity interactionTarget = (VillagerEntity)villager.getBrain().getMemory(mTargetMemory).get();
        if (villager.getDistanceSq(interactionTarget) <= 5.0D) {
            BrainUtil.lookApproachEachOther(villager, interactionTarget);
            villager.func_213746_a(interactionTarget, gameTime);
            if (mVillagerFood.canAbandonItems(villager) && (villager.getVillagerData().getProfession() == VillagerProfession.FARMER || mVillagerFood.wantsMoreFood(interactionTarget))) {
                shareItems(villager, mVillagerFood.getFoodItems(), interactionTarget);
            }

            if (!mShareableItems.isEmpty() && villager.getVillagerInventory().hasAny(mShareableItems)) {
                shareItems(villager, mShareableItems, interactionTarget);
            }

        }
    }

    /**
     * Share items with the target.
     * @param villager The villager.
     * @param items The set of items to share.
     * @param interactionTarget The target.
     */
    private static void shareItems(VillagerEntity villager, Set<Item> items, LivingEntity interactionTarget) {
        Inventory inventory = villager.getVillagerInventory();
        ItemStack itemStackToThrow = ItemStack.EMPTY;

        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                if (items.contains(item)) {
                    if (stack.getCount() > stack.getMaxStackSize() / 2) {
                        int numToThrow = stack.getCount() / 2;
                        itemStackToThrow = new ItemStack(item, numToThrow);
                        stack.shrink(numToThrow);
                        break;
                    }

                    if (stack.getCount() > 24) {
                        int numToThrow = stack.getCount() - 24;
                        itemStackToThrow = new ItemStack(item, numToThrow);
                        stack.shrink(numToThrow);
                        break;
                    }
                }
            }
        }

        if (!itemStackToThrow.isEmpty()) {
            BrainUtil.throwItemAt(villager, itemStackToThrow, interactionTarget);
        }

    }
}
