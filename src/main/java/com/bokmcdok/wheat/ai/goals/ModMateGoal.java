package com.bokmcdok.wheat.ai.goals;

import com.bokmcdok.wheat.entity.creature.animal.ModNestingEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;

import java.util.Random;

public class ModMateGoal extends BreedGoal {
    private final ModNestingEntity mNestingEntity;

    /**
     * Construction
     * @param nestingEntity The entity that has the nesting behaviour.
     * @param speed The speed at which to mate
     */
    public ModMateGoal(ModNestingEntity nestingEntity, double speed) {
        super(nestingEntity, speed);
        mNestingEntity = nestingEntity;
    }

    /**
     * Execute this goal if the entity can breed.
     * @return TRUE if the goal should execute
     */
    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && !mNestingEntity.canBreed();
    }

    /**
     * Overridden so that instead of creating a child this just fertilizes the nesting entity.
     */
    @Override
    protected void spawnBaby() {
        ServerPlayerEntity player = mNestingEntity.getLoveCause();
        if (player == null) {
            player = field_75391_e.getLoveCause();
        }

        if (player != null) {
            player.addStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(player, animal, field_75391_e, null);

            Random random = animal.getRNG();
            if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                Vec3d position = animal.getPositionVec();
                world.addEntity(new ExperienceOrbEntity(world, position.x, position.y, position.z, random.nextInt(7) + 1));
            }
        }

        ModNestingEntity female = mNestingEntity.getIsMale() ? (ModNestingEntity)field_75391_e : mNestingEntity;
        female.setIsFertilized(true);

        mNestingEntity.resetInLove();
        field_75391_e.resetInLove();
    }
}
