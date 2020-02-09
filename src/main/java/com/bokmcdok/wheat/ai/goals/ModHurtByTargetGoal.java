package com.bokmcdok.wheat.ai.goals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;
import java.util.Objects;

public class ModHurtByTargetGoal extends HurtByTargetGoal {
    private final Class<? extends Entity>[] mAllies;

    /**
     * Construction
     * @param entity The entity that owns this goal.
     * @param allies The entity types to call for help from.
     */
    public ModHurtByTargetGoal(CreatureEntity entity, Class<? extends Entity>... allies) {
        super(entity);
        setCallsForHelp();
        mAllies = allies;
    }

    /**
     * Set the target to get revenge upon.
     * @param attacker The attacking entity.
     * @param target The target entity.
     */
    @Override
    protected void setAttackTarget(MobEntity attacker, LivingEntity target) {
        if (goalOwner.canEntityBeSeen(target)) {
            attacker.setRevengeTarget(target);
            super.setAttackTarget(attacker, target);
        }
    }

    /**
     * Leaner and cleaner implementation of code to call for help.
     */
    @Override
    protected void alertOthers() {
        double distance = getTargetDistance();
        AxisAlignedBB boundingBox = new AxisAlignedBB(
                goalOwner.func_226277_ct_(), goalOwner.func_226278_cu_(), goalOwner.func_226281_cx_(),
                goalOwner.func_226277_ct_() + 1.0d, goalOwner.func_226278_cu_() + 1.0d, goalOwner.func_226281_cx_() + 1.0d);
        boundingBox = boundingBox.grow(distance, 10.0d, distance);

        for (Class<? extends Entity> c : mAllies) {
            List<Entity> entities = goalOwner.world.func_225317_b(c, boundingBox);
            for (Entity entity : entities) {
                MobEntity mob = (MobEntity)entity;
                if (goalOwner != entity &&
                        mob.getAttackTarget() == null &&
                        (!(goalOwner instanceof TameableEntity) || ((TameableEntity) this.goalOwner).getOwner() == ((TameableEntity) entity).getOwner()) &&
                        !entity.isOnSameTeam(Objects.requireNonNull(goalOwner.getRevengeTarget()))) {
                    setAttackTarget(mob, goalOwner.getRevengeTarget());
                }
            }
        }
    }
}
