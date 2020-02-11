package com.bokmcdok.wheat.render;

import com.bokmcdok.wheat.entity.tile.ModCampfireTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;

public class ModCampfireTileEntityRenderer extends TileEntityRenderer<ModCampfireTileEntity> {

    /**
     * Construction
     * @param rendererDispatcher The tile entity renderer.
     */
    public ModCampfireTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    /**
     * Render the food on the campfire.
     * @param entity The campfire tile entity.
     * @param p_225616_2_ ???
     * @param matrixStack The current matrix stack.
     * @param buffer The render buffer.
     * @param p_225616_5_ ???
     * @param p_225616_6_ ???
     */
    public void func_225616_a_(ModCampfireTileEntity entity, float p_225616_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int p_225616_5_, int p_225616_6_) {
        Direction direction = entity.getBlockState().get(CampfireBlock.FACING);
        NonNullList<ItemStack> nonnulllist = entity.getInventory();

        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = nonnulllist.get(i);
            if (itemstack != ItemStack.EMPTY) {
                matrixStack.func_227860_a_();
                matrixStack.func_227861_a_(0.5D, 0.44921875D, 0.5D);
                Direction direction1 = Direction.byHorizontalIndex((i + direction.getHorizontalIndex()) % 4);
                float f = -direction1.getHorizontalAngle();
                matrixStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(f));
                matrixStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(90.0F));
                matrixStack.func_227861_a_(-0.3125D, -0.3125D, 0.0D);
                matrixStack.func_227862_a_(0.375F, 0.375F, 0.375F);
                Minecraft.getInstance().getItemRenderer().func_229110_a_(itemstack, ItemCameraTransforms.TransformType.FIXED, p_225616_5_, p_225616_6_, matrixStack, buffer);
                matrixStack.func_227865_b_();
            }
        }
    }
}