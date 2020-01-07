package com.bokmcdok.wheat.entity.creature.feldgeister.heukatze;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModHeukatzeRenderFactory implements IRenderFactory<ModHeukatzeEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModHeukatzeEntity> createRenderFor(EntityRendererManager manager) {
        return new ModHeukatzeRenderer(manager);
    }
}