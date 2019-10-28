package com.bokmcdok.wheat.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class ModSlot extends Slot {

    private final Ingredient mValidItems;

    /**
     * Construction
     * @param validItems Items that are valid for this slot
     * @param inventoryIn The inventory to use for this slot
     * @param index The index into the inventory of this slot
     * @param xPosition The x position of the slot
     * @param yPosition The y position of the slots
     */
    public ModSlot(Ingredient validItems, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        mValidItems = validItems;
    }

    /**
     * Check an item is valid for the slot
     * @param stack The item stack to check
     * @return True if the item can be placed in the slot
     */
    public boolean isItemValid(ItemStack stack) {
        return mValidItems.test(stack);
    }
}
