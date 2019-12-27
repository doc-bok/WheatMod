package com.bokmcdok.wheat.entity.animal.widowbird;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModWidowbirdModel extends EntityModel<ModWidowbirdEntity> {
    private final RendererModel mBody;
    private final RendererModel mTail;
    private final RendererModel mWingLeft;
    private final RendererModel mWingRight;
    private final RendererModel mHead;
    private final RendererModel mFeather;
    private final RendererModel mLegLeft;
    private final RendererModel mLegRight;

    /**
     * Construction
     */
    public ModWidowbirdModel() {
        textureWidth = 32;
        textureHeight = 32;

        mBody = new RendererModel(this, 2, 8);
        mBody.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3);
        mBody.setRotationPoint(0.0F, 16.5F, -3.0F);

        mTail = new RendererModel(this, 22, 1);
        mTail.addBox(-1.5F, -1.0F, -1.0F, 3, 4, 1);
        mTail.setRotationPoint(0.0F, 21.07F, 1.16F);

        mWingLeft = new RendererModel(this, 19, 8);
        mWingLeft.addBox(-0.5F, 0.0F, -1.5F, 1, 5, 3);
        mWingLeft.setRotationPoint(1.5F, 16.94F, -2.76F);

        mWingRight = new RendererModel(this, 19, 8);
        mWingRight.addBox(-0.5F, 0.0F, -1.5F, 1, 5, 3);
        mWingRight.setRotationPoint(-1.5F, 16.94F, -2.76F);

        mHead = new RendererModel(this, 2, 2);
        mHead.addBox(-1.0F, -1.5F, -1.0F, 2, 3, 2);
        mHead.setRotationPoint(0.0F, 15.69F, -2.76F);

        RendererModel Head2 = new RendererModel(this, 10, 0);
        Head2.addBox(-1.0F, -0.5F, -2.0F, 2, 1, 4);
        Head2.setRotationPoint(0.0F, -2.0F, -1.0F);
        mHead.addChild(Head2);

        RendererModel Beak1 = new RendererModel(this, 11, 7);
        Beak1.addBox(-0.5F, -1.0F, -0.5F, 1, 2, 1);
        Beak1.setRotationPoint(0.0F, -0.5F, -1.5F);
        mHead.addChild(Beak1);

        RendererModel Beak2 = new RendererModel(this, 16, 7);
        Beak2.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1);
        Beak2.setRotationPoint(0.0F, -1.75F, -2.45F);
        mHead.addChild(Beak2);

        mFeather = new RendererModel(this, 2, 18);
        mFeather.addBox(0.0F, -4.0F, -2.0F, 0, 5, 4);
        mFeather.setRotationPoint(0.0F, -2.15F, 0.15F);
        mHead.addChild(mFeather);

        mLegLeft = new RendererModel(this, 14, 18);
        mLegLeft.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1);
        mLegLeft.setRotationPoint(1.0F, 22.0F, -1.05F);

        mLegRight = new RendererModel(this, 14, 18);
        mLegRight.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1);
        mLegRight.setRotationPoint(-1.0F, 22.0F, -1.05F);
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
    @Override
    public void render(ModWidowbirdEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        mBody.render(scale);
        mWingLeft.render(scale);
        mWingRight.render(scale);
        mTail.render(scale);
        mHead.render(scale);
        mLegLeft.render(scale);
        mLegRight.render(scale);
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
    @Override
    public void setRotationAngles(ModWidowbirdEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {        mHead.rotateAngleX = headPitch * 0.017453292F;
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
            RendererModel leg = mLegLeft;
            leg.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            leg = mLegRight;
            leg.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
        }
    }

    /**
     * Set the living animations.
     * @param entity The entity.
     * @param p_212843_2_
     * @param p_212843_3_
     * @param p_212843_4_
     */
    @Override
    public void setLivingAnimations(ModWidowbirdEntity entity, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
        mFeather.rotateAngleX = -0.2214F;

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
            RendererModel var10000 = mLegLeft;
            var10000.rotateAngleX += 0.6981317F;
            var10000 = mLegRight;
            var10000.rotateAngleX += 0.6981317F;
        }
    }
}
