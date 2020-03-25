package com.bokmcdok.wheat.entity;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.entity.creature.animal.butterfly.ModButterflyEntity;
import com.bokmcdok.wheat.entity.creature.animal.butterfly.ModButterflyRenderFactory;
import com.bokmcdok.wheat.entity.creature.animal.cornsnake.ModCornsnakeEntity;
import com.bokmcdok.wheat.entity.creature.animal.cornsnake.ModCornsnakeRenderFactory;
import com.bokmcdok.wheat.entity.creature.animal.mouse.ModMouseEntity;
import com.bokmcdok.wheat.entity.creature.animal.mouse.ModMouseRenderFactory;
import com.bokmcdok.wheat.entity.creature.animal.widowbird.ModWidowbirdEntity;
import com.bokmcdok.wheat.entity.creature.animal.widowbird.ModWidowbirdRenderFactory;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.ahrenkind.ModAhrenkindEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.ahrenkind.ModAhrenkindRenderFactory;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter.ModWeizenmutterCornsnakeEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter.ModWeizenmutterCornsnakeRenderFactory;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter.ModWeizenmutterEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter.ModWeizenmutterGetreidewolfEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter.ModWeizenmutterGetreidewulfRenderFactory;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter.ModWeizenmutterRenderFactory;
import com.bokmcdok.wheat.entity.creature.feldgeister.getreidehahn.ModGetreidehahnEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.getreidehahn.ModGetreidehahnRenderFactory;
import com.bokmcdok.wheat.entity.creature.feldgeister.getreidewolf.ModGetreidewolfEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.getreidewolf.ModGetreidewolfRenderFactory;
import com.bokmcdok.wheat.entity.creature.feldgeister.haferbock.ModHaferbockEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.haferbock.ModHaferbockRenderFactory;
import com.bokmcdok.wheat.entity.creature.feldgeister.heukatze.ModHeukatzeEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.heukatze.ModHeukatzeRenderFactory;
import com.bokmcdok.wheat.entity.creature.feldgeister.weizenbeller.ModWeizenbellerEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.weizenbeller.ModWeizenbellerRenderFactory;
import com.bokmcdok.wheat.entity.creature.feldgeister.weizenvogel.ModWeizenvogelEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.weizenvogel.ModWeizenvogelRenderFactory;
import com.bokmcdok.wheat.entity.tile.ModCampfireTileEntity;
import com.bokmcdok.wheat.entity.tile.ModInventoryTileEntity;
import com.bokmcdok.wheat.entity.tile.ModTrapTileEntity;
import com.bokmcdok.wheat.item.ModItemUtils;
import com.bokmcdok.wheat.render.ModCampfireTileEntityRenderer;
import com.bokmcdok.wheat.render.StoneRenderer;
import com.bokmcdok.wheat.supplier.ModBlockSupplier;
import com.bokmcdok.wheat.tag.ModTagRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.LazyValue;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Set;

@ObjectHolder(WheatMod.MOD_ID)
public class ModEntityRegistrar {    public static final EntityType<ThrownItemEntity> stone_entity = null;

    public static final EntityType<ModMouseEntity> field_mouse = null;
    public static final EntityType<ModButterflyEntity> butterfly = null;
    public static final EntityType<ModWidowbirdEntity> widowbird = null;
    public static final EntityType<ModGetreidewolfEntity> getreidewolf = null;
    public static final EntityType<ModWeizenbellerEntity> weizenbeller = null;
    public static final EntityType<ModHeukatzeEntity> heukatze = null;
    public static final EntityType<ModHaferbockEntity> haferbock = null;
    public static final EntityType<ModWeizenvogelEntity> weizenvogel = null;
    public static final EntityType<ModGetreidehahnEntity> getreidehahn = null;
    public static final EntityType<ModCornsnakeEntity> cornsnake = null;
    public static final EntityType<ModWeizenmutterEntity> weizenmutter = null;
    public static final EntityType<ModWeizenmutterCornsnakeEntity> weizenmutter_cornsnake = null;
    public static final EntityType<ModWeizenmutterGetreidewolfEntity> weizenmutter_getreidewolf = null;
    public static final EntityType<ModAhrenkindEntity> ahrenkind = null;

