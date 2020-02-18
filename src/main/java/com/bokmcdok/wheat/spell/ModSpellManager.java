package com.bokmcdok.wheat.spell;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class ModSpellManager {
    private final Map<String, IModSpell> mSpells;

    /**
     * Construction
     */
    public ModSpellManager() {
        mSpells = Maps.newHashMap();
        mSpells.put("call_lightning", new ModCallLightningSpell());
    }

    /**
     * Get a spell by a string name.
     * @param id The ID of the spell.
     * @return An instance of the spell.
     */
    public IModSpell getSpell(String id) {
        if (mSpells.containsKey(id)) {
            return mSpells.get(id);
        } else {
            return null;
        }
    }

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
    public static RayTraceResult rayTrace(World world, double range, boolean checkEntityCollision, @Nullable Entity shooter, RayTraceContext.BlockMode blockMode, Predicate<Entity> filter) {
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
