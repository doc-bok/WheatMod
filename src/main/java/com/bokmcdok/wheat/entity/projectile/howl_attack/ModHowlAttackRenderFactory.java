package com.bokmcdok.wheat.entity.projectile.howl_attack;

import com.bokmcdok.wheat.entity.feldgeister.getreidewolf.ModGetreidewolfRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class ModHowlAttackRenderFactory implements IRenderFactory<ModHowlAttackEntity> {
    /**
     * Create an entity renderer.
     * @param manager The entity render manager.
     * @return The new entity renderer.
     */
    @Override
    public EntityRenderer<? super ModHowlAttackEntity> createRenderFor(EntityRendererManager manager) {
        return new ModHowlAttackRenderer(manager);
    }
}