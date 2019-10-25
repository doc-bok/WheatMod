package com.bokmcdok.wheat.Recipe;

import com.bokmcdok.wheat.Block.ModBlocks;
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
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(IInventory inv, @Nonnull World worldIn) {
        return this.ingredient.test(inv.getStackInSlot(0));
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull IInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return result;
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return id;
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.flour_mill;
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return type;
    }

    @Override
    @Nonnull
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    @Override
    @Nonnull
    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.flour_mill);
    }

    public static final IRecipeType<FlourMillRecipe> flour_mill = IRecipeType.register("flour_mill");

    private final IRecipeType<?> type;
    private final ResourceLocation id;
    final String group;
    final Ingredient ingredient;
    final ItemStack result;
}