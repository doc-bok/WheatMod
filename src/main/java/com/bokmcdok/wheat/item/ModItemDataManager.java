package com.bokmcdok.wheat.item;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.data.ModDataManager;
import com.bokmcdok.wheat.data.ModEffectManager;
import com.bokmcdok.wheat.material.ModArmorMaterialManager;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Food;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

public class ModItemDataManager extends ModDataManager<IModItem> {
    private static final String ITEMS_FOLDER = "items";
    private static final String CONTAINERS_FOLDER = "containers";
    private static final String SPAWN_EGGS_FOLDER = "spawn_eggs";
    private ModEffectManager mEffectManager = new ModEffectManager();
    private ModArmorMaterialManager mArmorMaterialManager = new ModArmorMaterialManager();

    private enum ItemType {
        ITEM,
        BLOCK,
        BLOCK_NAMED,
        SPAWN_EGG,
        ARMOR,
        WEAPON
    }

    /**
     * Get the entries as a list of ModItems.
     * @return And array of items loaded by this data manager.
     */
    public IModItem[] getItems() {
        return getAllEntries().toArray(new IModItem[0]);
    }

    /**
     * Get an array
     * @return An array of items to register with Forge.
     */
    public Item[] getAsItems() {
        List<IModItem> values = new ArrayList(getAllEntries());
        List<Item> converted = Lists.transform(values, IModItem::asItem);
        return converted.toArray(new Item[0]);
    }

    /**
     * Load all the items from their data files.
     */
    public void loadItems() {
        //  Load effects
        mArmorMaterialManager.loadMaterials();
        mEffectManager.loadEffects();

        //  Load container items
        loadDataEntries(CONTAINERS_FOLDER);

        //  Load other items
        loadDataEntries(ITEMS_FOLDER);
    }

    /**
     * Load the spawn eggs.
     */
    public void loadSpawnEggs() {
        loadDataEntries(SPAWN_EGGS_FOLDER);
    }

