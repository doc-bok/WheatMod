package com.bokmcdok.wheat.item;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ModFoodItem extends Item {

    /**
     * Creates a new food item for the mod.
     * @param hunger The amount of hunger restored
     * @param saturation The amount of saturation granted
     * @param meat True if this is a meat item
     * @param raw True if this item is raw
     * @param registryName The name of this food item
     */
    public ModFoodItem(int hunger, float saturation, boolean meat, boolean raw, String registryName) {
        this(hunger, saturation, meat, raw, false, registryName);
    }

    /**
     * Creates a new food item for the mod.
     * @param hunger The amount of hunger restored
     * @param saturation The amount of saturation granted
     * @param meat True if this is a meat item
     * @param raw True if this item is raw
     * @param fastToEat True if this item is fast to eat
     * @param registryName The name of this food item
     */
    public ModFoodItem(int hunger, float saturation, boolean meat, boolean raw, boolean fastToEat, String registryName) {
        this(hunger, saturation, meat, raw, fastToEat, false, registryName);
    }

    /**
     * Creates a new food item for the mod.
     * @param hunger The amount of hunger restored
     * @param saturation The amount of saturation granted
     * @param meat True if this is a meat item
     * @param raw True if this item is raw
     * @param fastToEat True if this item is fast to eat
     * @param alwaysEdible True if this item can be eaten while full
     * @param registryName The name of this food item
     */
    public ModFoodItem(int hunger, float saturation, boolean meat, boolean raw, boolean fastToEat, boolean alwaysEdible, String registryName) {
            super(new Item.Properties().group(ItemGroup.FOOD).food(getFood(hunger, saturation, meat, raw, fastToEat, alwaysEdible)));
        setRegistryName(WheatMod.MOD_ID, registryName);
    }

    /**
     * Helper to create food that can be attached to an item
     * @param hunger The amount of hunger restored
     * @param saturation The amount of saturation granted
     * @param meat True if this is a meat item
     * @param alwaysEdible True if this item can be eaten while full
     * @param fastToEat True if this item is fast to eat
     * @param raw True if this item is raw
     * @return A newly created food instance
     */
    private static Food getFood(int hunger, float saturation, boolean meat, boolean raw, boolean fastToEat, boolean alwaysEdible) {
        Food.Builder builder = new Food.Builder().hunger(hunger).saturation(saturation);
        if (meat) {
            builder.meat();
        }

        if (alwaysEdible) {
            builder.setAlwaysEdible();
        }

        if (fastToEat) {
            builder.fastToEat();
        }

        if (raw) {
            builder.effect(new EffectInstance(Effects.HUNGER, 600, 0), 0.3F);
        }

        return builder.build();
    }
}
