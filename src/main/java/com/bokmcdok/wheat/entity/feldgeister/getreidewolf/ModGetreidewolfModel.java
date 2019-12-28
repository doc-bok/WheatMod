package com.bokmcdok.wheat.entity.feldgeister.getreidewolf;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModGetreidewolfModel<T extends ModGetreidewolfEntity> extends EntityModel<T> {
    private final RendererModel mHead;
    private final RendererModel mBody;
    private final RendererModel mLegBackRight;
    private final RendererModel mLegBackLeft;
    private final RendererModel mLegFrontRight;
    private final RendererModel mLegFrontLeft;
    private final RendererModel mTail;
    private final RendererModel mMane;

    /**
     * Construction
     */
    public ModGetreidewolfModel() {
        mHead = new RendererModel(this, 0, 0);
        mHead.addBox(-2.0F, -3.0F, -2.0F, 6, 6, 4, 0.0F);
        mHead.setRotationPoint(-1.0F, 13.5F, -7.0F);
        mBody = new RendererModel(this, 18, 14);
        mBody.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, 0.0F);
        mBody.setRotationPoint(0.0F, 14.0F, 2.0F);
        mMane = new RendererModel(this, 21, 0);
        mMane.addBox(-3.0F, -3.0F, -3.0F, 8, 6, 7, 0.0F);
        mMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
        mLegBackRight = new RendererModel(this, 0, 18);
        mLegBackRight.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        mLegBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
        mLegBackLeft = new RendererModel(this, 0, 18);
        mLegBackLeft.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        mLegBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
        mLegFrontRight = new RendererModel(this, 0, 18);
        mLegFrontRight.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        mLegFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
        mLegFrontLeft = new RendererModel(this, 0, 18);
        mLegFrontLeft.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        mLegFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);
        mTail = new RendererModel(this, 9, 18);
        mTail.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        mTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        mHead.setTextureOffset(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
        mHead.setTextureOffset(16, 14).addBox(2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
        mHead.setTextureOffset(0, 10).addBox(-0.5F, 0.0F, -5.0F, 3, 3, 4, 0.0F);
    }

    /**
     * Render the model.
     * @param entity The entity.
     * @param limbSwing The limb swing angle.
     * @param limbSwingAmount The amount of limb swing.
     * @param ageInTicks The age of the model.
     * @param netHeadYaw The head's yaw angle.
     * @param headPitch The head's pitch angle.
     * @param scale The size to render the bird.
     */
    public void render(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        mHead.renderWithRotation(scale);
        mBody.render(scale);
        mLegBackRight.render(scale);
        mLegBackLeft.render(scale);
        mLegFrontRight.render(scale);
        mLegFrontLeft.render(scale);
        mTail.renderWithRotation(scale);
        mMane.render(scale);
    }

    /**
     * Set the living animations.
     * @param entity The entity.
     * @param x Euler angle x.
     * @param y Euler angle y.
     * @param z Euler angle z.
     */
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
     * @param scale The size to render the bird.
     */
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        mHead.rotateAngleX = headPitch * 0.017453292F;
        mHead.rotateAngleY = netHeadYaw * 0.017453292F;
        mTail.rotateAngleX = ageInTicks;
    }
}
