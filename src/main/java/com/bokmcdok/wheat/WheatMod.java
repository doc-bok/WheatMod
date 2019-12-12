package com.bokmcdok.wheat;

import com.bokmcdok.wheat.container.ModContainerUtils;
import com.bokmcdok.wheat.entity.ThrownItemEntity;
import com.bokmcdok.wheat.render.StoneRenderer;
import com.bokmcdok.wheat.screen.FlourMillScreen;
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
        RenderingRegistry.registerEntityRenderingHandler(ThrownItemEntity.class, new StoneRenderer());

        ScreenManager.registerFactory(ModContainerUtils.flour_mill_container, FlourMillScreen::new);
    }
}
