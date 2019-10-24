package com.bokmcdok.wheat.Item;

import com.bokmcdok.wheat.Entity.StoneEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ModStoneItem extends ModBlockItem {

    public ModStoneItem(Block block, ItemGroup itemGroup, String registryName) {
        super(block, itemGroup, registryName);
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * {@link #onItemUse}.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        ItemStack itemstack1 = playerIn.abilities.isCreativeMode ? itemstack.copy() : itemstack.split(1);
        worldIn.playSound((PlayerEntity)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isRemote) {
            StoneEntity potionentity = new StoneEntity(worldIn, playerIn);
            potionentity.func_213884_b(itemstack1);
            potionentity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.5F, 1.0F);
            worldIn.addEntity(potionentity);
        }

        playerIn.addStat(Stats.ITEM_USED.get(this));
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
}
