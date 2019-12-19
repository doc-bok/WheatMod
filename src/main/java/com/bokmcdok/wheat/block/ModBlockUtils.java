package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.color.ModBlockColors;
import com.bokmcdok.wheat.data.ModBlockManager;
import com.bokmcdok.wheat.item.ModItemUtils;
import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.client.renderer.color.BlockColors;
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
public class ModBlockUtils {

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
     * Vegetables
     */
    public static final ModCropsBlock tomato = null;

    /**
     * Flour Mill
     */
    public static final FlourMillBlock flour_mill = null;

    private static final ModBlockManager BLOCK_MANAGER = new ModBlockManager();

    /**
     * Register all the new types of wheat in the game.
     */
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        BLOCK_MANAGER.loadBlocks();
        Block[] blocks = BLOCK_MANAGER.getAsBlocks();
        event.getRegistry().registerAll(blocks);

        event.getRegistry().registerAll(
                //  Wheat Blocks
                new WildWheatBlock(ModItemUtils.wild_einkorn_grain, 128, "wild_einkorn"),
                new WheatBlock(ModItemUtils.common_grain, 256, "common_wheat"),
                new WheatBlock(ModItemUtils.einkorn_grain, 256, "einkorn"),

                new WildWheatBlock(ModItemUtils.wild_emmer_grain, 128, "wild_emmer"),
                new WheatBlock(ModItemUtils.emmer_grain, 256, "emmer"),
                new WheatBlock(ModItemUtils.durum_grain, 256, "durum"),

                new WheatBlock(ModItemUtils.spelt_grain, 512, "spelt"),

                new WheatBlock(null, 1024, "diseased_wheat"),

                //  Stone
                new SmallStoneBlock().setRegistryName(WheatMod.MOD_ID, "small_cobblestone"),
                new SmallStoneBlock().setRegistryName(WheatMod.MOD_ID, "granite_stone"),
                new SmallStoneBlock().setRegistryName(WheatMod.MOD_ID, "diorite_stone"),
                new SmallStoneBlock().setRegistryName(WheatMod.MOD_ID, "andesite_stone"),
                new SmallStoneBlock().setRegistryName(WheatMod.MOD_ID, "small_sandstone"),

                //  Vegetabls
                new ModCropsBlock(ModItemUtils.tomato_seeds, "tomato"),

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
        IModBlock[] blocks = BLOCK_MANAGER.getBlocks();
        final BlockColors blockColors = event.getBlockColors();

        for (IModBlock i : blocks) {
            if (i.getColor() != null) {
                blockColors.register(i.getColor(), i.asBlock());
            }
        }

        blockColors.register(ModBlockColors.WILD_EINKORN, wild_einkorn);

        blockColors.register(ModBlockColors.COMMON_WHEAT, common_wheat);

        blockColors.register(ModBlockColors.EINKORN, einkorn);

        blockColors.register(ModBlockColors.WILD_EMMER, wild_emmer);

        blockColors.register(ModBlockColors.EMMER, emmer);

        blockColors.register(ModBlockColors.DURUM, durum);

        blockColors.register(ModBlockColors.SPELT, spelt);

        blockColors.register(ModBlockColors.DISEASED_WHEAT, diseased_wheat);
    }

    /**
     * Register the blocks for fire. Register mutations.
     */
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        IModBlock[] blocks = BLOCK_MANAGER.getBlocks();
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;

        for (IModBlock i : blocks) {
            if (i.getFlammability() > 0.0f) {
                fireBlock.setFireInfo(i.asBlock(), i.getFireEncouragement(), i.getFlammability());
            }
        }

        //  Mutations
        wild_einkorn.registerMutations(einkorn, common_wheat);
        wild_emmer.registerMutations(emmer, durum);

        emmer.registerMutation(spelt, common_wheat);
        common_wheat.registerMutation(spelt, emmer);
    }
}
