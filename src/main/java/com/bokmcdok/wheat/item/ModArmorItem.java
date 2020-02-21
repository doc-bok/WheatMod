package com.bokmcdok.wheat.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ModArmorItem extends ArmorItem implements IModItem {
    private final ModItemImpl mImpl;

    /**
     * Construction
     * @param material The material this armor is made out of.
     * @param slot The slot this armor is equipped to.
     * @param properties The properties of this armor item.
     */
    public ModArmorItem(IArmorMaterial material, EquipmentSlotType slot, ModItemImpl.ModItemProperties properties) {
        super(material, slot, properties);
        mImpl = new ModItemImpl(properties);
    }

    /**
     * Get the item's color
     * @return The color of the item.
     */
    public IItemColor getColor() { return  mImpl.getColor(); }

    /**
     * Get the chance an item will compost in the harvester.
     * @return A probability between 0 and 1
     */
    public float getCompostChance() { return mImpl.getCompostChance(); }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete. This is used for food that is contained in a bowl, for example.
     * @param stack The item stack to check.
     * @param world The current world.
     * @param entityLiving The entity that owns the item.
     * @return The item to replace the current one with.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
        ItemStack normalResult = super.onItemUseFinish(stack, world, entityLiving);
        ItemStack overrideResult = mImpl.onItemUseFinish(this, stack, world, entityLiving);
        return  overrideResult != null ? overrideResult : normalResult;
    }

    /**
     * Is the item in a container?
     * @param stack The item stack to check.
     * @return Returns TRUE if the item is in a container.
     */
    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return mImpl.hasContainerItem(this, stack) || super.hasContainerItem(stack);
    }

    /**
     * Get the container item.
     * @param stack The item stack to check.
     * @return The item to replace the current item with.
     */
    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ItemStack result = mImpl.getContainerItem(stack);
        return result != null ? result : super.getContainerItem(stack);
    }

    /**
     * Allows handling of data driven throwing items.
     * @param world The current world.
     * @param player The player that owns the item.
     * @param hand The hand holding the item.
     * @return The result of the action.
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ActionResult<ItemStack> result = mImpl.onItemRightClick(this, world, player, hand);
        return result != null ? result : super.onItemRightClick(world, player, hand);
    }

    /**
     * Get the time it takes to use this item.
     * @return The time it takes to use this item.
     */
    @Override
    public int getUseDuration(ItemStack stack) {
        int useDuration = mImpl.getUseDuration();
        return useDuration > 0 ? useDuration : super.getUseDuration(stack);
    }

    /**
     * Is this a spellcasting item?
     * @return TRUE if this item has a spell attached.
     */
    public boolean isSpell() {
        return mImpl.isSpell();
    }

    /**
     * Triggered when an item is damaged.
     * @param stack The item being damaged.
     * @param amount The amount of damage.
     * @param entity The entity being damaged.
     * @param onBroken The callback if the item is broken.
            * @param <T> The type of LivingEntity.
            * @return The damage done to the item.
            */
    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        int finalAmount = super.damageItem(stack, amount, entity, onBroken);
        mImpl.onDamageItem(finalAmount, entity);
        return finalAmount;
    }

    /**
     * Get the texture to use for this armor.
     * @param stack The armor item.
     * @param entity The entity wearing the armor.
     * @param slot The armor's equipment slot.
     * @param type The subtype (NULL or 'overlay')
     * @return The armor texture to use, if any.
     */
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return mImpl.getArmorTexture();
    }
}