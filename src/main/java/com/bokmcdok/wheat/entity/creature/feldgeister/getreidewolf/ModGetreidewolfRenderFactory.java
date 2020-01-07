package com.bokmcdok.wheat.entity.creature.feldgeister.getreidewolf;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModGetreidewolfRenderFactory implements IRenderFactory<ModGetreidewolfEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModGetreidewolfEntity> createRenderFor(EntityRendererManager manager) {
        return new ModGetreidewolfRenderer(manager);
    }
}