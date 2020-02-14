package com.bokmcdok.wheat.entity.creature.villager;

import com.bokmcdok.wheat.entity.creature.villager.crops.ModVillagerCrops;
import com.bokmcdok.wheat.entity.creature.villager.food.ModVillagerFood;
import com.bokmcdok.wheat.tag.ModTag;
import com.bokmcdok.wheat.tag.ModTagDataManager;
import com.google.common.collect.Sets;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.lwjgl.system.CallbackI;

import java.util.Set;

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

        tag = getProfessionItems(profession);
        if (tag != null) {
            return tag.contains(stack.getItem().getRegistryName());
        }

        return false;
    }

    public Set<Item> getSharableItems(VillagerEntity villager, VillagerEntity interactionTarget) {
        Ingredient seeds = mVillagerCrops.getSeedItems();

        ModTag villagerItems = mItemTagDataManager.getEntry("docwheat:villager_items");
        VillagerProfession villagerProfession = villager.getVillagerData().getProfession();
        VillagerProfession targetProfession = interactionTarget.getVillagerData().getProfession();
        ModTag villagerProfessionItems = getProfessionItems(villagerProfession);
        ModTag targetProfessionItems = getProfessionItems(targetProfession);

        Set<Item> shareableItems = Sets.newHashSet();
        if (villagerProfession == VillagerProfession.FARMER || targetProfession == VillagerProfession.FARMER) {
            for (ItemStack i: seeds.getMatchingStacks()) {
                shareableItems.add(i.getItem());
            }
        }

        IForgeRegistry<Item> itemRegistry = ForgeRegistries.ITEMS;
        if (villagerItems != null) {
            for(ResourceLocation i: villagerItems.getEntries()) {
                shareableItems.add(itemRegistry.getValue(i));
            }
        }

        if (villagerProfessionItems != null) {
            for(ResourceLocation i: villagerProfessionItems.getEntries()) {
                shareableItems.add(itemRegistry.getValue(i));
            }
        }

        if (targetProfessionItems != null) {
            for(ResourceLocation i: targetProfessionItems.getEntries()) {
                shareableItems.add(itemRegistry.getValue(i));
            }
        }

        return shareableItems;
    }

    private ModTag getProfessionItems(VillagerProfession profession) {
        //  Fix for modded professions having namespace in their name.
        String name = profession.toString();
        if (name.contains(":")) {
            name = name.split(":")[0];
        }

        return mItemTagDataManager.getEntry("docwheat:villager_" + name + "_items");
    }
}
