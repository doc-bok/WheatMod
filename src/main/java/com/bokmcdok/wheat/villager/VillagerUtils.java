package com.bokmcdok.wheat.villager;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.item.ModItemUtils;
import com.bokmcdok.wheat.trade.ModEmeraldForItemsTrade;
import com.bokmcdok.wheat.trade.ModItemsForEmeraldsTrade;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerUtils {
    private static Map<Item, Integer> FOOD_ITEMS = Maps.newHashMap();
    private static Map<Item, Block> FARM_ITEMS = Maps.newHashMap();
    private static Set<Item> MISC_ITEMS = Sets.newHashSet();

    /**
     * Register an item as edible by villagers
     * @param food The food item they can eat
     * @param hunger The amount of hunger villagers restore by eating the item
     */
    public static void registerFoodItem(Item food, int hunger) {
        FOOD_ITEMS.put(food, hunger);
    }

    /**
     * Register an item as farmable by villagers
     * @param seed The seed item to be planted
     * @param crop The block created when the seed is planted
     */
    public static void registerFarmItem(Item seed, Block crop) {
        FARM_ITEMS.put(seed, crop);
    }

    /**
     * Register an item that farmers can pick up
     * @param item The item to pick up
     */
    public static void registerMiscItem(Item item) {
        MISC_ITEMS.add(item);
    }

    /**
     * Test if a villager can pick up said item.
     * @param profession The profession of the villager. Farmers will also pickup farm items.
     * @param item The item to try and pick up.
     * @return True if the villager can pick up the item.
     */
    public static boolean canPickupItem(VillagerProfession profession, Item item) {
        if (FOOD_ITEMS.keySet().contains(item)) {
            return true;
        }

        return (profession == VillagerProfession.FARMER &&
               (FARM_ITEMS.keySet().contains(item) || MISC_ITEMS.contains(item)));
    }

    /**
     * Checks if the villager has a farm item in their inventory.
     * @param owner The villager that owns the inventory
     * @return True if a farm item is in the inventory
     */
    public static boolean isFarmItemInInventory(VillagerEntity owner) {
        Inventory inventory = owner.getVillagerInventory();
        return inventory.hasAny(FARM_ITEMS.keySet());
    }

    /**
     * Get the crop block generated from the specified seed
     * @param seed The seed item being planted
     * @return The crop block to create
     */
    public static Block getCropBlock(Item seed) {
        return FARM_ITEMS.get(seed);
    }

    /**
     * Get the items a particular villager is interested in
     */
    public static Set<Item> getInterestingItems(VillagerEntity villager) {
        Set<Item> items = Sets.newHashSet();
        items.addAll(FOOD_ITEMS.keySet());
        if (villager.getVillagerData().getProfession() == VillagerProfession.FARMER) {
            items.addAll(FARM_ITEMS.keySet());
        }

        return items;
    }

    /**
     * Modify a farmer's brain so they will pick up Mod items.
     * @param event The join world event.
     */
    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity().getType() == EntityType.VILLAGER) {
            VillagerEntity villager = (VillagerEntity)event.getEntity();
            VillagerProfession profession = villager.getVillagerData().getProfession();
            float speed = (float) villager.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();

            Brain<VillagerEntity> brain = villager.getBrain();
            brain.registerActivity(Activity.WORK, ModVillagerTaskHelper.work(profession, speed));
            brain.registerActivity(Activity.MEET, ModVillagerTaskHelper.meet(profession, speed));
            brain.registerActivity(Activity.IDLE, ModVillagerTaskHelper.idle(profession, speed));
            brain.registerActivity(Activity.CORE, ModVillagerTaskHelper.core(profession, speed));
        }
    }

    /**
     * Called during tick update. Allows villagers to pick up Mod Items
     * @param event
     */
    @SubscribeEvent
    public static void livingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().getType() == EntityType.VILLAGER) {
            VillagerEntity villager = (VillagerEntity) event.getEntityLiving();
            if (!villager.world.isRemote && villager.canPickUpLoot() && villager.isAlive() && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(villager.world, villager)) {
                for (ItemEntity itemEntity : villager.world.getEntitiesWithinAABB(ItemEntity.class, villager.getBoundingBox().grow(1.0D, 0.0D, 1.0D))) {
                    if (!itemEntity.removed && !itemEntity.getItem().isEmpty() && !itemEntity.cannotPickup()) {
                        ItemStack itemstack = itemEntity.getItem();
                        Item item = itemstack.getItem();
                        if (canPickupItem(villager.getVillagerData().getProfession(), item)) {
                            Inventory inventory = villager.getVillagerInventory();
                            boolean flag = false;

                            for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                                ItemStack itemstack1 = inventory.getStackInSlot(i);
                                if (itemstack1.isEmpty() || itemstack1.getItem() == item && itemstack1.getCount() < itemstack1.getMaxStackSize()) {
                                    flag = true;
                                    break;
                                }
                            }

                            if (!flag) {
                                return;
                            }

                            int j = inventory.count(item);
                            if (j == 256) {
                                return;
                            }

                            if (j > 256) {
                                inventory.func_223374_a(item, j - 256);
                                return;
                            }

                            villager.onItemPickup(itemEntity, itemstack.getCount());
                            ItemStack itemstack2 = inventory.addItem(itemstack);
                            if (itemstack2.isEmpty()) {
                                itemEntity.remove();
                            } else {
                                itemstack.setCount(itemstack2.getCount());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Intercept village trade events. Replace wheat trades with new item trades.
     * @param event
     */
    @SubscribeEvent
    public static void onTrade(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
            List<VillagerTrades.ITrade> noviceTrades = trades.get(1);
            VillagerTrades.ITrade wheatTrade = null;
            for (VillagerTrades.ITrade trade : noviceTrades) {
                MerchantOffer offer = trade.getOffer(null, null);
                if (offer.getSellingStack().getItem() == Items.WHEAT) {
                    wheatTrade = trade;
                    break;
                }
            }

            noviceTrades.add(new ModEmeraldForItemsTrade(ModItemUtils.mouse_tail, 20, 16, 2));

            if (wheatTrade != null) {
                noviceTrades.remove(wheatTrade);
                noviceTrades.add(new ModEmeraldForItemsTrade(ModItemUtils.common_straw, 20, 16, 2));
                noviceTrades.add(new ModEmeraldForItemsTrade(ModItemUtils.emmer_straw, 20, 16, 2));
                noviceTrades.add(new ModEmeraldForItemsTrade(ModItemUtils.einkorn_straw, 20, 16, 2));
                noviceTrades.add(new ModEmeraldForItemsTrade(ModItemUtils.durum_straw, 20, 16, 2));
                noviceTrades.add(new ModEmeraldForItemsTrade(ModItemUtils.spelt_straw, 10, 12, 3));
                noviceTrades.add(new ModEmeraldForItemsTrade(ModItemUtils.tomato, 20, 16, 2));
            }
        }
    }

    /**
     * Replace wandering trader seed trades with new grains and seeds.
     * @param event The event containing the trades.
     */
    @SubscribeEvent
    public static void onWandererTrade(WandererTradesEvent event) {
        List<VillagerTrades.ITrade> genericTrades = event.getGenericTrades();
        VillagerTrades.ITrade seedTrade = null;
        for (VillagerTrades.ITrade trade : genericTrades) {
            MerchantOffer offer = trade.getOffer(null, null);
            if (offer.getSellingStack().getItem() == Items.WHEAT_SEEDS) {
                seedTrade = trade;
                break;
            }
        }

        if (seedTrade != null) {
            genericTrades.remove(seedTrade);
            genericTrades.add(new ModItemsForEmeraldsTrade(ModItemUtils.common_grain, 1, 1, 12, 1));
            genericTrades.add(new ModItemsForEmeraldsTrade(ModItemUtils.einkorn_grain, 1, 1, 12, 1));
            genericTrades.add(new ModItemsForEmeraldsTrade(ModItemUtils.emmer_grain, 1, 1, 12, 1));
            genericTrades.add(new ModItemsForEmeraldsTrade(ModItemUtils.durum_grain, 1, 1, 12, 1));
            genericTrades.add(new ModItemsForEmeraldsTrade(ModItemUtils.spelt_grain, 2, 1, 12, 2));
            genericTrades.add(new ModItemsForEmeraldsTrade(ModItemUtils.tomato_seeds, 1, 1, 12, 1));
        }
    }

    static {
        registerFoodItem(Items.BREAD, 4);
        registerFoodItem(Items.POTATO, 1);
        registerFoodItem(Items.CARROT, 1);
        registerFoodItem(Items.BEETROOT, 1);

        registerFarmItem(Items.POTATO, Blocks.POTATOES);
        registerFarmItem(Items.WHEAT_SEEDS, Blocks.WHEAT);
        registerFarmItem(Items.CARROT, Blocks.CARROTS);
        registerFarmItem(Items.BEETROOT_SEEDS, Blocks.BEETROOTS);

        registerMiscItem(Items.WHEAT);
    }
}
