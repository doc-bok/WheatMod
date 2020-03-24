package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;

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

    public static final ModBlock cornsnake_egg = null;
    public static final ModBlock widowbird_nest = null;

    public static final ModTrapBlock mouse_trap = null;
    public static final ModTrapBlock seeded_mouse_trap = null;

    public static final ModCampfireBlock campfire = null;

    private static final ModBlockManager BLOCK_MANAGER = new ModBlockManager();

    private enum ModRenderType {
        SOLID,
        CUTOUT_MIPPED,
        CUTOUT,
        TRANSLUCENT,
        TRANSLUCENT_NO_CRUMBLING,
        LEASH,
        WATER_MASK,
        GLINT,
        ENTITY_GLINT,
        LIGHTNING,
        LINES
    }

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
                new FlourMillBlock("flour_mill"),

                new ModCampfireBlock(
                        Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN)
                                .hardnessAndResistance(2.0F)
                                .sound(SoundType.WOOD)
                                .lightValue(15)
                                .tickRandomly()
                                .func_226896_b_())
                        .setRegistryName(WheatMod.MOD_ID, "campfire")
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

            if (!"solid".equals(i.getRenderType())) {
                RenderTypeLookup.setRenderLayer(i.asBlock(), getRenderType(i.getRenderType()));
            }
        }

        RenderTypeLookup.setRenderLayer(campfire, getRenderType("cutout"));
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

    /**
     * Helper method to get the render type.
     * @param renderType The string name of the render type.
     * @return A RenderType instance.
     */
    private static RenderType getRenderType(String renderType) {
        ModRenderType modRenderType = ModRenderType.valueOf(renderType.toUpperCase());
        switch (modRenderType) {
            case CUTOUT_MIPPED:
                return RenderType.func_228641_d_();
            case CUTOUT:
                return RenderType.func_228643_e_();
            case TRANSLUCENT:
                return RenderType.func_228645_f_();
            case TRANSLUCENT_NO_CRUMBLING:
                return RenderType.func_228647_g_();
            case LEASH:
                return RenderType.func_228649_h_();
            case WATER_MASK:
                return RenderType.func_228651_i_();
            case GLINT:
                return RenderType.func_228653_j_();
            case ENTITY_GLINT:
                return RenderType.func_228655_k_();
            case LIGHTNING:
                return RenderType.func_228657_l_();
            case LINES:
                return RenderType.func_228659_m_();
            case SOLID:
            default:
                return RenderType.func_228639_c_();
        }
    }
}
