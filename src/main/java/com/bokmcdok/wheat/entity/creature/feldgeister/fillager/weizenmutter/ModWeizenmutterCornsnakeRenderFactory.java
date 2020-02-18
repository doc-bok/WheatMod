package com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter;

import com.bokmcdok.wheat.entity.creature.animal.cornsnake.ModCornsnakeRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModWeizenmutterCornsnakeRenderFactory implements IRenderFactory<ModWeizenmutterCornsnakeEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModWeizenmutterCornsnakeEntity> createRenderFor(EntityRendererManager manager) {
        return new ModCornsnakeRenderer(manager);
    }
}