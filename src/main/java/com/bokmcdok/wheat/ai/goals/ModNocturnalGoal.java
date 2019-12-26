package com.bokmcdok.wheat.ai.goals;

import net.minecraft.entity.MobEntity;

public class ModNocturnalGoal extends ModDespawnGoal {

    /**
     * Construction
     * @param owner The owner of this goal.
     */
    public ModNocturnalGoal(MobEntity owner) {
        super(owner);
    }

    /**
     * Execute this goal in the daytime.
     * @return TRUE if it's daytime.
     */
    @Override
    public boolean shouldExecute() {
        return mOwner.world.isDaytime();
    }
}
