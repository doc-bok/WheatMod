package com.bokmcdok.wheat.Entity;

import com.bokmcdok.wheat.Block.ModBlocks;
import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.CampfireTileEntity;
import net.minecraft.tileentity.TileEntityType;
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

    @SubscribeEvent
    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> entityRegistryEvent) {

        entityRegistryEvent.getRegistry().registerAll(
                TileEntityType.Builder.create(ModCampfireTileEntity::new, ModBlocks.campfire).build(null)
                        .setRegistryName(WheatMod.MOD_ID, "campfire")
        );
    }

    public static final EntityType<StoneEntity> stone_entity = null;
    public static final TileEntityType<CampfireTileEntity> campfire = null;
}
