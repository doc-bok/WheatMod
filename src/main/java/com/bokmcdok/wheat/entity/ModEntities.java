package com.bokmcdok.wheat.entity;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid=WheatMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(WheatMod.MOD_ID)
public class ModEntities {

    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> entityRegistryEvent) {


        entityRegistryEvent.getRegistry().registerAll(
                EntityType.Builder.<StoneEntity>create(StoneEntity::new, EntityClassification.MISC)
                        .size(0.25f, 0.25f).build("stone_entity")
                        .setRegistryName(WheatMod.MOD_ID, "stone_entity")
        );
    }

    public static final EntityType<StoneEntity> stone_entity = null;
}
