package com.bokmcdok.wheat.entity.tile;

import com.bokmcdok.wheat.entity.ModEntityRegistrar;
import net.minecraft.block.CampfireBlock;
import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class ModCampfireTileEntity extends TileEntity implements IClearable, ITickableTileEntity {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    private final int[] cookingTimes = new int[4];
    private final int[] cookingTotalTimes = new int[4];

    /**
     * Construction.
     */
    public ModCampfireTileEntity() {
        super(ModEntityRegistrar.campfire);
    }

    /**
     * Update the particles and check for food being cooked.
     */
    public void tick() {
        boolean flag = getBlockState().get(CampfireBlock.LIT);
        boolean flag1 = world.isRemote;
        if (flag1) {
            if (flag) {
                addParticles();
            }

        } else {
            if (flag) {
                cookAndDrop();
            } else {
                for(int i = 0; i < inventory.size(); ++i) {
                    if (cookingTimes[i] > 0) {
                        cookingTimes[i] = MathHelper.clamp(cookingTimes[i] - 2, 0, cookingTotalTimes[i]);
                    }
                }
            }

        }
    }

    /**
     * Returns a NonNullList<ItemStack> of items currently held in the campfire.
     */
    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    /**
     * Read the NBT data for the inventory and cooking times.
     * @param compound The NBT data.
     */
    public void read(CompoundNBT compound) {
        super.read(compound);
        inventory.clear();
        ItemStackHelper.loadAllItems(compound, inventory);
        if (compound.contains("CookingTimes", 11)) {
            int[] aint = compound.getIntArray("CookingTimes");
            System.arraycopy(aint, 0, cookingTimes, 0, Math.min(cookingTotalTimes.length, aint.length));
        }

        if (compound.contains("CookingTotalTimes", 11)) {
            int[] aint1 = compound.getIntArray("CookingTotalTimes");
            System.arraycopy(aint1, 0, cookingTotalTimes, 0, Math.min(cookingTotalTimes.length, aint1.length));
        }

    }

    /**
     * Write the NBT data for the inventory and cooking.
     * @param compound The NBT data.
     * @return The new NBT data.
     */
    public CompoundNBT write(CompoundNBT compound) {
        writeItems(compound);
        compound.putIntArray("CookingTimes", cookingTimes);
        compound.putIntArray("CookingTotalTimes", cookingTotalTimes);
        return compound;
    }

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 13, getUpdateTag());
    }

    /**
     * Handle a packet sent by the server.
     * @param networkManager The network manager.
     * @param packet The packet received.
     */
    @Override
    public void onDataPacket(NetworkManager networkManager, SUpdateTileEntityPacket packet) {
        read(packet.getNbtCompound());
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
     * many blocks change at once. This compound comes back to you clientside in handleUpdateTag
     */
    public CompoundNBT getUpdateTag() {
        return writeItems(new CompoundNBT());
    }

    /**
     * Check if the item has a campfire recipe.
     * @param stack The item stack to test.
     * @return The recipe for the item if it exists.
     */
    public Optional<CampfireCookingRecipe> findMatchingRecipe(ItemStack stack) {
        return inventory.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, new Inventory(stack), world);
    }

    /**
     * Add an item to the inventory.
     * @param itemStack The item stack to take an item from.
     * @param cookTime The time it will take to cook.
     * @return TRUE if an item was taken from the stack.
     */
    public boolean addItem(ItemStack itemStack, int cookTime) {
        for(int i = 0; i < inventory.size(); ++i) {
            ItemStack itemstack = inventory.get(i);
            if (itemstack.isEmpty()) {
                cookingTotalTimes[i] = cookTime;
                cookingTimes[i] = 0;
                inventory.set(i, itemStack.split(1));
                inventoryChanged();
                return true;
            }
        }

        return false;
    }

    /**
     * Clear the inventory.
     */
    public void clear() {
        inventory.clear();
    }

    /**
     * Drop all the items in the inventory.
     */
    public void dropAllItems() {
        if (!getWorld().isRemote) {
            InventoryHelper.dropItems(getWorld(), getPos(), getInventory());
        }

        inventoryChanged();
    }

    /**
     * Individually tracks the cooking of each item, then spawns the finished product in-world and clears the
     * corresponding held item.
     */
    private void cookAndDrop() {
        for(int i = 0; i < inventory.size(); ++i) {
            ItemStack itemstack = inventory.get(i);
            if (!itemstack.isEmpty()) {
                ++cookingTimes[i];
                if (cookingTimes[i] >= cookingTotalTimes[i]) {
                    IInventory iinventory = new Inventory(itemstack);
                    ItemStack itemstack1 = world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, iinventory, world).map((p_213979_1_) -> {
                        return p_213979_1_.getCraftingResult(iinventory);
                    }).orElse(itemstack);
                    BlockPos blockpos = getPos();
                    InventoryHelper.spawnItemStack(world, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), itemstack1);
                    inventory.set(i, ItemStack.EMPTY);
                    inventoryChanged();
                }
            }
        }

    }

    /**
     * Add the smoke particles - more if this is a signal fire.
     */
    private void addParticles() {
        World world = getWorld();
        if (world != null) {
            BlockPos blockpos = getPos();
            Random random = world.rand;
            if (random.nextFloat() < 0.11F) {
                for(int i = 0; i < random.nextInt(2) + 2; ++i) {
                    CampfireBlock.spawnSmokeParticles(world, blockpos, getBlockState().get(CampfireBlock.SIGNAL_FIRE), false);
                }
            }

            int l = getBlockState().get(CampfireBlock.FACING).getHorizontalIndex();

            for(int j = 0; j < inventory.size(); ++j) {
                if (!inventory.get(j).isEmpty() && random.nextFloat() < 0.2F) {
                    Direction direction = Direction.byHorizontalIndex(Math.floorMod(j + l, 4));
                    double d0 = (double)blockpos.getX() + 0.5D - (double)((float)direction.getXOffset() * 0.3125F) + (double)((float)direction.rotateY().getXOffset() * 0.3125F);
                    double d1 = (double)blockpos.getY() + 0.5D;
                    double d2 = (double)blockpos.getZ() + 0.5D - (double)((float)direction.getZOffset() * 0.3125F) + (double)((float)direction.rotateY().getZOffset() * 0.3125F);

                    for(int k = 0; k < 4; ++k) {
                        world.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
                    }
                }
            }

        }
    }

    /**
     * Write the NBT data for the inventory.
     * @param compound The NBT data.
     * @return The new NBT data.
     */
    private CompoundNBT writeItems(CompoundNBT compound) {
        super.write(compound);
        ItemStackHelper.saveAllItems(compound, inventory, true);
        return compound;
    }

    /**
     * Mark the inventory as dirty.
     */
    private void inventoryChanged() {
        markDirty();
        getWorld().notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 3);
    }
}
