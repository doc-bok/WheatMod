package com.bokmcdok.wheat.entity.animal.widowbird;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.TintedAgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class ModWidowbirdModel extends TintedAgeableModel<ModWidowbirdEntity> {
    private final ModelRenderer mBody;
    private final ModelRenderer mTail;
    private final ModelRenderer mWingLeft;
    private final ModelRenderer mWingRight;
    private final ModelRenderer mHead;
    private final ModelRenderer mLegLeft;
    private final ModelRenderer mLegRight;
    private final ImmutableList<ModelRenderer> mRenderers;

    /**
     * Construction
     */
    public ModWidowbirdModel() {
        textureWidth = 32;
        textureHeight = 32;

        mBody = new ModelRenderer(this, 2, 8);
        mBody.func_228300_a_(-1.5F, 0.0F, -1.5F, 3, 6, 3);
        mBody.setRotationPoint(0.0F, 16.5F, -3.0F);

        mTail = new ModelRenderer(this, 22, 1);
        mTail.func_228300_a_(-1.5F, -1.0F, -1.0F, 3, 12, 1);
        mTail.setRotationPoint(0.0F, 21.07F, 1.16F);

        mWingLeft = new ModelRenderer(this, 11, 22);
        mWingLeft.func_228300_a_(-0.5F, 0.0F, -1.5F, 1, 6, 3);
        mWingLeft.setRotationPoint(1.5F, 16.94F, -2.76F);

        mWingRight = new ModelRenderer(this, 11, 22);
        mWingRight.func_228300_a_(-0.5F, 0.0F, -1.5F, 1, 6, 3);
        mWingRight.setRotationPoint(-1.5F, 16.94F, -2.76F);

        mHead = new ModelRenderer(this, 2, 2);
        mHead.func_228300_a_(-1.0F, -1.5F, -1.0F, 2, 3, 2);
        mHead.setRotationPoint(0.0F, 15.69F, -2.76F);

        ModelRenderer beak = new ModelRenderer(this, 11, 7);
        beak.func_228300_a_(-0.5F, -0.5F, -0.5F, 1, 1, 1);
        beak.setRotationPoint(0.0F, -0.5F, -1.5F);
        mHead.addChild(beak);

        mLegLeft = new ModelRenderer(this, 14, 18);
        mLegLeft.func_228300_a_(-0.5F, 0.0F, -0.5F, 1, 2, 1);
        mLegLeft.setRotationPoint(1.0F, 22.0F, -1.05F);

        mLegRight = new ModelRenderer(this, 14, 18);
        mLegRight.func_228300_a_(-0.5F, 0.0F, -0.5F, 1, 2, 1);
        mLegRight.setRotationPoint(-1.0F, 22.0F, -1.05F);

        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.addAll(Arrays.asList(mBody, mTail, mWingLeft, mWingRight, mLegLeft, mLegRight));
        mRenderers = builder.build();
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
    public void func_225597_a_(ModWidowbirdEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        mHead.rotateAngleX = headPitch * 0.017453292F;
        mHead.rotateAngleY = netHeadYaw * 0.017453292F;
        mHead.rotateAngleZ = 0.0F;
        mHead.rotationPointX = 0.0F;

        mBody.rotationPointX = 0.0F;

        mTail.rotationPointX = 0.0F;

        mWingRight.rotationPointX = -1.5F;
        mWingLeft.rotationPointX = 1.5F;

        if (entity.isFlying()) {
            float frame = ageInTicks * 0.3F;
            mHead.rotationPointY = 15.69F + frame;
            mTail.rotateAngleX = 1.015F + MathHelper.cos(limbSwing * 0.6662F) * 0.3F * limbSwingAmount;
            mTail.rotationPointY = 21.07F + frame;
            mBody.rotationPointY = 16.5F + frame;
            mWingLeft.rotateAngleZ = -0.0873F - ageInTicks;
            mWingLeft.rotationPointY = 16.94F + frame;
            mWingRight.rotateAngleZ = 0.0873F + ageInTicks;
            mWingRight.rotationPointY = 16.94F + frame;
            mLegLeft.rotationPointY = 22.0F + frame;
            mLegRight.rotationPointY = 22.0F + frame;
        } else {
            ModelRenderer leg = mLegLeft;
            leg.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            leg = mLegRight;
            leg.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
        }
    }

    /**
     * Set the living animations.
     * @param entity The entity.
     * @param x Euler angle x.
     * @param y Euler angle y.
     * @param z Euler angle z.
     */
    @Override
    public void setLivingAnimations(ModWidowbirdEntity entity, float x, float y, float z) {
        mBody.rotateAngleX = 0.4937F;

        mWingLeft.rotateAngleX = -0.6981F;
        mWingLeft.rotateAngleY = -3.1415927F;
        mWingRight.rotateAngleX = -0.6981F;
        mWingRight.rotateAngleY = -3.1415927F;

        mLegLeft.rotateAngleX = -0.0299F;
        mLegRight.rotateAngleX = -0.0299F;
        mLegLeft.rotationPointY = 22.0F;
        mLegRight.rotationPointY = 22.0F;
        mLegLeft.rotateAngleZ = 0.0F;
        mLegRight.rotateAngleZ = 0.0F;

        if (entity.isFlying()) {
            ModelRenderer var10000 = mLegLeft;
            var10000.rotateAngleX += 0.6981317F;
            var10000 = mLegRight;
            var10000.rotateAngleX += 0.6981317F;
        }
    }

    @Override
    protected Iterable<ModelRenderer> func_225602_a_() {
        return ImmutableList.of(mHead);
    }

    @Override
    protected Iterable<ModelRenderer> func_225600_b_() {
        return mRenderers;
    }
}
