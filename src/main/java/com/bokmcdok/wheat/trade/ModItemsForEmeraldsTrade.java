package com.bokmcdok.wheat.trade;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;

import java.util.Random;

public class ModItemsForEmeraldsTrade implements VillagerTrades.ITrade {
    private final ItemStack mItemToSell;
    private final int mEmeraldCost;
    private final int mNumToSell;
    private final int mTradesUntilDisabled;
    private final int mMerchantXP;
    private final float mPriceMultiplier;

    public ModItemsForEmeraldsTrade(Block itemToSell, int emeraldCost, int numToSell, int tradesUntilDisabled, int merchantXP) {
        this(new ItemStack(itemToSell), emeraldCost, numToSell, tradesUntilDisabled, merchantXP);
    }

    public ModItemsForEmeraldsTrade(Item itemToSell, int emeraldCost, int numToSell, int merchantXP) {
        this(new ItemStack(itemToSell), emeraldCost, numToSell, 12, merchantXP);
    }

    public ModItemsForEmeraldsTrade(Item itemToSell, int emeraldCost, int numToSell, int tradesUntilDisabled, int merchantXP) {
        this(new ItemStack(itemToSell), emeraldCost, numToSell, tradesUntilDisabled, merchantXP);
    }

    public ModItemsForEmeraldsTrade(ItemStack itemToSell, int emeraldCost, int numToSell, int tradesUntilDisabled, int merchantXP) {
        this(itemToSell, emeraldCost, numToSell, tradesUntilDisabled, merchantXP, 0.05F);
    }

    public ModItemsForEmeraldsTrade(ItemStack itemToSell, int emeraldCost, int numToSell, int tradesUntilDisabled, int merchantXP, float priceMultiplier) {
        mItemToSell = itemToSell;
        mEmeraldCost = emeraldCost;
        mNumToSell = numToSell;
        mTradesUntilDisabled = tradesUntilDisabled;
        mMerchantXP = merchantXP;
        mPriceMultiplier = priceMultiplier;
    }

    public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
        return new MerchantOffer(new ItemStack(Items.EMERALD, mEmeraldCost), new ItemStack(mItemToSell.getItem(), mNumToSell), mTradesUntilDisabled, mMerchantXP, mPriceMultiplier);
    }
}
