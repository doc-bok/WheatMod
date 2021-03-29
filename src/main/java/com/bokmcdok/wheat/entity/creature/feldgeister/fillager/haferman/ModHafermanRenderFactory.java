package com.bokmcdok.wheat.entity.creature.feldgeister.fillager.haferman;

import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.ModFillagerRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModHafermanRenderFactory implements IRenderFactory<ModHafermanEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModHafermanEntity> createRenderFor(EntityRendererManager manager) {
        return new ModFillagerRenderer(manager);
    }
}
