package com.bokmcdok.wheat.villager;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.FirstShuffledTask;
import net.minecraft.entity.ai.brain.task.SpawnGolemTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.WalkTowardsPosTask;
import net.minecraft.entity.ai.brain.task.WalkTowardsRandomSecondaryPosTask;
import net.minecraft.entity.ai.brain.task.WorkTask;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;

public class ModVillagerTaskHelper {
    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> core(VillagerProfession profession, float movementSpeed) {
        return ImmutableList.of(Pair.of(4, new ModPickupFoodTask()));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> work(VillagerProfession profession, float p_220639_1_) {
        return ImmutableList.of(
                Pair.of(4, new FirstShuffledTask<>(ImmutableList.of(
                        Pair.of(new SpawnGolemTask(), 7),
                        Pair.of(new WorkTask(MemoryModuleType.JOB_SITE, 4), 2),
                        Pair.of(new WalkTowardsPosTask(MemoryModuleType.JOB_SITE, 1, 10), 5),
                        Pair.of(new WalkTowardsRandomSecondaryPosTask(MemoryModuleType.SECONDARY_JOB_SITE, 0.4F, 1, 6, MemoryModuleType.JOB_SITE), 5),
                        Pair.of(new ModFarmTask(), profession == VillagerProfession.FARMER ? 2 : 5)))),
                Pair.of(2, new ModGiveHeroGiftsTask(100)));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> meet(VillagerProfession profession, float p_220637_1_) {
        return ImmutableList.of(Pair.of(2, new ModGiveHeroGiftsTask(100)));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> idle(VillagerProfession p_220641_0_, float p_220641_1_) {
        return ImmutableList.of(Pair.of(2, new ModGiveHeroGiftsTask(100)));
    }
}
