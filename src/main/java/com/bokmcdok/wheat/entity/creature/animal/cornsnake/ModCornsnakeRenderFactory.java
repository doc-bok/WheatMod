package com.bokmcdok.wheat.entity.creature.animal.cornsnake;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModCornsnakeRenderFactory implements IRenderFactory<ModCornsnakeEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModCornsnakeEntity> createRenderFor(EntityRendererManager manager) {
        return new ModCornsnakeRenderer(manager);
    }
}