    /**
     * Convert a JSON file to a Minecraft Item.
     * @param json The JSON data from the file.
     * @return A new instance of the item.
     */
    protected IModItem deserialize(ResourceLocation location, JsonObject json) {
        ModItemImpl.ModItemProperties properties = new ModItemImpl.ModItemProperties();

        setInt(properties, json, "max_stack_size", Item.Properties::maxStackSize);
        setInt(properties, json, "default_max_damage", Item.Properties::defaultMaxDamage);
        setInt(properties, json, "max_damage", Item.Properties::maxDamage);
        setString(properties, json, "group", (x, value) -> x.group(getItemGroup(value)));
        setString(properties, json, "rarity", (x, value) -> x.rarity(Rarity.valueOf(value)));
        setIfFalse(properties, json, "repairable", Item.Properties::setNoRepair);
        setString(properties, json, "container", (x, value) -> x.containerItem(getContainerItem(value)));
        setFloat(properties, json, "compost_chance", ModItemImpl.ModItemProperties::compostChance);
        setString(properties, json, "spell", (x, value) -> x.spell(WheatMod.SPELL_REGISTRAR.getSpell(value)));
        setString(properties, json, "on_damaged_effect", ModItemImpl.ModItemProperties::onDamagedEffect);
        setInt(properties, json, "on_damaged_effect_duration", ModItemImpl.ModItemProperties::onDamagedEffectDuration);
        setInt(properties, json, "on_damaged_effect_amplifier", ModItemImpl.ModItemProperties::onDamagedEffectAmplifier);
        setString(properties, json, "armor_texture", ModItemImpl.ModItemProperties::armorTexture);
        setObjectArray(properties, json, "enchantments", this::parseEnchantments);

        deserializeTools(json, properties);
        deserializeFood(json, properties);
        deserializeThrowing(json, properties);
        deserializeColor(json, properties);
        deserializeWeapon(json, properties);

        //  Create the right item type
        String typeValue = JSONUtils.getString(json, "type");
        ItemType type = ItemType.valueOf(typeValue.toUpperCase());

        IModItem result = null;
        switch (type) {
            case ITEM:
                result = new ModItem(properties);
                break;

            case BLOCK: {
                if (!JSONUtils.hasField(json, "block")) {
                    LOGGER.error("Failed to load {}: block items need 'block' attribute", location.toString());
                    return null;
                }

                String blockName = JSONUtils.getString(json, "block");
                result = new ModBlockItem(getBlock(blockName), properties);
                break;
            }

            case BLOCK_NAMED: {
                if (!JSONUtils.hasField(json, "block")) {
                    LOGGER.error("Failed to load {}: named block items need 'block' attribute", location.toString());
                    return null;
                }

                String blockName = JSONUtils.getString(json, "block");
                result = new ModBlockNamedItem(getBlock(blockName), properties);
                break;
            }

            case SPAWN_EGG: {
                if (!JSONUtils.hasField(json, "entity") ||
                        !JSONUtils.hasField(json, "weight") ||
                        !JSONUtils.hasField(json,"secondary_color")) {
                    LOGGER.error("Failed to load {}: spawn eggs need 'entity', 'primary_color', and 'secondary_color' attributes", location.toString());
                    return null;
                }

                String entityName = JSONUtils.getString(json, "entity");

                Optional<EntityType<?>> entityType = Registry.ENTITY_TYPE.getValue(new ResourceLocation(entityName));
                if (entityType.isPresent()) {
                    JsonObject primaryColorJson = JSONUtils.getJsonObject(json, "primary_color");
                    JsonObject secondaryColorJson = JSONUtils.getJsonObject(json, "secondary_color");
                    int primaryColor = deserializeColor(primaryColorJson);
                    int secondaryColor = deserializeColor(secondaryColorJson);

                    result = new ModSpawnEggItem(entityType.get(), primaryColor, secondaryColor, properties);
                }

                break;
            }

            case ARMOR: {
                if (!JSONUtils.hasField(json, "armor_material") ||
                    !JSONUtils.hasField(json, "armor_slot")) {
                    LOGGER.error("Failed to load {}: armour needs 'armor_material' and 'armor_slot' attributes", location.toString());
                    return null;
                }

                String armorMaterial = JSONUtils.getString(json,"armor_material");
                String armorSlot = JSONUtils.getString(json, "armor_slot");
                IArmorMaterial material = mArmorMaterialManager.getEntry(armorMaterial);
                EquipmentSlotType slot = EquipmentSlotType.valueOf(armorSlot.toUpperCase());
                result = new ModArmorItem(material, slot, properties);
                break;
            }

            case WEAPON: {
                if (!JSONUtils.hasField(json, "damage") ||
                    !JSONUtils.hasField(json, "weight") ||
                    !JSONUtils.hasField(json,"tier")) {
                    LOGGER.error("Failed to load {}: weapons need 'damage', 'weight', and 'tier' attributes", location.toString());
                    return null;
                }

                float attackDamage = JSONUtils.getFloat(json, "damage");
                float weight = JSONUtils.getFloat(json, "weight");
                String tierName = JSONUtils.getString(json, "tier").toUpperCase();
                IItemTier tier = ItemTier.valueOf(tierName);
                result = new ModWeaponItem(tier, attackDamage, weight, properties);
                break;
            }

            default:
                LOGGER.error("Item type {} not supported", typeValue);
                return null;
        }

        result.asItem().setRegistryName(location);
        return result;
    }

    /**
     * Deserialize tool properties on items.
     * @param json The JSON object from the file
     * @param properties The properties to set.
     */
    private void deserializeTools(JsonObject json, ModItemImpl.ModItemProperties properties) {
        if (JSONUtils.hasField(json, "tool_types")) {
            JsonArray toolTypes = JSONUtils.getJsonArray(json, "tool_types");
            for (JsonElement tool : toolTypes) {
                JsonObject toolAsJsonObject = tool.getAsJsonObject();
                String type = JSONUtils.getString(toolAsJsonObject, "tool_type");
                int level = JSONUtils.getInt(toolAsJsonObject, "level");

                ToolType toolType = ToolType.get(type);
                properties.addToolType(toolType, level);
            }
        }
    }

    /**
     * Convert a JSON object to a Food instance.
     * @param json The JSON object from the item file.
     */
    private void deserializeFood(JsonObject json, ModItemImpl.ModItemProperties properties) {
        if (JSONUtils.hasField(json, "food")) {
            JsonObject food = JSONUtils.getJsonObject(json, "food");
            Food.Builder builder = new Food.Builder();

            setInt(builder, food, "hunger", Food.Builder::hunger);
            setFloat(builder, food, "saturation", Food.Builder::saturation);
            setIfTrue(builder, food, "meat", Food.Builder::meat);
            setIfTrue(builder, food, "always_edible", Food.Builder::setAlwaysEdible);
            setIfTrue(builder, food, "fast_to_eat", Food.Builder::fastToEat);
            setObjectArray(builder, food, "effects", (x, effect) -> {
                JsonObject effectAsJsonObject = effect.getAsJsonObject();

                String effectName = JSONUtils.getString(effectAsJsonObject, "effect_type");

                float probability = 1.0f;
                if (JSONUtils.hasField(effectAsJsonObject, "probability")) {
                    probability = JSONUtils.getFloat(effectAsJsonObject, "probability");
                }

                EffectInstance effectInstance = mEffectManager.getEntry(effectName);
                if (effectInstance != null) {
                    x.effect(effectInstance, probability);
                }
            });

            properties.food(builder.build());
        }
    }

