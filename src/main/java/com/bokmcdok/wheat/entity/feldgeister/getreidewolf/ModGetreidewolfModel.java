package com.bokmcdok.wheat.entity.feldgeister.getreidewolf;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.TintedAgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModGetreidewolfModel<T extends ModGetreidewolfEntity> extends TintedAgeableModel<T> {
    private final ModelRenderer mHead;
    private final ModelRenderer mBody;
    private final ModelRenderer mLegBackRight;
    private final ModelRenderer mLegBackLeft;
    private final ModelRenderer mLegFrontRight;
    private final ModelRenderer mLegFrontLeft;
    private final ModelRenderer mTail;
    private final ModelRenderer mMane;

    /**
     * Construction
     */
    public ModGetreidewolfModel() {
        mHead = new ModelRenderer(this, 0, 0);
        mHead.func_228301_a_(-2.0F, -3.0F, -2.0F, 6, 6, 4, 0.0F);
        mHead.setRotationPoint(-1.0F, 13.5F, -7.0F);
        mBody = new ModelRenderer(this, 18, 14);
        mBody.func_228301_a_(-3.0F, -2.0F, -3.0F, 6, 9, 6, 0.0F);
        mBody.setRotationPoint(0.0F, 14.0F, 2.0F);
        mMane = new ModelRenderer(this, 21, 0);
        mMane.func_228301_a_(-3.0F, -3.0F, -3.0F, 8, 6, 7, 0.0F);
        mMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
        mLegBackRight = new ModelRenderer(this, 0, 18);
        mLegBackRight.func_228301_a_(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        mLegBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
        mLegBackLeft = new ModelRenderer(this, 0, 18);
        mLegBackLeft.func_228301_a_(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        mLegBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
        mLegFrontRight = new ModelRenderer(this, 0, 18);
        mLegFrontRight.func_228301_a_(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        mLegFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
        mLegFrontLeft = new ModelRenderer(this, 0, 18);
        mLegFrontLeft.func_228301_a_(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        mLegFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);
        mTail = new ModelRenderer(this, 9, 18);
        mTail.func_228301_a_(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        mTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        mHead.setTextureOffset(16, 14).func_228301_a_(-2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
        mHead.setTextureOffset(16, 14).func_228301_a_(2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
        mHead.setTextureOffset(0, 10).func_228301_a_(-0.5F, 0.0F, -5.0F, 3, 3, 4, 0.0F);
    }

    @Override
    protected Iterable<ModelRenderer> func_225602_a_() {
        return ImmutableList.of(mHead);
    }

    @Override
    protected Iterable<ModelRenderer> func_225600_b_() {
        return ImmutableList.of(mBody, mLegBackRight, mLegBackLeft, mLegFrontRight, mLegFrontLeft, mTail, mMane);
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
        mTail.rotateAngleY = 0.0F;

        mBody.setRotationPoint(0.0F, 14.0F, 2.0F);
        mBody.rotateAngleX = 1.5707964F;
        mMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
        mMane.rotateAngleX = mBody.rotateAngleX;
        mTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        mLegBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
        mLegBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
        mLegFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
        mLegFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);
        mLegBackRight.rotateAngleX = MathHelper.cos(x * 0.6662F) * 1.4F * y;
        mLegBackLeft.rotateAngleX = MathHelper.cos(x * 0.6662F + 3.1415927F) * 1.4F * y;
        mLegFrontRight.rotateAngleX = MathHelper.cos(x * 0.6662F + 3.1415927F) * 1.4F * y;
        mLegFrontLeft.rotateAngleX = MathHelper.cos(x * 0.6662F) * 1.4F * y;


        mHead.rotateAngleZ = entity.getInterestedAngle(z) + entity.getShakeAngle(z, 0.0F);
        mMane.rotateAngleZ = entity.getShakeAngle(z, -0.08F);
        mBody.rotateAngleZ = entity.getShakeAngle(z, -0.16F);
        mTail.rotateAngleZ = entity.getShakeAngle(z, -0.2F);
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
        mTail.rotateAngleX = ageInTicks;
    }
}
