package com.bokmcdok.wheat.entity.creature.feldgeister.getreidewolf;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ModGetreidewolfRenderer extends MobRenderer<ModGetreidewolfEntity, ModGetreidewolfModel<ModGetreidewolfEntity>> {
    private static final ResourceLocation GETREIDEWOLF_TEXTURES =
            new ResourceLocation("docwheat:textures/entity/getreidewolf.png");

    /**
     * Construction
     * @param rendererManager The entity renderer.
     */
    public ModGetreidewolfRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ModGetreidewolfModel<>(), 0.5f);
    }

    /**
     * Render the wetness of the getreidewolf.
     * @param entity The entity.
     * @param p_225623_2_
     * @param partialTicks The time delta
     * @param stack The matrix stack
     * @param buffer The render buffer
     * @param p_225623_6_
     */
    @Override
    public void func_225623_a_(ModGetreidewolfEntity entity, float p_225623_2_, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int p_225623_6_) {
        if (entity.getIsWet()) {
            float tint = entity.getBrightness() * entity.getShadingWhileWet(partialTicks);
            entityModel.func_228253_a_(tint, tint, tint);
        }

        super.func_225623_a_(entity, p_225623_2_, partialTicks, stack, buffer, p_225623_6_);
        if (entity.getIsWet()) {
            entityModel.func_228253_a_(1.0F, 1.0F, 1.0F);
        }
    }

    /**
     * Handle tail rotation.
     * @param entity The entity.
     * @param partialTicks The time delta.
     * @return The tail's rotation.
     */
    @Override
    protected float handleRotationFloat(ModGetreidewolfEntity entity, float partialTicks) {
        return entity.getTailRotation();
    }

    /**
     * Get the texture to render the getreidewolf.
     * @param entity The entity.
     * @return The texture to use.
     */
    @Nullable
    @Override
    public ResourceLocation getEntityTexture(ModGetreidewolfEntity entity) {
        return GETREIDEWOLF_TEXTURES;
    }
}
