package com.bokmcdok.wheat.entity.creature.animal.widowbird;

import com.bokmcdok.wheat.entity.creature.ModFlappingBirdModel;
import com.bokmcdok.wheat.entity.creature.ModFlappingBirdRenderer;
import com.bokmcdok.wheat.entity.creature.ModFlappingController;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ModWidowbirdRenderer extends ModFlappingBirdRenderer<ModWidowbirdEntity, ModFlappingBirdModel<ModWidowbirdEntity>> {
    public static final ResourceLocation MALE_TEXTURE = new ResourceLocation("docwheat:textures/entity/animal/widowbird/male.png");
    public static final ResourceLocation FEMALE_TEXTURE = new ResourceLocation("docwheat:textures/entity/animal/widowbird/female.png");

    /**
     * Construction
     * @param rendererManager The entity renderer.
     */
    public ModWidowbirdRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ModFlappingBirdModel<>());
    }

    /**
     * Get the flapping controller for the entity.
     * @return An instance of a flapping controller
     */
    @Override
    protected ModFlappingController getFlappingController(ModWidowbirdEntity entity) {
        return entity.getFlappingController();
    }

    /**
     * Return the correct texture based on the widowbird's gender.
     * @param entity The entity.
     * @return The texture to render.
     */
    @Nullable
    @Override
    public ResourceLocation getEntityTexture(ModWidowbirdEntity entity) {
        return entity.getIsMale() && !entity.isChild() ? MALE_TEXTURE : FEMALE_TEXTURE;
    }
}
