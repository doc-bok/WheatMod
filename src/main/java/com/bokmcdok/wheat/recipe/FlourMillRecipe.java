package com.bokmcdok.wheat.recipe;

import com.bokmcdok.wheat.block.ModBlockUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class FlourMillRecipe implements IRecipe<IInventory> {

    /**
     * Construction
     * @param resourceLocation The location of this recipe
     * @param group The group this recipe belongs to
     * @param ingredient The ingredient for this recipe
     * @param result The output of this recipe
     */
    FlourMillRecipe(ResourceLocation resourceLocation, String group, Ingredient ingredient, ItemStack result) {
        type = flour_mill;
        id = resourceLocation;
        mGroup = group;
        mIngredient = ingredient;
        mResult = result;
    }

    /**
     * Check the inventory
     * @param inventory The inventory (crafting grid) to check
     * @param worldIn The current world
     * @return True if the inventory matches the recipe
     */
    @Override
    public boolean matches(IInventory inventory, @Nonnull World worldIn) {
        return mIngredient.test(inventory.getStackInSlot(0));
    }

    /**
     * Get the result of crafting this recipe
     * @param inventory The inventory of the crafting grid
     * @return The item created by this recipe
     */
    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull IInventory inventory) {
        return mResult.copy();
    }

    /**
     * Check if the recipe can fit in the specified grid
     * @param width The with of the grid
     * @param height The height of the grid
     * @return Always returns True
     */
    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    /**
     * Get the output of this recipe (without creating a new one)
     * @return The output of this recipe
     */
    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return mResult;
    }

    /**
     * Get the recipe's ID
     * @return The resource location of the recipe
     */
    @Override
    @Nonnull
    public ResourceLocation getId() {
        return id;
    }

    /**
     * Get the recipe serializer
     * @return The Flour Mill Serializer
     */
    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.flour_mill;
    }

    /**
     * Get the type of this recipe
     * @return Flour Mill Type
     */
    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return type;
    }

    /**
     * Get the ingredients for this recipe
     * @return The list of ingredients
     */
    @Override
    @Nonnull
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(mIngredient);
        return nonnulllist;
    }

    /**
     * Get the icon for this recipe
     * @return The flour mill item
     */
    @Override
    @Nonnull
    public ItemStack getIcon() {
        return new ItemStack(ModBlockUtils.flour_mill);
    }

    public static final IRecipeType<FlourMillRecipe> flour_mill = IRecipeType.register("flour_mill");

    private final IRecipeType<?> type;
    private final ResourceLocation id;
    final String mGroup;
    final Ingredient mIngredient;
    final ItemStack mResult;
}