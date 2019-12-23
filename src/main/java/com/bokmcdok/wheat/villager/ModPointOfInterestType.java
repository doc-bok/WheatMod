package com.bokmcdok.wheat.villager;


import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.block.ModBlockUtils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Method;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModPointOfInterestType {

    public static PointOfInterestType BAKER;

    @SubscribeEvent
    public static void registerPointsOfInterest(RegistryEvent.Register<PointOfInterestType> event) {
        BAKER = new PointOfInterestType(
                "baker",
                ImmutableSet.copyOf(ModBlockUtils.flour_mill.getStateContainer().getValidStates()),
                1, SoundEvents.ENTITY_VILLAGER_WORK_BUTCHER, 1);

        event.getRegistry().registerAll(BAKER.setRegistryName("baker"));

        try {
            Method func_221052_a = ObfuscationReflectionHelper.findMethod(PointOfInterestType.class, "func_221052_a", PointOfInterestType.class);
            func_221052_a.invoke(null, BAKER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
