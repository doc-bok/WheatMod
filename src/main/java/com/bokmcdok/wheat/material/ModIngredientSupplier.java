package com.bokmcdok.wheat.material;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.function.Supplier;

public class ModIngredientSupplier implements Supplier<Ingredient> {
    private final ResourceLocation mRegistryName;

    /**
     * Constrution
     * @param registryName The registry name of the item.
     */
    public ModIngredientSupplier(ResourceLocation registryName) {
        mRegistryName = registryName;
    }

    /**
     * Get the ingredient.
     * @return The ingredient based on the item registry name.
     */
    @Override
    public Ingredient get() {
        IForgeRegistry<Item> itemRegistry = RegistryManager.ACTIVE.getRegistry(GameData.ITEMS);
        Item item = itemRegistry.getValue(mRegistryName);
        return Ingredient.fromItems(item);
    }
}
