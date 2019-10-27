package com.bokmcdok.wheat.container;

import com.bokmcdok.wheat.block.ModBlockUtils;
import com.bokmcdok.wheat.item.ModItemUtils;
import com.bokmcdok.wheat.recipe.FlourMillRecipe;
import com.bokmcdok.wheat.sound.ModSoundUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;

import java.util.Optional;


public class FlourMillContainer extends Container {

    private static final int RESULT_SLOT_INDEX = 0;
    private static final int INPUT_SLOT_INDEX = 1;
    private static final int INVENTORY_START_INDEX = 2;
    private static final int HOTBAR_START_INDEX = 29;
    private static final int HOTBAR_END_INDEX = 38;

    private final CraftingInventory mCraftingGrid = new CraftingInventory(this, 3, 3);
    private final CraftResultInventory mResultSlot = new CraftResultInventory();
    private final IWorldPosCallable mCallable;
    private final PlayerEntity mPlayer;
    /**
     * Construction
     * @param windowId The id of the window this container uses
     * @param playerInventory The player's inventory
     */
    public FlourMillContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this(windowId, playerInventory);
    }

    /**
     * Construction
     * @param windowId The id of the window this container uses
     * @param playerInventory The player's inventory
     */
    public FlourMillContainer(int windowId, PlayerInventory playerInventory) {
        this(windowId, playerInventory, IWorldPosCallable.DUMMY);
    }

    /**
     * Construction
     * @param windowId The id of the window this container uses
     * @param playerInventory The player's inventory
     * @param callable ???
     */
    public FlourMillContainer(int windowId, PlayerInventory playerInventory, IWorldPosCallable callable) {
        super(ModContainerUtils.flour_mill_container, windowId);
        mCallable = callable;
        mPlayer = playerInventory.player;

        //  Setup the craft result slot.
        addSlot(new ModResultSlot(ModSoundUtils.mill_grind, FlourMillRecipe.flour_mill, mPlayer, mCraftingGrid, mResultSlot, 0, 124, 35));

        //  Setup crafting grid
        addSlot(new ModSlot(ModItemUtils.GRAIN_ITEMS, mCraftingGrid, 0, 48, 35));

        //  Setup player inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        //  Setup hotbar
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    /**
     * Called when the craft matrix changes. Updates the recipe result.
     * @param inventoryIn
     */
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        mCallable.consume((world, position) -> {
            onCraftMatrixChanged(windowId, world, mPlayer, mCraftingGrid, mResultSlot);
        });
    }

    /**
     * Clear the container.
     */
    public void clear() {
        mCraftingGrid.clear();
        mResultSlot.clear();
    }

    /**
     * Check a recipe matches what's in the crafting grid.
     * @param recipeIn The recipe to check
     * @return True if the recipe matches
     */
    public boolean matches(IRecipe<? super CraftingInventory> recipeIn) {
        return recipeIn.matches(mCraftingGrid, mPlayer.world);
    }

    /**
     * Called when the container is closed
     * @param playerIn The player that is interacting with the container
     */
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        mCallable.consume((world, position) -> {
            this.clearContainer(playerIn, world, mCraftingGrid);
        });
    }

    /**
     * Checks the specified player can interact with the block
     * @param playerIn The player that is interacting with the container
     * @return True if the player can interact
     */
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(mCallable, playerIn, ModBlockUtils.flour_mill);
    }

    /**
     * Handles shift-clicking.
     * @param playerIn The player that is interacting with the container
     * @param index The slot that was shift-clicked.
     * @return
     */
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (isResultSlot(index)) {
                mCallable.consume((world, position) -> {
                    itemstack1.getItem().onCreated(itemstack1, world, playerIn);
                });

                if (!mergeItemStack(itemstack1, INVENTORY_START_INDEX, HOTBAR_END_INDEX, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (isInputSlot(index)) {
                if (!mergeItemStack(itemstack1, INVENTORY_START_INDEX, HOTBAR_END_INDEX, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!mergeItemStack(itemstack1, INPUT_SLOT_INDEX, INVENTORY_START_INDEX, false)) {
                    if (isPlayerInventorySlot(index)) {
                        if (!mergeItemStack(itemstack1, HOTBAR_START_INDEX, HOTBAR_END_INDEX, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        if (!mergeItemStack(itemstack1, INVENTORY_START_INDEX, HOTBAR_START_INDEX, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }



            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
            if (index == 0) {
                playerIn.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }

    /**
     * Check we can merge the stack into the slot.
     * @param stack The stack to merge
     * @param slotIn The slot to merge into
     * @return True if the stack can be merged
     */
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != mCraftingGrid && super.canMergeSlot(stack, slotIn);
    }

    /**
     *  Updates the crafting result.
     * @param windowId The id of the window this container uses
     * @param world The world the container is in
     * @param player The player
     * @param inventory The crafting inventory
     * @param result The craft result slot
     */
    protected static void onCraftMatrixChanged(int windowId, World world, PlayerEntity player, CraftingInventory inventory, CraftResultInventory result) {
        if (!world.isRemote) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<FlourMillRecipe> optional = world.getServer().getRecipeManager().getRecipe(FlourMillRecipe.flour_mill, inventory, world);
            if (optional.isPresent()) {
                FlourMillRecipe recipe = optional.get();
                if (result.canUseRecipe(world, serverplayerentity, recipe)) {
                    itemstack = recipe.getCraftingResult(inventory);
                }
            }

            result.setInventorySlotContents(0, itemstack);
            serverplayerentity.connection.sendPacket(new SSetSlotPacket(windowId, 0, itemstack));
        }
    }

    /**
     * Check if the slot is the result slot
     * @param index The index of the slot
     * @return True if it is the index of the result slot
     */
    private static boolean isResultSlot(int index) { return index == RESULT_SLOT_INDEX; }

    /**
     * Check if the slot is the input slot
     * @param index The index of the slot
     * @return True if it is the index of the input slot
     */
    private static boolean isInputSlot(int index) { return index == INPUT_SLOT_INDEX; }

    /**
     * Check if the slot is a player inventory slot
     * @param index The index of the slot
     * @return True if it is the index of a player inventory slot
     */
    private static boolean isPlayerInventorySlot(int index) { return index >= INVENTORY_START_INDEX && index < HOTBAR_START_INDEX; }
}
