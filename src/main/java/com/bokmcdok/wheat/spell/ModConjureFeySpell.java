package com.bokmcdok.wheat.spell;

import com.bokmcdok.wheat.ai.goals.ModDespawnAfterTicksGoal;
import com.bokmcdok.wheat.ai.goals.ModDespawnIfDiesGoal;
import com.bokmcdok.wheat.ai.target.ModCopyTargetGoal;
import com.bokmcdok.wheat.supplier.ModEntityTypeSupplier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.LazyValue;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class ModConjureFeySpell extends ModSpell {
    private final LazyValue<EntityType<?>> mConjure;

    /**
     * Construction
     * @param conjure The registry name of the entity to conjure.
     */
    public ModConjureFeySpell(String conjure) {
        Supplier supplier = new ModEntityTypeSupplier(conjure);
        mConjure = new LazyValue<>(supplier);
    }

    /**
     * Convert the target to polymorph entity.
     * @param caster The caster of the spell.
     * @return TRUE if the spell was successfully cast.
     */
    @Override
    public boolean cast(LivingEntity caster) {
        World world = caster.world;
        RayTraceResult rayTraceResult = rayTrace(world, getRange(), false, caster, RayTraceContext.BlockMode.OUTLINE, null);
        if (rayTraceResult.getType() != RayTraceResult.Type.MISS) {

            Vec3d hitVec = rayTraceResult.getHitVec();
            if (caster.getDistanceSq(hitVec) <= getRangeSquared()) {

                LivingEntity conjured = (LivingEntity)mConjure.getValue().create(world);
                conjured.setPosition(hitVec.x, hitVec.y, hitVec.z);

                if (conjured instanceof MobEntity) {
                    MobEntity conjuredMob = (MobEntity)conjured;
                    conjuredMob.onInitialSpawn(world, world.getDifficultyForLocation(conjured.getPosition()), SpawnReason.CONVERSION, null, null);

                    conjuredMob.goalSelector.addGoal(1, new ModDespawnAfterTicksGoal(conjuredMob, getDuration()));
                    conjuredMob.goalSelector.addGoal(1, new ModDespawnIfDiesGoal(conjuredMob, caster));

                    if (caster instanceof MobEntity) {
                        MobEntity casterMob = (MobEntity)caster;
                        conjuredMob.setAttackTarget(casterMob.getAttackTarget());
                        conjuredMob.targetSelector.addGoal(1, new ModCopyTargetGoal(conjuredMob, casterMob));
                    }
                }

                world.addEntity(conjured);
                return true;
            }
        }

        return false;
    }


    @Override
    public int getCastingTime() {
        return 60;
    }

    @Override
    public int getCooldown() {
        return 100;
    }

    @Override
    public int getLevel() {
        return 6;
    }

    @Override
    public double getRange() {
        return 30;
    }

    @Override
    public int getDuration() {
        return 1200;
    }
}
