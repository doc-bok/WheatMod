package com.bokmcdok.wheat.Item;

import com.bokmcdok.wheat.block.ModBlocks;
import com.bokmcdok.wheat.Color.ModItemColors;
import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.ComposterBlock;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.*;
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
public class ModItems
{
    /**
     * Register all items used by the mod
     * @param event The item registry event
     */
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
                //  Grain
                new ModBlockNamedItem(ModBlocks.wild_einkorn, ItemGroup.MATERIALS, "wild_einkorn_grain"),
                new ModBlockNamedItem(ModBlocks.common_wheat, ItemGroup.MATERIALS, "common_grain"),
                new ModBlockNamedItem(ModBlocks.einkorn, ItemGroup.MATERIALS, "einkorn_grain"),

                new ModBlockNamedItem(ModBlocks.wild_emmer, ItemGroup.MATERIALS, "wild_emmer_grain"),
                new ModBlockNamedItem(ModBlocks.emmer, ItemGroup.MATERIALS, "emmer_grain"),
                new ModBlockNamedItem(ModBlocks.durum, ItemGroup.MATERIALS, "durum_grain"),

                new ModBlockNamedItem(ModBlocks.spelt, ItemGroup.MATERIALS, "spelt_grain"),

                //  Hay/Wheat
                new ModItem(ItemGroup.MATERIALS, "wild_einkorn_hay"),
                new ModItem(ItemGroup.MATERIALS, "common_straw"),
                new ModItem(ItemGroup.MATERIALS, "einkorn_straw"),

                new ModItem(ItemGroup.MATERIALS, "wild_emmer_hay"),
                new ModItem(ItemGroup.MATERIALS, "emmer_straw"),
                new ModItem(ItemGroup.MATERIALS, "durum_straw"),

                new ModItem(ItemGroup.MATERIALS, "spelt_straw"),

                //  Bales
                new ModBlockItem(ModBlocks.wild_einkorn_bale, ItemGroup.BUILDING_BLOCKS, "wild_einkorn_bale"),
                new ModBlockItem(ModBlocks.common_straw_bale, ItemGroup.BUILDING_BLOCKS, "common_straw_bale"),
                new ModBlockItem(ModBlocks.einkorn_straw_bale, ItemGroup.BUILDING_BLOCKS, "einkorn_straw_bale"),

                new ModBlockItem(ModBlocks.emmer_straw_bale, ItemGroup.BUILDING_BLOCKS, "emmer_straw_bale"),
                new ModBlockItem(ModBlocks.durum_straw_bale, ItemGroup.BUILDING_BLOCKS, "durum_straw_bale"),

                new ModBlockItem(ModBlocks.spelt_straw_bale, ItemGroup.BUILDING_BLOCKS, "spelt_straw_bale"),

                //  Thatch
                new ModBlockItem(ModBlocks.common_thatch, ItemGroup.BUILDING_BLOCKS, "common_thatch"),
                new ModBlockItem(ModBlocks.einkorn_thatch, ItemGroup.BUILDING_BLOCKS, "einkorn_thatch"),
                new ModBlockItem(ModBlocks.emmer_thatch, ItemGroup.BUILDING_BLOCKS, "emmer_thatch"),
                new ModBlockItem(ModBlocks.durum_thatch, ItemGroup.BUILDING_BLOCKS, "durum_thatch"),
                new ModBlockItem(ModBlocks.spelt_thatch, ItemGroup.BUILDING_BLOCKS, "spelt_thatch"),

                //  Straw Mats
                new ModBlockItem(ModBlocks.common_straw_mat, ItemGroup.DECORATIONS, "common_straw_mat"),
                new ModBlockItem(ModBlocks.einkorn_straw_mat, ItemGroup.DECORATIONS, "einkorn_straw_mat"),

                new ModBlockItem(ModBlocks.emmer_straw_mat, ItemGroup.DECORATIONS, "emmer_straw_mat"),
                new ModBlockItem(ModBlocks.durum_straw_mat, ItemGroup.DECORATIONS, "durum_straw_mat"),
                new ModBlockItem(ModBlocks.spelt_straw_mat, ItemGroup.DECORATIONS, "spelt_straw_mat"),

                //  Small Stones
                new ModStoneItem(ModBlocks.small_cobblestone, ItemGroup.DECORATIONS, "small_cobblestone"),
                new ModStoneItem(ModBlocks.granite_stone, ItemGroup.DECORATIONS, "granite_stone"),
                new ModStoneItem(ModBlocks.diorite_stone, ItemGroup.DECORATIONS, "diorite_stone"),
                new ModStoneItem(ModBlocks.andesite_stone, ItemGroup.DECORATIONS, "andesite_stone"),

