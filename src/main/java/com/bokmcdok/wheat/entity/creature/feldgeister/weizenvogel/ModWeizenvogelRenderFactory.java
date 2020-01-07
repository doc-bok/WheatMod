package com.bokmcdok.wheat.entity.creature.feldgeister.weizenvogel;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModWeizenvogelRenderFactory implements IRenderFactory<ModWeizenvogelEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModWeizenvogelEntity> createRenderFor(EntityRendererManager manager) {
        return new ModWeizenvogelRenderer(manager);
    }
}
