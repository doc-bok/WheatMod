package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Registers all the blocks used in this mod.
 */
@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(WheatMod.MOD_ID)
public class ModBlockUtils {
    public static final ModCropsBlock wild_einkorn = null;
    public static final ModCropsBlock common_wheat = null;
    public static final ModCropsBlock einkorn = null;
    public static final ModCropsBlock wild_emmer = null;
    public static final ModCropsBlock emmer = null;
    public static final ModCropsBlock durum = null;
    public static final ModCropsBlock spelt = null;
    public static final ModCropsBlock diseased_wheat = null;

    public static final ModCropsBlock tomato = null;

    public static final FlourMillBlock flour_mill = null;

    public static final ModBlock widowbird_nest = null;

    public static final ModTrapBlock mouse_trap = null;
    public static final ModTrapBlock seeded_mouse_trap = null;

    public static Set<Block> MUSHROOMS;
    public static Set<Block> CROPS;
    public static Set<Block> WHEAT;

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

        CROPS = new HashSet<>(Arrays.asList(
           Blocks.WHEAT,
           Blocks.BEETROOTS,
           Blocks.CARROTS,
           Blocks.POTATOES,
           tomato,
           wild_einkorn,
           wild_emmer,
           emmer,
           einkorn,
           common_wheat,
           durum,
           spelt
        ));

        WHEAT = new HashSet<>(Arrays.asList(
                Blocks.WHEAT,
                wild_einkorn,
                wild_emmer,
                emmer,
                einkorn,
                common_wheat,
                durum,
                spelt
        ));
    }

    /**
     * Check if the specified block is present within a certain radius.
     * @param world The current world.
     * @param position The position to check.
     * @param block The block to look for.
     * @param radius The radius to search.
     * @return TRUE if the block is within the specified radius.
     */
    public static boolean isBlockPresent(World world, BlockPos position, Block block, int radius) {
        for (int x = (radius * -1); x < radius + 1; x++) {
            for (int z = (radius * -1); z < radius + 1; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }

                BlockPos posToCheck = position.add(x, 0, z);
                if(world.getBlockState(posToCheck).getBlock() == block) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Get a list of traps loaded by the manager.
     * @return An array of traps if any are loaded.
     */
    public static List<Block> getTraps() {
        return BLOCK_MANAGER.getTraps();
    }
}
