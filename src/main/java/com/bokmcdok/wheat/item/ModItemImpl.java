package com.bokmcdok.wheat.item;

import com.bokmcdok.wheat.entity.ThrownItemEntity;
import com.bokmcdok.wheat.spell.ModSpell;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class ModItemImpl {

    private static final Random RNG = new Random();

    private final SoundEvent mThrowingSound;
    private final float mThrowingVolume;
    private final float mThrowingPitch;
    private final float mThrowingOffset;
    private final float mThrowingVelocity;
    private final float mThrowingInaccuracy;
    private final IItemColor mColor;
    private final float mCompostChance;
    private final ModSpell mSpell;
    private final Effect mOnDamagedEffect;
    private final int mOnDamagedEffectDuration;
    private final int mOnDamagedEffectAmplifier;
    private final String mArmorTexture;
    private final Map<ResourceLocation, Integer> mEnchantments;

    //  Weapon attributes
    private final ModDamageType mDamageType;
    private final IItemTier mItemTier;
    private final float mAttackDamage;
    private final float mAttackSpeed;
    private final Map<ModItemTrait, Float> mTraits;

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

        mSpell = properties.mSpell;

        mOnDamagedEffect = properties.mOnDamagedEffect;
        mOnDamagedEffectAmplifier = properties.mOnDamagedEffectAmplifier;
        mOnDamagedEffectDuration = properties.mOnDamagedEffectDuration;

        mArmorTexture = properties.mArmorTexture;
        mEnchantments = properties.mEnchantments;

        //  Weapons
        mDamageType = properties.mDamageType;
        mItemTier = properties.mItemTier;
        mAttackDamage = properties.mAttackDamage;
        mAttackSpeed = -2.0f + (-0.1f * properties.mWeight);
        mTraits = properties.mTraits;
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
     * @param entity The entity that owns the item.
     * @return The item to replace the current one with.
     */
    public ItemStack onItemUseFinish(Item item, ItemStack stack, World world, LivingEntity entity) {
        if (item.isFood() && item.hasContainerItem(stack)) {
            return item.getContainerItem(stack);
        }

        if (mSpell != null) {
            return castSpell(item, world, entity);
        }

        return null;
    }

    /**
     * Reset the cooldown if you stop using a spell item.
     * @param stack The item stack to check.
     * @param world The current world.
     * @param entity The entity that owns the item.
     * @param timeLeft The number of remaining ticks left before the item could have been used.
     */
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
        if (mSpell != null) {
            Hand activeHand = entity.getActiveHand();
            ItemStack itemStack = entity.getHeldItem(activeHand);
            itemStack.getTag().putInt("spell_cooldown", entity.ticksExisted + mSpell.getCooldown());
            world.playSound(null, entity.getPosition(), mSpell.getFailSound(), SoundCategory.PLAYERS, 5.0f, 1.0F);
        }
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
    public ItemStack getContainerItem(ItemStack stack) {
        if (stack.getMaxDamage() > 0) {
            ItemStack container = stack.copy();
            container.setDamage(stack.getDamage() + 1);
            if (container.getDamage() < container.getMaxDamage()) {
                return container;
            }
        }

        return null;
    }

    /**
     * Handle thrown items and spellcasting items.
     * @param world The current world.
     * @param player The player that owns the item.
     * @param hand The hand holding the item.
     * @return The result of the action.
     */
    public ActionResult<ItemStack> onItemRightClick(Item item, World world, PlayerEntity player, Hand hand) {
        if (mThrowingVelocity > 0.0f) {
            return throwItem(item, world, player, hand);
        }

        if (mSpell != null) {
            return prepareSpell(item, world, player, hand);
        }

        return null;
    }

    /**
     * Get the time it takes to use this item.
     * @return The time it takes to use this item.
     */
    public int getUseDuration() {
        if (mSpell != null) {
            return mSpell.getCastingTime();
        }

        return -1;
    }

    /**
     * Is this a spellcasting item?
     * @return TRUE if this item has a spell attached.
     */
    public boolean isSpell() {
        return mSpell != null;
    }

    /**
     * Apply effects triggered by armour being damaged.
     * @param amount The amount of damage.
     * @param entity The entity to apply the effect to.
     * @param <T> The type of living entity.
     */
    public <T extends LivingEntity> void onDamageItem(int amount, T entity) {
        if (mOnDamagedEffect != null) {
            int duration = amount * mOnDamagedEffectDuration;
            if (entity.isPotionActive(mOnDamagedEffect)) {
                EffectInstance activeInstance = entity.getActivePotionEffect(mOnDamagedEffect);
                duration += activeInstance.getDuration();
            }

            EffectInstance effectInstance = new EffectInstance(mOnDamagedEffect, duration, mOnDamagedEffectAmplifier);
            entity.addPotionEffect(effectInstance);
        }
    }

    /**
     * Get the texture to use with the armor.
     * @return The registry name of the texture to use.
     */
    public String getArmorTexture() {
        return mArmorTexture;
    }

    /**
     * Get the default enchantments for this item.
     * @return A map of registry names and enchantment levels.
     */
    public Map<ResourceLocation, Integer> getEnchantments() {
        return  mEnchantments;
    }

    /**
     * Get the damage type.
     * @return The type of damage this weapon inflicts.
     */
    public ModDamageType getDamageType() {
        return mDamageType;
    }

    /**
     * Get the tier of this item.
     * @return The item tier (WOOD, IRON, etc).
     */
    public IItemTier getItemTier() {
        return mItemTier;
    }

    /**
     * Get the attack damage of this weapon/tool.
     * @return The attack damage.
     */
    public float getAttackDamage() {
        return mAttackDamage;
    }

    /**
     * Get the attack speed of this weapon.
     * @return The attack speed.
     */
    public float getAttackSpeed() {
        return mAttackSpeed;
    }

    /**
     * Check if the item has a particular trait.
     * @param trait The trait to check.
     * @return TRUE if the item has the trait.
     */
    public boolean hasTrait(ModItemTrait trait) {
        return mTraits.containsKey(trait);
    }

    /**
     * Get the value of a trait.
     * @param trait The trait to look for.
     * @return The value of the attached trait.
     */
    public float getTraitValue(ModItemTrait trait) {
        if (hasTrait(trait)) {
            return mTraits.get(trait);
        }

        return 0.0f;
    }

    /**
     * Throw an item into the world.
     * @param item The item to throw.
     * @param world the current world.
     * @param player The player using the item.
     * @param hand Which hand the item is in.
     * @return The result of the action.
     */
    private ActionResult<ItemStack> throwItem(Item item, World world, PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getHeldItem(hand);
        ItemStack itemToUse = player.abilities.isCreativeMode ? heldItem.copy() : heldItem.split(1);
        world.playSound(null, player.getPosition(), mThrowingSound, SoundCategory.PLAYERS, mThrowingVolume, mThrowingPitch / (RNG.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote) {
            ThrownItemEntity entity = new ThrownItemEntity(world, player);
            entity.setItem(itemToUse);
            entity.shoot(player, player.rotationPitch, player.rotationYaw, mThrowingOffset, mThrowingVelocity, mThrowingInaccuracy);
            world.addEntity(entity);
        }

        player.addStat(Stats.ITEM_USED.get(item));
        return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
    }

    /**
     * Prepare a spell for casting.
     * @param item The item to throw.
     * @param world the current world.
     * @param player The player using the item.
     * @param hand Which hand the item is in.
     * @return The result of the action.
     */
    private ActionResult<ItemStack> prepareSpell(Item item, World world, PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getHeldItem(hand);
        int cooldown = heldItem.getTag().getInt("spell_cooldown");

        //  Fix for ticksExisted resetting after load.
        if (cooldown - player.ticksExisted > mSpell.getCooldown()) {
            cooldown = 0;
        }

        if (player.ticksExisted > cooldown) {
            world.playSound(null, player.getPosition(), mSpell.getPrepareSound(), SoundCategory.PLAYERS, 5.0f, 1.0F);
            player.setActiveHand(hand);

            player.addStat(Stats.ITEM_USED.get(item));
            return new ActionResult<>(ActionResultType.CONSUME, player.getHeldItem(hand));
        }

        return null;
    }

    /**
     * Cast a spell.
     * @param item The item containing the spell.
     * @param world the current world.
     * @param entity The player using the item.
     * @return The result of the action.
     */
    private ItemStack castSpell(Item item, World world, LivingEntity entity) {
        Hand activeHand = entity.getActiveHand();
        ItemStack itemStack = entity.getHeldItem(activeHand);
        itemStack.getTag().putInt("spell_cooldown", entity.ticksExisted + mSpell.getCooldown());

        if (mSpell.cast(entity)) {
            world.playSound(null, entity.getPosition(), mSpell.getCastSound(), SoundCategory.PLAYERS, 5.0f, 1.0F);

            itemStack.damageItem(1, entity, (x) -> x.sendBreakAnimation(activeHand));
            if (itemStack.getDamage() > 0) {
                return itemStack;
            } else {
                return item.getContainerItem(itemStack);
            }
        } else {
            world.playSound(null, entity.getPosition(), mSpell.getFailSound(), SoundCategory.PLAYERS, 5.0f, 1.0F);
        }

        return null;
    }

    public static class ModItemProperties extends Item.Properties {

        private ModSpell mSpell = null;
        private SoundEvent mThrowingSound = SoundEvents.ENTITY_SPLASH_POTION_THROW;
        private float mThrowingVolume = 0.5f;
        private float mThrowingPitch = -0.4f;
        private float mThrowingOffset = 0.0f;
        private float mThrowingVelocity = 0.0f;
        private float mThrowingInaccuracy = 0.0f;
        private IItemColor mColor = null;
        private float mCompostChance = 0.0f;
        private Effect mOnDamagedEffect = null;
        private int mOnDamagedEffectDuration = 0;
        private int mOnDamagedEffectAmplifier = 0;
        private String mArmorTexture = null;
        private final Map<ResourceLocation, Integer> mEnchantments = Maps.newHashMap();

        //  Weapon attributes
        private ModDamageType mDamageType;
        private IItemTier mItemTier;
        private float mAttackDamage;
        private float mWeight;
        private final Map<ModItemTrait, Float> mTraits = Maps.newHashMap();

        /**
         * This item can be thrown by right-clicking.
         * @param offset The offset to throw it at.
         * @param velocity The speed at which the item is thrown.
         * @param inaccuracy How inaccurate the throw is.
         */
        public void throwing(float offset, float velocity, float inaccuracy) {
            mThrowingOffset = offset;
            mThrowingVelocity = velocity;
            mThrowingInaccuracy = inaccuracy;
        }

        /**
         * The sound made when the item is thrown.
         * @param event The sound to play.
         * @param volume The volume of the sound.
         * @param pitch The pitch of the sound.
         */
        public void throwingSound(SoundEvent event, float volume, float pitch) {
            mThrowingSound = event;
            mThrowingVolume = volume;
            mThrowingPitch = pitch;
        }

        /**
         * Set the color tint of the item.
         * @param color The color to tint the item with.
         */
        public void color(IItemColor color) {
            mColor = color;
        }

        /**
         * Set the chance the item will compost.
         * @param compostChance The chance composting is successful.
         */
        public void compostChance(float compostChance) { mCompostChance = compostChance; }

        /**
         * The spell cast when this item is used.
         * @param spell A spell instance.
         */
        public void spell(ModSpell spell) { mSpell = spell; }

        /**
         * The effect applied to the owner when this item is damaged.
         * @param effectName The effect to apply.
         */
        public void onDamagedEffect(String effectName) {
            ResourceLocation effectKey = new ResourceLocation(effectName);
            Optional<Effect> effect = Registry.EFFECTS.getValue(effectKey);
            effect.ifPresent(value -> mOnDamagedEffect = value);
        }

        /**
         * The duration of the effect applied when this item is damaged.
         * @param duration The duration in ticks.
         */
        public void onDamagedEffectDuration(int duration) {
            mOnDamagedEffectDuration = duration;
        }

        /**
         * The strength of the effect applied when this item is damaged.
         * @param amplifier The effect amplifier.
         */
        public void onDamagedEffectAmplifier(int amplifier) {
            mOnDamagedEffectAmplifier = amplifier;
        }

        /**
         * The texture to apply to armor items.
         * @param texture The texture's registry name.
         */
        public void armorTexture(String texture) {
            mArmorTexture = texture;
        }

        /**
         * An enchantment to apply when an ItemStack is created.
         * @param registryName The registry name of the enchantment.
         * @param level The level of the enchantment.
         */
        public void addEnchantment(String registryName, int level) {
            mEnchantments.put(new ResourceLocation(registryName), level);
        }

        /**
         * The damage type of the weapon.
          * @param damageType The type to set.
         */
        public void setDamageType(ModDamageType damageType) {
            mDamageType = damageType;
        }

        /**
         * Set the tier of the weapon.
         * @param itemTier The item tier (e.g. WOOD).
         */
        public void setItemTier(IItemTier itemTier) {
            mItemTier = itemTier;
        }

        /**
         * Set the attack damage of the weapon.
         * @param damage The attack damage.
         */
        public void setAttackDamage(float damage) {
            mAttackDamage = damage;
        }

        /**
         * Set the weight of the weapon.
         * @param weight The weight.
         */
        public void setWeight(float weight) {
            mWeight = weight;
        }

        /**
         * Add a trait to the weapon.
         * @param trait The trait to add.
         */
        public void addTrait(ModItemTrait trait) {
            addTrait(trait, 0.0f);
        }

        /**
         * Add a trait to the weapon.
         * @param trait The trait to add.
         * @param value The optional value of the trait.
         */
        public void addTrait(ModItemTrait trait, float value) {
            mTraits.put(trait, value);
        }
    }
}
