package com.bokmcdok.wheat.Recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import java.util.stream.Stream;

public class PotionSerializer implements IIngredientSerializer<Ingredient> {
    public static final PotionSerializer INSTANCE  = new PotionSerializer();

    public Ingredient parse(PacketBuffer buffer)
    {
        return Ingredient.fromItemListStream(Stream.generate(() -> new Ingredient.SingleItemList(buffer.readItemStack())).limit(buffer.readVarInt()));
    }

    public Ingredient parse(JsonObject json)
    {
        return Ingredient.fromItemListStream(Stream.of(deserializePotion(json)));
    }

    public void write(PacketBuffer buffer, Ingredient ingredient)
    {
        ItemStack[] items = ingredient.getMatchingStacks();
        buffer.writeVarInt(items.length);

        for (ItemStack stack : items)
            buffer.writeItemStack(stack);
    }

    public static Ingredient.IItemList deserializePotion(JsonObject json) {
        if (json.has("potion")) {
            ResourceLocation location = new ResourceLocation(JSONUtils.getString(json, "potion"));

            Potion potion = Registry.POTION.getValue(location).orElseThrow(() -> {
                return new JsonSyntaxException("Unknown item '" + location + "'");
            });

            ItemStack stack = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), potion);
            return new Ingredient.SingleItemList(stack);
        } else {
            throw new JsonParseException("A potion ingredient entry needs a potion");
        }
    }
}
