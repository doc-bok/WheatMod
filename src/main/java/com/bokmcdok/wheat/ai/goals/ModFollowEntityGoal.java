package com.bokmcdok.wheat.ai.goals;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;

import java.util.EnumSet;

/**
 * A goal for following any entity.
 */
public class ModFollowEntityGoal extends Goal {
    private final MobEntity mOwner;
    private final PathNavigator mNavigator;
    private final LookController mLookController;
    private final Entity mTarget;
    private final double mSpeedModifier;
    private final float mStopDistance;
    private float mOldWaterCost;
    private int mTimeToRecalcPath;

    /**
     * Construction
     * @param owner The owner of the goal.
     * @param target The entity to follow.
     * @param speedModifier The modifier to apply to the owner's speed.
     * @param stopDistance The distance to stop from the target.
     */
    public ModFollowEntityGoal(MobEntity owner, Entity target, double speedModifier, float stopDistance) {
        mOwner = owner;
        mTarget = target;
        mNavigator = mOwner.getNavigator();
        mLookController = owner.getLookController();
        mSpeedModifier = speedModifier;
        mStopDistance = stopDistance;

        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * This task should always execute.
     * @return Always TRUE.
     */
    @Override
    public boolean shouldExecute() {
        return true;
    }

    /**
     * Continue executing as long as target entity is too far away.
     * @return TRUE if too far away from the target.
     */
    @Override
    public boolean shouldContinueExecuting() {
        return !mNavigator.noPath() && mTarget.getDistanceSq(mOwner) > (mStopDistance * mStopDistance);
    }

    /**
     * Initialise the pathfinding for this task.
     */
    @Override
    public void startExecuting() {
        mTimeToRecalcPath = 0;
        mOldWaterCost = mOwner.getPathPriority(PathNodeType.WATER);
        mOwner.setPathPriority(PathNodeType.WATER, 0f);
    }

    /**
     * Reset this task to it's default state.
     */
    @Override
    public void resetTask() {
        mNavigator.clearPath();
        mOwner.setPathPriority(PathNodeType.WATER, mOldWaterCost);
    }

    /**
     * Try and navigate towards the target.
     */
    @Override
    public void tick() {
        if (!mOwner.getLeashed()) {
            mLookController.setLookPositionWithEntity(mTarget, 10f, mOwner.getVerticalFaceSpeed());
            if (--mTimeToRecalcPath <= 0) {
                mTimeToRecalcPath = 10;
                double x = mOwner.func_226277_ct_() - mTarget.func_226277_ct_();
                double y = mOwner.func_226278_cu_() - mTarget.func_226278_cu_();
                double z = mOwner.func_226281_cx_() - mTarget.func_226281_cx_();
                double s = (x * x) + (y * y) + (z * z);

                if (!(s < mStopDistance * mStopDistance)) {
                    mNavigator.tryMoveToEntityLiving(mTarget, mSpeedModifier);
                } else {
                    mNavigator.clearPath();
                    if (s <= mStopDistance ||
                        mLookController.getLookPosX() == mOwner.func_226277_ct_() ||
                        mLookController.getLookPosY() == mOwner.func_226278_cu_() ||
                        mLookController.getLookPosZ() == mOwner.func_226281_cx_()) {

                        double ix = mTarget.func_226277_ct_() - mOwner.func_226277_ct_();
                        double iz = mTarget.func_226281_cx_() - mOwner.func_226281_cx_();

                        mNavigator.tryMoveToXYZ(mOwner.func_226277_ct_() - ix, mOwner.func_226277_ct_(),
                                                mOwner.func_226281_cx_() - ix, mSpeedModifier);
                    }
                }
            }
        }
    }
}
