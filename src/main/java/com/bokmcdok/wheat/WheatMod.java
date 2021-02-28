package com.bokmcdok.wheat;

import com.bokmcdok.wheat.block.ModBlockRegistrar;
import com.bokmcdok.wheat.container.ModContainerUtils;
import com.bokmcdok.wheat.dimension.ModDimensionRegistrar;
import com.bokmcdok.wheat.entity.ModEntityRegistrar;
import com.bokmcdok.wheat.screen.FlourMillScreen;
import com.bokmcdok.wheat.spell.ModSpellRegistrar;
import com.bokmcdok.wheat.tag.ModTagRegistrar;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * The Mod Class
 * Just holds constants used in other places in the mod.
 */
@Mod(WheatMod.MOD_ID)
public class WheatMod
{
    public static final String MOD_ID = "docwheat";
    public static final ModSpellRegistrar SPELL_REGISTRAR = new ModSpellRegistrar();
    public static final ModTagRegistrar TAG_REGISTRAR = new ModTagRegistrar();

    public WheatMod() {
        new ModBlockRegistrar(TAG_REGISTRAR);
        new ModEntityRegistrar(TAG_REGISTRAR);
        new ModForgeEventHandler(TAG_REGISTRAR);
        new ModDimensionRegistrar();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainerUtils.flour_mill_container, FlourMillScreen::new);
    }
}
