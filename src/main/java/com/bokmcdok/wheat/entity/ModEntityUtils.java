package com.bokmcdok.wheat.entity;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.entity.animal.butterfly.ModButterflyEntity;
import com.bokmcdok.wheat.entity.animal.butterfly.ModButterflyRenderFactory;
import com.bokmcdok.wheat.entity.animal.mouse.ModMouseEntity;
import com.bokmcdok.wheat.entity.animal.mouse.ModMouseRenderFactory;
import com.bokmcdok.wheat.entity.animal.widowbird.ModWidowbirdEntity;
import com.bokmcdok.wheat.entity.animal.widowbird.ModWidowbirdRenderFactory;
import com.bokmcdok.wheat.entity.feldgeister.getreidewolf.ModGetreidewolfEntity;
import com.bokmcdok.wheat.entity.feldgeister.getreidewolf.ModGetreidewolfRenderFactory;
import com.bokmcdok.wheat.entity.feldgeister.weizenbeller.ModWeizenbellerEntity;
import com.bokmcdok.wheat.entity.feldgeister.weizenbeller.ModWeizenbellerRenderFactory;
import com.bokmcdok.wheat.entity.tile.ModInventoryTileEntity;
import com.bokmcdok.wheat.entity.tile.ModTrapTileEntity;
import com.bokmcdok.wheat.item.ModItemUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;

@Mod.EventBusSubscriber(modid=WheatMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(WheatMod.MOD_ID)
public class ModEntityUtils {

    public static final EntityType<ModMouseEntity> field_mouse = null;
    public static final EntityType<ModButterflyEntity> butterfly = null;
    public static final EntityType<ModWidowbirdEntity> widowbird = null;
    public static final EntityType<ModGetreidewolfEntity> getreidewolf = null;
    public static final EntityType<ModGetreidewolfEntity> weizenbeller = null;

    public static final TileEntityType<ModInventoryTileEntity> inventory = null;
    public static final TileEntityType<ModTrapTileEntity> trap = null;

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
                        .setRegistryName(WheatMod.MOD_ID, "getreidewolf"),

                EntityType.Builder.create(ModWeizenbellerEntity::new, EntityClassification.MONSTER)
                        .size(0.4f, 0.3f)
                        .build("weizenbeller")
                        .setRegistryName(WheatMod.MOD_ID, "weizenbeller")
        );

        ModItemUtils.loadSpawnEggs();
    }

    /**
     * Register tile entities.
     * @param event The tile registry event.
     */
    @SubscribeEvent
    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {

        //  Register the trap tile entity with any blocks that have traps.
        List<Block> traps = ModBlockUtils.getTraps();
        if (!traps.isEmpty()) {
            event.getRegistry().registerAll(
                    TileEntityType.Builder.create(ModTrapTileEntity::new, traps.toArray(new Block[0]))
                            .build(null)
                            .setRegistryName(WheatMod.MOD_ID, "trap")
            );
        }
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
        EntitySpawnPlacementRegistry.register(widowbird, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModWidowbirdEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(getreidewolf, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModGetreidewolfEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(weizenbeller, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModWeizenbellerEntity::canSpawn);

        RenderingRegistry.registerEntityRenderingHandler(ModMouseEntity.class, new ModMouseRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(ModButterflyEntity.class, new ModButterflyRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(ModWidowbirdEntity.class, new ModWidowbirdRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(ModGetreidewolfEntity.class, new ModGetreidewolfRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(ModWeizenbellerEntity.class, new ModWeizenbellerRenderFactory());
    }
}
