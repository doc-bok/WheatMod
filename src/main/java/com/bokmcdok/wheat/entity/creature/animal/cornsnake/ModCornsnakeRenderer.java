package com.bokmcdok.wheat.entity.creature.animal.cornsnake;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class ModCornsnakeRenderer extends MobRenderer<ModCornsnakeEntity, ModCornsnakeModel<ModCornsnakeEntity>> {
    private static final ResourceLocation TEXTURES = new ResourceLocation("docwheat:textures/entity/animal/cornsnake/plains.png");

    /**
     * Construction
     * @param rendererManager The render manager
     */
    public ModCornsnakeRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ModCornsnakeModel<>(), 0.3f);
    }

    /**
     * Get the texture to use.
     * @param entity The entity to get the texture for.
     * @return The texture to use.
     */
    @Override
    public ResourceLocation getEntityTexture(ModCornsnakeEntity entity) {
        return TEXTURES;
    }

    /**
     * Get the max rotation for death.
     * @param entity The entity.
     * @return The maximum rotation on death.
     */
    @Override
    protected float getDeathMaxRotation(ModCornsnakeEntity entity) {
        return 180.0f;
    }
}
