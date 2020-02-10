package com.bokmcdok.wheat;

import com.bokmcdok.wheat.dimension.ModDimensionRegistrar;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.RegisterDimensionsEvent;

public class ModForgeEventHandler {
    public static final ResourceLocation WHEAT_DIMENSION_TYPE = new ResourceLocation(WheatMod.MOD_ID, "wheat_dimension");

    /**
     * Construction
     */
    public ModForgeEventHandler() {
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterDimensionsEvent);
    }

    /**
     * Register dimension types.
     * @param event The event.
     */
    private void onRegisterDimensionsEvent(RegisterDimensionsEvent event) {
        if (DimensionType.byName(WHEAT_DIMENSION_TYPE) == null) {
            DimensionManager.registerDimension(WHEAT_DIMENSION_TYPE, ModDimensionRegistrar.WHEAT_DIMENSION, null, true);
        }
    }
}
