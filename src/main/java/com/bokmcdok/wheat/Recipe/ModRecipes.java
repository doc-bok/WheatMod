package com.bokmcdok.wheat.Recipe;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Provides help with custom mod recipes.
 */
@ObjectHolder(WheatMod.MOD_ID)
@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipes {

    /**
     * Register new recipe serialisers
      * @param event The registry event for recipe serialisers
     */
    @SubscribeEvent
    public static void registerRecipes(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
        CraftingHelper.register(new ResourceLocation(WheatMod.MOD_ID, "potion"), PotionSerializer.INSTANCE);
        event.getRegistry().register(new PotionRecipe.Serializer().setRegistryName("crafting_potion"));
        event.getRegistry().register(new FlourMillRecipeSerializer<>(FlourMillRecipe::new).setRegistryName("flour_mill"));
    }

    public static final IRecipeSerializer<PotionRecipe> POTION = null;
    public static final FlourMillRecipeSerializer<FlourMillRecipe> flour_mill = null;
}
