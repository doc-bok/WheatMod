package com.bokmcdok.wheat.entity.animal.butterfly;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;

public class ModButterflyModel extends EntityModel<ModButterflyEntity> {
    private final RendererModel mButterflyBody;
    private final RendererModel mButterflyRightWing;
    private final RendererModel mButterflyLeftWing;

    public ModButterflyModel() {
        textureWidth = 64;
        textureHeight = 64;

        mButterflyBody = new RendererModel(this, 0, 16);


        //  x, y, z, dx, dy, dz
        mButterflyBody.addBox(-1.5F, 4.0F, -1.5f, 3, 10, 3);

        mButterflyRightWing = new RendererModel(this, 42, 0);
        mButterflyRightWing.addBox(-12.0F, 1.0F, 0.75F, 10, 16, 1);

        mButterflyLeftWing = new RendererModel(this, 42, 0);
        mButterflyLeftWing.mirror = true;
        mButterflyLeftWing.addBox(2.0F, 1.0F, 0.75F, 10, 16, 1);

        mButterflyBody.addChild(mButterflyRightWing);
        mButterflyBody.addChild(mButterflyLeftWing);
    }

    @Override
    public void render(ModButterflyEntity entity, float p_78088_2_, float p_78088_3_, float wingAngles, float y, float x, float p_78088_7_) {
        setRotationAngles(entity, p_78088_2_, p_78088_3_, wingAngles, y, x, p_78088_7_);
        mButterflyBody.render(p_78088_7_);
    }

    @Override
    public void setRotationAngles(ModButterflyEntity entity, float p_212844_2_, float p_212844_3_, float wingAngles, float y, float x, float p_212844_7_) {
        mButterflyRightWing.setRotationPoint(0.0F, 0.0F, 0.0F);
        mButterflyLeftWing.setRotationPoint(0.0F, 0.0F, 0.0F);

        mButterflyBody.rotateAngleX = 0.7853982F + MathHelper.cos(wingAngles * 0.1F) * 0.15F;
        mButterflyBody.rotateAngleY = 0.0F;

        mButterflyRightWing.rotateAngleY = MathHelper.cos(wingAngles * 1.3F) * 3.1415927F * 0.25F;
        mButterflyLeftWing.rotateAngleY = -mButterflyRightWing.rotateAngleY;
    }
}
