package com.bokmcdok.wheat.ai.goals;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

/**
 * A temporary goal that gets removed after a number of ticks.
 */
public class ModTemporaryGoal extends Goal {
    private final MobEntity mOwner;
    private final Goal mGoal;
    private final long mEndTick;

    /**
     * Construction
     * @param owner The owner of this task.
     * @param goal The actual goal that is temporary.
     * @param duration The duration of the temporary goal.
     */
    public ModTemporaryGoal(MobEntity owner, Goal goal, long duration) {
        mOwner = owner;
        mGoal = goal;
        mEndTick = owner.world.getGameTime() + duration;

        setMutexFlags(goal.getMutexFlags());
    }

    /**
     * Until the duration is up return whether or not the wrapped goal should execute.
     * @return TRUE if the goal should execute.
     */
    @Override
    public boolean shouldExecute() {
        if (mOwner.world.getGameTime() < mEndTick) {
            return mGoal.shouldExecute();
        } else {
            removeGoal();
            return false;
        }
    }

    /**
     * Until the duration is up return whether or not the wrapped goal should execute.
     * @return TRUE if the goal should execute.
     */
    @Override
    public boolean shouldContinueExecuting() {
        if (mOwner.world.getGameTime() < mEndTick) {
            return mGoal.shouldContinueExecuting();
        } else {
            removeGoal();
            return false;
        }
    }

    /**
     * Can the goal be preempted by higher priority goals?
     * @return TRUE if the wrapped goal is preemptible.
     */
    @Override
    public boolean isPreemptible() {
        return mGoal.isPreemptible();
    }

    /**
     * Execute a one shot task or start executing a continuous task.
     */
    @Override
    public void startExecuting() {
        mGoal.startExecuting();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {
        mGoal.resetTask();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        mGoal.tick();
    }

    /**
     * Return a name containing both goal classes.
     * @return The name of the goal
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "->" + mGoal.toString();
    }

    /**
     * Remove this goal from the owner.
     */
    private void removeGoal() {
        mGoal.resetTask();
        mOwner.goalSelector.removeGoal(this);
    }
}
