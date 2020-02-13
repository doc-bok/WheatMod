package com.bokmcdok.wheat.entity.creature.villager;

import com.bokmcdok.wheat.ai.tasks.ModCreateFarmTask;
import com.bokmcdok.wheat.ai.tasks.ModFarmTask;
import com.bokmcdok.wheat.ai.tasks.ModGiveHeroGiftsTask;
import com.bokmcdok.wheat.ai.tasks.ModPickupFoodTask;
import com.bokmcdok.wheat.entity.creature.villager.crops.ModVillagerCrops;
import com.bokmcdok.wheat.entity.creature.villager.crops.ModVillagerCropsDataManager;
import com.bokmcdok.wheat.entity.creature.villager.food.ModVillagerFood;
import com.bokmcdok.wheat.entity.creature.villager.food.ModVillagerFoodDataManager;
import com.bokmcdok.wheat.entity.creature.villager.trade.ModVillagerTradeModifier;
import com.bokmcdok.wheat.entity.creature.villager.trade.ModVillagerTradeModifierDataManager;
import com.bokmcdok.wheat.tag.ModTagDataManager;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.FirstShuffledTask;
import net.minecraft.entity.ai.brain.task.SpawnGolemTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.WalkTowardsPosTask;
import net.minecraft.entity.ai.brain.task.WalkTowardsRandomSecondaryPosTask;
import net.minecraft.entity.ai.brain.task.WorkTask;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;

public class ModVillagerEventHandler {
    private final ModVillagerItems mVillagerItems;
    private final ModVillagerCrops mVillagerCrops;
    private final ModVillagerTradeModifierDataManager mTradeModifiers;

    /**
     * Construction
     */
    public ModVillagerEventHandler(ModTagDataManager itemTagDataManager) {
        mTradeModifiers = new ModVillagerTradeModifierDataManager();
        mTradeModifiers.loadDataEntries("villager/trades");

        ModVillagerFoodDataManager foodDataManager = new ModVillagerFoodDataManager();
        foodDataManager.loadDataEntries("villager/food");
        ModVillagerFood villagerFood = foodDataManager.getEntry("docwheat:food");

        ModVillagerCropsDataManager cropsDataManager = new ModVillagerCropsDataManager();
        cropsDataManager.loadDataEntries("villager/crops");
        mVillagerCrops = cropsDataManager.getEntry("docwheat:crops");

        mVillagerItems = new ModVillagerItems(itemTagDataManager, villagerFood, mVillagerCrops);

    }

    /**
     * Update the villagers to use modded AI that interacts with items from the mod.
     * @param event The event data.
     */
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.getEntity().getType() == EntityType.VILLAGER) {
            VillagerEntity villager = (VillagerEntity)event.getEntity();
            VillagerProfession profession = villager.getVillagerData().getProfession();

            Brain<VillagerEntity> brain = villager.getBrain();
            brain.registerActivity(Activity.WORK, work(profession));
            brain.registerActivity(Activity.MEET, meet());
            brain.registerActivity(Activity.IDLE, idle());
            brain.registerActivity(Activity.CORE, core());
        }
    }

    /**
     * Fired when a living entity does an update.
     * @param event The event data.
     */
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().getType() == EntityType.VILLAGER) {

            VillagerEntity villager = (VillagerEntity) event.getEntityLiving();
            if (!villager.world.isRemote &&
                villager.canPickUpLoot() &&
                villager.isAlive() &&
                net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(villager.world, villager)) {

                for (ItemEntity itemEntity : villager.world.getEntitiesWithinAABB(ItemEntity.class, villager.getBoundingBox().grow(1.0D, 0.0D, 1.0D))) {
                    if (!itemEntity.removed &&
                        !itemEntity.getItem().isEmpty() &&
                        !itemEntity.cannotPickup()) {

                        ItemStack itemStack = itemEntity.getItem();
                        if (mVillagerItems.canPickupItem(villager.getVillagerData().getProfession(), itemStack)) {
                            Item item = itemStack.getItem();
                            Inventory inventory = villager.getVillagerInventory();

                            boolean foundInventorySpace = false;
                            for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                                ItemStack inventoryItem = inventory.getStackInSlot(i);
                                if (inventoryItem.isEmpty() ||
                                    inventoryItem.getItem() == item &&
                                    inventoryItem.getCount() < inventoryItem.getMaxStackSize()) {
                                    foundInventorySpace = true;
                                    break;
                                }
                            }

                            if (!foundInventorySpace) {
                                return;
                            }

                            int inventoryItemCount = inventory.count(item);
                            if (inventoryItemCount == 256) {
                                return;
                            }

                            if (inventoryItemCount > 256) {
                                inventory.func_223374_a(item, inventoryItemCount - 256);
                                return;
                            }

                            villager.onItemPickup(itemEntity, itemStack.getCount());

                            ItemStack remainingItems = inventory.addItem(itemStack);
                            if (remainingItems.isEmpty()) {
                                itemEntity.remove();
                            } else {
                                itemStack.setCount(remainingItems.getCount());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Modify villager trades so they use modded items and get rid of removed vanilla items.
     * @param event The event data.
     */
    public void onVillagerTradesEvent(VillagerTradesEvent event) {
        ModVillagerTradeModifier tradeModifier = mTradeModifiers.getModifier(event.getType());
        if (tradeModifier != null) {
            tradeModifier.onVillagerTradesEvent(event);
        }
    }

    /**
     * Modded Core AI that allows villagers to pick up modded food.
     * @return A list of tasks to add to the Villager's brain.
     */
    private ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> core() {
        return ImmutableList.of(Pair.of(4, new ModPickupFoodTask(mVillagerItems)));
    }

    /**
     * Modded Work AI that allows farmers to farm modded crops.
     * @param profession The villager's profession.
     * @return A list of tasks to add to the Villager's brain.
     */
    private ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> work(VillagerProfession profession) {
        return ImmutableList.of(
                Pair.of(4, new FirstShuffledTask<>(ImmutableList.of(
                        Pair.of(new SpawnGolemTask(), 7),
                        Pair.of(new WorkTask(MemoryModuleType.JOB_SITE, 4), 2),
                        Pair.of(new WalkTowardsPosTask(MemoryModuleType.JOB_SITE, 1, 10), 5),
                        Pair.of(new WalkTowardsRandomSecondaryPosTask(MemoryModuleType.SECONDARY_JOB_SITE, 0.4F, 1, 6, MemoryModuleType.JOB_SITE), 5),
                        Pair.of(new ModFarmTask(mVillagerCrops), profession == VillagerProfession.FARMER ? 2 : 5),
                        Pair.of(new ModCreateFarmTask(), profession == VillagerProfession.FARMER ? 2 : 5)
                ))),
                Pair.of(2, new ModGiveHeroGiftsTask(100)));
    }

    /**
     * Modded Meet AI that allows villagers with new professions to give gifts.
     * @return A list of tasks to add to the Villager's brain.
     */
    private ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> meet() {
        return ImmutableList.of(Pair.of(2, new ModGiveHeroGiftsTask(100)));
    }

    /**
     * Modded Meet AI that allows villagers with new professions to give gifts.
     * @return A list of tasks to add to the Villager's brain.
     */
    private ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> idle() {
        return ImmutableList.of(Pair.of(2, new ModGiveHeroGiftsTask(100)));
    }
}
