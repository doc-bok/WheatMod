package com.bokmcdok.wheat.entity.creature;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.util.math.MathHelper;

public abstract class ModFlappingBirdRenderer<T extends CreatureEntity, U extends AgeableModel<T>> extends MobRenderer<T, U> {

    /**
     * Construction
     * @param rendererManager The entity renderer.
     */
    public ModFlappingBirdRenderer(EntityRendererManager rendererManager, U model) {
        super(rendererManager, model, 0.3f);
    }

    /**
     * Handle the rotations for the animated wings.
     * @param entity The entity.
     * @param partialTicks The time delta.
     * @return The updated angle.
     */
    @Override
    protected float handleRotationFloat(T entity, float partialTicks) {
        ModFlappingController flappingController = getFlappingController(entity);
        float f = MathHelper.lerp(partialTicks, flappingController.getOldFlap(), flappingController.getFlap());
        float f1 = MathHelper.lerp(partialTicks, flappingController.getOldFlapSpeed(), flappingController.getFlapSpeed());
        return (MathHelper.sin(f) + 1.0F) * f1;
    }

    /**
     * Get the flapping controller for the entity.
     * @return An instance of a flapping controller
     */
    protected abstract ModFlappingController getFlappingController(T entity);
}
