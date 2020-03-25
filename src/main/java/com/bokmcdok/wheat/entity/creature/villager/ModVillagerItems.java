package com.bokmcdok.wheat.entity.creature.villager;

import com.bokmcdok.wheat.entity.creature.villager.crops.ModVillagerCrops;
import com.bokmcdok.wheat.entity.creature.villager.food.ModVillagerFood;
import com.bokmcdok.wheat.tag.ModTag;
import com.bokmcdok.wheat.tag.ModTagRegistrar;
import com.google.common.collect.Sets;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Set;

public class ModVillagerItems {
    private final ModTagRegistrar mTagRegistrar;
    private final ModVillagerFood mVillagerFood;
    private final ModVillagerCrops mVillagerCrops;

    /**
     * Construction
     * @param tagRegistrar The item tag data manager.
     * @param food The villager food instance.
     */
    public ModVillagerItems(ModTagRegistrar tagRegistrar, ModVillagerFood food, ModVillagerCrops crops) {
        mTagRegistrar = tagRegistrar;
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

        ModTag tag = mTagRegistrar.getItemTag("docwheat:villager_items");
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

    /**
     * Get a list of shareable items.
     * @param villager The villager.
     * @param interactionTarget The target .
     * @return A list of items that can be shared.
     */
    public Set<Item> getSharableItems(VillagerEntity villager, VillagerEntity interactionTarget) {
        Ingredient seeds = mVillagerCrops.getSeedItems();

        ModTag villagerItems = mTagRegistrar.getItemTag("docwheat:villager_items");
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

    /**
     * Get profession-specific items.
     * @param profession The profession to check.
     * @return A Tag with registry names.
     */
    private ModTag getProfessionItems(VillagerProfession profession) {
        //  Fix for modded professions having namespace in their name.
        String name = profession.toString();
        if (name.contains(":")) {
            name = name.split(":")[0];
        }

        return mTagRegistrar.getItemTag("docwheat:villager_" + name + "_items");
    }
}
