package com.bokmcdok.wheat.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ModCallLightningSpell implements IModSpell {

    /**
     * Summon lightning at the target block or entity.
     * @param world The current world.
     * @param entity The entity casting the spell.
     * @return TRUE if the spell was successfully cast.
     */
    @Override
    public boolean cast(World world, Entity entity) {
        if (!entity.isInWater() && !entity.isInLava()) {
            RayTraceResult rayTraceResult = entity.pick(20.0d, 1.0f, true);
            if (rayTraceResult.getType() != RayTraceResult.Type.MISS) {
                BlockPos position = rayTraceResult.getType() == RayTraceResult.Type.BLOCK ?
                        ((BlockRayTraceResult) rayTraceResult).getPos() :
                        ((EntityRayTraceResult) rayTraceResult).getEntity().getPosition();
                if (world instanceof ServerWorld) {
                    LightningBoltEntity lightningboltentity = new LightningBoltEntity(world, (double) position.getX() + 0.5D, position.getY(), (double) position.getZ() + 0.5D, false);
                    lightningboltentity.setCaster(entity instanceof ServerPlayerEntity ? (ServerPlayerEntity) entity : null);
                    ((ServerWorld) world).addLightningBolt(lightningboltentity);
                }

                world.playSound(null, position.getX(), position.getY(), position.getZ(), SoundEvents.ITEM_TRIDENT_THUNDER, SoundCategory.PLAYERS, 5.0f, 1.0F);
                return true;
            }
        }

        return false;
    }

    /**
     * Get the range of the spell.
     * @return The range of the spell.
     */
    @Override
    public int getRange() {
        return 40;
    }
}
