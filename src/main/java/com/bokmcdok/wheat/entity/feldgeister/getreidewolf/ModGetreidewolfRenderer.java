package com.bokmcdok.wheat.entity.feldgeister.getreidewolf;

import com.mojang.blaze3d.platform.GlStateManager;
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
     * @param x The x-position.
     * @param y The y-position.
     * @param z The z-position.
     * @param yaw The yaw.
     * @param partialTicks The time delta.
     */
    @Override
    public void doRender(ModGetreidewolfEntity entity, double x, double y, double z, float yaw, float partialTicks) {
        if (entity.getIsWet()) {
            float shading = entity.getBrightness() * entity.getShadingWhileWet(partialTicks);
            GlStateManager.color3f(shading, shading, shading);
        }

        super.doRender(entity, x, y, z, yaw, partialTicks);
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
    protected ResourceLocation getEntityTexture(ModGetreidewolfEntity entity) {
        return GETREIDEWOLF_TEXTURES;
    }
}
