package com.bokmcdok.wheat.entity.feldgeister.haferbock;

import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
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
        field_78147_e = new RendererModel(this, 0, 16);
        field_78147_e.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, 0.0f);
        field_78147_e.setRotationPoint(0.0f, 12.0f, -5.0f);
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
        setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        headModel.render(scale);
        field_78148_b.render(scale);
        field_78149_c.render(scale);
        field_78146_d.render(scale);
        field_78147_e.render(scale);
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
        headModel.rotateAngleX = headPitch * 0.017453292F;
        headModel.rotateAngleY = netHeadYaw * 0.017453292F;
        field_78148_b.rotateAngleX = 1.5707964F;
        field_78149_c.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        field_78146_d.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
        field_78147_e.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
    }
}
