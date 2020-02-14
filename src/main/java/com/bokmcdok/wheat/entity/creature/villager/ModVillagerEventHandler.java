package com.bokmcdok.wheat.entity.creature.villager;

import com.bokmcdok.wheat.ai.tasks.ModBreedWithVillagerTask;
import com.bokmcdok.wheat.ai.tasks.ModCreateBabyVillagerTask;
import com.bokmcdok.wheat.ai.tasks.ModCreateFarmTask;
import com.bokmcdok.wheat.ai.tasks.ModFarmTask;
import com.bokmcdok.wheat.ai.tasks.ModGiveHeroGiftsTask;
import com.bokmcdok.wheat.ai.tasks.ModMultiTask;
import com.bokmcdok.wheat.ai.tasks.ModPickupFoodTask;
import com.bokmcdok.wheat.ai.tasks.ModShareItemsTask;
import com.bokmcdok.wheat.entity.creature.villager.crops.ModVillagerCrops;
import com.bokmcdok.wheat.entity.creature.villager.crops.ModVillagerCropsDataManager;
import com.bokmcdok.wheat.entity.creature.villager.food.ModVillagerFood;
import com.bokmcdok.wheat.entity.creature.villager.food.ModVillagerFoodDataManager;
import com.bokmcdok.wheat.entity.creature.villager.trade.ModVillagerTradeModifier;
import com.bokmcdok.wheat.entity.creature.villager.trade.ModVillagerTradeModifierDataManager;
import com.bokmcdok.wheat.tag.ModTagDataManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.DummyTask;
import net.minecraft.entity.ai.brain.task.FindInteractionAndLookTargetTask;
import net.minecraft.entity.ai.brain.task.FindWalkTargetTask;
import net.minecraft.entity.ai.brain.task.FirstShuffledTask;
import net.minecraft.entity.ai.brain.task.InteractWithEntityTask;
import net.minecraft.entity.ai.brain.task.JumpOnBedTask;
import net.minecraft.entity.ai.brain.task.ShowWaresTask;
import net.minecraft.entity.ai.brain.task.SpawnGolemTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.WalkTowardsLookTargetTask;
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
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;


public class ModVillagerEventHandler {
    private final ModMemoryModuleRegistrar mMemoryModuleRegistrar;
    private final ModVillagerItems mVillagerItems;
    private final ModVillagerCrops mVillagerCrops;
    private final ModVillagerTradeModifierDataManager mTradeModifiers;
    private final ModVillagerFood mVillagerFood;

    /**
     * Construction
     */
    public ModVillagerEventHandler(ModMemoryModuleRegistrar memoryModuleRegistrar, ModTagDataManager itemTagDataManager) {
        mMemoryModuleRegistrar = memoryModuleRegistrar;

        mTradeModifiers = new ModVillagerTradeModifierDataManager();
        mTradeModifiers.loadDataEntries("villager/trades");

        ModVillagerFoodDataManager foodDataManager = new ModVillagerFoodDataManager();
        foodDataManager.loadDataEntries("villager/food");
        mVillagerFood = foodDataManager.getEntry("docwheat:food");

        ModVillagerCropsDataManager cropsDataManager = new ModVillagerCropsDataManager();
        cropsDataManager.loadDataEntries("villager/crops");
        mVillagerCrops = cropsDataManager.getEntry("docwheat:crops");

        mVillagerItems = new ModVillagerItems(itemTagDataManager, mVillagerFood, mVillagerCrops);
    }

    /**
     * Update the villagers to use modded AI that interacts with items from the mod.
     * @param event The event data.
     */
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.getEntity().getType() == EntityType.VILLAGER) {
            VillagerEntity villager = (VillagerEntity)event.getEntity();
            VillagerProfession profession = villager.getVillagerData().getProfession();
            float speed = (float)villager.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();

            MemoryModuleType<VillagerEntity> breedTarget = (MemoryModuleType<VillagerEntity>)mMemoryModuleRegistrar.getMemoryModule(ModMemoryModuleRegistrar.BREED_TARGET);
            MemoryModuleType<LivingEntity> interactionTarget = (MemoryModuleType<LivingEntity>)mMemoryModuleRegistrar.getMemoryModule(ModMemoryModuleRegistrar.INTERACTION_TARGET);

            Brain<VillagerEntity> brain = villager.getBrain();

            try {
                Field memories = ObfuscationReflectionHelper.findField(Brain.class, "memories");
                Map<MemoryModuleType<?>, Optional<?>> map = (Map<MemoryModuleType<?>, Optional<?>>)memories.get(brain);
                map.put(breedTarget, Optional.empty());
                map.put(interactionTarget, Optional.empty());
            } catch (Exception e) {
                e.printStackTrace();
            }

            brain.registerActivity(Activity.WORK, work(profession));
            brain.registerActivity(Activity.MEET, meet());
            brain.registerActivity(Activity.IDLE, idle(speed));
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
    private ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> idle(float speed) {
        MemoryModuleType<VillagerEntity> breedTarget = (MemoryModuleType<VillagerEntity>)mMemoryModuleRegistrar.getMemoryModule(ModMemoryModuleRegistrar.BREED_TARGET);
        MemoryModuleType<LivingEntity> interactionTarget = (MemoryModuleType<LivingEntity>)mMemoryModuleRegistrar.getMemoryModule(ModMemoryModuleRegistrar.INTERACTION_TARGET);

        return ImmutableList.of(
                Pair.of(1, new FirstShuffledTask<>(
                        ImmutableList.of(
                                Pair.of(InteractWithEntityTask.func_220445_a(EntityType.VILLAGER, 8, interactionTarget, speed, 2), 2),
                                Pair.of(new ModBreedWithVillagerTask(mVillagerFood, 8, breedTarget, speed, 2), 1),
                                Pair.of(InteractWithEntityTask.func_220445_a(EntityType.CAT, 8, interactionTarget, speed, 2), 1),
                                Pair.of(new FindWalkTargetTask(speed), 1), Pair.of(new WalkTowardsLookTargetTask(speed, 2), 1),
                                Pair.of(new JumpOnBedTask(speed), 1),
                                Pair.of(new DummyTask(30, 60), 1)))),
                Pair.of(2, new ModGiveHeroGiftsTask(100)),
                Pair.of(2, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)),
                Pair.of(2, new ShowWaresTask(400, 1600)),
                Pair.of(2, new ModMultiTask<>(ImmutableMap.of(), ImmutableSet.of(interactionTarget), ModMultiTask.Ordering.ORDERED, ModMultiTask.RunType.RUN_ONE, ImmutableList.of(
                        Pair.of(new ModShareItemsTask(mVillagerFood, mVillagerItems, interactionTarget), 1)))),
                Pair.of(2, new ModMultiTask<>(ImmutableMap.of(), ImmutableSet.of(breedTarget), ModMultiTask.Ordering.ORDERED, ModMultiTask.RunType.RUN_ONE, ImmutableList.of(
                        Pair.of(new ModCreateBabyVillagerTask(mVillagerFood, breedTarget), 1)))));
    }
}
