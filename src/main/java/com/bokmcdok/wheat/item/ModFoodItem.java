package com.bokmcdok.wheat.item;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ModFoodItem extends Item {

    public ModFoodItem(int hunger, float saturation, boolean raw, String registryName) {
        super(new Item.Properties().group(ItemGroup.FOOD).food(
                raw ? (new Food.Builder()).hunger(hunger).saturation(saturation).effect(new EffectInstance(Effects.HUNGER, 600, 0), 0.3F).build()
                    : (new Food.Builder()).hunger(hunger).saturation(saturation).build()));
        setRegistryName(WheatMod.MOD_ID, registryName);
    }

}
