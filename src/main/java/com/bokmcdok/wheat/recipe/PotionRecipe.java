package com.bokmcdok.wheat.recipe;

import com.bokmcdok.wheat.WheatMod;
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
    static int MAX_WIDTH = 3;
    static int MAX_HEIGHT = 3;

    public PotionRecipe(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
    }

    public boolean matches(CraftingInventory inv, World worldIn) {
        RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
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

    //  Get the remaining items (i.e. return any glass bottles)
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

    //  Serialize the recipe.
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.POTION;
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PotionRecipe> {
        private static final ResourceLocation NAME = new ResourceLocation(WheatMod.MOD_ID, "crafting_potion");
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
