package com.bokmcdok.wheat.entity.creature;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ModFlappingController<T extends CreatureEntity> {
    private final CreatureEntity mOwner;
    private float mFlap;
    private float mFlapSpeed;
    private float mOldFlapSpeed;
    private float mOldFlap;
    private float mFlapping = 1.0F;

    public ModFlappingController(T owner) {
        mOwner = owner;
    }

    /**
     * Calculate flapping motion.
     */
    public void livingTick() {
        mOldFlap = mFlap;
        mOldFlapSpeed = mFlapSpeed;

        mFlapSpeed = mFlapSpeed + (!mOwner.onGround && !mOwner.isPassenger() ? 4.0f : -1.0f) * 0.3f;
        mFlapSpeed = MathHelper.clamp(mFlapSpeed, 0.0f, 1.0f);

        if (!mOwner.onGround && mFlapping < 1.0f) {
            mFlapping = 1.0f;
        }

        mFlapping = mFlapping * 0.9f;

        Vec3d motion = mOwner.getMotion();
        if (!mOwner.onGround && motion.y < 0.0d) {
            mOwner.setMotion(motion.mul(1.0d, 0.6d, 1.0d));
        }

        mFlap += mFlapping * 2.0f;
    }

    /**
     * Get the old flap angle.
     * @return The old flap angle.
     */
    public float getOldFlap() {
        return mOldFlap;
    }

    /**
     * Get the flap angle.
     * @return The flap angle.
     */
    public float getFlap() {
        return mFlap;
    }

    /**
     * Get the old flap speed.
     * @return The old flap speed.
     */
    public float getOldFlapSpeed() {
        return mOldFlapSpeed;
    }

    /**
     * Get the flap speed.
     * @return The flap speed.
     */
    public float getFlapSpeed() {
        return mFlapSpeed;
    }
}
