package com.bokmcdok.wheat.entity.animal.mouse;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModMouseModel<T extends Entity> extends EntityModel<T> {
    private final RendererModel[] mTextureMap;
    private final RendererModel[] mModel;
    private final float[] mZPlacement = new float[7];
    private static final int[][] MOUSE_BOX_LENGTH = new int[][]{{1, 1, 1}, {3, 2, 2}, {4, 3, 3}, {3, 3, 3}, {3, 3, 3}, {1, 1, 2}, {1, 1, 2}};
    private static final int[][] MOUSE_TEXTURE_POSITIONS = new int[][]{{13, 4}, {0, 0}, {0, 9}, {0, 16}, {0, 16}, {11, 0}, {13, 4}};

    public ModMouseModel() {
        mTextureMap = new RendererModel[7];
        float f = -3.5F;

        for(int i = 0; i < mTextureMap.length; ++i) {
            mTextureMap[i] = new RendererModel(this, MOUSE_TEXTURE_POSITIONS[i][0], MOUSE_TEXTURE_POSITIONS[i][1]);
            mTextureMap[i].addBox((float) MOUSE_BOX_LENGTH[i][0] * -0.5F, 0.0F, (float) MOUSE_BOX_LENGTH[i][2] * -0.5F, MOUSE_BOX_LENGTH[i][0], MOUSE_BOX_LENGTH[i][1], MOUSE_BOX_LENGTH[i][2]);
            mTextureMap[i].setRotationPoint(0.0F, (float)(24 - MOUSE_BOX_LENGTH[i][1]), f);
            mZPlacement[i] = f;
            if (i < mTextureMap.length - 1) {
                f += (float)(MOUSE_BOX_LENGTH[i][2] + MOUSE_BOX_LENGTH[i + 1][2]) * 0.5F;
            }
        }

        mModel = new RendererModel[3];
        mModel[0] = new RendererModel(this, 20, 0);
        mModel[0].addBox(-5.0F, 0.0F, (float) MOUSE_BOX_LENGTH[2][2] * -0.5F, 10, 8, MOUSE_BOX_LENGTH[2][2]);
        mModel[0].setRotationPoint(0.0F, 16.0F, mZPlacement[2]);
        mModel[1] = new RendererModel(this, 20, 11);
        mModel[1].addBox(-3.0F, 0.0F, (float) MOUSE_BOX_LENGTH[4][2] * -0.5F, 6, 4, MOUSE_BOX_LENGTH[4][2]);
        mModel[1].setRotationPoint(0.0F, 20.0F, mZPlacement[4]);
        mModel[2] = new RendererModel(this, 20, 18);
        mModel[2].addBox(-3.0F, 0.0F, (float) MOUSE_BOX_LENGTH[4][2] * -0.5F, 6, 5, MOUSE_BOX_LENGTH[1][2]);
        mModel[2].setRotationPoint(0.0F, 19.0F, mZPlacement[1]);
    }

    public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        for(RendererModel renderermodel : mTextureMap) {
            renderermodel.render(scale);
        }

        for(RendererModel renderermodel1 : mModel) {
            renderermodel1.render(scale);
        }

    }

    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
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
}
