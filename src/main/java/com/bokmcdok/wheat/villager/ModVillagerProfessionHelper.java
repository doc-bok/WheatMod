package com.bokmcdok.wheat.villager;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.ai.tasks.ModGiveHeroGiftsTask;
import com.bokmcdok.wheat.data.ModProfessionManager;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModVillagerProfessionHelper {
    private static ModProfessionManager PROFESSION_MANAGER = new ModProfessionManager();

    @SubscribeEvent
    public static void registerPointsOfInterest(RegistryEvent.Register<PointOfInterestType> event) {
        PROFESSION_MANAGER.loadDataEntries("villagers");

        Collection<ModVillagerProfession> professions = PROFESSION_MANAGER.getAllEntries();
        for (ModVillagerProfession i : professions) {
            Optional<Block> block = Registry.BLOCK.getValue(i.getPOI());
            if (block.isPresent()) {
                PointOfInterestType poi = new PointOfInterestType(
                        i.getLocation().getPath(),
                        ImmutableSet.copyOf(block.get().getStateContainer().getValidStates()),
                        1, SoundEvents.ENTITY_VILLAGER_WORK_BUTCHER, 1);

                event.getRegistry().registerAll(poi.setRegistryName(i.getLocation()));

                try {
                    Method func_221052_a = ObfuscationReflectionHelper.findMethod(PointOfInterestType.class, "func_221052_a", PointOfInterestType.class);
                    func_221052_a.invoke(null, poi);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerProfession(RegistryEvent.Register<VillagerProfession> event) {
        Collection<ModVillagerProfession> professions = PROFESSION_MANAGER.getAllEntries();
        for (ModVillagerProfession i : professions) {
            Optional<PointOfInterestType> poi = Registry.POINT_OF_INTEREST_TYPE.getValue(i.getLocation());
            if (poi.isPresent()) {
                VillagerProfession profession = new VillagerProfession(i.getLocation().getPath(), poi.get(), ImmutableSet.of(), ImmutableSet.of());
                event.getRegistry().registerAll(profession.setRegistryName(i.getLocation()));
            }
        }
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {

        Collection<ModVillagerProfession> professions = PROFESSION_MANAGER.getAllEntries();
        for (ModVillagerProfession i : professions) {
            Optional<VillagerProfession> profession = Registry.VILLAGER_PROFESSION.getValue(i.getLocation());
            if (profession.isPresent()) {
                List<VillagerTrades.ITrade> noviceTrades = new ArrayList<>();
                List<VillagerTrades.ITrade> apprenticeTrades = new ArrayList<>();
                List<VillagerTrades.ITrade> journeymanTrades = new ArrayList<>();
                List<VillagerTrades.ITrade> expertTrades = new ArrayList<>();
                List<VillagerTrades.ITrade> masterTrades = new ArrayList<>();

                List<ModVillagerTrade> temp = i.getTrades(ModVillagerProfession.TradeLevel.NOVICE);
                for (ModVillagerTrade x : temp) {
                    noviceTrades.add(x.build());
                }

                temp = i.getTrades(ModVillagerProfession.TradeLevel.APPRENTICE);
                for (ModVillagerTrade x : temp) {
                    apprenticeTrades.add(x.build());
                }

                temp = i.getTrades(ModVillagerProfession.TradeLevel.JOURNEYMAN);
                for (ModVillagerTrade x : temp) {
                    journeymanTrades.add(x.build());
                }

                temp = i.getTrades(ModVillagerProfession.TradeLevel.EXPERT);
                for (ModVillagerTrade x : temp) {
                    expertTrades.add(x.build());
                }

                temp = i.getTrades(ModVillagerProfession.TradeLevel.MASTER);
                for (ModVillagerTrade x : temp) {
                    masterTrades.add(x.build());
                }

                Int2ObjectMap<VillagerTrades.ITrade[]> trades = new Int2ObjectOpenHashMap<>(ImmutableMap.of(
                        1, noviceTrades.toArray(new VillagerTrades.ITrade[0]),
                        2, apprenticeTrades.toArray(new VillagerTrades.ITrade[0]),
                        3, journeymanTrades.toArray(new VillagerTrades.ITrade[0]),
                        4, expertTrades.toArray(new VillagerTrades.ITrade[0]),
                        5, masterTrades.toArray(new VillagerTrades.ITrade[0])));

                VillagerTrades.field_221239_a.put(profession.get(), trades);

                ModGiveHeroGiftsTask.GIFTS.put(profession.get(), i.getGifts());
            }
        }
    }
}
