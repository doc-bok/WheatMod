package com.bokmcdok.wheat;

import com.bokmcdok.wheat.dimension.ModDimensionRegistrar;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus= Mod.EventBusSubscriber.Bus.FORGE)
public class ModForgeEventHandler {
    public static final ResourceLocation WHEAT_DIMENSION_TYPE = new ResourceLocation(WheatMod.MOD_ID, "wheat_dimension");

    /**
     * Register dimension types.
     * @param event The event.
     */
    @SubscribeEvent
    public static void onRegisterDimensionsEvent(RegisterDimensionsEvent event) {
        if (DimensionType.byName(WHEAT_DIMENSION_TYPE) == null) {
            DimensionManager.registerDimension(WHEAT_DIMENSION_TYPE, ModDimensionRegistrar.WHEAT_DIMENSION, null, true);
        }
    }
}
