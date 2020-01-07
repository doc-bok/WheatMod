package com.bokmcdok.wheat.entity.creature.animal.cat;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.ai.goals.CatTemptGoal;
import com.bokmcdok.wheat.entity.creature.animal.AnimalUtils;
import com.bokmcdok.wheat.entity.creature.animal.butterfly.ModButterflyEntity;
import com.bokmcdok.wheat.entity.creature.animal.mouse.ModMouseEntity;
import com.bokmcdok.wheat.entity.creature.animal.widowbird.ModWidowbirdEntity;
import com.bokmcdok.wheat.item.ModItemUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.NonTamedTargetGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CatUtils {

    protected final static Random rand = new Random();

    /**
     * Adds a new goal to the cat so they are tempted by the new items.
     * @param event The event fired when an entity joins the world.
     */
    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent event)
    {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.CAT) {
            CatEntity cat = (CatEntity)entity;
            cat.goalSelector.addGoal(3, new CatTemptGoal(cat, 0.6D, ModItemUtils.FISH_ITEMS, true));
            cat.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(cat, ModMouseEntity.class, 10,false, false, null));
            cat.targetSelector.addGoal(1, new NonTamedTargetGoal<>(cat, ModButterflyEntity.class, false, null));
            cat.targetSelector.addGoal(1, new NonTamedTargetGoal<>(cat, ModWidowbirdEntity.class, false, null));

        }
    }

    /**
     * Handle feeding a cat with new items.
     * @param event The event fired when a player interacts with an entity.
     */
    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent.EntityInteract event) {
        Entity target = event.getTarget();
        if (target.getType() == EntityType.CAT) {
            PlayerEntity player = event.getPlayer();
            Hand hand = event.getHand();
            ItemStack stack = player.getHeldItem(hand);
            Item item = stack.getItem();
            CatEntity cat = (CatEntity) event.getTarget();
            if (cat.isTamed()) {
                if (cat.isOwner(player) && ModItemUtils.FISH_ITEMS.test(stack) &&
                    AnimalUtils.heal(cat, (float) item.getFood().getHealing())) {
                    AnimalUtils.consumeEvent(event, player, stack);
                    return;
                }
            } else if (ModItemUtils.FISH_ITEMS.test(stack)) {
                AnimalUtils.consumeEvent(event, player, stack);
                if (!cat.world.isRemote) {
                    if (rand.nextInt(3) == 0) {
                        cat.setTamedBy(player);
                        AnimalUtils.playTameEffect(cat, true);
                        //cat.sitGoal.setSitting(true);
                        cat.world.setEntityState(cat, (byte) 7);
                    } else {
                        AnimalUtils.playTameEffect(cat, false);
                        cat.world.setEntityState(cat, (byte) 7);
                    }
                }

                return;
            }
        }
    }
}
