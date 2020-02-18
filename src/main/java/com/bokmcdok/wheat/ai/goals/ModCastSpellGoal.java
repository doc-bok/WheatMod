package com.bokmcdok.wheat.ai.goals;

import com.bokmcdok.wheat.ai.behaviour.ISpellcaster;
import com.bokmcdok.wheat.spell.ModSpell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundCategory;

public class ModCastSpellGoal extends Goal {
    private final MobEntity mCaster;
    private final ModSpell mSpell;
    private final double mSpeed;
    private int mCastingTime = 0;

    /**
     * Construction
     * @param caster The entity that will cast the spell.
     * @param spell The spell to cast.
     */
    public ModCastSpellGoal(MobEntity caster, ModSpell spell, double speed) {
        mCaster = caster;
        mSpell = spell;
        mSpeed = speed;
    }

    /**
     * Execute if we have a target.
     * @return TRUE if the goal should execute.
     */
    @Override
    public boolean shouldExecute() {
        return mCaster.getAttackTarget() != null;
    }

    /**
     * Should we continue executing.
     * @return TRUE if the goal should continue executing.
     */
    @Override
    public boolean shouldContinueExecuting() {
        return shouldExecute();
    }

    /**
     * Start casting the spell.
     */
    @Override
    public void startExecuting() {
        super.startExecuting();
    }

    /**
     * Reset the task if it is cancelled for any reason.
     */
    @Override
    public void resetTask() {
        super.resetTask();

        if (mCaster instanceof ISpellcaster) {
            ((ISpellcaster) mCaster).setCastingSpell(false);
        }

        mCastingTime = 0;
    }

    /**
     * Move in range of the target and cast the spell.
     */
    @Override
    public void tick() {
        LivingEntity attackTarget = mCaster.getAttackTarget();
        mCaster.getLookController().setLookPositionWithEntity(attackTarget, 10, mCaster.getVerticalFaceSpeed());

        if (mCaster.getDistanceSq(attackTarget) > mSpell.getRangeSquared()) {
            mCaster.getNavigator().tryMoveToEntityLiving(attackTarget, mSpeed);

            if (mCastingTime != 0) {
                mCaster.world.playSound(null, mCaster.getPosition(), mSpell.getFailSound(), SoundCategory.HOSTILE, 5.0f, 1.0F);
                resetTask();
            }
        } else {
            if (mCastingTime == 0) {
                if (mCaster instanceof ISpellcaster) {
                    ((ISpellcaster)mCaster).setCastingSpell(true);
                }

                mCaster.world.playSound(null, mCaster.getPosition(), mSpell.getPrepareSound(), SoundCategory.HOSTILE, 5.0f, 1.0F);
            }

            ++mCastingTime;
            if (mCastingTime >= mSpell.getCastingTime()) {
                if (mSpell.cast(mCaster, attackTarget)) {
                    mCaster.world.playSound(null, mCaster.getPosition(), mSpell.getCastSound(), SoundCategory.HOSTILE, 5.0f, 1.0F);
                } else {
                    mCaster.world.playSound(null, mCaster.getPosition(), mSpell.getFailSound(), SoundCategory.HOSTILE, 5.0f, 1.0F);
                }

                resetTask();
            }
        }
    }
}
