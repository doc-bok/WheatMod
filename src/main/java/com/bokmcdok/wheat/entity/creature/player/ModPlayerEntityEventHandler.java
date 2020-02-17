package com.bokmcdok.wheat.entity.creature.player;

import com.bokmcdok.wheat.item.ModItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ModPlayerEntityEventHandler {

    /**
     * Fired when a living entity does an update.
     * @param event The event data.
     */
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.world;
        if (world.isRemote() && entity instanceof PlayerEntity) {

            //  Handle particle effects when player casts a spell.
            PlayerEntity player = (PlayerEntity) entity;
            if (player.isHandActive()) {
                ItemStack heldItem = player.getHeldItem(player.getActiveHand());
                Item item = heldItem.getItem();
                if (item instanceof ModItem && ((ModItem) item).isSpell()) {
                    double dX = 0.3d;
                    double dY = 0.7d;
                    double dZ = 0.3d;
                    float yaw = player.renderYawOffset * ((float) Math.PI / 180F) + MathHelper.cos((float) player.ticksExisted * 0.6662F) * 0.25F;
                    float cos = MathHelper.cos(yaw);
                    float sin = MathHelper.sin(yaw);

                    double playerX = player.func_226277_ct_();
                    double playerY = player.func_226278_cu_();
                    double playerZ = player.func_226281_cx_();

                    world.addParticle(ParticleTypes.HAPPY_VILLAGER, playerX + (double) sin * 0.6D, playerY + 1.4D, playerZ + (double) cos * 0.6D, dX, dY, dZ);
                    world.addParticle(ParticleTypes.HAPPY_VILLAGER, playerX - (double) sin * 0.6D, playerY + 1.4D, playerZ - (double) cos * 0.6D, dX, dY, dZ);
                    world.addParticle(ParticleTypes.HAPPY_VILLAGER, playerX + (double) sin * 0.6D, playerY + 1.4D, playerZ - (double) cos * 0.6D, dX, dY, dZ);
                    world.addParticle(ParticleTypes.HAPPY_VILLAGER, playerX - (double) sin * 0.6D, playerY + 1.4D, playerZ + (double) cos * 0.6D, dX, dY, dZ);
                }
            }
        }
    }
}
