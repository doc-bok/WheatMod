package com.bokmcdok.wheat.entity.creature.animal;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class ModNestingEntity extends AnimalEntity {
    private static final DataParameter<Boolean> IS_MALE = EntityDataManager.createKey(ModNestingEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_FERTILIZED = EntityDataManager.createKey(ModNestingEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Optional<BlockPos>> NEST_POSITION = EntityDataManager.createKey(ModNestingEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);

    /**
     * Construction
     * @param type The type of this entity.
     * @param world The current world.
     */
    public ModNestingEntity(EntityType<? extends ModNestingEntity> type, World world) {
        super(type, world);
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        setIsMale(rand.nextBoolean());
        return super.onInitialSpawn(world, difficulty, reason, data, nbt);
    }

    /**
     * Check the gender of the widowbird.
     * @return TRUE if male, FALSE if female.
     */
    public boolean getIsMale() {
        return dataManager.get(IS_MALE); }

    /**
     * Check if the entity can mate with the other animal.
     * @param animal The animal to try and mate with.
     * @return TRUE if the other animal is a widowbird of the opposite gender.
     */
    @Override
    public boolean canMateWith(AnimalEntity animal) {
        if (animal.getType() == getType()) {
            ModNestingEntity mate = (ModNestingEntity)animal;
            return mate.getIsMale() != getIsMale() && super.canMateWith(animal);
        }

        return false;
    }

    /**
     * Can only breed if widowbird doesn't already have a nest.
     * @return TRUE if the widowbird can breed.
     */
    @Override
    public boolean canBreed() {
        return !getHasNest() && !getIsFertilized() && super.canBreed();
    }

    /**
     * Set whether or not this animal is fertilized and should build a nest.
     * @param value The value to set.
     */
    public void setIsFertilized(boolean value) {
        dataManager.set(IS_FERTILIZED, value);
    }

    /**
     * Check if the widowbird is fertilised.
     * @return TRUE if the widowbird is fertilised.
     */
    public boolean getIsFertilized() {
        return dataManager.get(IS_FERTILIZED);
    }

    /**
     * Set the entity's nest position.
     * @param position The position to set.
     */
    public void setNestPosition(BlockPos position) {
        dataManager.set(NEST_POSITION, Optional.of(position));
    }

    /**
     * Remove the nest position.
     */
    public void resetNestPosition() {
        dataManager.set(NEST_POSITION, Optional.empty());
    }

    /**
     * Check whether the widowbird has a nest.
     * @return TRUE if the bird has a nest.
     */
    public boolean getHasNest() {
        return dataManager.get(NEST_POSITION).isPresent();
    }

    /**
     * Get the position of this bird's nest
     * @return TRUE if the nest is here.
     */
    public BlockPos getNestPosition() {
        return dataManager.get(NEST_POSITION).get();
    }

    /**
     * Store NBT data so that gender/breeding status is maintained between saves.
     * @param data The NBT data.
     */
    @Override
    public void writeAdditional(CompoundNBT data) {
        super.writeAdditional(data);
        data.putBoolean("IsMale", getIsMale());
        data.putBoolean("IsFertilized", getIsFertilized());
        data.putBoolean("HasNest", getHasNest());
        if (getHasNest()) {
            data.putInt("NestX", getNestPosition().getX());
            data.putInt("NestY", getNestPosition().getY());
            data.putInt("NestZ", getNestPosition().getZ());
        }
    }

    /**
     * Read NBT data so that gender/breeding status is maintained between saves.
     * @param data The NBT data.
     */
    @Override
    public void readAdditional(CompoundNBT data) {
        super.readAdditional(data);
        setIsMale(data.getBoolean("IsMale"));
        setIsFertilized(data.getBoolean("IsFertilized"));
        if (data.getBoolean("HasNest")) {
            int x = data.getInt("NestX");
            int y = data.getInt("NestY");
            int z = data.getInt("NestZ");
            setNestPosition(new BlockPos(x, y, z));
        }
    }

    /**
     * Save the gender to the data manager.
     */
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(IS_MALE, true);
        dataManager.register(IS_FERTILIZED, false);
        dataManager.register(NEST_POSITION, Optional.empty());
    }

    /**
     * Set the gender of the widowbird.
     * @param isMale TRUE if male, FALSE if female.
     */
    private void setIsMale(boolean isMale) {
        dataManager.set(IS_MALE, isMale);
    }
}
