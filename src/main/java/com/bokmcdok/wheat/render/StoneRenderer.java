package com.bokmcdok.wheat.render;

import com.bokmcdok.wheat.entity.StoneEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class StoneRenderer implements IRenderFactory<StoneEntity> {

    @Override
    public EntityRenderer<? super StoneEntity> createRenderFor(EntityRendererManager manager) {
        return new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer());
    }
}