package com.bokmcdok.wheat.entity.feldgeister;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Predicate;

public class ModFeldgeisterEntity extends MonsterEntity {
    protected static final DataParameter<Boolean> FED = EntityDataManager.createKey(ModFeldgeisterEntity.class, DataSerializers.BOOLEAN);
    protected static final Predicate<LivingEntity> IS_CHILD = (entity) -> entity.isChild();

    /**
     * Store NBT data so that status is maintained between saves.
     * @param data The NBT data.
     */
    @Override
    public void writeAdditional(CompoundNBT data) {
        super.writeAdditional(data);
        data.putBoolean("IsFed", getIsFed());
    }

    /**
     * Read NBT data so that status is maintained between saves.
     * @param data The NBT data.
     */
    @Override
    public void readAdditional(CompoundNBT data) {
        super.readAdditional(data);
        setIsFed(data.getBoolean("IsFed"));
    }

    /**
     * Construction
     * @param type The type of this entity.
     * @param world The current world.
     */
    protected ModFeldgeisterEntity(EntityType<? extends ModFeldgeisterEntity> type, World world) {
        super(type, world);
    }

    /**
     * Register data to the data manager.
     */
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(FED, false);
    }

    /**
     * Check whether or not the getreidewolf has been fed.
     * @return TRUE if the getreidewolf has been fed.
     */
    protected boolean getIsFed() {
        return dataManager.get(FED);
    }

    /**
     * Set whether or not the getreidewolf has been fed.
     * @param value TRUE if the getreidewolf has been fed.
     */
    protected void setIsFed(boolean value) {
        dataManager.set(FED, value);
        world.setEntityState(this, (byte)18);
    }

    /**
     * Play enderman teleport sound when removed.
     */
    @Override
    public void remove() {
        super.remove();

        world.playSound(null, prevPosX, prevPosY, prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, getSoundCategory(), 1.0F, 1.0F);
        playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

        if (world.isRemote) {
            for (int i = 0; i < 50; ++i) {
                world.addParticle(ParticleTypes.PORTAL, posX + (rand.nextDouble() - 0.5D) * (double) getWidth(), posY + rand.nextDouble() * (double) getHeight() - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double) getWidth(), (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D);
            }
        }
    }

    /**
     * Show love particles
     * @param status The status update from the server.
     */
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte status) {
        if (status == 18) {
            for(int i = 0; i < 7; ++i) {
                double x = rand.nextGaussian() * 0.02D;
                double y = rand.nextGaussian() * 0.02D;
                double z = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.HEART, posX + (double)(rand.nextFloat() * getWidth() * 2.0F) - (double)getWidth(), posY + 0.5D + (double)(rand.nextFloat() * getHeight()), posZ + (double)(rand.nextFloat() * getWidth() * 2.0F) - (double)getWidth(), x, y, z);
            }
        } else {
            super.handleStatusUpdate(status);
        }
    }
}
