package com.bokmcdok.wheat.villager;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.item.ModItemUtils;
import com.bokmcdok.wheat.trade.ModEmeraldForItemsTrade;
import com.bokmcdok.wheat.trade.ModItemsForEmeraldsTrade;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Items;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModVillagerProfession {
    public static VillagerProfession BAKER;

    @SubscribeEvent
    public static void registerProfession(RegistryEvent.Register<VillagerProfession> event) {
        BAKER = new VillagerProfession("baker", ModPointOfInterestType.BAKER, ImmutableSet.of(), ImmutableSet.of());
        event.getRegistry().registerAll(BAKER.setRegistryName("baker"));
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        Int2ObjectMap<VillagerTrades.ITrade[]> bakerTrades = new Int2ObjectOpenHashMap<>(ImmutableMap.of(
                1, new VillagerTrades.ITrade[] {
                        new ModEmeraldForItemsTrade(ModItemUtils.common_flour, 20, 16, 2),
                        new ModEmeraldForItemsTrade(ModItemUtils.durum_flour, 20, 16, 2),
                        new ModItemsForEmeraldsTrade(Items.BREAD, 1, 6, 16, 1)
                },
                2, new VillagerTrades.ITrade[] {
                        new ModEmeraldForItemsTrade(ModItemUtils.dough, 6, 12, 10),
                        new ModItemsForEmeraldsTrade(ModItemUtils.porridge, 1, 4, 5),
                        new ModItemsForEmeraldsTrade(ModItemUtils.pasta, 1, 4, 16, 5)
                },
                3, new VillagerTrades.ITrade[] {
                        new ModItemsForEmeraldsTrade(ModItemUtils.donut, 3, 18, 10),
                        new ModEmeraldForItemsTrade(Items.COOKIE, 4, 12, 20)
                },
                4, new VillagerTrades.ITrade[] {
                        new ModItemsForEmeraldsTrade(Items.CAKE, 4, 1, 15),
                        new ModItemsForEmeraldsTrade(ModItemUtils.gravy, 4, 1, 15),
                },
                5, new VillagerTrades.ITrade[] {
                        new ModItemsForEmeraldsTrade(ModItemUtils.meat_pie, 4, 1, 30),
                        new ModItemsForEmeraldsTrade(ModItemUtils.fish_pie, 4, 1, 30),
                        new ModItemsForEmeraldsTrade(ModItemUtils.apple_pie, 4, 1, 30)
                }));

        VillagerTrades.field_221239_a.put(ModVillagerProfession.BAKER, bakerTrades);
    }
}
