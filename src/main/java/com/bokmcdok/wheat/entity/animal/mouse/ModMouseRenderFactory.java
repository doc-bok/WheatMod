package com.bokmcdok.wheat.entity.animal.mouse;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModMouseRenderFactory implements IRenderFactory<ModMouseEntity> {

    @Override
    public EntityRenderer<? super ModMouseEntity> createRenderFor(EntityRendererManager manager) {
        return new ModMouseRenderer(manager);
    }
}