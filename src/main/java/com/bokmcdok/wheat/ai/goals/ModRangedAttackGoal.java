package com.bokmcdok.wheat.ai.goals;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class ModRangedAttackGoal extends Goal {
    private final MobEntity mOwner;
    private final IRangedAttackMob mRangedAttackEntity;
    private final float mRange;
    private final float mRangeSquared;
    private final long mAttackInterval;

    private LivingEntity mTarget;
    private double mOwnerMoveSpeed;
    private long mLastAttackTime;
    private int mSeeTime;

    /**
     * Construction
     * @param owner The owner of this goal.
     * @param ownerMoveSpeed The move speed of the owner.
     * @param attackInterval The interval between attacks.
     * @param range The range of the attack.
     */
    public ModRangedAttackGoal(IRangedAttackMob owner, double ownerMoveSpeed, long attackInterval, float range) {
        mLastAttackTime = -1;
        if (!(owner instanceof MobEntity)) {
            throw new IllegalArgumentException("ModRangedAttackGoal requires MobEntity that implements RangedAttackMob");
        } else {
            mRangedAttackEntity = owner;
            mOwner = (MobEntity)owner;
            mOwnerMoveSpeed = ownerMoveSpeed;
            mAttackInterval = attackInterval;
            mRange = range;
            mRangeSquared = range * range;
            setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }
    }

    /**
     * Return true only after the attack delay.
     * @return Parent's result if the last attack happened enough ticks ago.
     */
    @Override
    public boolean shouldExecute() {
        if (mOwner.world.getGameTime() > mLastAttackTime + mAttackInterval) {
            LivingEntity target = mOwner.getAttackTarget();
            if (target != null && target.isAlive()) {
                mTarget = target;
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * Return true only after the attack delay.
     * @return Parent's result if the last attack happened enough ticks ago.
     */
    @Override
    public boolean shouldContinueExecuting() {
        return mOwner.world.getGameTime() > mLastAttackTime + mAttackInterval && (shouldExecute() || !mOwner.getNavigator().noPath());
    }

    /**
     * Resets the task.
     */
    @Override
    public void resetTask() {
        mTarget = null;
        mSeeTime = 0;
        //mRangedAttackTime = -1;
    }

    /**
     * Perform the ranged attack.
     */
    @Override
    public void tick() {
        Vec3d position = mTarget.getPositionVec();
        double distanceSquared = mOwner.getDistanceSq(position.x, mTarget.getBoundingBox().minY, position.z);
        boolean canSee = mOwner.getEntitySenses().canSee(mTarget);
        if (canSee) {
            ++mSeeTime;
        } else {
            mSeeTime = 0;
        }

        if (distanceSquared <= (double) mRangeSquared && mSeeTime >= 5) {
            mOwner.getNavigator().clearPath();
        } else {
            mOwner.getNavigator().tryMoveToEntityLiving(mTarget, mOwnerMoveSpeed);
        }

        mOwner.getLookController().setLookPositionWithEntity(mTarget, 30.0F, 30.0F);
        if (canSee) {
            float distanceRatio = MathHelper.sqrt(distanceSquared) / mRange;
            float clampedDistanceRatio = MathHelper.clamp(distanceRatio, 0.1F, 1.0F);
            mRangedAttackEntity.attackEntityWithRangedAttack(mTarget, clampedDistanceRatio);
            mLastAttackTime = mOwner.world.getGameTime();
        }
    }
}
