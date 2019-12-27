package com.bokmcdok.wheat.entity.animal.butterfly;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class ModButterflyRenderer extends MobRenderer<ModButterflyEntity, ModButterflyModel> {
    private static final ResourceLocation BUTTERFLY_TEXTURE = new ResourceLocation("docwheat:textures/entity/butterfly.png");
    private static final ResourceLocation MOTH_TEXTURE = new ResourceLocation("docwheat:textures/entity/moth.png");

    /**
     * Construction
     * @param rendererManager The renderer manager.
     */
    public ModButterflyRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ModButterflyModel(), 0.1f);
    }

    /**
     * Get the texture to use.
     * @param entity The entity to get the texture for.
     * @return The texture to use.
     */
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(ModButterflyEntity entity) {
        if (entity.getIsButterfly()) {
            return BUTTERFLY_TEXTURE;
        } else {
            return MOTH_TEXTURE;
        }
    }

    /**
     * Scale the entity down.
     * @param entity The butterfly entity.
     * @param partialTickTime The time delta.
     */
    @Override
    protected void preRenderCallback(ModButterflyEntity entity, float partialTickTime) {
        GlStateManager.scalef(0.35F, 0.35F, 0.35F);
    }

    /**
     * Rotate the entity.
     * @param entity The butterfly entity.
     * @param ageInTicks The age of the entity.
     * @param rotationYaw The rotation of the entity.
     * @param partialTicks The time delta.
     */
    @Override
    protected void applyRotations(ModButterflyEntity entity, float ageInTicks, float rotationYaw, float partialTicks) {
        GlStateManager.translatef(
                0.0F,
                MathHelper.cos(ageInTicks * 0.3F) * 0.1F,
                0.0F);
        super.applyRotations(entity, ageInTicks, rotationYaw, partialTicks);
    }
}