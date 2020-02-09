package com.bokmcdok.wheat.ai.goals;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class ModTransformEntityGoal extends Goal {
    private final EntityPredicate mConditions;

    private LivingEntity mTarget = null;
    private final World mWorld;
    private final LivingEntity mOwner;
    private final Class<? extends LivingEntity> mFrom;
    private final EntityType<? extends LivingEntity> mTo;

    /**
     * Construction
     * @param owner The owner of this goal.
     * @param from The entity to convert from.
     * @param to The entity to convert to.
     */
    public ModTransformEntityGoal(LivingEntity owner,
                                  Class<? extends LivingEntity> from,
                                  EntityType<? extends LivingEntity> to,
                                  Predicate<LivingEntity> customPredicate) {
        mWorld = owner.world;
        mOwner = owner;
        mFrom = from;
        mTo = to;

        mConditions = new EntityPredicate().setDistance(8.0d).allowInvulnerable().allowFriendlyFire().setLineOfSiteRequired();
        if (customPredicate != null) {
            mConditions.setCustomPredicate(customPredicate);
        }
    }

    /**
     * Whether or not we should execute the task.
     * @return TRUE if the task should execute.
     */
    @Override
    public boolean shouldExecute() {
        mTarget = findTarget();
        return mTarget != null;
    }

    /**
     * Stop execting if the target dies.
     * @return FALSE if the target is dead.
     */
    @Override
    public boolean shouldContinueExecuting() {
        return super.shouldContinueExecuting() && mTarget.isAlive();
    }

    /**
     * Reset the target to null.
     */
    @Override
    public void resetTask() {
        super.resetTask();
        mTarget = null;
    }

    /**
     * Convert the target to another target.
     */
    @Override
    public void tick() {
        super.tick();

        LivingEntity converted = mTo.create(mWorld);
        converted.copyLocationAndAnglesFrom(mTarget);
        mTarget.remove();
        if (mTarget.hasCustomName()) {
            converted.setCustomName(mTarget.getCustomName());
            converted.setCustomNameVisible(mTarget.isCustomNameVisible());
        }

        converted.setInvulnerable(mOwner.isInvulnerable());
        mWorld.addEntity(converted);

        mTarget = null;
    }

    /**
     * Attempt to find a target to transform.
     * @return An entity to target, NULL if there are no nearby targets.
     */
    private LivingEntity findTarget() {
        List<LivingEntity> nearbyTargets = mWorld.getTargettableEntitiesWithinAABB(mFrom, mConditions, mOwner, mOwner.getBoundingBox().grow(8.0d));
        double minDistanceSquared = Double.MAX_VALUE;
        LivingEntity target = null;
        for (LivingEntity i : nearbyTargets) {
            if (mOwner.getDistanceSq(i) < minDistanceSquared) {
                target = i;
                minDistanceSquared = mOwner.getDistanceSq(i);
            }
        }

        return target;
    }
}
