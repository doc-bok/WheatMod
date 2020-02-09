package com.bokmcdok.wheat.entity.creature.feldgeister.fillager;

import com.bokmcdok.wheat.entity.creature.feldgeister.ModFeldgeisterEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class ModFillagerEntity extends ModFeldgeisterEntity {
    /**
     * Construction
     * @param type  The type of this entity.
     * @param world The current world.
     */
    protected ModFillagerEntity(EntityType<? extends ModFeldgeisterEntity> type, World world) {
        super(type, world);
    }

    /**
     * Get the arm pose of the Fillager Entity
     * @return The current arm pose of the Fillager.
     */
    @OnlyIn(Dist.CLIENT)
    public AbstractIllagerEntity.ArmPose getArmPose()
    {
        return AbstractIllagerEntity.ArmPose.NEUTRAL;
    }

    /**
     * Get the texture to use with this entity.
     * @return The resource location of the texture.
     */
    public abstract ResourceLocation getTexture();
}
