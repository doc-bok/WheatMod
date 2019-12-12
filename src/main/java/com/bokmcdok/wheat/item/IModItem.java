package com.bokmcdok.wheat.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;

public interface IModItem {
    Item asItem();

    IItemColor getColor();
}
