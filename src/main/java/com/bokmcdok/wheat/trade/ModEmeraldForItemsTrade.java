package com.bokmcdok.wheat.trade;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.IItemProvider;

import java.util.Random;

public class ModEmeraldForItemsTrade implements VillagerTrades.ITrade {
    private final Item mItemToBuy;
    private final int mQuantityToBuy;
    private final int mTradesUntilDisabled;
    private final int mVillagerXP;
    private final float mPriceMultiplier;

    /**
     * Create a new trade that gives the player an emerald.
     * @param itemToBuy The item the villager will buy
     * @param quantityToBuy The number of items needed for a sale
     * @param tradesUntilDisabled The number of trades until this trade is disabled
     * @param villagerXP The XP the villager gets on a successful sale
     */
    public ModEmeraldForItemsTrade(IItemProvider itemToBuy, int quantityToBuy, int tradesUntilDisabled, int villagerXP) {
        this.mItemToBuy = itemToBuy.asItem();
        this.mQuantityToBuy = quantityToBuy;
        this.mTradesUntilDisabled = tradesUntilDisabled;
        this.mVillagerXP = villagerXP;
        this.mPriceMultiplier = 0.05F;
    }

    /**
     * Get an offer for a new villager
     * @param villager The villager to generate an offer for
     * @param random The current RNG
     * @return A new offer based on this trade
     */
    public MerchantOffer getOffer(Entity villager, Random random) {
        ItemStack itemstack = new ItemStack(this.mItemToBuy, this.mQuantityToBuy);
        return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD), this.mTradesUntilDisabled, this.mVillagerXP, this.mPriceMultiplier);
    }
}