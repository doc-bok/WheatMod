package com.bokmcdok.wheat.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FlourMillRecipeSerializer<T extends FlourMillRecipe> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

    private final FlourMillRecipeSerializer.IFactory<T> mFactory;

    /**
     * Construction
     * @param factory The factory used to create recipes
     */
    FlourMillRecipeSerializer(FlourMillRecipeSerializer.IFactory<T> factory) {
        mFactory = factory;
    }

    /**
     * Read a recipe from a json file
     * @param recipeId The location of the recipe
     * @param json The JSON data
     * @return The newly created recipe
     */
    @Override
    @Nonnull
    public T read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
        String s = JSONUtils.getString(json, "group", "");
        JsonElement jsonelement = JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient");
        Ingredient ingredient = Ingredient.deserialize(jsonelement);
        ItemStack itemstack;

        if (!json.has("result")) {
            throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
        }

        if (json.get("result").isJsonObject()) {
            itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
        } else {
            String s1 = JSONUtils.getString(json, "result");
            ResourceLocation resourcelocation = new ResourceLocation(s1);
            itemstack = new ItemStack(Registry.ITEM.getValue(resourcelocation).orElseThrow(() -> new IllegalStateException("item: " + s1 + " does not exist")));
        }

        return this.mFactory.create(recipeId, s, ingredient, itemstack);
    }

    /**
     * Read a recipe from a buffer
     * @param recipeId The location of the recipe
     * @param buffer The buffer to read from
     * @return The new recipe
     */
    @Nullable
    @Override
    public T read(@Nonnull ResourceLocation recipeId, PacketBuffer buffer) {
        String s = buffer.readString(32767);
        Ingredient ingredient = Ingredient.read(buffer);
        ItemStack itemstack = buffer.readItemStack();

        return this.mFactory.create(recipeId, s, ingredient, itemstack);
    }

    /**
     * Write a recipe to a buffer
     * @param buffer The buffer to write to
     * @param recipe The recipe to write
     */
    @Override
    public void write(PacketBuffer buffer, T recipe) {
        buffer.writeString(recipe.getGroup());
        recipe.getIngredient().write(buffer);
        buffer.writeItemStack(recipe.getRecipeOutput());
    }

    /**
     * The factory for creating a recipe
     * @param <T> The recipe type to create
     */
    interface IFactory<T extends FlourMillRecipe> {
        T create(ResourceLocation resourceLocation, String group, Ingredient ingredient, ItemStack result);
    }
}