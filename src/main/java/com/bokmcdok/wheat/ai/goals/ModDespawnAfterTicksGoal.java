package com.bokmcdok.wheat.ai.goals;

import net.minecraft.entity.MobEntity;

public class ModDespawnAfterTicksGoal extends ModDespawnGoal {
    private final int mTicksToExist;

    /**
     * Construction
     * @param owner The owner of this goal.
     */
    public ModDespawnAfterTicksGoal(MobEntity owner, int ticksToExist) {
        super(owner);
        mTicksToExist = ticksToExist;
    }

    /**
     * Despawn after a certain number of ticks have passed.
     * @return TRUE if the entity should despawn.
     */
    @Override
    public boolean shouldExecute() {
        return mOwner.ticksExisted > mTicksToExist;
    }
}
