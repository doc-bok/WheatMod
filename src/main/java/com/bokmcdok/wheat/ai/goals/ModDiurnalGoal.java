package com.bokmcdok.wheat.ai.goals;

import net.minecraft.entity.MobEntity;

public class ModDiurnalGoal extends ModDespawnGoal {

    /**
     * Construction
     *
     * @param owner The owner of this goal.
     */
    public ModDiurnalGoal(MobEntity owner) {
        super(owner);
    }

    /**
     * Execute this goal in the night time.
     *
     * @return TRUE if it's night time.
     */
    @Override
    public boolean shouldExecute() {
        return !mOwner.world.isDaytime();
    }
}