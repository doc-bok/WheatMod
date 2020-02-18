package com.bokmcdok.wheat.ai.goals;

import com.bokmcdok.wheat.ai.behaviour.ISpellcaster;
import com.bokmcdok.wheat.spell.ModSpell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundCategory;

import java.util.EnumSet;
import java.util.function.BiFunction;

public abstract class ModCastSpellGoal extends Goal {
    protected final MobEntity mCaster;
    private final ModSpell mSpell;
    private final double mSpeed;
    private final BiFunction<MobEntity, LivingEntity, Boolean> mPredicate;
    private int mCastingTime = 0;

    /**
     * Execute if we have a target.
     * @return TRUE if the goal should execute.
     */
    @Override
    public boolean shouldExecute() {
        LivingEntity target = getTarget();
        if (target != null) {
            return mPredicate == null || mPredicate.apply(mCaster, target);
        }

        return false;
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
        LivingEntity target = getTarget();
        mCaster.getLookController().setLookPositionWithEntity(target, 10, mCaster.getVerticalFaceSpeed());

        if (mCaster != target && mCaster.getDistanceSq(target) > mSpell.getRangeSquared()) {
            mCaster.getNavigator().tryMoveToEntityLiving(target, mSpeed);

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
                if (mSpell.cast(mCaster, target)) {
                    mCaster.world.playSound(null, mCaster.getPosition(), mSpell.getCastSound(), SoundCategory.HOSTILE, 5.0f, 1.0F);
                } else {
                    mCaster.world.playSound(null, mCaster.getPosition(), mSpell.getFailSound(), SoundCategory.HOSTILE, 5.0f, 1.0F);
                }

                resetTask();
            }
        }
    }

    /**
     * Construction
     * @param caster The entity that will cast the spell.
     * @param spell The spell to cast.
     */
    protected ModCastSpellGoal(MobEntity caster, ModSpell spell, double speed) {
        this(caster, spell, speed, null);
    }

    /**
     * Construction
     * @param caster The entity that will cast the spell.
     * @param spell The spell to cast.
     * @param predicate The conditions under which to cast the spell.
     */
    protected ModCastSpellGoal(MobEntity caster, ModSpell spell, double speed,
                               BiFunction<MobEntity, LivingEntity, Boolean> predicate) {
        mCaster = caster;
        mSpell = spell;
        mSpeed = speed;
        mPredicate = predicate;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * Get a living entity target.
     * @return The entity being targeted, if any.
     */
    protected abstract LivingEntity getTarget();
}
