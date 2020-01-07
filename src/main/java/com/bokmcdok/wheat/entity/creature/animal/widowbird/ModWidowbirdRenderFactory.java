package com.bokmcdok.wheat.entity.creature.animal.widowbird;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModWidowbirdRenderFactory implements IRenderFactory<ModWidowbirdEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModWidowbirdEntity> createRenderFor(EntityRendererManager manager) {
        return new ModWidowbirdRenderer(manager);
    }
}
