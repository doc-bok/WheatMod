package com.bokmcdok.wheat.ai;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.village.PointOfInterestType;

public class ModVillagerTasks {
    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> core(VillagerProfession profession, float movementSpeed) {
        return ImmutableList.of(
                Pair.of(0, new SwimTask(0.4F, 0.8F)),
                Pair.of(0, new InteractWithDoorTask()),
                Pair.of(0, new LookTask(45, 90)),
                Pair.of(0, new PanicTask()),
                Pair.of(0, new WakeUpTask()),
                Pair.of(0, new HideFromRaidOnBellRingTask()),
                Pair.of(0, new BeginRaidTask()),
                Pair.of(1, new WalkToTargetTask(200)),
                Pair.of(2, new TradeTask(movementSpeed)),

                //  Replace with custom pickup food task
                Pair.of(4, new ModPickupFoodTask()),

                Pair.of(10, new GatherPOITask(profession.getPointOfInterest(), MemoryModuleType.JOB_SITE, true)),
                Pair.of(10, new GatherPOITask(PointOfInterestType.HOME, MemoryModuleType.HOME, false)),
                Pair.of(10, new GatherPOITask(PointOfInterestType.MEETING, MemoryModuleType.MEETING_POINT, true)),
                Pair.of(10, new AssignProfessionTask()), Pair.of(10, new ChangeJobTask()));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> work(VillagerProfession p_220639_0_, float p_220639_1_) {
        return ImmutableList.of(
                lookAtTasks(),
                Pair.of(4, new FirstShuffledTask<>(ImmutableList.of(
                        Pair.of(new SpawnGolemTask(), 7),
                        Pair.of(new WorkTask(MemoryModuleType.JOB_SITE, 4), 2),
                        Pair.of(new WalkTowardsPosTask(MemoryModuleType.JOB_SITE, 1, 10), 5),
                        Pair.of(new WalkTowardsRandomSecondaryPosTask(MemoryModuleType.SECONDARY_JOB_SITE, 0.4F, 1, 6, MemoryModuleType.JOB_SITE), 5),
                        Pair.of(new ModFarmTask(), p_220639_0_ == VillagerProfession.FARMER ? 2 : 5)))),
                Pair.of(10, new ShowWaresTask(400, 1600)),
                Pair.of(10, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)),
                Pair.of(2, new StayNearPointTask(MemoryModuleType.JOB_SITE, p_220639_1_, 9, 100, 1200)),
                Pair.of(3, new GiveHeroGiftsTask(100)),
                Pair.of(3, new ExpirePOITask(p_220639_0_.getPointOfInterest(), MemoryModuleType.JOB_SITE)),
                Pair.of(99, new UpdateActivityTask()));
    }

    private static Pair<Integer, Task<LivingEntity>> lookAtTasks() {
        return Pair.of(5, new FirstShuffledTask<>(ImmutableList.of(
                Pair.of(new LookAtEntityTask(EntityType.VILLAGER, 8.0F), 2),
                Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 2),
                Pair.of(new DummyTask(30, 60), 8))));
    }
}
