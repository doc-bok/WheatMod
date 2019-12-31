package com.bokmcdok.wheat.entity.tile;

import com.bokmcdok.wheat.entity.ModEntityUtils;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public class ModInventoryTileEntity extends TileEntity {
    private NonNullList<ItemStack> mInventory;

    /**
     * Construction
     */
    public ModInventoryTileEntity() {
        this(5);
    }

    /**
     * Construction
     * @param numSlots The number of slots in the inventory.
     */
    public ModInventoryTileEntity(int numSlots) {
        super(ModEntityUtils.inventory);
        mInventory = NonNullList.withSize(numSlots, ItemStack.EMPTY);
    }

    /**
     * Read inventory data from a save.
     * @param data The NBT data.
     */
    @Override
    public void read(CompoundNBT data) {
        super.read(data);
        mInventory = NonNullList.withSize(mInventory.size(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(data, mInventory);
    }

    /**
     * Write the inventory to a save.
     * @param data The NBT data.
     * @return Updated NBT data with the inventory saved.
     */
    @Override
    public CompoundNBT write(CompoundNBT data) {
        super.write(data);
        ItemStackHelper.saveAllItems(data, mInventory);
        return data;
    }

    /**
     * Get the number of slots in the inventory.
     * @return The size of the inventory.
     */
    public int getNumSlots() {
        return mInventory.size();
    }

    /**
     * Get the item stack in a specific slot.
     * @param slot The slot to get the item stack from.
     * @return The item stack in the slot.
     */
    public ItemStack getItemStack(int slot) {
        return  mInventory.get(0);
    }
}
