package com.bokmcdok.wheat.entity.animal.butterfly;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModButterflyModel extends SegmentedModel<ModButterflyEntity> {
    private final ModelRenderer mButterflyBody;
    private final ModelRenderer mButterflyRightWing;
    private final ModelRenderer mButterflyLeftWing;

    public ModButterflyModel() {
        textureWidth = 64;
        textureHeight = 64;

        mButterflyBody = new ModelRenderer(this, 0, 16);


        //  x, y, z, dx, dy, dz
        mButterflyBody.func_228300_a_(-1.5F, 4.0F, -1.5f, 3, 10, 3);

        mButterflyRightWing = new ModelRenderer(this, 42, 0);
        mButterflyRightWing.func_228300_a_(-12.0F, 1.0F, 0.75F, 10, 16, 1);

        mButterflyLeftWing = new ModelRenderer(this, 42, 0);
        mButterflyLeftWing.mirror = true;
        mButterflyLeftWing.func_228300_a_(2.0F, 1.0F, 0.75F, 10, 16, 1);

        mButterflyBody.addChild(mButterflyRightWing);
        mButterflyBody.addChild(mButterflyLeftWing);
    }

    @Override
    public void func_225597_a_(ModButterflyEntity butterflyEntity, float v, float v1, float wingAngles, float v3, float v4) {
        mButterflyRightWing.setRotationPoint(0.0F, 0.0F, 0.0F);
        mButterflyLeftWing.setRotationPoint(0.0F, 0.0F, 0.0F);

        mButterflyBody.rotateAngleX = 0.7853982F + MathHelper.cos(wingAngles * 0.1F) * 0.15F;
        mButterflyBody.rotateAngleY = 0.0F;

        mButterflyRightWing.rotateAngleY = MathHelper.cos(wingAngles * 1.3F) * 3.1415927F * 0.25F;
        mButterflyLeftWing.rotateAngleY = -mButterflyRightWing.rotateAngleY;
    }

    @Override
    public Iterable<ModelRenderer> func_225601_a_() {
        return ImmutableList.of(mButterflyBody);
    }
}
