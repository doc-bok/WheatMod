package com.bokmcdok.wheat;

import com.bokmcdok.wheat.Container.ModContainers;
import com.bokmcdok.wheat.Entity.StoneEntity;
import com.bokmcdok.wheat.Render.StoneRenderer;
import com.bokmcdok.wheat.Screen.FlourMillScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * The Mod Class
 *
 * Just holds constants used in other places in the mod.
 */
@Mod(WheatMod.MOD_ID)
public class WheatMod
{
    public static final String MOD_ID = "docwheat";

    public WheatMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(StoneEntity.class, new StoneRenderer());

        ScreenManager.registerFactory(ModContainers.flour_mill_container, FlourMillScreen::new);
    }
}
