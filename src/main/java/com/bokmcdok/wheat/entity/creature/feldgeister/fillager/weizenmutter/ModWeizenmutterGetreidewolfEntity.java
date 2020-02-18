package com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.ai.goals.ModCastSpellOnSelfGoal;
import com.bokmcdok.wheat.entity.creature.feldgeister.getreidewolf.ModGetreidewolfEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ModWeizenmutterGetreidewolfEntity extends ModGetreidewolfEntity {
    /**
     * Construction
     *
     * @param type  The type of this entity.
     * @param world The current world.
     */
    public ModWeizenmutterGetreidewolfEntity(EntityType<? extends ModGetreidewolfEntity> type, World world) {
        super(type, world);
    }

    /**
     * Register the behaviours of the weizenmutter.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new ModCastSpellOnSelfGoal(this, WheatMod.SPELL_REGISTRAR.getSpell("true_polymorph_weizenmutter"), (caster, target) -> {
            LivingEntity attackTarget = caster.getAttackTarget();
            if (attackTarget != null) {
                return attackTarget.getHealth() / attackTarget.getMaxHealth() >= 0.5f;
            }

            return false;
        }));

        //targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, false, false, null));
    }

    /**
     * Weizenmutters are similar to witches.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(26.0D);
    }
}
