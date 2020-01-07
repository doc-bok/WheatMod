package com.bokmcdok.wheat.entity.creature.animal.mouse;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModMouseRenderFactory implements IRenderFactory<ModMouseEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModMouseEntity> createRenderFor(EntityRendererManager manager) {
        return new ModMouseRenderer(manager);
    }
}