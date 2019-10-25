package com.bokmcdok.wheat.Container;

import com.bokmcdok.wheat.Recipe.FlourMillRecipe;
import com.bokmcdok.wheat.Recipe.ModRecipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;

public class ModResultSlot<C extends CraftingInventory, T extends IRecipe<C>> extends CraftingResultSlot {

    /**
     * Constructor
     * @param recipeType The type of recipe to use as a result
     * @param player The player
     * @param craftingInventory The inventory used for crafting
     * @param inventoryIn The inventory to move crafted items to
     * @param slotIndex The index of this crafting result
     * @param xPosition The x-position of this slot
     * @param yPosition The y-position of this slot
     */
    public ModResultSlot(IRecipeType<T> recipeType, PlayerEntity player, C craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
        mCraftingInventory = craftingInventory;
        mPlayer = player;
        mRecipeType = recipeType;
    }

    /**
     * Overriden purely to use the recipe type specified instead of a crafting recipe
     * @param thePlayer The player
     * @param stack The item stak we are trying to take
     * @return The stack we took
     */
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        this.onCrafting(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
        NonNullList<ItemStack> nonnulllist = thePlayer.world.getRecipeManager().getRecipeNonNull(mRecipeType, mCraftingInventory, thePlayer.world);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = this.mCraftingInventory.getStackInSlot(i);
            ItemStack itemstack1 = nonnulllist.get(i);
            if (!itemstack.isEmpty()) {
                this.mCraftingInventory.decrStackSize(i, 1);
                itemstack = this.mCraftingInventory.getStackInSlot(i);
            }

            if (!itemstack1.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.mCraftingInventory.setInventorySlotContents(i, itemstack1);
                } else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
                    itemstack1.grow(itemstack.getCount());
                    this.mCraftingInventory.setInventorySlotContents(i, itemstack1);
                } else if (!this.mPlayer.inventory.addItemStackToInventory(itemstack1)) {
                    this.mPlayer.dropItem(itemstack1, false);
                }
            }
        }

        return stack;
    }

    private final IRecipeType<T> mRecipeType;
    private final C mCraftingInventory;
    private final PlayerEntity mPlayer;
}