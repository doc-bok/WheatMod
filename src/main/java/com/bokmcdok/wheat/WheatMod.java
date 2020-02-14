package com.bokmcdok.wheat;

import com.bokmcdok.wheat.container.ModContainerUtils;
import com.bokmcdok.wheat.dimension.ModDimensionRegistrar;
import com.bokmcdok.wheat.entity.creature.villager.ModMemoryModuleRegistrar;
import com.bokmcdok.wheat.screen.FlourMillScreen;
import net.minecraft.client.gui.ScreenManager;
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
        new ModForgeEventHandler();
        new ModDimensionRegistrar();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainerUtils.flour_mill_container, FlourMillScreen::new);
    }
}
