package com.bokmcdok.wheat.entity.creature.feldgeister.weizenmutter;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class ModWeizenmutterRenderer extends MobRenderer<ModWeizenmutterEntity, ModWeizenmutterModel<ModWeizenmutterEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("docwheat:textures/entity/feldgeister/weizenmutter.png");

    /**
     * Construction
     * @param renderManager The render manager.
     */
    public ModWeizenmutterRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModWeizenmutterModel<>(0.0f, 0.0f, 64, 64), 0.5f);
        addLayer(new HeadLayer<>(this));
        addLayer(new HeldItemLayer<>(this));
    }

    /**
     * Get the entity's texture.
     * @param entity The entity to get the texture for.
     * @return The texture's resource location.
     */
    @Override
    public ResourceLocation getEntityTexture(ModWeizenmutterEntity entity) {
        return TEXTURE;
    }

    /**
     *
     * @param entity The entity.
     * @param matrixStack The transformation.
     * @param p_225620_3_
     */
    @Override
    protected void func_225620_a_(ModWeizenmutterEntity entity, MatrixStack matrixStack, float p_225620_3_) {
        matrixStack.func_227862_a_(0.9375F, 0.9375F, 0.9375F);
    }
}
