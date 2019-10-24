package com.bokmcdok.wheat.Recipe;

import com.bokmcdok.wheat.Item.ModItems;
import com.bokmcdok.wheat.WheatMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModItemRecipes {

    /**
     * Handle custom recipe
     */
    @SubscribeEvent
    public static void itemCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
        if (event.getCrafting().getItem() == ModItems.chicken_breast) {
            ItemStack legs = new ItemStack(ModItems.chicken_leg);
            legs.setCount(2);
            event.getPlayer().addItemStackToInventory(legs);

            ItemStack wings = new ItemStack(ModItems.chicken_wing);
            wings.setCount(2);
            event.getPlayer().addItemStackToInventory(wings);
        }
    }
}
