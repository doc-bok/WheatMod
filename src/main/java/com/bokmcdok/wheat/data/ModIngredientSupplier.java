package com.bokmcdok.wheat.data;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.List;
import java.util.function.Supplier;

public class ModIngredientSupplier implements Supplier<Ingredient> {
    private final ResourceLocation mRegistryName[];

    /**
     * Constrution
     * @param registryName The registry name of the item.
     */
    public ModIngredientSupplier(ResourceLocation ... registryName) {
        mRegistryName = registryName;
    }

    /**
     * Get the ingredient.
     * @return The ingredient based on the item registry name.
     */
    @Override
    public Ingredient get() {
        IForgeRegistry<Item> itemRegistry = RegistryManager.ACTIVE.getRegistry(GameData.ITEMS);
        List<Item> items = Lists.newArrayList();
        for (ResourceLocation i: mRegistryName) {
            Item item = itemRegistry.getValue(i);
            if (item != null && item != Items.AIR) {
                items.add(item);
            }
        }

        return Ingredient.fromItems(items.toArray(new Item[0]));
    }
}
