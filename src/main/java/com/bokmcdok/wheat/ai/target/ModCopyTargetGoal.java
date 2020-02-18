package com.bokmcdok.wheat.ai.target;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

public class ModCopyTargetGoal extends TargetGoal {
    private final MobEntity mToCopy;

    public ModCopyTargetGoal(MobEntity owner, MobEntity toCopy) {
        super(owner, false);
        mToCopy = toCopy;
        setMutexFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public void tick() {
        goalOwner.setAttackTarget(mToCopy.getAttackTarget());
    }

    @Override
    public boolean shouldExecute() {
        return true;
    }
}
