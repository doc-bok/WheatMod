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
                    double d0 = 0.3d;
                    double d1 = 0.35d;
                    double d2 = 0.3d;
                    float f = player.renderYawOffset * ((float) Math.PI / 180F) + MathHelper.cos((float) player.ticksExisted * 0.6662F) * 0.25F;
                    float f2 = MathHelper.cos(f);
                    float f1 = MathHelper.sin(f);
                    world.addParticle(ParticleTypes.ENTITY_EFFECT, player.func_226277_ct_() + (double) f1 * 0.6D, player.func_226278_cu_() + 1.0D, player.func_226281_cx_() + (double) f2 * 0.6D, d0, d1, d2);
                    world.addParticle(ParticleTypes.ENTITY_EFFECT, player.func_226277_ct_() - (double) f1 * 0.6D, player.func_226278_cu_() + 1.0D, player.func_226281_cx_() - (double) f2 * 0.6D, d0, d1, d2);
                }
            }
        }
    }
}
