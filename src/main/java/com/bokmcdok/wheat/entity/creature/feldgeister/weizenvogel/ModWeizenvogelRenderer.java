package com.bokmcdok.wheat.entity.creature.feldgeister.weizenvogel;

import com.bokmcdok.wheat.entity.creature.ModFlappingBirdModel;
import com.bokmcdok.wheat.entity.creature.ModFlappingBirdRenderer;
import com.bokmcdok.wheat.entity.creature.ModFlappingController;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ModWeizenvogelRenderer extends ModFlappingBirdRenderer<ModWeizenvogelEntity, ModFlappingBirdModel<ModWeizenvogelEntity>> {
    public static final ResourceLocation WEIZENVOGEL_TEXTURE = new ResourceLocation("docwheat:textures/entity/feldgeister/weizenvogel.png");

    /**
     * Construction
     * @param rendererManager The entity renderer.
     */
    public ModWeizenvogelRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ModFlappingBirdModel<>());
    }

    /**
     * Get the flapping controller for the entity.
     * @return An instance of a flapping controller
     */
    @Override
    protected ModFlappingController getFlappingController(ModWeizenvogelEntity entity) {
        return entity.getFlappingController();
    }

    /**
     * Return the correct texture based on the widowbird's gender.
     * @param entity The entity.
     * @return The texture to render.
     */
    @Nullable
    @Override
    public ResourceLocation getEntityTexture(ModWeizenvogelEntity entity) {
        return WEIZENVOGEL_TEXTURE;
    }
}