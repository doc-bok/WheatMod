package com.bokmcdok.wheat.material;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public class ModArmorMaterial implements IArmorMaterial {
    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private final String mName;
    private final int mDurability;
    private final int[] mDefenseRatings;
    private final int mEnchantability;
    private final SoundEvent mEquipSound;
    private final float mToughness;
    private final LazyValue<Ingredient> mRepairItem;

    /**
     * Create a new armor material.
     * @param name The name of this material.
     * @param durability The durability of this material
     * @param defenseRatings The defense ratings af the armor by type.
     * @param enchantability How enchantable the armor is.
     * @param equipSound The sound made when the armor is equipped.
     * @param toughness Bonus protection the material provides.
     * @param repairItem The item used to repair this material.
     */
    public ModArmorMaterial(String name, int durability, int[] defenseRatings, int enchantability, SoundEvent equipSound, float toughness, Supplier<Ingredient> repairItem) {
        mName = name;
        mDurability = durability;
        mDefenseRatings = defenseRatings;
        mEnchantability = enchantability;
        mEquipSound = equipSound;
        mToughness = toughness;
        mRepairItem = new LazyValue<>(repairItem);
    }

    /**
     * Get the durability of the armor.
     * @param slot The item slot the armor is equipped to.
     * @return The durability.
     */
    public int getDurability(EquipmentSlotType slot) {
        return MAX_DAMAGE_ARRAY[slot.getIndex()] * mDurability;
    }

    /**
     * Get the damage reduction of the armor.
     * @param slot The item slot the armor is equipped to.
     * @return The damage reduction.
     */
    public int getDamageReductionAmount(EquipmentSlotType slot) {
        return mDefenseRatings[slot.getIndex()];
    }

    /**
     * Get the enchantability of the armor.
     * @return The enchantability.
     */
    public int getEnchantability() {
        return mEnchantability;
    }

    /**
     * Get the sound to play when the armor is equipped.
     * @return The sound event.
     */
    @Override
    public SoundEvent getSoundEvent() {
        return mEquipSound;
    }

    /**
     * Get the item used to repair the armor.
     * @return An ingredient used to repair.
     */
    public Ingredient getRepairMaterial() {
        return mRepairItem.getValue();
    }

    /**
     * Get the name of the material.
     * @return A string name for the material.
     */
    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return mName;
    }

    /**
     * Get the armor's toughness.
     * @return The toughness.
     */
    public float getToughness() {
        return mToughness;
    }
}
