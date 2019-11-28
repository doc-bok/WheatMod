package com.bokmcdok.wheat.item;

import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.color.ModItemColors;
import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.data.ModItemManager;
import com.bokmcdok.wheat.data.ModResourceManager;
import com.bokmcdok.wheat.entity.VillagerUtils;
import net.minecraft.block.ComposterBlock;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.world.gen.feature.structure.VillageStructure;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;
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

    /**
     * Miscellaneous Items for reference
     */
    public static final Item stone_bowl = null;

    public static Ingredient WHEAT_ITEMS = null;
    public static Ingredient SEED_ITEMS = null;
    public static Ingredient BALE_ITEMS = null;
    public static Ingredient FISH_ITEMS = null;
    public static Ingredient FLOUR_ITEMS = null;
    public static Ingredient GRAIN_ITEMS = null;

    /**
     * Register all items used by the mod
     * @param event The item registry event
     */
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        //  TODO: This should be moved to a more centralised location.

        ModResourceManager modResourceManager = new ModResourceManager(ResourcePackType.SERVER_DATA, WheatMod.MOD_ID);
        ModItemManager itemManager = new ModItemManager();
        itemManager.loadItems(modResourceManager);

        event.getRegistry().registerAll(itemManager.getItems());

        event.getRegistry().registerAll(
                //  Grain
                new ModBlockNamedItem(ModBlockUtils.wild_einkorn, ItemGroup.MATERIALS, "wild_einkorn_grain"),
                new ModBlockNamedItem(ModBlockUtils.common_wheat, ItemGroup.MATERIALS, "common_grain"),
                new ModBlockNamedItem(ModBlockUtils.einkorn, ItemGroup.MATERIALS, "einkorn_grain"),

                new ModBlockNamedItem(ModBlockUtils.wild_emmer, ItemGroup.MATERIALS, "wild_emmer_grain"),
                new ModBlockNamedItem(ModBlockUtils.emmer, ItemGroup.MATERIALS, "emmer_grain"),
                new ModBlockNamedItem(ModBlockUtils.durum, ItemGroup.MATERIALS, "durum_grain"),

                new ModBlockNamedItem(ModBlockUtils.spelt, ItemGroup.MATERIALS, "spelt_grain"),

                //  Bales
                new ModBlockItem(ModBlockUtils.wild_einkorn_bale, ItemGroup.BUILDING_BLOCKS, "wild_einkorn_bale"),
                new ModBlockItem(ModBlockUtils.common_straw_bale, ItemGroup.BUILDING_BLOCKS, "common_straw_bale"),
                new ModBlockItem(ModBlockUtils.einkorn_straw_bale, ItemGroup.BUILDING_BLOCKS, "einkorn_straw_bale"),
                new ModBlockItem(ModBlockUtils.emmer_straw_bale, ItemGroup.BUILDING_BLOCKS, "emmer_straw_bale"),
                new ModBlockItem(ModBlockUtils.durum_straw_bale, ItemGroup.BUILDING_BLOCKS, "durum_straw_bale"),
                new ModBlockItem(ModBlockUtils.spelt_straw_bale, ItemGroup.BUILDING_BLOCKS, "spelt_straw_bale"),

                //  Thatch
                new ModBlockItem(ModBlockUtils.common_thatch, ItemGroup.BUILDING_BLOCKS, "common_thatch"),
                new ModBlockItem(ModBlockUtils.einkorn_thatch, ItemGroup.BUILDING_BLOCKS, "einkorn_thatch"),
                new ModBlockItem(ModBlockUtils.emmer_thatch, ItemGroup.BUILDING_BLOCKS, "emmer_thatch"),
                new ModBlockItem(ModBlockUtils.durum_thatch, ItemGroup.BUILDING_BLOCKS, "durum_thatch"),
                new ModBlockItem(ModBlockUtils.spelt_thatch, ItemGroup.BUILDING_BLOCKS, "spelt_thatch"),

                //  Straw Mats
                new ModBlockItem(ModBlockUtils.common_straw_mat, ItemGroup.DECORATIONS, "common_straw_mat"),
                new ModBlockItem(ModBlockUtils.einkorn_straw_mat, ItemGroup.DECORATIONS, "einkorn_straw_mat"),

                new ModBlockItem(ModBlockUtils.emmer_straw_mat, ItemGroup.DECORATIONS, "emmer_straw_mat"),
                new ModBlockItem(ModBlockUtils.durum_straw_mat, ItemGroup.DECORATIONS, "durum_straw_mat"),
                new ModBlockItem(ModBlockUtils.spelt_straw_mat, ItemGroup.DECORATIONS, "spelt_straw_mat"),

                //  Small Stones
                new ModStoneItem(ModBlockUtils.small_cobblestone, ItemGroup.DECORATIONS, "small_cobblestone"),
                new ModStoneItem(ModBlockUtils.granite_stone, ItemGroup.DECORATIONS, "granite_stone"),
                new ModStoneItem(ModBlockUtils.diorite_stone, ItemGroup.DECORATIONS, "diorite_stone"),
                new ModStoneItem(ModBlockUtils.andesite_stone, ItemGroup.DECORATIONS, "andesite_stone"),

                //  Tools
                new ModDurableItem(ItemGroup.TOOLS, 131, "mortar_pestle"),
                new ModDurableItem(ItemGroup.TOOLS, 65, "flint_knife"),

                //  Food - Chicken
                new ModFoodItem(1, 0.1f, true, true, "chicken_leg"),
                new ModFoodItem(1, 0.1f, true, true, "chicken_breast"),
                new ModFoodItem(1, 0.1f, true, true, "chicken_wing"),
                new ModFoodItem(1, 0.1f, true, true, "flour_chicken_leg"),
                new ModFoodItem(1, 0.1f, true, true, "flour_chicken_breast"),
                new ModFoodItem(1, 0.1f, true, true, "flour_chicken_wing"),
                new ModFoodItem(2, 0.2f, true, false, "cooked_chicken_leg"),
                new ModFoodItem(2, 0.2f, true, false, "cooked_chicken_breast"),
                new ModFoodItem(2, 0.2f, true, false, "cooked_chicken_wing"),
                new ModFoodItem(3, 0.3f, true, false, "fried_chicken_leg"),
                new ModFoodItem(3, 0.3f, true, false, "fried_chicken_breast"),
                new ModFoodItem(3, 0.3f, true, false, "fried_chicken_wing"),

                //  Food - Bread
                new ModFoodItem(1, 6.0f, false, true, true, "dough"),
                new ModFoodItem(5, 6.0f, false, true, "bread_dough"),
                new ModFoodItem(1, 2.0f, false, false, true, "roll"),
                new ModFoodItem(9, 14.8f, true, false, "burger"),
                new ModFoodItem(10, 16.0f, true,false,"tomato_burger"),

                //  Food - Fish and Chips
                new ModFoodItem(2, 0.5f, false,   true, "cod_fillet"),
                new ModFoodItem(2, 0.3f, false,  false, "sashimi"),
                new ModFoodItem(1, 0.6f, false,   true, "sliced_potato"),
                new ModFoodItem(5, 7.0f, false,   false, "chips"),
                new ModDurableBucketItem(ItemGroup.FOOD, 8, "batter_bucket"),
                new ModFoodItem(3, 0.2f, false, true, "battered_cod"),
                new ModFoodItem(6, 7.0f, false, false, "deep_fried_fish"),
                new ModFoodItem(11, 14.0f, false, false, "fish_and_chips"),

                //  Food - Sweet Things
                new ModFoodItem(14, 2.8f,false, true, "cake_mix"),
                new ModFoodItem(2, 0.4f,  false, true, "cookie_dough"),
                new ModFoodItem(7, 5.0f,  false, true, "uncooked_donut"),
                new ModFoodItem(7, 5.0f, false, false, "donut"),
                new ModFoodItem(1, 0.4f, false, true, "biscuit_dough"),
                new ModFoodItem(1, 0.4f, false, false, "biscuit"),

                //  Food - Pasta
                new ModFoodItem(4, 7.2f, false, true, "raw_pasta"),
                new ModFoodItem(1, 7.2f, false, true, "raw_noodles"),
                new ModFoodItem(4, 7.2f, false, false, "pasta"),
                new ModFoodItem(4, 8.0f, false, false, "noodles"),

                //  Food - Porridge
                new ModBowlFoodItem(6, 7.2f, false, "porridge"),
                new ModBowlFoodItem(8, 8.0f, false, "warm_porridge"),

                //  Food - Pies
                new ModFoodItem(13, 18.8f, true, true, "uncooked_meat_pie"),
                new ModFoodItem(13, 18.8f, true, false, "meat_pie"),
                new ModFoodItem(13, 10.4f, false, true, "uncooked_apple_pie"),
                new ModFoodItem(13, 10.4f, false, false, "apple_pie"),
                new ModFoodItem(16, 21.6f, false, true, "uncooked_fish_pie"),
                new ModFoodItem(16, 21.6f, false, false, "fish_pie"),

                //  Food - Vegetables
                new ModFoodItem(1, 1.2f, false, false, "tomato"),
                new ModBlockNamedItem(ModBlockUtils.tomato, ItemGroup.MATERIALS, "tomato_seeds"),

                //  Food - Misc
                new ModStoneBowlFoodItem(6, 7.2f, false, "gravy"),

                //  Flour Mill
                new ModBlockNamedItem(ModBlockUtils.flour_mill, ItemGroup.BUILDING_BLOCKS, "flour_mill")
        );
    }

    /**
     * Registers the color handlers for all the new seeds.
     * @param event The color handler event
     */
    @SubscribeEvent
    public static void registerBlockColourHandlers(ColorHandlerEvent.Item event)
    {
        final ItemColors blockColors = event.getItemColors();

        blockColors.register(ModItemColors.WILD_EINKORN, wild_einkorn_grain);
        blockColors.register(ModItemColors.WILD_EINKORN, wild_einkorn_hay);
        blockColors.register(ModItemColors.WILD_EINKORN, wild_einkorn_bale);

        blockColors.register(ModItemColors.COMMON_WHEAT, common_grain);
        blockColors.register(ModItemColors.COMMON_WHEAT, common_straw);
        blockColors.register(ModItemColors.COMMON_WHEAT, common_straw_bale);
        blockColors.register(ModItemColors.COMMON_WHEAT, common_thatch);
        blockColors.register(ModItemColors.COMMON_WHEAT, common_straw_mat);
        blockColors.register(ModItemColors.COMMON_FLOUR, common_flour);

        blockColors.register(ModItemColors.EINKORN, einkorn_grain);
        blockColors.register(ModItemColors.EINKORN, einkorn_straw);
        blockColors.register(ModItemColors.EINKORN, einkorn_straw_bale);
        blockColors.register(ModItemColors.EINKORN, einkorn_thatch);
        blockColors.register(ModItemColors.EINKORN, einkorn_straw_mat);

        blockColors.register(ModItemColors.WILD_EMMER, wild_emmer_grain);
        blockColors.register(ModItemColors.WILD_EMMER, wild_emmer_hay);

        blockColors.register(ModItemColors.EMMER, emmer_grain);
        blockColors.register(ModItemColors.EMMER, emmer_straw);
        blockColors.register(ModItemColors.EMMER, emmer_straw_bale);
        blockColors.register(ModItemColors.EMMER, emmer_thatch);
        blockColors.register(ModItemColors.EMMER, emmer_straw_mat);

        blockColors.register(ModItemColors.DURUM, durum_grain);
        blockColors.register(ModItemColors.DURUM, durum_straw);
        blockColors.register(ModItemColors.DURUM, durum_straw_bale);
        blockColors.register(ModItemColors.DURUM, durum_thatch);
        blockColors.register(ModItemColors.DURUM, durum_straw_mat);
        blockColors.register(ModItemColors.DURUM_FLOUR, durum_flour);

        blockColors.register(ModItemColors.SPELT, spelt_grain);
        blockColors.register(ModItemColors.SPELT, spelt_straw);
        blockColors.register(ModItemColors.SPELT, spelt_straw_bale);
        blockColors.register(ModItemColors.SPELT, spelt_thatch);
        blockColors.register(ModItemColors.SPELT, spelt_straw_mat);
        blockColors.register(ModItemColors.SPELT_FLOUR, spelt_flour);

        blockColors.register(ModItemColors.TOMATO_SEEDS, tomato_seeds);

    }

    /**
     * Register all items with the composter and set up item groups.
     * @param event The common setup event
     */
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event)
    {
        ComposterBlock.CHANCES.put(wild_einkorn_grain, 0.3f);
        ComposterBlock.CHANCES.put(wild_einkorn_hay, 0.65f);
        ComposterBlock.CHANCES.put(wild_einkorn_bale, 0.85f);

        ComposterBlock.CHANCES.put(common_grain, 0.3f);
        ComposterBlock.CHANCES.put(common_straw, 0.65f);
        ComposterBlock.CHANCES.put(common_straw_bale, 0.85f);

        ComposterBlock.CHANCES.put(einkorn_grain, 0.3f);
        ComposterBlock.CHANCES.put(einkorn_straw, 0.65f);
        ComposterBlock.CHANCES.put(einkorn_straw_bale, 0.85f);

        ComposterBlock.CHANCES.put(wild_emmer_grain, 0.3f);
        ComposterBlock.CHANCES.put(wild_emmer_hay, 0.65f);

        ComposterBlock.CHANCES.put(emmer_grain, 0.3f);
        ComposterBlock.CHANCES.put(emmer_straw, 0.65f);
        ComposterBlock.CHANCES.put(emmer_straw_bale, 0.85f);

        ComposterBlock.CHANCES.put(durum_grain, 0.3f);
        ComposterBlock.CHANCES.put(durum_straw, 0.65f);
        ComposterBlock.CHANCES.put(durum_straw_bale, 0.85f);

        ComposterBlock.CHANCES.put(spelt_grain, 0.3f);
        ComposterBlock.CHANCES.put(spelt_straw, 0.65f);
        ComposterBlock.CHANCES.put(spelt_straw_bale, 0.85f);

        ComposterBlock.CHANCES.put(tomato_seeds, 0.3f);
        ComposterBlock.CHANCES.put(tomato, 0.65f);

        FLOUR_ITEMS = Ingredient.fromItems(common_flour, durum_flour, spelt_flour);

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
