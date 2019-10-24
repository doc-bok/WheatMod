package com.bokmcdok.wheat.Item;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ModBowlFoodItem extends Item {

    public ModBowlFoodItem(int hunger, float saturation, boolean raw, String registryName) {
        super(new Properties().group(ItemGroup.FOOD).food(
                raw ? (new Food.Builder()).hunger(hunger).saturation(saturation).effect(new EffectInstance(Effects.HUNGER, 600, 0), 0.3F).build()
                        : (new Food.Builder()).hunger(hunger).saturation(saturation).build()));
        setRegistryName(WheatMod.MOD_ID, registryName);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(Items.BOWL);
    }
}
