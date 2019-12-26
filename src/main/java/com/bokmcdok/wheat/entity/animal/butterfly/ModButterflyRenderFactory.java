package com.bokmcdok.wheat.entity.animal.butterfly;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModButterflyRenderFactory implements IRenderFactory<ModButterflyEntity> {

    @Override
    public EntityRenderer<? super ModButterflyEntity> createRenderFor(EntityRendererManager manager) {
        return new ModButterflyRenderer(manager);
    }
}
