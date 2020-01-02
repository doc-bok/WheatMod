package com.bokmcdok.wheat.entity.feldgeister.weizenbeller;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModWeizenbellerRenderFactory implements IRenderFactory<ModWeizenbellerEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModWeizenbellerEntity> createRenderFor(EntityRendererManager manager) {
        return new ModWeizenbellerRenderer(manager);
    }
}