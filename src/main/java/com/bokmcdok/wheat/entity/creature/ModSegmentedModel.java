package com.bokmcdok.wheat.entity.creature;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

public abstract class ModSegmentedModel<T extends Entity> extends SegmentedModel<T> {
    private final ModelRenderer[] mSegments;
    private final ImmutableList<ModelRenderer> mRenderers;
    private final float mWriggleSpeed;

    /**
     * Construction
     */
    public ModSegmentedModel(int[][] segmentSizes, int[][] textureOffsets, float wriggleSpeed) {
        mWriggleSpeed = wriggleSpeed;

        final float[] zPlacement = new float[segmentSizes.length];
        mSegments = new ModelRenderer[segmentSizes.length];
        float f = -3.5f;

        for (int i = 0; i < mSegments.length; ++i) {
            mSegments[i] = new ModelRenderer(this, textureOffsets[i][0], textureOffsets[i][1]);
            mSegments[i].func_228300_a_(
                    segmentSizes[i][0] * -0.5f, 0.0f, segmentSizes[i][2] * -0.5f,
                    segmentSizes[i][0], segmentSizes[i][1], segmentSizes[i][2]);
            mSegments[i].setRotationPoint(0.0f, 24.0f - segmentSizes[i][1], f);
            zPlacement[i] = f;
            if (i < mSegments.length - 1) {
                f += (segmentSizes[i][2] + segmentSizes[i + 1][2]) * 0.5f;
            }
        }

        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.addAll(Arrays.asList(mSegments));
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
    public void func_225597_a_(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        for(int i = 0; i < mSegments.length; ++i) {
            mSegments[i].rotateAngleY = MathHelper.cos(ageInTicks * mWriggleSpeed + (float)i * 0.15F * (float)Math.PI) * (float)Math.PI * 0.05F * (float)(1 + Math.abs(i - 2));
            mSegments[i].rotationPointX = MathHelper.sin(ageInTicks * mWriggleSpeed + (float)i * 0.15F * (float)Math.PI) * (float)Math.PI * 0.2F * (float)Math.abs(i - 2);
        }
    }

    /**
     * Get the list of models to render.
     * @return A list of ModelRenderers.a
     */
    @Override
    public ImmutableList<ModelRenderer> func_225601_a_() {
        return mRenderers;
    }
}
