package com.bokmcdok.wheat.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class PotionRecipe extends ShapelessRecipe {
    private static int MAX_WIDTH = 3;
    private static int MAX_HEIGHT = 3;

    /**
     * Construction
     * @param idIn The resource location of this recipe
     * @param groupIn The group for the recipe book
     * @param recipeOutputIn The Output of the recipe
     * @param recipeItemsIn The ingredients for the recipe
     */
    public PotionRecipe(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
    }

    /**
     * Check the inventory matches the recipe
     * @param inv The crafting grid to check
     * @param worldIn The current world
     * @return True if the recipe matches
     */
    public boolean matches(CraftingInventory inv, World worldIn) {
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();

        for(int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (!itemstack.isEmpty()) {
                inputs.add(itemstack);
            }
        }

        NonNullList<Ingredient> recipeItems = getIngredients();
        if (inputs.size() == recipeItems.size()) {
            for (ItemStack i: inputs) {

                boolean found = false;
                for (Ingredient j: recipeItems) {
                      if (i.getItem() == Items.POTION) {
                          for (ItemStack k: j.getMatchingStacks()) {
                              if (k.getItem() == Items.POTION && PotionUtils.getPotionFromItem(i) == PotionUtils.getPotionFromItem(k)) {
                                  found = true;
                                  break;
                              }
                          }
                      } else if (j.test(i)) {
                          found = true;
                          break;
                      }
                }

                if (!found) { return false; }
            }

            return true;
        }

        return false;
    }

    /**
     * Get the remaining items (i.e. return any glass bottles)
     * @param inv The crafting grid
     * @return The remaining items, which includes glass bottles
     */
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == Items.POTION) {
                    nonnulllist.set(i, new ItemStack(Items.GLASS_BOTTLE, 1));
                } else if (stack.hasContainerItem()) {
                    nonnulllist.set(i, stack.getContainerItem());
                }
            }
        }

        return nonnulllist;
    }

    /**
     * Serialize the recipe.
     * @return The serialised recipe
     */
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.POTION;
    }

    /**
     * The recipe serializer
     */
    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PotionRecipe> {

        /**
         * Read the recipe from a JSON string
         * @param recipeId The ID of the recipe
         * @param json The JSON data
         * @return A new instance of the recipe
         */
        public PotionRecipe read(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getString(json, "group", "");
            NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > PotionRecipe.MAX_WIDTH * PotionRecipe.MAX_HEIGHT) {
                throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + (PotionRecipe.MAX_WIDTH * PotionRecipe.MAX_HEIGHT));
            } else {
                ItemStack itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
                return new PotionRecipe(recipeId, s, itemstack, nonnulllist);
            }
        }

        /**
         * Read the ingredients from a JSON array
         * @param jsonArray The Array of ingredients
         * @return A list of ingredients
         */
        private static NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.deserialize(jsonArray.get(i));
                if (!ingredient.hasNoMatchingItems()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        /**
         * Read the receipe from a network buffer
         * @param recipeId The ID of the recipe
         * @param buffer The network buffer
         * @return A new instance of the recipe
         */
        public PotionRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readString(32767);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.read(buffer));
            }

            ItemStack itemstack = buffer.readItemStack();
            return new PotionRecipe(recipeId, s, itemstack, nonnulllist);
        }

        /**
         * Write the recipe to a network buffer
         * @param buffer The buffer to write to
         * @param recipe The recipe to write
         */
        public void write(PacketBuffer buffer, PotionRecipe recipe) {
            NonNullList<Ingredient> recipeItems = recipe.getIngredients();
            buffer.writeString(recipe.getGroup());
            buffer.writeVarInt(recipeItems.size());

            for(Ingredient ingredient : recipeItems) {
                ingredient.write(buffer);
            }

            buffer.writeItemStack(recipe.getRecipeOutput());
        }
    }
}
