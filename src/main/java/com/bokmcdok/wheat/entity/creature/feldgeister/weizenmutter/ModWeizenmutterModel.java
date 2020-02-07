package com.bokmcdok.wheat.entity.creature.feldgeister.weizenmutter;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class ModWeizenmutterModel<T extends ModWeizenmutterEntity> extends SegmentedModel<T> implements IHasArm, IHasHead {
    private final ModelRenderer mHead;
    private final ModelRenderer mHat;
    private final ModelRenderer mBody;
    private final ModelRenderer mArms;
    private final ModelRenderer mRightLeg;
    private final ModelRenderer mLeftLeg;
    private final ModelRenderer mRightArm;
    private final ModelRenderer mLeftArm;

    /**
     * Construction
     * @param scaleFactor The scale of the model
     * @param rotationPointY The rotation point at Y
     * @param textureWidth The width of the texture
     * @param textureHeight The height of the texture
     */
    public ModWeizenmutterModel(float scaleFactor, float rotationPointY, int textureWidth, int textureHeight) {
        mHead = (new ModelRenderer(this)).setTextureSize(textureWidth, textureHeight);
        mHead.setRotationPoint(0.0F, 0.0F + rotationPointY, 0.0F);
        mHead.setTextureOffset(0, 0).func_228301_a_(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, scaleFactor);

        mHat = (new ModelRenderer(this, 32, 0)).setTextureSize(textureWidth, textureHeight);
        mHat.func_228301_a_(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, scaleFactor + 0.45F);
        mHead.addChild(mHat);
        mHat.showModel = false;

        ModelRenderer headRenderer = (new ModelRenderer(this)).setTextureSize(textureWidth, textureHeight);
        headRenderer.setRotationPoint(0.0F, rotationPointY - 2.0F, 0.0F);
        headRenderer.setTextureOffset(24, 0).func_228301_a_(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, scaleFactor);
        mHead.addChild(headRenderer);

        mBody = (new ModelRenderer(this)).setTextureSize(textureWidth, textureHeight);
        mBody.setRotationPoint(0.0F, 0.0F + rotationPointY, 0.0F);
        mBody.setTextureOffset(16, 20).func_228301_a_(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, scaleFactor);
        mBody.setTextureOffset(0, 38).func_228301_a_(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, scaleFactor + 0.5F);

        mArms = (new ModelRenderer(this)).setTextureSize(textureWidth, textureHeight);
        mArms.setRotationPoint(0.0F, 0.0F + rotationPointY + 2.0F, 0.0F);
        mArms.setTextureOffset(44, 22).func_228301_a_(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, scaleFactor);

        ModelRenderer armRenderer = (new ModelRenderer(this, 44, 22)).setTextureSize(textureWidth, textureHeight);
        armRenderer.mirror = true;
        armRenderer.func_228301_a_(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, scaleFactor);
        mArms.addChild(armRenderer);
        mArms.setTextureOffset(40, 38).func_228301_a_(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, scaleFactor);

        mRightLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(textureWidth, textureHeight);
        mRightLeg.setRotationPoint(-2.0F, 12.0F + rotationPointY, 0.0F);
        mRightLeg.func_228301_a_(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scaleFactor);

        mLeftLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(textureWidth, textureHeight);
        mLeftLeg.mirror = true;
        mLeftLeg.setRotationPoint(2.0F, 12.0F + rotationPointY, 0.0F);
        mLeftLeg.func_228301_a_(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scaleFactor);

        mRightArm = (new ModelRenderer(this, 40, 46)).setTextureSize(textureWidth, textureHeight);
        mRightArm.func_228301_a_(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scaleFactor);
        mRightArm.setRotationPoint(-5.0F, 2.0F + rotationPointY, 0.0F);

        mLeftArm = (new ModelRenderer(this, 40, 46)).setTextureSize(textureWidth, textureHeight);
        mLeftArm.mirror = true;
        mLeftArm.func_228301_a_(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scaleFactor);
        mLeftArm.setRotationPoint(5.0F, 2.0F + rotationPointY, 0.0F);
    }

    /**
     * Get the list of renderers.
     * @return A list of renderers.
     */
    @Override
    public Iterable<ModelRenderer> func_225601_a_() {
        return ImmutableList.of(mHead, mBody, mRightLeg, mLeftLeg, mArms, mRightArm, mLeftArm);
    }

    /**
     * Set rotation angles.
     * @param entity The current entity.
     * @param limbSwing The current limb swing.
     * @param limbSwingAmount The amount of limb swing.
     * @param partialTick The time delta.
     * @param headYaw The head's yaw (left/right).
     * @param headPitch The head's pitch (up/down).
     */
    @Override
    public void func_225597_a_(T entity, float limbSwing, float limbSwingAmount, float partialTick, float headYaw, float headPitch) {
        mHead.rotateAngleY = headYaw * ((float)Math.PI / 180F);
        mHead.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        mArms.rotationPointY = 3.0F;
        mArms.rotationPointZ = -1.0F;
        mArms.rotateAngleX = -0.75F;
        if (isSitting) {
            mRightArm.rotateAngleX = (-(float)Math.PI / 5F);
            mRightArm.rotateAngleY = 0.0F;
            mRightArm.rotateAngleZ = 0.0F;
            mLeftArm.rotateAngleX = (-(float)Math.PI / 5F);
            mLeftArm.rotateAngleY = 0.0F;
            mLeftArm.rotateAngleZ = 0.0F;
            mRightLeg.rotateAngleX = -1.4137167F;
            mRightLeg.rotateAngleY = ((float)Math.PI / 10F);
            mRightLeg.rotateAngleZ = 0.07853982F;
            mLeftLeg.rotateAngleX = -1.4137167F;
            mLeftLeg.rotateAngleY = (-(float)Math.PI / 10F);
            mLeftLeg.rotateAngleZ = -0.07853982F;
        } else {
            mRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
            mRightArm.rotateAngleY = 0.0F;
            mRightArm.rotateAngleZ = 0.0F;
            mLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
            mLeftArm.rotateAngleY = 0.0F;
            mLeftArm.rotateAngleZ = 0.0F;
            mRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
            mRightLeg.rotateAngleY = 0.0F;
            mRightLeg.rotateAngleZ = 0.0F;
            mLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
            mLeftLeg.rotateAngleY = 0.0F;
            mLeftLeg.rotateAngleZ = 0.0F;
        }

        AbstractIllagerEntity.ArmPose abstractillagerentity$armpose = entity.getArmPose();
        if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.ATTACKING) {
            float f = MathHelper.sin(swingProgress * (float)Math.PI);
            float f1 = MathHelper.sin((1.0F - (1.0F - swingProgress) * (1.0F - swingProgress)) * (float)Math.PI);
            mRightArm.rotateAngleZ = 0.0F;
            mLeftArm.rotateAngleZ = 0.0F;
            mRightArm.rotateAngleY = 0.15707964F;
            mLeftArm.rotateAngleY = -0.15707964F;
            if (entity.getPrimaryHand() == HandSide.RIGHT) {
                mRightArm.rotateAngleX = -1.8849558F + MathHelper.cos(partialTick * 0.09F) * 0.15F;
                mLeftArm.rotateAngleX = -0.0F + MathHelper.cos(partialTick * 0.19F) * 0.5F;
                mRightArm.rotateAngleX += f * 2.2F - f1 * 0.4F;
                mLeftArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
            } else {
                mRightArm.rotateAngleX = -0.0F + MathHelper.cos(partialTick * 0.19F) * 0.5F;
                mLeftArm.rotateAngleX = -1.8849558F + MathHelper.cos(partialTick * 0.09F) * 0.15F;
                mRightArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
                mLeftArm.rotateAngleX += f * 2.2F - f1 * 0.4F;
            }

            mRightArm.rotateAngleZ += MathHelper.cos(partialTick * 0.09F) * 0.05F + 0.05F;
            mLeftArm.rotateAngleZ -= MathHelper.cos(partialTick * 0.09F) * 0.05F + 0.05F;
            mRightArm.rotateAngleX += MathHelper.sin(partialTick * 0.067F) * 0.05F;
            mLeftArm.rotateAngleX -= MathHelper.sin(partialTick * 0.067F) * 0.05F;

        } else if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.SPELLCASTING) {
            mRightArm.rotationPointZ = 0.0F;
            mRightArm.rotationPointX = -5.0F;
            mLeftArm.rotationPointZ = 0.0F;
            mLeftArm.rotationPointX = 5.0F;
            mRightArm.rotateAngleX = MathHelper.cos(partialTick * 0.6662F) * 0.25F;
            mLeftArm.rotateAngleX = MathHelper.cos(partialTick * 0.6662F) * 0.25F;
            mRightArm.rotateAngleZ = 2.3561945F;
            mLeftArm.rotateAngleZ = -2.3561945F;
            mRightArm.rotateAngleY = 0.0F;
            mLeftArm.rotateAngleY = 0.0F;

        } else if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
            mRightArm.rotationPointZ = 0.0F;
            mRightArm.rotationPointX = -5.0F;
            mRightArm.rotateAngleX = MathHelper.cos(partialTick * 0.6662F) * 0.05F;
            mRightArm.rotateAngleZ = 2.670354F;
            mRightArm.rotateAngleY = 0.0F;
            mLeftArm.rotationPointZ = 0.0F;
            mLeftArm.rotationPointX = 5.0F;
            mLeftArm.rotateAngleX = MathHelper.cos(partialTick * 0.6662F) * 0.05F;
            mLeftArm.rotateAngleZ = -2.3561945F;
            mLeftArm.rotateAngleY = 0.0F;
        }

        boolean flag = abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CROSSED;
        mArms.showModel = flag;
        mLeftArm.showModel = !flag;
        mRightArm.showModel = !flag;
    }

    /**
     * Set living animations.
     * @param entity The current entity.
     * @param limbSwing The current limb swing.
     * @param limbSwingAmount The amount of limb swing.
     * @param partialTick The time delta.
     */
    public void setLivingAnimations(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTick);
    }

    /**
     * Get the hat renderer.
     * @return The hat renderer.
     */
    public ModelRenderer getHat() {
        return mHat;
    }

    /**
     * Get head renderer.
     * @return The head renderer.
     */
    @Override
    public ModelRenderer func_205072_a() {
        return mHead;
    }

    /**
     * Transform the arm.
     * @param handSide Which side to update.
     * @param matrixStack The transformation to apply.
     */
    @Override
    public void func_225599_a_(HandSide handSide, MatrixStack matrixStack) {
        getArm(handSide).func_228307_a_(matrixStack);
    }

    /**
     * Get the arm renderer.
     * @param handSide The side to get.
     * @return The requested arm renderer.
     */
    private ModelRenderer getArm(HandSide handSide) {
        return handSide == HandSide.LEFT ? mLeftArm : mRightArm;
    }
}
