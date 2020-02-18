package com.bokmcdok.wheat.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public abstract class ModSpell {

    /**
     * Handle casting the spell.
     * @param caster The entity casting the spell.
     * @return TRUE if the spell was successfully cast.
     */
    public boolean cast(LivingEntity caster) {
        return false;
    }

    /**
     * Handle casting the spell with an entity as a target.
     * @param caster The caster of the spell.
     * @param target The target entity.
     * @return TRUE if the spell was successfully cast.
     */
    public boolean cast(LivingEntity caster, Entity target) {
        return cast(caster);
    }

    /**
     * Check if the spell has a vocal component.
     * @return TRUE if the spell has a vocal component.
     */
    public boolean getHasVocalComponent() {
        return true;
    }

    /**
     * Check if the spell has a somatic component.
     * @return TRUE if the spell has a somatic component.
     */
    public boolean getHasSomaticComponent() {
        return true;
    }

    /**
     * Get the material component of the spell, if any.
     * @return The material component.
     */
    public Ingredient getMaterialComponent() {
        return Ingredient.EMPTY;
    }

    /**
     * Get the focus of the spell, if any.
     * @return The focus item.
     */
    public Ingredient getFocus() {
        return Ingredient.EMPTY;
    }

    /**
     * Get the casting time of the spell (defaults to 6 seconds).
     * @return The casting time.
     */
    public int getCastingTime() {
        return 60;
    }

    /**
     * Get the duration of the spell.
     * @return The duration of the spell.
     */
    public int getDuration() {
        return 0;
    }

    /**
     * Get the time until the spell can be cast again after the last attempt.
     * @return The spell's cooldown.
     */
    public int getCooldown() {
        return 40;
    }

    /**
     * Get the range of the spell.
     * @return The range of the spell.
     */
    public double getRange() {
        return 0;
    }

    /**
     * Convenience function for range checks.
     * @return
     */
    public double getRangeSquared() {
        return getRange() * getRange();
    }

    /**
     * Get the sound made when an entity starts casting the spell.
     * @return A sound event.
     */
    public SoundEvent getPrepareSound() {
        return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
    }

    /**
     * Get the sound made when the spell is successfully cast.
     * @return A sound event.
     */
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
    }

    /**
     * Get the sound made when a spell fails to cast.
     * @return A sound event.
     */
    public SoundEvent getFailSound() {
        return SoundEvents.ENTITY_BLAZE_SHOOT;
    }

    /**
     * Get the level of the spell (affects stamina cost to use).s
     * @return The level of the spell.
     */
    public abstract int getLevel();

    /**
     * Helper function to do raytracing for spells.
     * @param world The current world.
     * @param range The range of the spell.
     * @param checkEntityCollision Can the spell target entities?
     * @param shooter The shooter.
     * @param blockMode The mode to use to raytrace blocks.
     * @param filter Filter for the kind of entities that can be targeted.
     * @return The result of the raytrace.
     */
    protected RayTraceResult rayTrace(World world, double range, boolean checkEntityCollision, @Nullable Entity shooter, RayTraceContext.BlockMode blockMode, Predicate<Entity> filter) {
        Vec3d eyePosition = shooter.getEyePosition(1.0F);
        Vec3d lookDirection = shooter.getLook(1.0F).scale(range);
        Vec3d lookTarget = eyePosition.add(lookDirection);
        RayTraceResult rayTraceResult = world.rayTraceBlocks(new RayTraceContext(eyePosition, lookTarget, blockMode, RayTraceContext.FluidMode.NONE, shooter));
        if (checkEntityCollision) {
            if (rayTraceResult.getType() != RayTraceResult.Type.MISS) {
                lookTarget = rayTraceResult.getHitVec();
            }

            AxisAlignedBB boundingBox = new AxisAlignedBB(shooter.getPosition()).grow(range);
            RayTraceResult entityRayTraceResult = ProjectileHelper.rayTraceEntities(world, shooter, eyePosition, lookTarget, boundingBox, filter);
            if (entityRayTraceResult != null) {
                rayTraceResult = entityRayTraceResult;
            }
        }

        return rayTraceResult;
    }
}
