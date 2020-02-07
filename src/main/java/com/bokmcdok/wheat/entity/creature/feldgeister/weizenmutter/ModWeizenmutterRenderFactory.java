package com.bokmcdok.wheat.entity.creature.feldgeister.weizenmutter;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModWeizenmutterRenderFactory implements IRenderFactory<ModWeizenmutterEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModWeizenmutterEntity> createRenderFor(EntityRendererManager manager) {
        return new ModWeizenmutterRenderer(manager);
    }
}