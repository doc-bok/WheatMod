package com.bokmcdok.wheat.ai.goals;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.GameRules;

public class ModBreedGoal extends BreedGoal {

    /**
     * Construction
     * @param animal The animal to breed.
     * @param speed The speed at which to breed.
     */
    public ModBreedGoal(AnimalEntity animal, double speed) {
        this(animal, speed, animal.getClass());
    }

    /**
     * Construction
     * @param animal The animal to breed.
     * @param speed The speed at which to breed.
     * @param animalClass The class of the animal.
     */
    public ModBreedGoal(AnimalEntity animal, double speed, Class<? extends AnimalEntity> animalClass) {
        super(animal, speed, animalClass);
    }

    /**
     * Spawn a baby. This override prevents XP spawning if a player isn't responsible for breeding.
     */
    @Override
    protected void spawnBaby() { 
        AgeableEntity child = animal.createChild(field_75391_e);
        final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(animal, field_75391_e, child);
        final boolean cancelled = net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        child = event.getChild();
        if (cancelled) {
            //Reset the "inLove" state for the animals
            animal.setGrowingAge(6000);
            field_75391_e.setGrowingAge(6000);
            animal.resetInLove();
            field_75391_e.resetInLove();
            return;
        }
        if (child != null) {
            ServerPlayerEntity player = animal.getLoveCause();
            if (player == null && field_75391_e.getLoveCause() != null) {
                player = field_75391_e.getLoveCause();
            }

            if (player != null) {
                player.addStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(player, animal, field_75391_e, child);

                if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                    world.addEntity(new ExperienceOrbEntity(world, animal.posX, animal.posY, animal.posZ, animal.getRNG().nextInt(7) + 1));
                }
            }

            animal.setGrowingAge(6000);
            field_75391_e.setGrowingAge(6000);
            animal.resetInLove();
            field_75391_e.resetInLove();
            child.setGrowingAge(-24000);
            child.setLocationAndAngles(animal.posX, animal.posY, animal.posZ, 0.0F, 0.0F);
            world.addEntity(child);
            world.setEntityState(animal, (byte)18);
        }
    }
}
