package com.bokmcdok.wheat.entity.creature.feldgeister.fillager;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class ModFillagerRenderer extends MobRenderer<ModFillagerEntity, ModFillagerModel<ModFillagerEntity>> {

    /**
     * Construction
     * @param renderManager The render manager.
     */
    public ModFillagerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModFillagerModel<>(0.0f, 0.0f, 64, 64), 0.5f);
        addLayer(new HeadLayer<>(this));
        addLayer(new HeldItemLayer<>(this));
    }

    /**
     * Get the entity's texture.
     * @param entity The entity to get the texture for.
     * @return The texture's resource location.
     */
    @Override
    public ResourceLocation getEntityTexture(ModFillagerEntity entity) {
        return entity.getTexture();
    }

    /**
     *
     * @param entity The entity.
     * @param matrixStack The transformation.
     * @param p_225620_3_ ???
     */
    @Override
    protected void func_225620_a_(ModFillagerEntity entity, MatrixStack matrixStack, float p_225620_3_) {
        float f = 0.9375F;
        if (entity.isChild()) {
            f = (float)((double)f * 0.5D);
            shadowSize = 0.25F;
        } else {
            shadowSize = 0.5F;
        }

        matrixStack.func_227862_a_(f, f, f);
    }
}