                //  Stone Bowl
                new ModItem(ItemGroup.MATERIALS, "stone_bowl"),

                //  Tools
                new ModDurableItem(ItemGroup.TOOLS, 131, "mortar_pestle"),
                new ModDurableItem(ItemGroup.TOOLS, 65, "flint_knife"),

                //  Ingredients
                new ModItem(ItemGroup.FOOD, "common_flour"),
                new ModItem(ItemGroup.FOOD, "durum_flour"),
                new ModItem(ItemGroup.FOOD, "spelt_flour"),

                //  Food - Chicken
                new ModFoodItem(1, 0.1f, true, "chicken_leg"),
                new ModFoodItem(1, 0.1f, true, "chicken_breast"),
                new ModFoodItem(1, 0.1f, true, "chicken_wing"),
                new ModFoodItem(1, 0.1f, true, "flour_chicken_leg"),
                new ModFoodItem(1, 0.1f, true, "flour_chicken_breast"),
                new ModFoodItem(1, 0.1f, true, "flour_chicken_wing"),
                new ModFoodItem(2, 0.2f, false, "cooked_chicken_leg"),
                new ModFoodItem(2, 0.2f, false, "cooked_chicken_breast"),
                new ModFoodItem(2, 0.2f, false, "cooked_chicken_wing"),
                new ModFoodItem(3, 0.3f, false, "fried_chicken_leg"),
                new ModFoodItem(3, 0.3f, false, "fried_chicken_breast"),
                new ModFoodItem(3, 0.3f, false, "fried_chicken_wing"),

                //  Food - Bread
                new ModFoodItem(1, 6.0f, true, "dough"),
                new ModFoodItem(5, 6.0f, true, "bread_dough"),
                new ModFoodItem(1, 2.0f, false, "roll"),
                new ModFoodItem(9, 14.8f, false, "burger"),
                new ModFoodItem(10, 16.0f, false, "tomato_burger"),

                //  Food - Fish and Chips
                new ModFoodItem(2, 0.5f, false, "cod_fillet"),
                new ModFoodItem(2, 0.3f, false, "sashimi"),
                new ModFoodItem(1, 0.6f, true, "sliced_potato"),
                new ModFoodItem(5, 7.0f, false, "chips"),
                new ModDurableBucketItem(ItemGroup.FOOD, 8, "batter_bucket"),
                new ModFoodItem(3, 0.2f, true, "battered_cod"),
                new ModFoodItem(6, 7.0f, false, "deep_fried_fish"),
                new ModFoodItem(11, 14.0f, false, "fish_and_chips"),

                //  Food - Sweet Things
                new ModFoodItem(14, 2.8f, true, "cake_mix"),
                new ModFoodItem(2, 0.4f, true, "cookie_dough"),
                new ModFoodItem(7, 5.0f, true, "uncooked_donut"),
                new ModFoodItem(7, 5.0f, false, "donut"),
                new ModFoodItem(1, 0.4f, true, "biscuit_dough"),
                new ModFoodItem(1, 0.4f, false, "biscuit"),

                //  Food - Pasta
                new ModFoodItem(4, 7.2f, true, "raw_pasta"),
                new ModFoodItem(1, 7.2f, true, "raw_noodles"),
                new ModFoodItem(4, 7.2f, false, "pasta"),
                new ModFoodItem(4, 8.0f, false, "noodles"),

                //  Food - Porridge
                new ModBowlFoodItem(6, 7.2f, false, "porridge"),
                new ModBowlFoodItem(8, 8.0f, false, "warm_porridge"),

                //  Food - Pies
                new ModFoodItem(13, 18.8f, true, "uncooked_meat_pie"),
                new ModFoodItem(13, 18.8f, false, "meat_pie"),
                new ModFoodItem(13, 10.4f, true, "uncooked_apple_pie"),
                new ModFoodItem(13, 10.4f, false, "apple_pie"),
                new ModFoodItem(16, 21.6f, true, "uncooked_fish_pie"),
                new ModFoodItem(16, 21.6f, false, "fish_pie"),

                //  Food - Vegetables
                new ModFoodItem(1, 1.2f, false, "tomato"),
                new ModBlockNamedItem(ModBlocks.tomato, ItemGroup.MATERIALS, "tomato_seeds"),

                //  Food - Misc
                new ModStoneBowlFoodItem(6, 7.2f, false, "gravy"),

                //  Flour Mill
                new ModBlockNamedItem(ModBlocks.flour_mill, ItemGroup.BUILDING_BLOCKS, "flour_mill")
        );
    }

    /**
     * Registers the color handlers for all the new seeds.
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
    }

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
    public static final Item bread_dough = null;
    public static final Item raw_pasta = null;

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
}
