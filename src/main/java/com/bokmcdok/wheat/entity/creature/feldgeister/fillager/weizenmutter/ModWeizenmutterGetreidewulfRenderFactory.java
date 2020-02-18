package com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter;

import com.bokmcdok.wheat.entity.creature.feldgeister.getreidewolf.ModGetreidewolfRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModWeizenmutterGetreidewulfRenderFactory implements IRenderFactory<ModWeizenmutterGetreidewulfEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModWeizenmutterGetreidewulfEntity> createRenderFor(EntityRendererManager manager) {
        return new ModGetreidewolfRenderer(manager);
    }
}