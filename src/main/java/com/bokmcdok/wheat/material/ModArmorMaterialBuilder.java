package com.bokmcdok.wheat.material;

import com.bokmcdok.wheat.data.ModIngredientSupplier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class ModArmorMaterialBuilder {
    private String mName;
    private int mDurability;
    private int[] mDefenseRatings = new int[4];
    private int mEnchantability;
    private SoundEvent mEquipSound;
    private float mToughness;
    private ModIngredientSupplier mRepairItem;

    /**
     * Set the name of the material.
     * @param value The value to set.
     */
    public void setName(String value) {
        mName = value;
    }

    /**
     * Set the durability of the material.
     * @param value The value to set.
     */
    public void setDurability(int value) {
        mDurability = value;
    }

    /**
     * Set the defense rating of the material.
     * @param slot The slot to set the defense rating for.
     * @param value The value to set.
     */
    public void setDefenseRating(EquipmentSlotType slot, int value) {
        mDefenseRatings[slot.getIndex()] = value;
    }

    /**
     * Set the enchantability of the material.
     * @param value The value to set.
     */
    public void setEnchantability(int value) {
        mEnchantability = value;
    }

    /**
     * Set the sound the material makes when it is equipped.
     * @param value The value to set.
     */
    public void setEquipSound(SoundEvent value) {
        mEquipSound = value;
    }

    /**
     * Set the toughness of the material.
     * @param value The value to set.
     */
    public void setToughness(float value) {
        mToughness = value;
    }

    /**
     * Set the repair item of the material.
     * @param registryName The resource location of the item.
     */
    public void setRepairItem(ResourceLocation registryName) {
        mRepairItem = new ModIngredientSupplier(registryName);
    }

    /**
     * Build the Armor Material.
     * @return A new Armor Material.
     */
    public ModArmorMaterial build() {
        return new ModArmorMaterial(mName, mDurability, mDefenseRatings, mEnchantability, mEquipSound, mToughness, mRepairItem);
    }
}
