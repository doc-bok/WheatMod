package com.bokmcdok.wheat.material;

import com.bokmcdok.wheat.data.ModDataManager;
import com.google.gson.JsonObject;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.util.ResourceLocation;

public class ModArmorMaterialManager extends ModDataManager<IArmorMaterial> {
    private static final String ARMOR_MATERIALS_FOLDER = "armor_materials";

    /**
     * Load materials and add default entries for vanilla materials.
     */
    public void loadMaterials() {
        loadDataEntries(ARMOR_MATERIALS_FOLDER);
        addCustomEntry("minecraft:chain", ArmorMaterial.CHAIN);
        addCustomEntry("minecraft:diamond", ArmorMaterial.DIAMOND);
        addCustomEntry("minecraft:gold", ArmorMaterial.GOLD);
        addCustomEntry("minecraft:iron", ArmorMaterial.IRON);
        addCustomEntry("minecraft:leather", ArmorMaterial.LEATHER);
        addCustomEntry("minecraft:turtle", ArmorMaterial.TURTLE);
    }

    /**
     * Deserialize an armor material.
     * @param location The location of the resource.
     * @param json The JSON data to parse.
     * @return A new Armor Material.
     */
    @Override
    protected IArmorMaterial deserialize(ResourceLocation location, JsonObject json) {
        ModArmorMaterialBuilder builder = new ModArmorMaterialBuilder();
        builder.setName(location.getNamespace());
        setInt(builder, json, "durability", ModArmorMaterialBuilder::setDurability);
        setInt(builder, json, "feet_defense", (x, value) -> x.setDefenseRating(EquipmentSlotType.FEET, value));
        setInt(builder, json, "legs_defense", (x, value) -> x.setDefenseRating(EquipmentSlotType.LEGS, value));
        setInt(builder, json, "chest_defense", (x, value) -> x.setDefenseRating(EquipmentSlotType.CHEST, value));
        setInt(builder, json, "head_defense", (x, value) -> x.setDefenseRating(EquipmentSlotType.HEAD, value));
        setInt(builder, json, "enchantability", ModArmorMaterialBuilder::setEnchantability);
        setSoundEvent(builder, json, "equip_sound", ModArmorMaterialBuilder::setEquipSound);
        setFloat(builder, json, "toughness", ModArmorMaterialBuilder::setToughness);
        setResourceLocation(builder, json, "repair_item", ModArmorMaterialBuilder::setRepairItem);
        return  builder.build();
    }
}
