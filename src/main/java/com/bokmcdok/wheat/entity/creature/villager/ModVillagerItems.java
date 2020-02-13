package com.bokmcdok.wheat.entity.creature.villager;

import com.bokmcdok.wheat.entity.creature.villager.crops.ModVillagerCrops;
import com.bokmcdok.wheat.entity.creature.villager.food.ModVillagerFood;
import com.bokmcdok.wheat.entity.creature.villager.profession.ModVillagerProfession;
import com.bokmcdok.wheat.tag.ModTag;
import com.bokmcdok.wheat.tag.ModTagDataManager;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.ItemStack;

public class ModVillagerItems {
    private final ModTagDataManager mItemTagDataManager;
    private final ModVillagerFood mVillagerFood;
    private final ModVillagerCrops mVillagerCrops;

    /**
     * Construction
     * @param itemTagDataManager The item tag data manager.
     * @param food The villager food instance.
     */
    public ModVillagerItems(ModTagDataManager itemTagDataManager, ModVillagerFood food, ModVillagerCrops crops) {
        mItemTagDataManager = itemTagDataManager;
        mVillagerFood = food;
        mVillagerCrops = crops;
    }

    /**
     * Test if a villager can pick up said item.
     * @param profession The profession of the villager. Farmers will also pickup farm items.
     * @param stack The item to try and pick up.
     * @return True if the villager can pick up the item.
     */
    public boolean canPickupItem(VillagerProfession profession, ItemStack stack) {
        if (mVillagerFood.isFoodItem(stack)) {
            return true;
        }

        ModTag tag = mItemTagDataManager.getEntry("docwheat:villager_items");
        if (tag != null && tag.contains(stack.getItem().getRegistryName())) {
            return true;
        }

        if (profession == VillagerProfession.FARMER && mVillagerCrops.isSeedItem(stack)) {
            return true;
        }

        //  Fix for modded professions having namespace in their name.
        String name = profession.toString();
        if (name.contains(":")) {
            name = name.split(":")[0];
        }

        tag = mItemTagDataManager.getEntry("docwheat:villager_" + name + "_items");
        if (tag != null) {
            return tag.contains(stack.getItem().getRegistryName());
        }

        return false;
    }
}
