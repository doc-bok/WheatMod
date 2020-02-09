package com.bokmcdok.wheat.ai.goals;

import com.bokmcdok.wheat.ai.behaviour.ISpellcaster;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class ModTransformEntityGoal extends Goal {
    private final EntityPredicate mConditions;

    private MobEntity mTarget = null;
    private final World mWorld;
    private final MobEntity mOwner;
    private final Class<? extends MobEntity> mFrom;
    private final EntityType<? extends MobEntity> mTo;
    private final double mSpeed;
    private int mDelay = 0;

    /**
     * Construction
     * @param owner The owner of this goal.
     * @param from The entity to convert from.
     * @param to The entity to convert to.
     */
    public ModTransformEntityGoal(MobEntity owner, double speed, Class<? extends MobEntity> from, EntityType<? extends MobEntity> to) {
        this(owner, speed, from, to, null);
    }

    /**
     * Construction
     * @param owner The owner of this goal.
     * @param from The entity to convert from.
     * @param to The entity to convert to.
     * @param customPredicate Any custom conditions for selecting a target.
     */
    public ModTransformEntityGoal(MobEntity owner,
                                  double speed,
                                  Class<? extends MobEntity> from,
                                  EntityType<? extends MobEntity> to,
                                  Predicate<LivingEntity> customPredicate) {
        mWorld = owner.world;
        mOwner = owner;
        mSpeed = speed;
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
        return super.shouldContinueExecuting() && mDelay < 60 && mTarget.isAlive();
    }

    /**
     * Reset the target to null.
     */
    @Override
    public void resetTask() {
        super.resetTask();
        mTarget = null;
        mDelay = 0;
    }

    /**
     * Convert the target to another target.
     */
    @Override
    public void tick() {
        super.tick();

        mOwner.getLookController().setLookPositionWithEntity(mTarget, 10, mOwner.getVerticalFaceSpeed());
        mOwner.getNavigator().tryMoveToEntityLiving(mTarget, mSpeed);

        ++mDelay;
        if (mDelay >= 60 && mOwner.getDistanceSq(mTarget) < 9.0d) {
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
            mDelay = 0;

            if (mOwner instanceof ISpellcaster) {
                ((ISpellcaster)mOwner).setCastingSpell(false);
            }
        }
    }

    /**
     * Tell spellcasters to start casting a spell.
     */
    @Override
    public void startExecuting() {
        super.startExecuting();

        if (mOwner instanceof ISpellcaster) {
            ((ISpellcaster)mOwner).setCastingSpell(true);
        }
    }

    /**
     * Attempt to find a target to transform.
     * @return An entity to target, NULL if there are no nearby targets.
     */
    private MobEntity findTarget() {
        List<MobEntity> nearbyTargets = mWorld.getTargettableEntitiesWithinAABB(mFrom, mConditions, mOwner, mOwner.getBoundingBox().grow(8.0d));
        double minDistanceSquared = Double.MAX_VALUE;
        MobEntity target = null;
        for (MobEntity i : nearbyTargets) {
            if (mOwner.getDistanceSq(i) < minDistanceSquared) {
                target = i;
                minDistanceSquared = mOwner.getDistanceSq(i);
            }
        }

        return target;
    }
}
