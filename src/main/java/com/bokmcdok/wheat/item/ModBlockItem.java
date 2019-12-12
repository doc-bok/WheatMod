package com.bokmcdok.wheat.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ModBlockItem extends BlockItem {
    private final ModItemImpl mImpl;

    public ModBlockItem(Block blockIn, ModItemImpl.ModItemProperties properties) {
        super(blockIn, properties);
        mImpl = new ModItemImpl(properties);
    }

    /**
     * Allows handling of data driven throwing items.
     * @param world The current world.
     * @param player The player that owns the item.
     * @param hand The hand holding the item.
     * @return The result of the action.
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ActionResult<ItemStack> result = mImpl.onItemRightClick(this, world, player, hand);
        return result != null ? result : super.onItemRightClick(world, player, hand);
    }
}
