package com.bokmcdok.wheat.entity.feldgeister.haferbock;

import com.bokmcdok.wheat.entity.feldgeister.heukatze.ModHeukatzeRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModHaferbockRenderFactory implements IRenderFactory<ModHaferbockEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModHaferbockEntity> createRenderFor(EntityRendererManager manager) {
        return new ModHaferbockRenderer(manager);
    }
}