package com.bokmcdok.wheat.Block;

import com.bokmcdok.wheat.Color.ModBlockColors;
import com.bokmcdok.wheat.Item.ModItems;
import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.*;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Registers all the blocks used in this mod.
 */
@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(WheatMod.MOD_ID)
public class ModBlocks {
    /**
     * Register all the new types of wheat in the game.
     */
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(
                //  Wheat Blocks
                new WildWheatBlock(ModItems.wild_einkorn_grain, 128, "wild_einkorn"),
                new WheatBlock(ModItems.common_grain, 256, "common_wheat"),
                new WheatBlock(ModItems.einkorn_grain, 256, "einkorn"),

                new WildWheatBlock(ModItems.wild_emmer_grain, 128, "wild_emmer"),
                new WheatBlock(ModItems.emmer_grain, 256, "emmer"),
                new WheatBlock(ModItems.durum_grain, 256, "durum"),

                new WheatBlock(ModItems.spelt_grain, 512, "spelt"),

                new WheatBlock(null, 1024, "diseased_wheat"),

                //  Bale Blocks
                new BaleBlock(MaterialColor.GRASS, "wild_einkorn_bale"),
                new BaleBlock(MaterialColor.YELLOW, "common_straw_bale"),
                new BaleBlock(MaterialColor.YELLOW, "einkorn_straw_bale"),

                new BaleBlock(MaterialColor.YELLOW, "emmer_straw_bale"),
                new BaleBlock(MaterialColor.YELLOW, "durum_straw_bale"),

                new BaleBlock(MaterialColor.YELLOW, "spelt_straw_bale"),

                //  Straw Mats
                new StrawMatBlock("common_straw_mat"),
                new StrawMatBlock("einkorn_straw_mat"),

                new StrawMatBlock("emmer_straw_mat"),
                new StrawMatBlock("durum_straw_mat"),

                new StrawMatBlock("spelt_straw_mat"),

                //  Stone
                new SmallStoneBlock().setRegistryName(WheatMod.MOD_ID, "small_cobblestone"),
                new SmallStoneBlock().setRegistryName(WheatMod.MOD_ID, "granite_stone"),
                new SmallStoneBlock().setRegistryName(WheatMod.MOD_ID, "diorite_stone"),
                new SmallStoneBlock().setRegistryName(WheatMod.MOD_ID, "andesite_stone"),

                //  Vegetabls
                new ModCropsBlock(ModItems.tomato_seeds, "tomato"),

                //  Flour Mill
                new FlourMillBlock("flour_mill")
        );
    }

    /**
     * Colour Handlers
     *
     * Allows us to control the colours of each wheat type with less code.
     * @param event The event containing the registry for the blocks.
     */
    @SubscribeEvent
    public static void registerBlockColourHandlers(ColorHandlerEvent.Block event)
    {
        final BlockColors blockColors = event.getBlockColors();

        blockColors.register(ModBlockColors.WILD_EINKORN, wild_einkorn);
        blockColors.register(ModBlockColors.WILD_EINKORN, wild_einkorn_bale);

        blockColors.register(ModBlockColors.COMMON_WHEAT, common_wheat);
        blockColors.register(ModBlockColors.COMMON_WHEAT, common_straw_bale);
        blockColors.register(ModBlockColors.COMMON_WHEAT, common_straw_mat);

        blockColors.register(ModBlockColors.EINKORN, einkorn);
        blockColors.register(ModBlockColors.EINKORN, einkorn_straw_bale);
        blockColors.register(ModBlockColors.EINKORN, einkorn_straw_mat);

        blockColors.register(ModBlockColors.WILD_EMMER, wild_emmer);

        blockColors.register(ModBlockColors.EMMER, emmer);
        blockColors.register(ModBlockColors.EMMER, emmer_straw_bale);
        blockColors.register(ModBlockColors.EMMER, emmer_straw_mat);

        blockColors.register(ModBlockColors.DURUM, durum);
        blockColors.register(ModBlockColors.DURUM, durum_straw_bale);
        blockColors.register(ModBlockColors.DURUM, durum_straw_mat);

        blockColors.register(ModBlockColors.SPELT, spelt);
        blockColors.register(ModBlockColors.SPELT, spelt_straw_bale);
        blockColors.register(ModBlockColors.SPELT, spelt_straw_mat);

        blockColors.register(ModBlockColors.DISEASED_WHEAT, diseased_wheat);
    }

    /**
     * Register the blocks for fire. Register mutations.
     */
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        FireBlock fireBlock = (FireBlock)Blocks.FIRE;

        //  Bales
        fireBlock.setFireInfo(wild_einkorn_bale, 60, 20);
        fireBlock.setFireInfo(common_straw_bale, 60, 20);
        fireBlock.setFireInfo(einkorn_straw_bale, 60, 20);
        fireBlock.setFireInfo(emmer_straw_bale, 60, 20);
        fireBlock.setFireInfo(durum_straw_bale, 60, 20);
        fireBlock.setFireInfo(spelt_straw_bale, 60, 20);

        //  Straw Mats
        fireBlock.setFireInfo(common_straw_mat, 60, 20);
        fireBlock.setFireInfo(einkorn_straw_mat, 60, 20);
        fireBlock.setFireInfo(emmer_straw_mat, 60, 20);
        fireBlock.setFireInfo(durum_straw_mat, 60, 20);
        fireBlock.setFireInfo(spelt_straw_mat, 60, 20);

        //  Mutations
        wild_einkorn.registerMutations(einkorn, common_wheat);
        wild_emmer.registerMutations(emmer, durum);

        emmer.registerMutation(spelt, common_wheat);
        common_wheat.registerMutation(spelt, emmer);
    }

    /**
     * The new types of wheat.
     */
    public static final WildWheatBlock wild_einkorn = null;
    public static final WheatBlock common_wheat = null;
    public static final WheatBlock einkorn = null;

    public static final WildWheatBlock wild_emmer = null;
    public static final WheatBlock emmer = null;
    public static final WheatBlock durum = null;

    public static final WheatBlock spelt = null;

    public static final WheatBlock diseased_wheat = null;

    /**
     * The new types of Hay/Straw
     */
    public static final BaleBlock wild_einkorn_bale = null;
    public static final BaleBlock common_straw_bale = null;
    public static final BaleBlock einkorn_straw_bale = null;

    public static final BaleBlock emmer_straw_bale = null;
    public static final BaleBlock durum_straw_bale = null;

    public static final BaleBlock spelt_straw_bale = null;

    /**
     * Straw Mats
     */
    public static final StrawMatBlock common_straw_mat = null;
    public static final StrawMatBlock einkorn_straw_mat = null;

    public static final StrawMatBlock emmer_straw_mat = null;
    public static final StrawMatBlock durum_straw_mat = null;

    public static final StrawMatBlock spelt_straw_mat = null;

    /**
     * Stones
     */
    public static final SmallStoneBlock small_cobblestone = null;
    public static final SmallStoneBlock granite_stone = null;
    public static final SmallStoneBlock diorite_stone = null;
    public static final SmallStoneBlock andesite_stone = null;

    /**
     * Vegetables
     */
    public static final ModCropsBlock tomato = null;

    /**
     * Flour Mill
     */
    public static final FlourMillBlock flour_mill = null;
}
