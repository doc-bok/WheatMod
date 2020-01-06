package com.bokmcdok.wheat.entity.animal.cow;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.entity.feldgeister.haferbock.ModHaferbockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCowUtils {

    /**
     * Prevent cows from giving milk if a Haferbock is nearby.
     */
    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent.EntityInteract event)
    {
        Entity target = event.getTarget();
        if (target.getType() == EntityType.COW ||
            target.getType() == EntityType.MOOSHROOM) {
            ItemStack heldItem = event.getPlayer().getHeldItem(event.getHand());
            if (heldItem.getItem() == Items.BUCKET) {

                //  Get the bounding box
                AxisAlignedBB boundingBox = target.getBoundingBox();
                boundingBox = boundingBox.grow(8.0d);

                //  Apply effects to all entities in AoE
                List<ModHaferbockEntity> entities = event.getWorld().getEntitiesWithinAABB(ModHaferbockEntity.class, boundingBox);
                if (!entities.isEmpty()) {
                    event.setCanceled(true);
                    event.setCancellationResult(ActionResultType.FAIL);
                }
            }
        }
    }
}
