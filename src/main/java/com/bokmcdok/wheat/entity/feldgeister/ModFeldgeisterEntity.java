package com.bokmcdok.wheat.entity.feldgeister;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

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
    }
}
