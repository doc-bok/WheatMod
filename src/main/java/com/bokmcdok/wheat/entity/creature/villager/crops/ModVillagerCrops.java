package com.bokmcdok.wheat.entity.creature.villager.crops;

import com.bokmcdok.wheat.data.ModIngredientSupplier;
import net.minecraft.block.Block;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Map;

public class ModVillagerCrops {
    private final Map<ResourceLocation, ResourceLocation> mCrops;
    private final LazyValue<Ingredient> mSeedItems;

    /**
     * Construction
     * @param crops The resource locations of the seeds and crops.
     */
    public ModVillagerCrops(Map<ResourceLocation, ResourceLocation> crops) {
        mCrops = crops;

        ModIngredientSupplier seedSupplier = new ModIngredientSupplier(mCrops.keySet().toArray(new ResourceLocation[0]));
        mSeedItems = new LazyValue<>(seedSupplier);
    }

    /**
     * Check if the item is a seed.
     * @param stack The item to check.
     * @return TRUE if the item is a seed.
     */
    public boolean isSeedItem(ItemStack stack) {
        return mSeedItems.getValue().test(stack);
    }

    /**
     * Check if a seed is in the villager's inventory.
     * @param villager The villager to check.
     * @return TRUE if a seed is in the villager's inventory.
     */
    public boolean isSeedInInventory(VillagerEntity villager) {
        Inventory inventory = villager.getVillagerInventory();
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            if (mSeedItems.getValue().test(inventory.getStackInSlot(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the crop block of the specified seed.
     * @param seed The item registry name of the seed.
     * @return The crop block.
     */
    public Block getCropBlock(Item seed) {
        ResourceLocation blockRegistryName = mCrops.get(seed.getRegistryName());
        IForgeRegistry<Block> blockRegistry = ForgeRegistries.BLOCKS;
        return blockRegistry.getValue(blockRegistryName);
    }

    /**
     * Get the seed items.
     * @return An ingredient containing all seed items.
     */
    public Ingredient getSeedItems() {
        return mSeedItems.getValue();
    }
}
