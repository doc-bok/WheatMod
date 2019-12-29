package com.bokmcdok.wheat.entity.projectile.howl_attack;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModHowlAttackModel<T extends Entity> extends EntityModel<T> {
    private final RendererModel mModel;

    /**
     * Construction
     */
    public ModHowlAttackModel() {
        this(0.0f);
    }

    /**
     * Construction
     * @param size The size of this projectile.
     */
    public ModHowlAttackModel(float size) {
        mModel = new RendererModel(this);

        mModel.setTextureOffset(0, 0).addBox(-4.0F, 0.0F, 0.0F, 2, 2, 2, size);
        mModel.setTextureOffset(0, 0).addBox(0.0F, -4.0F, 0.0F, 2, 2, 2, size);
        mModel.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -4.0F, 2, 2, 2, size);
        mModel.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 0.0F, 2, 2, 2, size);
        mModel.setTextureOffset(0, 0).addBox(2.0F, 0.0F, 0.0F, 2, 2, 2, size);
        mModel.setTextureOffset(0, 0).addBox(0.0F, 2.0F, 0.0F, 2, 2, 2, size);
        mModel.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 2.0F, 2, 2, 2, size);
        mModel.setRotationPoint(0.0F, 0.0F, 0.0F);
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
    public void render(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        mModel.render(scale);
    }
}
