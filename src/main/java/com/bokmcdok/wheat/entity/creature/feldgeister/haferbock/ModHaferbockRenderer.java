package com.bokmcdok.wheat.entity.creature.feldgeister.haferbock;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModHaferbockRenderer extends MobRenderer<ModHaferbockEntity, ModHaferbockModel<ModHaferbockEntity>> {
    private static final ResourceLocation HAFERBOCK_TEXTURES = new ResourceLocation("docwheat:textures/entity/feldgeister/haferbock.png");

    /**
     * Construction
     * @param renderManager The render manager.
     */
    public ModHaferbockRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModHaferbockModel<>(), 0.7F);
    }

    /**
     * Get the entity textures.
     * @param entity The entity to get textures for.
     * @return The textures to use.
     */
    public ResourceLocation getEntityTexture(ModHaferbockEntity entity) {
        return HAFERBOCK_TEXTURES;
    }
}