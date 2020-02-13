package com.bokmcdok.wheat.tag;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTagUtils {
    private static ModTagDataManager ITEM_TAG_MANAGER = new ModTagDataManager();
    private static ModTagDataManager BLOCK_TAG_MANAGER = new ModTagDataManager();

    /**
     * Load all the tags.
     * @param event The common setup event
     */
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event)
    {
        ITEM_TAG_MANAGER.loadDataEntries("tags/items");
        BLOCK_TAG_MANAGER.loadDataEntries("tags/blocks");
    }

    /**
     * Get an item tag.
     * @param location The location of the tag.
     * @return The list of values associated with the tag.
     */
    public static ModTag getItemTag(String location) {
        return ITEM_TAG_MANAGER.getEntry(new ResourceLocation(location));
    }

    /**
     * Get a block tag.
     * @param location The location of the tag.
     * @return The list of values associated with the tag.
     */
    public static ModTag getBlockTag(String location) {
        return BLOCK_TAG_MANAGER.getEntry(new ResourceLocation(location));
    }
}
