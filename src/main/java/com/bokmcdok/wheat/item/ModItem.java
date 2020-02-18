package com.bokmcdok.wheat.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ModItem extends Item implements IModItem {
    private final ModItemImpl mImpl;

    /**
     * Construction
     * @param properties The properties of the item.
     */
    public ModItem(ModItemImpl.ModItemProperties properties) {
        super(properties);
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
     * Get the animation to play as the item is used.
     * @param stack The item stack being used
     * @return The relevant use action.
     */
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return mImpl.getUseAction(stack);
    }

    /**
     * Is this a spellcasting item?
     * @return TRUE if this item has a spell attached.
     */
    public boolean isSpell() {
        return mImpl.isSpell();
    }
}
