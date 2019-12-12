package com.bokmcdok.wheat.item;

import com.bokmcdok.wheat.entity.ThrownItemEntity;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.Random;

public class ModItemImpl {

    private static final Random RNG = new Random();

    private SoundEvent mThrowingSound;
    private float mThrowingVolume;
    private float mThrowingPitch;

    private float mThrowingOffset;
    private float mThrowingVelocity;
    private float mThrowingInaccuracy;

    private IItemColor mColor;

    private float mCompostChance;

    /**
     * Construction
     * @param properties The modded properties for the item.
     */
    ModItemImpl(ModItemProperties properties) {
        mThrowingSound = properties.mThrowingSound;
        mThrowingVolume = properties.mThrowingVolume;
        mThrowingPitch = properties.mThrowingPitch;

        mThrowingOffset = properties.mThrowingOffset;
        mThrowingVelocity = properties.mThrowingVelocity;
        mThrowingInaccuracy = properties.mThrowingInaccuracy;

        mColor = properties.mColor;

        mCompostChance = properties.mCompostChance;
    }

    /**
     * Get the item's color
     * @return The color of the item.
     */
    public IItemColor getColor() { return  mColor; }

    /**
     * Get the chance an item will compost in the harvester.
     * @return A probability between 0 and 1
     */
    public float getCompostChance() { return mCompostChance; }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete. This is used for food that is contained in a bowl, for example.
     * @param stack The item stack to check.
     * @param world The current world.
     * @param entityLiving The entity that owns the item.
     * @return The item to replace the current one with.
     */
    public ItemStack onItemUseFinish(Item item, ItemStack stack, World world, LivingEntity entityLiving) {
        if (item.isFood() && item.hasContainerItem(stack)) {
            return item.getContainerItem(stack);
        }

        return null;
    }

    /**
     * Support for durable ingredients.
     * @param stack The item stack to check.
     * @return TRUE if the item is a durable ingredient.
     */
    public boolean hasContainerItem(Item item, ItemStack stack) {
        return item.getMaxDamage(stack) > 0;
    }

    /**
     * Get the container item. If the item is a durable ingredient, this will return a more damaged item on each use. If
     * the item is destroyed it will return an empty item, unless the item has an actual container that will be returned
     * instead.
     * @param stack The item stack to check.
     * @return The item to replace the current item with.
     */
    public ItemStack getContainerItem(Item item, ItemStack stack) {
        if (item.getMaxDamage(stack) > 0) {
            ItemStack container = stack.copy();
            container.setDamage(stack.getDamage() + 1);
            if (container.getDamage() < container.getMaxDamage()) {
                return container;
            }
        }

        return null;
    }

    /**
     * This will create an entity and throw the item in the world.
     * @param world The current world.
     * @param player The player that owns the item.
     * @param hand The hand holding the item.
     * @return The result of the action.
     */
    public ActionResult<ItemStack> onItemRightClick(Item item, World world, PlayerEntity player, Hand hand) {
        if (mThrowingVelocity > 0.0f) {
            ItemStack itemstack = player.getHeldItem(hand);
            ItemStack itemstack1 = player.abilities.isCreativeMode ? itemstack.copy() : itemstack.split(1);
            world.playSound(null, player.posX, player.posY, player.posZ, mThrowingSound, SoundCategory.PLAYERS, mThrowingVolume, mThrowingPitch / (RNG.nextFloat() * 0.4F + 0.8F));
            if (!world.isRemote) {
                ThrownItemEntity entity = new ThrownItemEntity(world, player);
                entity.func_213884_b(itemstack1);
                entity.shoot(player, player.rotationPitch, player.rotationYaw, mThrowingOffset, mThrowingVelocity, mThrowingInaccuracy);
                world.addEntity(entity);
            }

            player.addStat(Stats.ITEM_USED.get(item));
            return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
        }

        return null;
    }

    public static class ModItemProperties extends Item.Properties {

        private SoundEvent mThrowingSound = SoundEvents.ENTITY_SPLASH_POTION_THROW;
        private float mThrowingVolume = 0.5f;
        private float mThrowingPitch = -0.4f;

        private float mThrowingOffset = 0.0f;
        private float mThrowingVelocity = 0.0f;
        private float mThrowingInaccuracy = 0.0f;

        private IItemColor mColor = null;

        private float mCompostChance = 0.0f;

        public void throwing(float offset, float velocity, float inaccuracy) {
            mThrowingOffset = offset;
            mThrowingVelocity = velocity;
            mThrowingInaccuracy = inaccuracy;
        }

        public void throwingSound(SoundEvent event, float volume, float pitch) {
            mThrowingSound = event;
            mThrowingVolume = volume;
            mThrowingPitch = pitch;
        }

        public void color(IItemColor color) {
            mColor = color;
        }

        public void compostChance(float compostChance) { mCompostChance = compostChance; }
    }
}
