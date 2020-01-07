package com.bokmcdok.wheat.entity.creature.animal.mouse;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class ModMouseModel<T extends Entity> extends SegmentedModel<ModMouseEntity> {
    private final ModelRenderer[] mTextureMap;
    private final ModelRenderer[] mModel;
    private final ImmutableList<ModelRenderer> mRenderers;
    private static final int[][] MOUSE_BOX_LENGTH = new int[][]{{1, 1, 1}, {3, 2, 2}, {4, 3, 3}, {3, 3, 3}, {3, 3, 3}, {1, 1, 2}, {1, 1, 2}};
    private static final int[][] MOUSE_TEXTURE_POSITIONS = new int[][]{{13, 4}, {0, 0}, {0, 9}, {0, 16}, {0, 16}, {11, 0}, {13, 4}};

    /**
     * Construction
     */
    public ModMouseModel() {
        final float[] zPlacement = new float[7];
        mTextureMap = new ModelRenderer[7];
        float f = -3.5F;

        for(int i = 0; i < mTextureMap.length; ++i) {
            mTextureMap[i] = new ModelRenderer(this, MOUSE_TEXTURE_POSITIONS[i][0], MOUSE_TEXTURE_POSITIONS[i][1]);
            mTextureMap[i].func_228300_a_((float) MOUSE_BOX_LENGTH[i][0] * -0.5F, 0.0F, (float) MOUSE_BOX_LENGTH[i][2] * -0.5F, MOUSE_BOX_LENGTH[i][0], MOUSE_BOX_LENGTH[i][1], MOUSE_BOX_LENGTH[i][2]);
            mTextureMap[i].setRotationPoint(0.0F, (float)(24 - MOUSE_BOX_LENGTH[i][1]), f);
            zPlacement[i] = f;
            if (i < mTextureMap.length - 1) {
                f += (float)(MOUSE_BOX_LENGTH[i][2] + MOUSE_BOX_LENGTH[i + 1][2]) * 0.5F;
            }
        }

        mModel = new ModelRenderer[3];
        mModel[0] = new ModelRenderer(this, 20, 0);
        mModel[0].func_228300_a_(-5.0F, 0.0F, (float) MOUSE_BOX_LENGTH[2][2] * -0.5F, 10, 8, MOUSE_BOX_LENGTH[2][2]);
        mModel[0].setRotationPoint(0.0F, 16.0F, zPlacement[2]);
        mModel[1] = new ModelRenderer(this, 20, 11);
        mModel[1].func_228300_a_(-3.0F, 0.0F, (float) MOUSE_BOX_LENGTH[4][2] * -0.5F, 6, 4, MOUSE_BOX_LENGTH[4][2]);
        mModel[1].setRotationPoint(0.0F, 20.0F, zPlacement[4]);
        mModel[2] = new ModelRenderer(this, 20, 18);
        mModel[2].func_228300_a_(-3.0F, 0.0F, (float) MOUSE_BOX_LENGTH[4][2] * -0.5F, 6, 5, MOUSE_BOX_LENGTH[1][2]);
        mModel[2].setRotationPoint(0.0F, 19.0F, zPlacement[1]);

        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.addAll(Arrays.asList(mTextureMap));
        builder.addAll(Arrays.asList(mModel));
        mRenderers = builder.build();
    }

    /**
     * Render the model.
     * @param stack The current matrix stack
     * @param vertexBuilder
     * @param p_225598_3_
     * @param p_225598_4_
     * @param p_225598_5_
     * @param p_225598_6_
     * @param p_225598_7_
     * @param p_225598_8_
     */
    @Override
    public void func_225598_a_(MatrixStack stack, IVertexBuilder vertexBuilder, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        stack.func_227860_a_();
        if (isChild) {
            stack.func_227862_a_(0.5F, 0.5F, 0.5F);
            stack.func_227861_a_(0.0F, /*24.0f / 16.0f*/1.5f, 0.0F);
            //stack.func_227861_a_(0.0F, 24.0F * scale, 0.0F);
        }

        super.func_225598_a_(stack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);

        stack.func_227865_b_();
    }

    /**
     * Set the model's rotation angles.
     * @param entity The entity.
     * @param limbSwing The limb swing angle.
     * @param limbSwingAmount The amount of limb swing.
     * @param ageInTicks The age of the model.
     * @param netHeadYaw The head's yaw angle.
     * @param headPitch The head's pitch angle.
     */
    @Override
    public void func_225597_a_(ModMouseEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        for(int i = 0; i < mTextureMap.length; ++i) {
            mTextureMap[i].rotateAngleY = MathHelper.cos(ageInTicks * 0.9F + (float)i * 0.15F * (float)Math.PI) * (float)Math.PI * 0.05F * (float)(1 + Math.abs(i - 2));
            mTextureMap[i].rotationPointX = MathHelper.sin(ageInTicks * 0.9F + (float)i * 0.15F * (float)Math.PI) * (float)Math.PI * 0.2F * (float)Math.abs(i - 2);
        }

        mModel[0].rotateAngleY = mTextureMap[2].rotateAngleY;
        mModel[1].rotateAngleY = mTextureMap[4].rotateAngleY;
        mModel[1].rotationPointX = mTextureMap[4].rotationPointX;
        mModel[2].rotateAngleY = mTextureMap[1].rotateAngleY;
        mModel[2].rotationPointX = mTextureMap[1].rotationPointX;
    }

    @Override
    public ImmutableList<ModelRenderer> func_225601_a_() {
        return mRenderers;
    }
}
