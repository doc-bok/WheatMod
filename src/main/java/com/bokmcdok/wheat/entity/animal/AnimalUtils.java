package com.bokmcdok.wheat.entity.animal;

import com.bokmcdok.wheat.item.ModItemUtils;
import com.bokmcdok.wheat.WheatMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnimalUtils {

    protected final static Random rand = new Random();

    /**
     * Add new goals to certain animals so they will be attracted to new items.
     */
    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent event)
    {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.COW || entity.getType() == EntityType.SHEEP)
        {
            AnimalEntity animal = (AnimalEntity)entity;
            animal.goalSelector.addGoal(3, new TemptGoal(animal, 1.25D, ModItemUtils.WHEAT_ITEMS, false));
        } else if (entity.getType() == EntityType.CHICKEN) {
            AnimalEntity animal = (AnimalEntity)entity;
            animal.goalSelector.addGoal(3, new TemptGoal(animal, 1.0D, ModItemUtils.SEED_ITEMS, false));
        }
    }

    /**
     * Handle feeding with new items.
     */
    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent.EntityInteract event)
    {
        Entity target = event.getTarget();
        if (target.getType() == EntityType.COW || target.getType() == EntityType.SHEEP) {
            feedAnimal(event, ModItemUtils.WHEAT_ITEMS);
        }
        else if (target.getType() == EntityType.CHICKEN) {
            feedAnimal(event, ModItemUtils.SEED_ITEMS);
        }
        else if (target.getType() == EntityType.LLAMA) {
            feedLlama(event);
        }
        else if (target.getType() == EntityType.HORSE ||
                target.getType() == EntityType.DONKEY ||
                target.getType() == EntityType.MULE ) {
            feedHorse(event);
        }
    }

    /**
     * Feed an animal with the specified food items.
     */
    private static void feedAnimal(PlayerInteractEvent.EntityInteract event, Ingredient food) {
        PlayerEntity player = event.getPlayer();
        Hand hand = event.getHand();
        ItemStack stack = player.getHeldItem(hand);

        if (food.test(stack)) {
            AnimalEntity animal = (AnimalEntity) event.getTarget();
            if (animal.getGrowingAge() == 0 && animal.canBreed()) {
                consumeEvent(event, player, stack);
                animal.setInLove(player);
            }

            if (animal.isChild()) {
                consumeEvent(event, player, stack);
                animal.ageUp((int)((float)(-animal.getGrowingAge() / 20) * 0.1F), true);
            }
        }
    }

    private static void feedLlama(PlayerInteractEvent.EntityInteract event) {

        PlayerEntity player = event.getPlayer();
        Hand hand = event.getHand();
        ItemStack stack = player.getHeldItem(hand);
        AbstractHorseEntity animal = (AbstractHorseEntity) event.getTarget();

        if (cannotEat(animal, player)) {
            return;
        }

        boolean eating = false;

        int growth = 0;
        int temper = 0;
        float heal = 0.0f;
        if (ModItemUtils.WHEAT_ITEMS.test(stack)) {
            growth = 10;
            temper = 3;
            heal = 2.0f;
        } else if (ModItemUtils.BALE_ITEMS.test(stack)) {
            growth = 90;
            temper = 6;
            heal = 10.f;

            if (animal.isTame() && animal.getGrowingAge() == 0 && animal.canBreed()) {
                eating = true;
                animal.setInLove(player);
            }
        }

        feedHorseOrLlama(event, eating, growth, temper, heal, SoundEvents.ENTITY_LLAMA_EAT);
    }

    private static void feedHorse(PlayerInteractEvent.EntityInteract event) {

        PlayerEntity player = event.getPlayer();
        Hand hand = event.getHand();
        ItemStack stack = player.getHeldItem(hand);
        AbstractHorseEntity animal = (AbstractHorseEntity) event.getTarget();

        if (cannotEat(animal, player)) {
            return;
        }

        int growth = 0;
        int temper = 0;
        float heal = 0.0f;
        if (ModItemUtils.WHEAT_ITEMS.test(stack)) {
            growth = 20;
            temper = 3;
            heal = 2.0f;
        }
        else if (ModItemUtils.BALE_ITEMS.test(stack)) {
            growth = 180;
            heal = 20.f;
        }

        feedHorseOrLlama(event,false, growth, temper, heal, SoundEvents.ENTITY_HORSE_EAT);
    }

    public static boolean heal(AnimalEntity animal, float heal) {
        if (animal.getHealth() < animal.getMaxHealth() && heal > 0.0f) {
            animal.heal(heal);
            return true;
        }

        return false;
    }

    private static boolean grow(AnimalEntity animal, int growth) {
        if (animal.isChild() && growth > 0) {
            Vec3d position = animal.getPositionVec();
            animal.world.addParticle(ParticleTypes.HAPPY_VILLAGER,
                    position.x + (double)(rand.nextFloat() * animal.getWidth() * 2.0F) - (double)animal.getWidth(),
                    position.y + 0.5D + (double)(rand.nextFloat() * animal.getHeight()),
                    position.z + (double)(rand.nextFloat() * animal.getWidth() * 2.0F) - (double)animal.getWidth(),
                    0.0D, 0.0D, 0.0D);

            if (!animal.world.isRemote) {
                animal.addGrowth(growth);
            }

            return true;
        }

        return false;
    }

    private static boolean improveTemper(AbstractHorseEntity animal, int temper, boolean eating) {
        if (temper > 0 && (eating || !animal.isTame()) && animal.getTemper() < animal.getMaxTemper()) {
            if (!animal.world.isRemote()) {
                animal.increaseTemper(temper);
            }

            return true;
        }

        return false;
    }

    private static void feedHorseOrLlama(PlayerInteractEvent.EntityInteract event,
                                         boolean eating, int growth, int temper, float heal,
                                         SoundEvent eatingSound)
    {
        AbstractHorseEntity animal = (AbstractHorseEntity) event.getTarget();
        boolean shouldConsume = eating | heal(animal, heal);
        shouldConsume |= grow(animal, growth);
        shouldConsume |= improveTemper(animal, temper, shouldConsume);

        if (shouldConsume) {
            PlayerEntity player = event.getPlayer();
            Hand hand = event.getHand();
            ItemStack stack = player.getHeldItem(hand);
            consumeEvent(event, player, stack);

            if (!animal.isSilent()) {
                Vec3d position = animal.getPositionVec();
                animal.world.playSound(null, position.x, position.y, position.z,
                        eatingSound, animal.getSoundCategory(),
                        1.0F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F);
            }
        }
    }

    private static boolean cannotEat(AbstractHorseEntity entity, PlayerEntity player) {
        if (entity.isChild()) {
            if (entity.isTame() && player.isCrouching()) { return true; }
            return entity.isBeingRidden();
        }

        return false;
    }

    /**
     * Consumes the used item and consumes the event
     * @param event The event to consume
     * @param player The player
     * @param stack The stack of items used on the mob
     */
    public static void consumeEvent(PlayerInteractEvent.EntityInteract event, PlayerEntity player, ItemStack stack) {
        if (!player.abilities.isCreativeMode) {
            stack.shrink(1);
        }

        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.SUCCESS);
    }

    /**
     * Play the taming effect, will either be hearts or smoke depending on status
     */
    public static void playTameEffect(TameableEntity entity, boolean play) {
        IParticleData iparticledata = ParticleTypes.HEART;
        if (!play) {
            iparticledata = ParticleTypes.SMOKE;
        }

        Vec3d position = entity.getPositionVec();
        for(int i = 0; i < 7; ++i) {
            double d0 = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            entity.world.addParticle(iparticledata,
                    position.x + (double)(rand.nextFloat() * entity.getWidth() * 2.0F) - (double)entity.getWidth(),
                    position.y + 0.5D + (double)(rand.nextFloat() * entity.getHeight()),
                    position.z + (double)(rand.nextFloat() * entity.getWidth() * 2.0F) - (double)entity.getWidth(), d0, d1, d2);
        }
    }
}