    public static final TileEntityType<ModInventoryTileEntity> inventory = null;
    public static final TileEntityType<ModTrapTileEntity> trap = null;
    public static final TileEntityType<ModCampfireTileEntity> campfire = null;

    private static LazyValue<Block> CAMPFIRE = new LazyValue<>(new ModBlockSupplier("docwheat:campfire"));

    private final ModTagRegistrar mTagRegistrar;

    /**
     * Construction
     * @param tagRegistrar The tag registrar
     */
    public ModEntityRegistrar(ModTagRegistrar tagRegistrar) {
        mTagRegistrar = tagRegistrar;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(EntityType.class, this::onEntityRegistryEvent);
        modEventBus.addGenericListener(TileEntityType.class, this::onTileEntityRegistryEvent);
        modEventBus.addListener(this::commonSetup);
    }

    /**
     * Register spawn entries
     * @param event The common setup event
     */
    protected void commonSetup(FMLCommonSetupEvent event)
    {
        EntitySpawnPlacementRegistry.register(field_mouse, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModMouseEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(butterfly, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModButterflyEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(widowbird, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModWidowbirdEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(getreidewolf, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModGetreidewolfEntity::canGetreideWolfSpawn);
        EntitySpawnPlacementRegistry.register(weizenbeller, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModWeizenbellerEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(heukatze, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModHeukatzeEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(haferbock, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModHaferbockEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(weizenvogel, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModWeizenvogelEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(getreidehahn, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModGetreidehahnEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(cornsnake, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModCornsnakeEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(weizenmutter, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModWeizenmutterEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(weizenmutter_cornsnake, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModWeizenmutterCornsnakeEntity::canWeizenmutterSpawn);
        EntitySpawnPlacementRegistry.register(weizenmutter_getreidewolf, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModWeizenmutterGetreidewolfEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(ahrenkind, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ModAhrenkindEntity::canSpawn);

        RenderingRegistry.registerEntityRenderingHandler(stone_entity, new StoneRenderer());
        RenderingRegistry.registerEntityRenderingHandler(field_mouse, new ModMouseRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(butterfly, new ModButterflyRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(widowbird, new ModWidowbirdRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(getreidewolf, new ModGetreidewolfRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(weizenbeller, new ModWeizenbellerRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(heukatze, new ModHeukatzeRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(haferbock, new ModHaferbockRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(weizenvogel, new ModWeizenvogelRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(getreidehahn, new ModGetreidehahnRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(cornsnake, new ModCornsnakeRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(weizenmutter, new ModWeizenmutterRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(weizenmutter_cornsnake, new ModWeizenmutterCornsnakeRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(weizenmutter_getreidewolf, new ModWeizenmutterGetreidewulfRenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(ahrenkind, new ModAhrenkindRenderFactory());

        ClientRegistry.bindTileEntityRenderer(campfire, ModCampfireTileEntityRenderer::new);
    }

    /**
     * Register the mod-specific entities.
     * @param event The event data.
     */
    private void onEntityRegistryEvent(final RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(
                EntityType.Builder.<ThrownItemEntity>create(ThrownItemEntity::new, EntityClassification.MISC)
                        .size(0.25f, 0.25f).build("stone_entity")
                        .setRegistryName(WheatMod.MOD_ID, "stone_entity"),

                EntityType.Builder.<ModMouseEntity>create(ModMouseEntity::new, EntityClassification.CREATURE)
                        .size(0.3f, 0.3f)
                        .build("field_mouse")
                        .setRegistryName(WheatMod.MOD_ID, "field_mouse"),

                EntityType.Builder.<ModButterflyEntity>create(ModButterflyEntity::new, EntityClassification.CREATURE)
                        .size(0.3f, 0.4f)
                        .build("butterfly")
                        .setRegistryName(WheatMod.MOD_ID, "butterfly"),

                EntityType.Builder.create(ModWidowbirdEntity::new, EntityClassification.CREATURE)
                        .size(0.5f, 0.9f)
                        .build("widowbird")
                        .setRegistryName(WheatMod.MOD_ID, "widowbird"),

                EntityType.Builder.create(ModCornsnakeEntity::new, EntityClassification.CREATURE)
                        .size(0.3f, 0.3f)
                        .build("cornsnake")
                        .setRegistryName(WheatMod.MOD_ID, "cornsnake"),

                EntityType.Builder.create(ModGetreidewolfEntity::new, EntityClassification.MONSTER)
                        .size(0.6f, 0.85f)
                        .build("getreidewolf")
                        .setRegistryName(WheatMod.MOD_ID, "getreidewolf"),

                EntityType.Builder.create(ModWeizenbellerEntity::new, EntityClassification.MONSTER)
                        .size(0.6f, 0.7f)
                        .build("weizenbeller")
                        .setRegistryName(WheatMod.MOD_ID, "weizenbeller"),

                EntityType.Builder.create(ModHeukatzeEntity::new, EntityClassification.MONSTER)
                        .size(0.6f, 0.7f)
                        .build("heukatze")
                        .setRegistryName(WheatMod.MOD_ID, "heukatze"),

                EntityType.Builder.create(ModHaferbockEntity::new, EntityClassification.MONSTER)
                        .size(0.9f, 1.4f)
                        .build("haferbock")
                        .setRegistryName(WheatMod.MOD_ID, "haferbock"),

                EntityType.Builder.create(ModWeizenvogelEntity::new, EntityClassification.MONSTER)
                        .size(0.9f, 1.4f)
                        .build("weizenvogel")
                        .setRegistryName(WheatMod.MOD_ID, "weizenvogel"),

                EntityType.Builder.create(ModGetreidehahnEntity::new, EntityClassification.MONSTER)
                        .size(0.9f, 1.4f)
                        .build("getreidehahn")
                        .setRegistryName(WheatMod.MOD_ID, "getreidehahn"),

                EntityType.Builder.create(ModWeizenmutterEntity::new, EntityClassification.MONSTER)
                        .size(0.6f, 1.95f)
                        .build("weizenmutter")
                        .setRegistryName(WheatMod.MOD_ID, "weizenmutter"),

                EntityType.Builder.create(ModWeizenmutterCornsnakeEntity::new, EntityClassification.MONSTER)
                        .size(0.6f, 1.95f)
                        .build("weizenmutter_cornsnake")
                        .setRegistryName(WheatMod.MOD_ID, "weizenmutter_cornsnake"),

                EntityType.Builder.create(ModWeizenmutterGetreidewolfEntity::new, EntityClassification.MONSTER)
                        .size(0.6f, 1.95f)
                        .build("weizenmutter_getreidewolf")
                        .setRegistryName(WheatMod.MOD_ID, "weizenmutter_getreidewolf"),

                EntityType.Builder.create(ModAhrenkindEntity::new, EntityClassification.MONSTER)
                        .size(0.6f, 1.95f)
                        .build("ahrenkind")
                        .setRegistryName(WheatMod.MOD_ID, "ahrenkind")
        );

        ModItemUtils.loadSpawnEggs();
    }

    /**
     * Register the mod-specific tile entities.
     * @param event The event data.
     */
    private void onTileEntityRegistryEvent(final RegistryEvent.Register<TileEntityType<?>> event) {

        //  Register the trap tile entity with any blocks that have traps.
        Set<Block> traps = mTagRegistrar.getBlockTag("docwheat:trap").getBlocks();
        if (!traps.isEmpty()) {
            event.getRegistry().registerAll(
                    TileEntityType.Builder.create(ModTrapTileEntity::new, traps.toArray(new Block[0]))
                            .build(null)
                            .setRegistryName(WheatMod.MOD_ID, "trap")
            );
        }

        event.getRegistry().registerAll(
                TileEntityType.Builder.create(ModCampfireTileEntity::new, CAMPFIRE.getValue())
                        .build(null)
                        .setRegistryName(WheatMod.MOD_ID, "campfire"));
    }
}
