package com.bokmcdok.wheat.entity.creature.feldgeister.weizenbeller;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ModWeizenbellerRenderer extends MobRenderer<ModWeizenbellerEntity, ModWeizenbellerModel<ModWeizenbellerEntity>> {
    private static final ResourceLocation WEIZENBELLER_TEXTURES =
            new ResourceLocation("docwheat:textures/entity/feldgeister/weizenbeller.png");

    /**
     * Construction
     * @param rendererManager The entity renderer.
     */
    public ModWeizenbellerRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ModWeizenbellerModel<>(), 0.4f);
    }

    /**
     * Get the texture to render the getreidewolf.
     * @param entity The entity.
     * @return The texture to use.
     */
    @Nullable
    @Override
    public ResourceLocation getEntityTexture(ModWeizenbellerEntity entity) {
        return WEIZENBELLER_TEXTURES;
    }
}
