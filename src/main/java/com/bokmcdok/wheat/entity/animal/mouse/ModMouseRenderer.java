package com.bokmcdok.wheat.entity.animal.mouse;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModMouseRenderer extends MobRenderer<ModMouseEntity, ModMouseModel<ModMouseEntity>> {
    private static final ResourceLocation MOUSE_TEXTURES = new ResourceLocation("docwheat:textures/entity/mouse.png");

    public ModMouseRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ModMouseModel<>(), 0.3f);
    }

    @Override
    public ResourceLocation getEntityTexture(ModMouseEntity entity) {
        return MOUSE_TEXTURES;
    }

    @Override
    protected float getDeathMaxRotation(ModMouseEntity entity) {
        return 180.0f;
    }
}
