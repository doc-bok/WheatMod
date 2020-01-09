package com.bokmcdok.wheat.entity.creature.feldgeister.getreidehahn;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModGetreidehahnRenderFactory implements IRenderFactory<ModGetreidehahnEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModGetreidehahnEntity> createRenderFor(EntityRendererManager manager) {
        return new ModGetreidehahnRenderer(manager);
    }
}
