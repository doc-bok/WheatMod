package com.bokmcdok.wheat.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Represents a weapon item. Can be created using only data files.
 */
public class ModWeaponItem extends SwordItem implements IModItem {
    private final ModItemImpl mImpl;

    /**
     * Construction
     * @param properties The properties of the item.
     */
    public ModWeaponItem(IItemTier tier, float attackDamage, float weight, ModItemImpl.ModItemProperties properties) {
        super(tier, (int)attackDamage, -2f + (-0.1f * weight), properties);
        mImpl = new ModItemImpl(properties);
    }

    /**
     * Only slashing weapons can harvest webs.
     * @param block The block to try and harvest.
     * @return TRUE if the block can be harvested.
     */
    @Override
    public boolean canHarvestBlock(BlockState block) {
        if (mImpl.getDamageType() == ModDamageType.SLASHING) {
            return super.canHarvestBlock(block);
        }

        return false;
    }

    /**
     * Get the item's color
     * @return The color of the item.
     */
    @Override
    public IItemColor getColor() { return  mImpl.getColor(); }

    /**
     * Get the chance an item will compost in the harvester.
     * @return A probability between 0 and 1
     */
    @Override
    public float getCompostChance() { return mImpl.getCompostChance(); }

    /**
     * Get the weapons damage type.
     * @return The weapon's damage type.
     */
    public ModDamageType getDamageType() { return mImpl.getDamageType(); }

    /**
     * Check if the weapon has a specific trait.
     * @param trait The trait to check.
     * @return TRUE if the weapon has this trait.
     */
    public boolean hasTrait(ModItemTrait trait) { return mImpl.hasTrait(trait); }

    /**
     * Get the value assigned to a trait.
     * @param trait The trait to look for.
     * @return The value of the trait, or zero if it doesn't exist.
     */
    public float getTraitValue(ModItemTrait trait) { return mImpl.getTraitValue(trait); }

    /**
     * Only slashing weapons can cut through webs effectively.
     * @param stack ???
     * @param state The state of the block being damaged.
     * @return The speed at which to destroy the block.
     */
    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (mImpl.getDamageType() == ModDamageType.SLASHING) {
            return super.getDestroySpeed(stack, state);
        }

        Material material = state.getMaterial();
        return material != Material.PLANTS && material != Material.TALL_PLANTS && material != Material.CORAL &&
                !state.isIn(BlockTags.LEAVES) && material != Material.GOURD ? 1.0F : 1.5F;
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete. This is used for food that is contained in a bowl, for example.
     * @param stack The item stack to check.
     * @param world The current world.
     * @param entityLiving The entity that owns the item.
     * @return The item to replace the current one with.
     */
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
        ItemStack normalResult = super.onItemUseFinish(stack, world, entityLiving);
        ItemStack overrideResult = mImpl.onItemUseFinish(this, stack, world, entityLiving);
        return  overrideResult != null ? overrideResult : normalResult;
    }

    /**
     * Reset the cooldown if you stop using a spell item.
     * @param stack The item stack to check.
     * @param world The current world.
     * @param entity The entity that owns the item.
     * @param timeLeft The number of remaining ticks left before the item could have been used.
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
        mImpl.onPlayerStoppedUsing(stack, world, entity, timeLeft);
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
}
