package com.bokmcdok.wheat.ai.goals;

import com.bokmcdok.wheat.spell.ModSpell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

import java.util.function.BiFunction;

public class ModCastSpellOnSelfGoal extends ModCastSpellGoal {

    /**
     * Construction
     * @param caster The entity that will cast the spell.
     * @param spell The spell to cast.
     */
    public ModCastSpellOnSelfGoal(MobEntity caster, ModSpell spell) {
        this(caster, spell, null);
    }

    /**
     * Construction
     * @param caster The entity that will cast the spell.
     * @param spell The spell to cast.
     * @param predicate The conditions under which to cast the spell.
     */
    public ModCastSpellOnSelfGoal(MobEntity caster, ModSpell spell, BiFunction<MobEntity, LivingEntity, Boolean> predicate) {
        super(caster, spell, 1.0d, predicate);
    }

    /**
     * Get a living entity target.
     * @return The entity being targeted, if any.
     */
    @Override
    protected LivingEntity getTarget() {
        return mCaster;
    }
}
