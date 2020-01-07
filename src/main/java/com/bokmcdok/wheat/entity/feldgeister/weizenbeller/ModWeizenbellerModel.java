package com.bokmcdok.wheat.entity.feldgeister.weizenbeller;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModWeizenbellerModel<T extends ModWeizenbellerEntity> extends SegmentedModel<T> {
    private final ModelRenderer mHead;
    private final ModelRenderer mBody;
    private final ModelRenderer mBackRightLeg;
    private final ModelRenderer mBackLeftLeg;
    private final ModelRenderer mFrontRightLeg;
    private final ModelRenderer mFrontLeftLeg;
    private final ModelRenderer mTail;

    /**
     * Construction
     */
    public ModWeizenbellerModel() {
        textureWidth = 48;
        textureHeight = 32;

        mHead = new ModelRenderer(this, 1, 5);
        mHead.func_228300_a_(-3.0F, -2.0F, -5.0F, 8, 6, 6);
        mHead.setRotationPoint(-1.0F, 16.5F, -3.0F);

        ModelRenderer rightEar = new ModelRenderer(this, 8, 1);
        rightEar.func_228300_a_(-3.0F, -4.0F, -4.0F, 2, 2, 1);

        ModelRenderer leftEar = new ModelRenderer(this, 15, 1);
        leftEar.func_228300_a_(3.0F, -4.0F, -4.0F, 2, 2, 1);

        ModelRenderer snout = new ModelRenderer(this, 6, 18);
        snout.func_228300_a_(-1.0F, 2.01F, -8.0F, 4, 2, 3);

        mHead.addChild(rightEar);
        mHead.addChild(leftEar);
        mHead.addChild(snout);

        mBody = new ModelRenderer(this, 24, 15);
        mBody.func_228300_a_(-3.0F, 3.999F, -3.5F, 6, 11, 6);
        mBody.setRotationPoint(0.0F, 16.0F, -6.0F);

        mBackRightLeg = new ModelRenderer(this, 13, 24);
        mBackRightLeg.func_228301_a_(2.0F, 0.5F, -1.0F, 2, 6, 2, 0.001F);
        mBackRightLeg.setRotationPoint(-5.0F, 17.5F, 7.0F);

        mBackLeftLeg = new ModelRenderer(this, 4, 24);
        mBackLeftLeg.func_228301_a_(2.0F, 0.5F, -1.0F, 2, 6, 2, 0.001F);
        mBackLeftLeg.setRotationPoint(-1.0F, 17.5F, 7.0F);

        mFrontRightLeg = new ModelRenderer(this, 13, 24);
        mFrontRightLeg.func_228301_a_(2.0F, 0.5F, -1.0F, 2, 6, 2, 0.001F);
        mFrontRightLeg.setRotationPoint(-5.0F, 17.5F, 0.0F);

        mFrontLeftLeg = new ModelRenderer(this, 4, 24);
        mFrontLeftLeg.func_228301_a_(2.0F, 0.5F, -1.0F, 2, 6, 2, 0.001F);
        mFrontLeftLeg.setRotationPoint(-1.0F, 17.5F, 0.0F);

        mTail = new ModelRenderer(this, 30, 0);
        mTail.func_228300_a_(2.0F, 0.0F, -1.0F, 4, 9, 5);
        mTail.setRotationPoint(-4.0F, 15.0F, -1.0F);

        mBody.addChild(mTail);
    }

    @Override
    public ImmutableList<ModelRenderer> func_225601_a_() {
        return ImmutableList.of(mHead, mBody, mBackRightLeg, mBackLeftLeg, mFrontRightLeg, mFrontLeftLeg, mTail);
    }

    /**
     * Set the living animations.
     * @param entity The entity.
     * @param x Euler angle x.
     * @param y Euler angle y.
     * @param z Euler angle z.
     */
    @Override
    public void setLivingAnimations(T entity, float x, float y, float z) {
        mBody.rotateAngleX = 1.5707964F;
        mTail.rotateAngleX = -0.05235988F;
        mBackRightLeg.rotateAngleX = MathHelper.cos(x * 0.6662F) * 1.4F * y;
        mBackLeftLeg.rotateAngleX = MathHelper.cos(x * 0.6662F + 3.1415927F) * 1.4F * y;
        mFrontRightLeg.rotateAngleX = MathHelper.cos(x * 0.6662F + 3.1415927F) * 1.4F * y;
        mFrontLeftLeg.rotateAngleX = MathHelper.cos(x * 0.6662F) * 1.4F * y;
        mHead.setRotationPoint(-1.0F, 16.5F, -3.0F);
        mHead.rotateAngleY = 0.0F;
        mHead.rotateAngleZ = 0.0f;
        mBackRightLeg.showModel = true;
        mBackLeftLeg.showModel = true;
        mFrontRightLeg.showModel = true;
        mFrontLeftLeg.showModel = true;
        mBody.setRotationPoint(0.0F, 16.0F, -6.0F);
        mBody.rotateAngleZ = 0.0F;
        mBackRightLeg.setRotationPoint(-5.0F, 17.5F, 7.0F);
        mBackLeftLeg.setRotationPoint(-1.0F, 17.5F, 7.0F);
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
        mHead.rotateAngleX = headPitch * 0.017453292F;
        mHead.rotateAngleY = netHeadYaw * 0.017453292F;
    }
}
