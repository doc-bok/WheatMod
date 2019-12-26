package com.bokmcdok.wheat.ai.goals;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

public abstract class ModDespawnGoal extends Goal {
    protected final MobEntity mOwner;

    /**
     * Construction
     * @param owner The owner of this goal.
     */
    public ModDespawnGoal(MobEntity owner) {
        mOwner = owner;
    }

    /**
     * Despawn the entity when this goal is executing.
     */
    @Override
    public void tick() {
        mOwner.remove();
    }
}
