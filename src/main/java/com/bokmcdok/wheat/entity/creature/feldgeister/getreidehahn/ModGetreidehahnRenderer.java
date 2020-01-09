package com.bokmcdok.wheat.entity.creature.feldgeister.getreidehahn;

import com.bokmcdok.wheat.entity.creature.ModFlappingBirdRenderer;
import com.bokmcdok.wheat.entity.creature.ModFlappingController;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ModGetreidehahnRenderer extends ModFlappingBirdRenderer<ModGetreidehahnEntity, ChickenModel<ModGetreidehahnEntity>> {
    public static final ResourceLocation GETREIDEHAHN_TEXTURE = new ResourceLocation("docwheat:textures/entity/getreidehahn.png");

    /**
     * Construction
     * @param rendererManager The entity renderer.
     */
    public ModGetreidehahnRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ChickenModel<>());
    }

    /**
     * Get the flapping controller for the entity.
     * @return An instance of a flapping controller
     */
    @Override
    protected ModFlappingController getFlappingController(ModGetreidehahnEntity entity) {
        return entity.getFlappingController();
    }

    /**
     * Return the correct texture based on the widowbird's gender.
     * @param entity The entity.
     * @return The texture to render.
     */
    @Nullable
    @Override
    public ResourceLocation getEntityTexture(ModGetreidehahnEntity entity) {
        return GETREIDEHAHN_TEXTURE;
    }
}
