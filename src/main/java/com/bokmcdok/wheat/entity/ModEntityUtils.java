package com.bokmcdok.wheat.entity;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.entity.animal.butterfly.ModButterflyEntity;
import com.bokmcdok.wheat.entity.animal.butterfly.ModButterflyRenderFactory;
import com.bokmcdok.wheat.entity.animal.mouse.ModMouseEntity;
import com.bokmcdok.wheat.entity.animal.mouse.ModMouseRenderFactory;
import com.bokmcdok.wheat.entity.animal.widowbird.ModWidowbirdEntity;
import com.bokmcdok.wheat.entity.animal.widowbird.ModWidowbirdRenderFactory;
import com.bokmcdok.wheat.entity.feldgeister.getreidewolf.ModGetreidewolfEntity;
import com.bokmcdok.wheat.entity.feldgeister.getreidewolf.ModGetreidewolfRenderFactory;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid=WheatMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(WheatMod.MOD_ID)
public class ModEntityUtils {

    public static final EntityType<ThrownItemEntity> stone_entity = null;
    public static final EntityType<ModMouseEntity> field_mouse = null;
    public static final EntityType<ModButterflyEntity> butterfly = null;
    public static final EntityType<ModWidowbirdEntity> widowbird = null;
    public static final EntityType<ModGetreidewolfEntity> getreidewolf = null;

    /**
     * Register entities used by the mod
     * @param event The event containing the Entity Registry
     */
    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(
                EntityType.Builder.<ThrownItemEntity>create(ThrownItemEntity::new, EntityClassification.MISC)
                        .size(0.25f, 0.25f).build("stone_entity")
                        .setRegistryName(WheatMod.MOD_ID, "stone_entity"),

                EntityType.Builder.<ModMouseEntity>create(ModMouseEntity::new, EntityClassification.CREATURE)
                        .size(0.4f, 0.3f)
                        .build("field_mouse")
                        .setRegistryName(WheatMod.MOD_ID, "field_mouse"),

                EntityType.Builder.<ModButterflyEntity>create(ModButterflyEntity::new, EntityClassification.CREATURE)
                        .size(0.4f, 0.3f)
                        .build("butterfly")
                        .setRegistryName(WheatMod.MOD_ID, "butterfly"),

                EntityType.Builder.create(ModWidowbirdEntity::new, EntityClassification.CREATURE)
                        .size(0.4f, 0.3f)
                        .build("widowbird")
                        .setRegistryName(WheatMod.MOD_ID, "widowbird"),

                EntityType.Builder.create(ModGetreidewolfEntity::new, EntityClassification.MONSTER)
                        .size(0.4f, 0.3f)
                        .build("getreidewolf")
                        .setRegistryName(WheatMod.MOD_ID, "getreidewolf")
        );
    }

    /**
     * Register spawn entries
     * @param event The common setup event
     */
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event)
    {
        EntitySpawnPlacementRegistry.register(field_mouse, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModMouseEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(butterfly, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModButterflyEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(widowbird, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModWidowbirdEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(getreidewolf, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModGetreidewolfEntity::canSpawn);

        RenderingRegistry.registerEntityRenderingHandler(ModMouseEntity.class, new ModMouseRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(ModButterflyEntity.class, new ModButterflyRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(ModWidowbirdEntity.class, new ModWidowbirdRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(ModGetreidewolfEntity.class, new ModGetreidewolfRenderFactory());
    }
}
