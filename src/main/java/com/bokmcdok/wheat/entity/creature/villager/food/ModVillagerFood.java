package com.bokmcdok.wheat.entity.creature.villager.food;

import com.bokmcdok.wheat.data.ModIngredientSupplier;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Method;
import java.util.Map;

public class ModVillagerFood {
    private final Map<ResourceLocation, Integer> mFoodHungerValues;
    private final LazyValue<Ingredient> mFoodItems;

    /**
     * Construction
     * @param foodHungerValues The food hunger files loaded into the builder.
     */
    public ModVillagerFood(Map<ResourceLocation, Integer> foodHungerValues) {
        mFoodHungerValues = foodHungerValues;
        ModIngredientSupplier supplier = new ModIngredientSupplier(mFoodHungerValues.keySet().toArray(new ResourceLocation[0]));
        mFoodItems = new LazyValue<>(supplier);
    }

    /**
     * Check if the item stack contains food items.
     * @param stack The item stack.
     * @return TRUE if this is a food item.
     */
    public boolean isFoodItem(ItemStack stack) {
        return mFoodItems.getValue().test(stack);
    }

    /**
     * Get the hunger restored by a food item.
     * @param stack The item stack.
     * @return The hunger gained if the item is a food item.
     */
    public int getHunger(ItemStack stack) {
        if (isFoodItem(stack)) {
            return mFoodHungerValues.get(stack.getItem().getRegistryName());
        }

        return 0;
    }

    /**
     * Check if a villager can breed.
     * @param villager The villager to check.
     * @return TRUE if the villager can breed.
     */
    public boolean canBreed(VillagerEntity villager) {

        return getFoodLevel(villager) + getFoodValueFromInventory(villager) >= 12 && villager.getGrowingAge() == 0;
    }

    /**
     * Consume food for breeding
     * @param villager The villager consuming food.
     */
    public void consumeFood(VillagerEntity villager) {
        if (getFoodLevel(villager) < 12 && getFoodValueFromInventory(villager) != 0) {
            for(int i = 0; i < villager.getVillagerInventory().getSizeInventory(); ++i) {
                ItemStack stack = villager.getVillagerInventory().getStackInSlot(i);
                if (!stack.isEmpty()) {
                    int hunger = getHunger(stack);
                    if (hunger != 0) {
                        int j = stack.getCount();

                        for(int k = j; k > 0; --k) {
                            modifyFoodLevel(villager, hunger);
                            villager.getVillagerInventory().decrStackSize(i, 1);
                            if (getFoodLevel(villager) >= 12) {
                                return;
                            }
                        }
                    }
                }
            }
        }

        modifyFoodLevel(villager, -12);
    }

    /**
     * Get the total value of food in the villager's inventory.
     * @param villager The villager to check.
     * @return The total hunger value of all food in the inventory.
     */
    private int getFoodValueFromInventory(VillagerEntity villager) {
        IForgeRegistry<Item> itemRegistry = ForgeRegistries.ITEMS;

        Inventory inventory = villager.getVillagerInventory();
        return mFoodHungerValues.entrySet().stream().mapToInt((x) -> {
            Item item = itemRegistry.getValue(x.getKey());
            return inventory.count(item) * x.getValue();
        }).sum();
    }

    /**
     * Get the current food level of the villager.
     * @param villager The villager to get the food level for.
     * @return The current food level of the villager.
     */
    private int getFoodLevel(VillagerEntity villager) {
        CompoundNBT compoundNBT = villager.writeWithoutTypeId(new CompoundNBT());
        return compoundNBT.getByte("FoodLevel");
    }

    /**
     * Modify the food level of the villager.
     * @param villager The villager to modify.
     * @param mod How much to modify the food level.
     */
    private void modifyFoodLevel(VillagerEntity villager, int mod) {
        try {
            Method decrFoodLevel = ObfuscationReflectionHelper.findMethod(VillagerEntity.class, "decrFoodLevel", int.class);
            decrFoodLevel.invoke(villager,mod * -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
