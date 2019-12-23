package com.bokmcdok.wheat.item;

import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.data.ModItemManager;
import com.bokmcdok.wheat.villager.VillagerUtils;
import net.minecraft.block.ComposterBlock;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Registers all the items used in this mod.
 */
@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(WheatMod.MOD_ID)
public class ModItemUtils
{

    /**
     * The grain items for reference.
     */
    public static final Item wild_einkorn_grain = null;
    public static final Item common_grain = null;
    public static final Item einkorn_grain = null;

    public static final Item wild_emmer_grain = null;
    public static final Item emmer_grain = null;
    public static final Item durum_grain = null;

    public static final Item spelt_grain = null;

    /**
     * The hay/wheat items for reference.
     */
    public static final Item wild_einkorn_hay = null;
    public static final Item common_straw = null;
    public static final Item einkorn_straw = null;

    public static final Item wild_emmer_hay = null;
    public static final Item emmer_straw = null;
    public static final Item durum_straw = null;

    public static final Item spelt_straw = null;

    /**
     * The bale items for reference
     */
    public static final Item wild_einkorn_bale = null;
    public static final Item common_straw_bale = null;
    public static final Item einkorn_straw_bale = null;

    public static final Item emmer_straw_bale = null;
    public static final Item durum_straw_bale = null;

    public static final Item spelt_straw_bale = null;

    /**
     * The thatch items for reference
     */
    public static final Item common_thatch = null;
    public static final Item einkorn_thatch = null;
    public static final Item emmer_thatch = null;
    public static final Item durum_thatch = null;
    public static final Item spelt_thatch = null;

    /**
     * The mat items for reference
     */
    public static final Item common_straw_mat = null;
    public static final Item einkorn_straw_mat = null;

    public static final Item emmer_straw_mat = null;
    public static final Item durum_straw_mat = null;

    public static final Item spelt_straw_mat = null;

    /**
     * Ingredients for reference
     */
    public static final Item common_flour = null;
    public static final Item durum_flour = null;
    public static final Item spelt_flour = null;

    /**
     * Food Items for reference
     */
    public static final Item chicken_leg = null;
    public static final Item chicken_breast = null;
    public static final Item chicken_wing = null;

    public static final Item cod_fillet = null;
    public static final Item sashimi = null;

    public static final Item tomato = null;
    public static final Item tomato_seeds = null;

    public static final Item dough = null;

    public static final Item porridge = null;
    public static final Item pasta = null;
    public static final Item donut = null;
    public static final Item gravy = null;
    public static final Item meat_pie = null;
    public static final Item fish_pie = null;
    public static final Item apple_pie = null;

    /**
     * Miscellaneous Items for reference
     */
    public static final Item stone_bowl = null;

    public static Ingredient WHEAT_ITEMS = null;
    public static Ingredient SEED_ITEMS = null;
    public static Ingredient BALE_ITEMS = null;
    public static Ingredient FISH_ITEMS = null;
    public static Ingredient GRAIN_ITEMS = null;

    private static ModItemManager ITEM_MANAGER = new ModItemManager();

    /**
     * Register all items used by the mod
     * @param event The item registry event
     */
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        ITEM_MANAGER.loadItems();
        event.getRegistry().registerAll(ITEM_MANAGER.getAsItems());
    }

    /**
     * Registers the color handlers for all the new seeds.
     * @param event The color handler event
     */
    @SubscribeEvent
    public static void registerItemColourHandlers(ColorHandlerEvent.Item event)
    {
        IModItem[] items = ITEM_MANAGER.getItems();
        final ItemColors itemColors = event.getItemColors();

        for (IModItem i : items) {
            if (i.getColor() != null) {
                itemColors.register(i.getColor(), i.asItem());
            }
        }
    }

    /**
     * Register all items with the composter and set up item groups.
     * @param event The common setup event
     */
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event)
    {
        IModItem[] items = ITEM_MANAGER.getItems();
        for (IModItem i : items) {
            if (i.getCompostChance() > 0.0) {
                ComposterBlock.CHANCES.put(i.asItem(), i.getCompostChance());
            }
        }

        WHEAT_ITEMS = Ingredient.fromItems(
                wild_einkorn_hay, common_straw, einkorn_straw,
                wild_emmer_hay, emmer_straw, durum_straw,
                spelt_straw,
                Items.WHEAT);

        SEED_ITEMS = Ingredient.fromItems(
                wild_einkorn_grain, common_grain, einkorn_grain,
                wild_emmer_grain, emmer_grain, durum_grain,
                spelt_grain,
                tomato_seeds,
                Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.WHEAT_SEEDS);

        BALE_ITEMS = Ingredient.fromItems(
                wild_einkorn_bale, common_straw_bale, einkorn_straw_bale,
                emmer_straw_bale, durum_straw_bale,
                spelt_straw_bale,
                Items.HAY_BLOCK);

        FISH_ITEMS = Ingredient.fromItems(Items.COD, Items.SALMON, cod_fillet, sashimi);

        GRAIN_ITEMS = Ingredient.fromItems(
                wild_einkorn_grain, common_grain, einkorn_grain,
                wild_emmer_grain, emmer_grain, durum_grain,
                spelt_grain);

        VillagerUtils.registerFoodItem(ModItemUtils.tomato, 1);

        VillagerUtils.registerFarmItem(common_grain, ModBlockUtils.common_wheat);
        VillagerUtils.registerFarmItem(einkorn_grain, ModBlockUtils.einkorn);
        VillagerUtils.registerFarmItem(emmer_grain, ModBlockUtils.emmer);
        VillagerUtils.registerFarmItem(durum_grain, ModBlockUtils.durum);
        VillagerUtils.registerFarmItem(spelt_grain, ModBlockUtils.spelt);
        VillagerUtils.registerFarmItem(tomato, ModBlockUtils.tomato);

        VillagerUtils.registerMiscItem(common_straw);
        VillagerUtils.registerMiscItem(einkorn_straw);
        VillagerUtils.registerMiscItem(emmer_straw);
        VillagerUtils.registerMiscItem(durum_straw);
        VillagerUtils.registerMiscItem(spelt_straw);
    }
}
