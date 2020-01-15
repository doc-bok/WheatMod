package com.bokmcdok.wheat.block;

import net.minecraft.util.ResourceLocation;

public class ModNestProperties {
    private ResourceLocation mEntityToSpawn;
    private int mMinimum;
    private int mMaximum;
    private float mSpawnChance;

    /**
     * The entity that is spawned by this nest.
     * @param value The resource location of the entity type.
     */
    public void setEntityToSpawn(ResourceLocation value) {
        mEntityToSpawn = value;
    }

    /**
     * Set the minimum number of animals to spawn.
     * @param value The minimum value.
     */
    public void setMinimum(int value) {
        mMinimum = value;
    }

    /**
     * Set the maximum number of animals to spawn.
     * @param value The maximum value.
     */
    public void setMaximum(int value) {
        mMaximum = value;
    }

    /**
     * Set the number of animals to spawn.
     * @param value The number of animals to spawn.
     */
    public void setCount(int value) {
        setMinimum(value);
        setMaximum(value);
    }

    /**
     * Set the chance that the animals are born.
     * @param value The chance each random tick that the animals are born.
     */
    public void setSpawnChance(float value) {
        mSpawnChance = value;
    }

    /**
     * Get the entity to spawn.
     * @return The resource location of the entity.
     */
    public ResourceLocation getEntityToSpawn() {
        return mEntityToSpawn;
    }

    /**
     * Get the minimum number to spawn.
     * @return The minimum number.
     */
    public int getMinimum() {
        return mMinimum;
    }

    /**
     * Get the maximum number to spawn.
     * @return The maximum number.
     */
    public int getMaximum() {
        return mMaximum;
    }

    /**
     * Get the chance each random tick that the entities will spawn.
     * @return The random spawn chance.
     */
    public float getSpawnChance() {
        return mSpawnChance;
    }
}
