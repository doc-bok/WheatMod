package com.bokmcdok.wheat.data;

import com.bokmcdok.wheat.item.ModItem;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.util.Map.Entry;

import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class ModItemManager extends JsonReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    private Set<Item> mItems = ImmutableSet.of();

    private enum ItemType {
        ITEM,
        FOOD,
        DURABLE,
        DURABLE_BUCKET,
        BOWL_FOOD,
        BLOCK_NAMED,
        BLOCK,
        STONE_BOWL_FOOD,
        STONE
    }

    public ModItemManager() { super(GSON, "items"); }

    public Item[] getItems() {
        return mItems.toArray(new Item[0]);
    }

    public void apply(Map<ResourceLocation, JsonObject> splashList, IResourceManager resourceManager, IProfiler profiler) {
        Map<ResourceLocation, JsonObject> itemResources = prepare(resourceManager, profiler);

        mItems = Sets.newHashSet();

        for(Entry<ResourceLocation, JsonObject> entry : itemResources.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_")) { continue; }

            try {
                Item item = deserializeItem(entry.getValue());
                if (item == null) {
                    LOGGER.info("Skipping loading item {} as it's serializer returned null", resourceLocation);
                    continue;
                }

                mItems.add(item);

            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading item {}", resourceLocation, exception);
            }
        }

        LOGGER.info("Loaded {} items", mItems.size());
    }

    private Item deserializeItem(JsonObject json) {
    String typeValue = JSONUtils.getString(json, "type");
        ItemType type = ItemType.valueOf(typeValue.toUpperCase());

        switch (type) {
            case ITEM:
                return deserializeBasicItem(json);

            default:
                LOGGER.info("Item type {} not supported", typeValue);
                return null;
        }
    }

    private Item deserializeBasicItem(JsonObject json) {
        String itemGroup = JSONUtils.getString(json, "itemGroup");
        String registryName = JSONUtils.getString(json, "name");

        ItemGroup group = getItemGroup(itemGroup);

        return new ModItem(group, registryName);
    }

    private ItemGroup getItemGroup(String name) {
        for (ItemGroup group : ItemGroup.GROUPS) {
            if (group.getTabLabel().equals(name)) {
                return group;
            }
        }

        throw new IllegalArgumentException("Invalid Item Group:" + name);
    }
}
