package com.bokmcdok.wheat.entity.creature.animal.butterfly;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class ModButterflyRenderer extends MobRenderer<ModButterflyEntity, ModButterflyModel> {
    private static final ResourceLocation[] BUTTERFLY_TEXTURE = {
            new ResourceLocation("docwheat:textures/entity/animal/butterfly/butterfly.png"),
            new ResourceLocation("docwheat:textures/entity/animal/butterfly/butterfly02.png"),
            new ResourceLocation("docwheat:textures/entity/animal/butterfly/butterfly03.png"),
            new ResourceLocation("docwheat:textures/entity/animal/butterfly/butterfly04.png"),
            new ResourceLocation("docwheat:textures/entity/animal/butterfly/butterfly05.png"),
            new ResourceLocation("docwheat:textures/entity/animal/butterfly/butterfly06.png"),
            new ResourceLocation("docwheat:textures/entity/animal/butterfly/butterfly07.png"),
            new ResourceLocation("docwheat:textures/entity/animal/butterfly/butterfly08.png")
    };
    private static final ResourceLocation MOTH_TEXTURE = new ResourceLocation("docwheat:textures/entity/animal/moth.png");

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
    public ResourceLocation getEntityTexture(ModButterflyEntity entity) {
        if (entity.getIsButterfly()) {
            return BUTTERFLY_TEXTURE[entity.getVariety()];
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
    protected void func_225620_a_(ModButterflyEntity entity, MatrixStack stack, float partialTickTime) {
        stack.func_227862_a_(0.35F, 0.35F, 0.35F);
    }

    /**
     * Rotate the entity.
     * @param entity The butterfly entity.
     * @param ageInTicks The age of the entity.
     * @param rotationYaw The rotation of the entity.
     * @param partialTicks The time delta.
     */
    @Override
    protected void func_225621_a_(ModButterflyEntity entity, MatrixStack stack, float ageInTicks, float rotationYaw, float partialTicks) {
        stack.func_227861_a_(
                0.0F,
                MathHelper.cos(ageInTicks * 0.3F) * 0.1F,
                0.0F);
        super.func_225621_a_(entity, stack, ageInTicks, rotationYaw, partialTicks);
    }
}
