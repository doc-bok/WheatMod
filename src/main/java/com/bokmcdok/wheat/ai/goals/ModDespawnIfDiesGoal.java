package com.bokmcdok.wheat.ai.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

public class ModDespawnIfDiesGoal extends ModDespawnGoal {
    private final LivingEntity mTarget;

    /**
     * Construction
     * @param owner The owner of this goal.
     */
    public ModDespawnIfDiesGoal(MobEntity owner, LivingEntity target) {
        super(owner);
        mTarget = target;
    }

    /**
     * Despawn if the target dies.
     * @return TRUE if the entity should despawn.
     */
    @Override
    public boolean shouldExecute() {
        return !mTarget.isAlive();
    }
}
