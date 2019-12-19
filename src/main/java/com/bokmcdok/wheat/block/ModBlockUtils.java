package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.data.ModBlockManager;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Registers all the blocks used in this mod.
 */
@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(WheatMod.MOD_ID)
public class ModBlockUtils {

    /**
     * The new types of wheat.
     */
    public static final ModCropsBlock wild_einkorn = null;
    public static final ModCropsBlock common_wheat = null;
    public static final ModCropsBlock einkorn = null;
    public static final ModCropsBlock wild_emmer = null;
    public static final ModCropsBlock emmer = null;
    public static final ModCropsBlock durum = null;
    public static final ModCropsBlock spelt = null;

    /**
     * Vegetables
     */
    public static final ModCropsBlock tomato = null;

    /**
     * Flour Mill
     */
    public static final FlourMillBlock flour_mill = null;

    public static Set<Block> MUSHROOMS;
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

        MUSHROOMS = new HashSet<Block>(Arrays.asList(
            Blocks.BROWN_MUSHROOM_BLOCK,
            Blocks.RED_MUSHROOM_BLOCK,
            Blocks.MUSHROOM_STEM,
            Blocks.BROWN_MUSHROOM,
            Blocks.RED_MUSHROOM,
            Blocks.POTTED_BROWN_MUSHROOM,
            Blocks.POTTED_RED_MUSHROOM
        ));
    }
}
