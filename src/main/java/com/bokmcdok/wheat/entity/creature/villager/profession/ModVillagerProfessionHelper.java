package com.bokmcdok.wheat.entity.creature.villager.profession;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.ai.tasks.ModGiveHeroGiftsTask;
import com.bokmcdok.wheat.entity.creature.villager.trade.ModVillagerTradeBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModVillagerProfessionHelper {
    public static ModVillagerProfessionDataManager PROFESSION_MANAGER = new ModVillagerProfessionDataManager();

    @SubscribeEvent
    public static void registerPointsOfInterest(RegistryEvent.Register<PointOfInterestType> event) {
        PROFESSION_MANAGER.loadProfessions();

        Collection<ForgeRegistryEntry<VillagerProfession>> professions = PROFESSION_MANAGER.getAllEntries();
        for (ForgeRegistryEntry<VillagerProfession> i : professions) {
            if (i instanceof ModVillagerProfession) {
                ModVillagerProfession profession = (ModVillagerProfession) i;
                Optional<Block> block = Registry.BLOCK.getValue(profession.getPOI());
                if (block.isPresent()) {
                    try {
                        Method func_226359_a_ = ObfuscationReflectionHelper.findMethod(PointOfInterestType.class, "func_226359_a_", String.class, Set.class, int.class, int.class);
                        func_226359_a_.invoke(null, profession.getLocation().toString(), ImmutableSet.copyOf(block.get().getStateContainer().getValidStates()), 1, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerProfession(RegistryEvent.Register<VillagerProfession> event) {
        Collection<ForgeRegistryEntry<VillagerProfession>> professions = PROFESSION_MANAGER.getAllEntries();
        for (ForgeRegistryEntry<VillagerProfession> i : professions) {
            if (i instanceof ModVillagerProfession) {
                ModVillagerProfession profession = (ModVillagerProfession) i;
                Optional<PointOfInterestType> poi = Registry.POINT_OF_INTEREST_TYPE.getValue(profession.getLocation());
                if (poi.isPresent()) {

                    try {
                        Method func_226557_a_ = ObfuscationReflectionHelper.findMethod(VillagerProfession.class, "func_226557_a_", String.class, PointOfInterestType.class, ImmutableSet.class, ImmutableSet.class, SoundEvent.class);
                        func_226557_a_.invoke(null, profession.getLocation().toString(), poi.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_BUTCHER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {

        Collection<ForgeRegistryEntry<VillagerProfession>> professions = PROFESSION_MANAGER.getAllEntries();
        for (ForgeRegistryEntry<VillagerProfession> i : professions) {
            if (i instanceof ModVillagerProfession) {
                ModVillagerProfession professioData = (ModVillagerProfession) i;
                Optional<VillagerProfession> profession = Registry.VILLAGER_PROFESSION.getValue(professioData.getLocation());
                if (profession.isPresent()) {
                    List<VillagerTrades.ITrade> noviceTrades = new ArrayList<>();
                    List<VillagerTrades.ITrade> apprenticeTrades = new ArrayList<>();
                    List<VillagerTrades.ITrade> journeymanTrades = new ArrayList<>();
                    List<VillagerTrades.ITrade> expertTrades = new ArrayList<>();
                    List<VillagerTrades.ITrade> masterTrades = new ArrayList<>();

                    List<ModVillagerTradeBuilder> temp = professioData.getTrades(ModVillagerProfession.TradeLevel.NOVICE);
                    for (ModVillagerTradeBuilder x : temp) {
                        noviceTrades.add(x.build());
                    }

                    temp = professioData.getTrades(ModVillagerProfession.TradeLevel.APPRENTICE);
                    for (ModVillagerTradeBuilder x : temp) {
                        apprenticeTrades.add(x.build());
                    }

                    temp = professioData.getTrades(ModVillagerProfession.TradeLevel.JOURNEYMAN);
                    for (ModVillagerTradeBuilder x : temp) {
                        journeymanTrades.add(x.build());
                    }

                    temp = professioData.getTrades(ModVillagerProfession.TradeLevel.EXPERT);
                    for (ModVillagerTradeBuilder x : temp) {
                        expertTrades.add(x.build());
                    }

                    temp = professioData.getTrades(ModVillagerProfession.TradeLevel.MASTER);
                    for (ModVillagerTradeBuilder x : temp) {
                        masterTrades.add(x.build());
                    }

                    Int2ObjectMap<VillagerTrades.ITrade[]> trades = new Int2ObjectOpenHashMap<>(ImmutableMap.of(
                            1, noviceTrades.toArray(new VillagerTrades.ITrade[0]),
                            2, apprenticeTrades.toArray(new VillagerTrades.ITrade[0]),
                            3, journeymanTrades.toArray(new VillagerTrades.ITrade[0]),
                            4, expertTrades.toArray(new VillagerTrades.ITrade[0]),
                            5, masterTrades.toArray(new VillagerTrades.ITrade[0])));

                    VillagerTrades.VILLAGER_DEFAULT_TRADES.put(profession.get(), trades);

                    ModGiveHeroGiftsTask.GIFTS.put(profession.get(), professioData.getGifts());
                }
            }
        }
    }
}
