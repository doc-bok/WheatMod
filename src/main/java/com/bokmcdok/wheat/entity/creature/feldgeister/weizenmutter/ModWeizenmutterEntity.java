package com.bokmcdok.wheat.entity.creature.feldgeister.weizenmutter;

import com.bokmcdok.wheat.entity.creature.feldgeister.ModFeldgeisterEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.ModFillagerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ModWeizenmutterEntity extends ModFillagerEntity {
    private static final ResourceLocation TEXTURE = new ResourceLocation("docwheat:textures/entity/feldgeister/weizenmutter.png");

    /**
     * Construction
     * @param type  The type of this entity.
     * @param world The current world.
     */
    public ModWeizenmutterEntity(EntityType<? extends ModFeldgeisterEntity> type, World world) {
        super(type, world);
    }

    /**
     * Get the texture to use with this entity.
     * @return The resource location of the texture.
     */
    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    /**
     * Weizenmutters are similar to witches.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(26.0D);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    /**
     * Get the ambient sound.
     * @return The ambient sound of the entity.
     */
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITCH_AMBIENT;
    }

    /**
     * Get the hurt sound.
     * @param source The source of the damage.
     * @return The sound to play when this entity is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WITCH_HURT;
    }

}
