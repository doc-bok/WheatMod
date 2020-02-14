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
    private final MemoryModuleType<LivingEntity> mInteractionTarget;
    private Set<Item> mShareableItems = ImmutableSet.of();

    public ModShareItemsTask(ModVillagerFood villagerFood, ModVillagerItems villagerItems, MemoryModuleType<LivingEntity> interactionTarget) {
        super(ImmutableMap.of(interactionTarget, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_MOBS, MemoryModuleStatus.VALUE_PRESENT));
        mVillagerFood = villagerFood;
        mVillagerItems = villagerItems;
        mInteractionTarget = interactionTarget;
    }

    @Override
    protected boolean shouldExecute(ServerWorld world, VillagerEntity villager) {
        return BrainUtil.isCorrectVisibleType(villager.getBrain(), mInteractionTarget, EntityType.VILLAGER);
    }

    @Override
    protected boolean shouldContinueExecuting(ServerWorld world, VillagerEntity villager, long gameTime) {
        return shouldExecute(world, villager);
    }

    @Override
    protected void startExecuting(ServerWorld world, VillagerEntity villager, long gameTime) {
        VillagerEntity interactionTarget = (VillagerEntity)villager.getBrain().getMemory(mInteractionTarget).get();
        BrainUtil.lookApproachEachOther(villager, interactionTarget);
        mShareableItems = mVillagerItems.getSharableItems(villager, interactionTarget);
    }

    @Override
    protected void updateTask(ServerWorld world, VillagerEntity villager, long gameTime) {
        VillagerEntity interactionTarget = (VillagerEntity)villager.getBrain().getMemory(mInteractionTarget).get();
        if (!(villager.getDistanceSq(interactionTarget) > 5.0D)) {
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

    private static void shareItems(VillagerEntity villager, Set<Item> items, LivingEntity interactionTarget) {
        Inventory inventory = villager.getVillagerInventory();
        ItemStack itemstack = ItemStack.EMPTY;
        int i = 0;

        while(i < inventory.getSizeInventory()) {
            ItemStack itemstack1;
            Item item;
            int j;
            label28: {
                itemstack1 = inventory.getStackInSlot(i);
                if (!itemstack1.isEmpty()) {
                    item = itemstack1.getItem();
                    if (items.contains(item)) {
                        if (itemstack1.getCount() > itemstack1.getMaxStackSize() / 2) {
                            j = itemstack1.getCount() / 2;
                            break label28;
                        }

                        if (itemstack1.getCount() > 24) {
                            j = itemstack1.getCount() - 24;
                            break label28;
                        }
                    }
                }

                ++i;
                continue;
            }

            itemstack1.shrink(j);
            itemstack = new ItemStack(item, j);
            break;
        }

        if (!itemstack.isEmpty()) {
            BrainUtil.throwItemAt(villager, itemstack, interactionTarget);
        }

    }
}
