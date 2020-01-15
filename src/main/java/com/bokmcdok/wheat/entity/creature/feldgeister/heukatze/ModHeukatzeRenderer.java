package com.bokmcdok.wheat.entity.creature.feldgeister.heukatze;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.OcelotModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ModHeukatzeRenderer extends MobRenderer<ModHeukatzeEntity, OcelotModel<ModHeukatzeEntity>> {
    private static final ResourceLocation HEUKATZE_TEXTURES =
            new ResourceLocation("docwheat:textures/entity/feldgeister/heukatze.png");

    public ModHeukatzeRenderer(EntityRendererManager renderer) {
        super(renderer, new OcelotModel<>(0.0f), 0.4f);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(ModHeukatzeEntity entity) {
        return HEUKATZE_TEXTURES;
    }
}