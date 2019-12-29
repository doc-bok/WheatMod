package com.bokmcdok.wheat.entity.projectile.howl_attack;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ModHowlAttackRenderer extends EntityRenderer<ModHowlAttackEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("docwheat:textures/entity/projectile/howl_attack.png");
    private final ModHowlAttackModel mModel = new ModHowlAttackModel<>();

    /**
     * Construction
     * @param renderer The entity renderer.
     */
    public ModHowlAttackRenderer(EntityRendererManager renderer) {
        super(renderer);
    }

    /**
     * Render the projectile.
     * @param entity The entity for this projectile.
     * @param x The x-position.
     * @param y The y-position.
     * @param z The z-position.
     * @param entityYaw The yaw.
     * @param partialTicks The time delta.
     */
    @Override
    public void doRender(ModHowlAttackEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x, (float)y + 0.15F, (float)z);
        GlStateManager.rotatef(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw) - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch), 0.0F, 0.0F, 1.0F);
        bindEntityTexture(entity);
        if (renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(getTeamColor(entity));
        }

        mModel.render(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        if (renderOutlines) {
            GlStateManager.tearDownSolidRenderingTextureCombine();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Get the entity texture.
     * @param modHowlAttackEntity The entity for this projectile.
     * @return The texture resource to use.
     */
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(ModHowlAttackEntity modHowlAttackEntity) {
        return TEXTURE;
    }
}
