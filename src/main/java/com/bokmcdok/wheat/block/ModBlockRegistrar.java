package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.tag.ModTagRegistrar;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ModBlockRegistrar {
    private  final ModBlockDataManager mBlockDataManager;
    private final ModTagRegistrar mTagRegistrar;

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
     * Construction
     * @param tagRegistrar The tag registrar.
     */
    public ModBlockRegistrar(ModTagRegistrar tagRegistrar) {
        mTagRegistrar = tagRegistrar;
        mBlockDataManager = new ModBlockDataManager(tagRegistrar);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(Block.class, this::onBlockRegistryEvent);
        modEventBus.addListener(this::onColorHandlerEvent);
        modEventBus.addListener(this::commonSetup);
    }

    /**
     * Load and register any mod-specific blocks.
     * @param event The event data.
     */
    private void onBlockRegistryEvent(RegistryEvent.Register<Block> event) {
        mBlockDataManager.loadBlocks();
        Block[] blocks = mBlockDataManager.getAsBlocks();
        event.getRegistry().registerAll(blocks);

        event.getRegistry().registerAll(
                new FlourMillBlock("flour_mill"),

                new ModCampfireBlock(
                        mTagRegistrar,
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
     * Register any custom colors for blocks.
     * @param event The event data.
     */
    private void onColorHandlerEvent(ColorHandlerEvent.Block event) {
        IModBlock[] blocks = mBlockDataManager.getBlocks();
        final BlockColors blockColors = event.getBlockColors();

        for (IModBlock i : blocks) {
            if (i.getColor() != null) {
                blockColors.register(i.getColor(), i.asBlock());
            }
        }
    }

    /**
     * Set the flammability and render types of blocks.
     * @param event The event data.
     */
    private void commonSetup(FMLCommonSetupEvent event) {
        IModBlock[] blocks = mBlockDataManager.getBlocks();
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;

        for (IModBlock i : blocks) {
            if (i.getFlammability() > 0.0f) {
                fireBlock.setFireInfo(i.asBlock(), i.getFireEncouragement(), i.getFlammability());
            }

            if (!"solid".equals(i.getRenderType())) {
                RenderTypeLookup.setRenderLayer(i.asBlock(), getRenderType(i.getRenderType()));
            }
        }

        RenderTypeLookup.setRenderLayer(ModBlockUtils.campfire, getRenderType("cutout"));
    }

    /**
     * Helper method to get the render type.
     * @param renderType The string name of the render type.
     * @return A RenderType instance.
     */
    private RenderType getRenderType(String renderType) {
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