    /**
     * Deserialize throwing properties on items.
     * @param json The JSON object from the file
     * @param properties The properties to set.
     */
    private void deserializeThrowing(JsonObject json, ModItemImpl.ModItemProperties properties) {
        if (JSONUtils.hasField(json, "throwing")) {
            JsonObject throwing = JSONUtils.getJsonObject(json, "throwing");
            float velocity = JSONUtils.getFloat(throwing, "velocity");
            float offset = JSONUtils.getFloat(throwing, "offset");
            float inaccuracy = JSONUtils.getFloat(throwing, "inaccuracy");

            properties.throwing(offset, velocity, inaccuracy);

            if (JSONUtils.hasField(throwing, "sound")) {
                String sound = JSONUtils.getString(throwing, "sound");
                float volume = 0.5f;
                float pitch = 0.4f;

                if (JSONUtils.hasField(throwing, "volume")) {
                    volume = JSONUtils.getFloat(throwing, "volume");
                }

                if (JSONUtils.hasField(throwing, "pitch")) {
                    pitch = JSONUtils.getFloat(throwing, "pitch");
                }

                Optional<SoundEvent> event = Registry.SOUND_EVENT.getValue(new ResourceLocation(sound));
                if (event.isPresent()) {
                    properties.throwingSound(event.get(), volume, pitch);
                }
            }
        }
    }

    /**
     * Deserialize the color of an item
     * @param json The JSON object from the file
     * @param properties The properties to set.
     */
    private void deserializeColor(JsonObject json, ModItemImpl.ModItemProperties properties) {
        int color = deserializeColor(json);
        if (color != -1) {
            properties.color((item, state) -> deserializeColor(json));
        }
    }

    /**
     * Read weapon properties, if any.
     * @param json The JSON object from the file
     * @param properties The properties to set.
     */
    private void deserializeWeapon(JsonObject json, ModItemImpl.ModItemProperties properties) {
        setFloat(properties, json, "damage", ModItemImpl.ModItemProperties::setAttackDamage);
        setFloat(properties, json, "weight", ModItemImpl.ModItemProperties::setWeight);

        if (JSONUtils.hasField(json, "damage_type")) {
            String damageType = JSONUtils.getString(json, "damage_type").toUpperCase();
            properties.setDamageType(ModDamageType.valueOf(damageType));
        }

        if (JSONUtils.hasField(json, "tier")) {
            String tier = JSONUtils.getString(json, "tier").toUpperCase();
            properties.setItemTier(ItemTier.valueOf(tier));
        }

        setObjectArray(properties, json, "traits", (x, trait) -> {
            JsonObject traitObject = trait.getAsJsonObject();

            if (JSONUtils.hasField(json, "name")) {
                String traitName = JSONUtils.getString(traitObject, "name").toUpperCase();
                float value = 0f;
                if (JSONUtils.hasField(traitObject, "value")) {
                    value = JSONUtils.getFloat(traitObject, "value");
                }

                properties.addTrait(ModItemTrait.valueOf(traitName), value);
            }
        });
    }

    /**
     * Get the Item Group for an item.
     * @param name The string name of an ItemGroup.
     * @return The instance of an ItemGroup.
     */
    private ItemGroup getItemGroup(String name) {
        for (ItemGroup group : ItemGroup.GROUPS) {
            if (group.getTabLabel().equals(name)) {
                return group;
            }
        }

        throw new IllegalArgumentException("Invalid Item Group:" + name);
    }

    /**
     * Get the container for an item.
     * @param containerItemName The name of the container.
     * @return The instance of the container item.
     */
    private Item getContainerItem(String containerItemName) {
        ResourceLocation location = new ResourceLocation(containerItemName);
        if ("minecraft".equals(location.getNamespace())) {
            return Registry.ITEM.getOrDefault(location);
        }

        return getEntry(location).asItem();
    }

    /**
     * Parse default enchantment(s) that are applied to this item.
     * @param properties The item properties being parsed.
     * @param object The JSON object containing the enchantment data.
     */
    private void parseEnchantments(ModItemImpl.ModItemProperties properties, JsonObject object) {
        if (JSONUtils.hasField(object, "type") &&
            JSONUtils.hasField(object, "level")) {
            String type = JSONUtils.getString(object, "type");
            int level = JSONUtils.getInt(object, "level");
            properties.addEnchantment(type, level);
        }
    }
}
