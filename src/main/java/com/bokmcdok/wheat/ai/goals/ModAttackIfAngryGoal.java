package com.bokmcdok.wheat.ai.goals;

import com.bokmcdok.wheat.ai.behaviour.IGetsAngry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ModAttackIfAngryGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private IGetsAngry mOwner;

    /**
     * Create
     * @param owner The owner of the goal.
     * @param targetClass The entity type to attack.
     * @param checkSight Whether or not to check line-of-sight before attacking.
     */
    public static <T extends LivingEntity> ModAttackIfAngryGoal<T> create(IGetsAngry owner, Class<T> targetClass, boolean checkSight) {
        return create(owner, targetClass, checkSight, false);
    }

    /**
     * Create
     * @param owner The owner of the goal.
     * @param targetClass The entity type to attack.
     * @param checkSight Whether or not to check line-of-sight before attacking.
     * @param nearbyOnly TRUE if the owner should only target nearby entities.
     */
    public static <T extends LivingEntity> ModAttackIfAngryGoal<T> create(IGetsAngry owner, Class<T> targetClass, boolean checkSight, boolean nearbyOnly) {
        return create(owner, targetClass, 10, checkSight, nearbyOnly, null);
    }

    /**
     * Create
     * @param owner The owner of the goal.
     * @param targetClass The entity type to attack.
     * @param targetChance The chance an entity is targeted.
     * @param checkSight Whether or not to check line-of-sight before attacking.
     * @param nearbyOnly TRUE if the owner should only target nearby entities.
     * @param predicate The condition for choosing a target entity.
     */
    public static <T extends LivingEntity> ModAttackIfAngryGoal<T> create(IGetsAngry owner, Class<T> targetClass, int targetChance, boolean checkSight, boolean nearbyOnly, @Nullable Predicate<LivingEntity> predicate) {
        if (!(owner instanceof MobEntity)) {
            throw new IllegalArgumentException("ModAttackIfAngryGoal requires MobEntity that implements IGetsAngry");
        } else {
            return new ModAttackIfAngryGoal<>(owner, targetClass, targetChance, checkSight, nearbyOnly, predicate);
        }
    }

    /**
     * Only execute if the mob is angry.
     * @return TRUE if the goal should execute.
     */
    @Override
    public boolean shouldExecute() {
        return mOwner.isAngry() && super.shouldExecute();
    }

    /**
     * Construction
     * @param owner The owner of the goal.
     * @param targetClass The entity type to attack.
     * @param targetChance The chance an entity is targeted.
     * @param checkSight Whether or not to check line-of-sight before attacking.
     * @param nearbyOnly TRUE if the owner should only target nearby entities.
     * @param predicate The condition for choosing a target entity.
     */
    private ModAttackIfAngryGoal(IGetsAngry owner, Class<T> targetClass, int targetChance, boolean checkSight, boolean nearbyOnly, @Nullable Predicate<LivingEntity> predicate) {
        super((MobEntity) owner, targetClass, targetChance, checkSight, nearbyOnly, predicate);
        mOwner = owner;
    }
}
