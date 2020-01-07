package com.bokmcdok.wheat.entity.feldgeister.haferbock;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModHaferbockModel<T extends Entity> extends CowModel<T> {

    /**
     * Construction
     */
    public ModHaferbockModel() {
        super();
        legFrontRight = new ModelRenderer(this, 0, 16);
        legFrontRight.func_228301_a_(-2.0f, 0.0f, -2.0f, 4, 12, 4, 0.0f);
        legFrontRight.setRotationPoint(0.0f, 12.0f, -5.0f);
    }

    @Override
    protected Iterable<ModelRenderer> func_225600_b_() {
        return ImmutableList.of(body, legBackRight, legBackLeft, legFrontRight);
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
        headModel.rotateAngleX = headPitch * 0.017453292F;
        headModel.rotateAngleY = netHeadYaw * 0.017453292F;
        body.rotateAngleX = 1.5707964F;
        legBackRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        legBackLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        legFrontRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
    }
}
