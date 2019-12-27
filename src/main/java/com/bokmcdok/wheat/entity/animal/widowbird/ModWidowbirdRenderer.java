package com.bokmcdok.wheat.entity.animal.widowbird;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ModWidowbirdRenderer extends MobRenderer<ModWidowbirdEntity, ModWidowbirdModel> {
    public static final ResourceLocation MALE_TEXTURE = new ResourceLocation("docwheat:textures/entity/widowbird/widowbird_male.png");
    public static final ResourceLocation FEMALE_TEXTURE = new ResourceLocation("docwheat:textures/entity/widowbird/widowbird_female.png");

    /**
     * Construction
     * @param rendererManager The entity renderer.
     */
    public ModWidowbirdRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ModWidowbirdModel(), 0.3f);
    }

    /**
     * Handle the rotations for the animated wings.
     * @param entity The entity.
     * @param partialTicks The time delta.
     * @return The updated angle.
     */
    @Override
    protected float handleRotationFloat(ModWidowbirdEntity entity, float partialTicks) {
        float f = MathHelper.lerp(partialTicks, entity.getOldFlap(), entity.getFlap());
        float f1 = MathHelper.lerp(partialTicks, entity.getOldFlapSpeed(), entity.getFlapSpeed());
        return (MathHelper.sin(f) + 1.0F) * f1;
    }

    /**
     * Return the correct texture based on the widowbird's gender.
     * @param entity The entity.
     * @return The texture to render.
     */
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(ModWidowbirdEntity entity) {
        return entity.getIsMale() ? MALE_TEXTURE : FEMALE_TEXTURE;
    }
}
