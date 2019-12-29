package com.bokmcdok.wheat.entity.projectile.howl_attack;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.entity.feldgeister.getreidewolf.ModGetreidewolfEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.UUID;

@ObjectHolder(WheatMod.MOD_ID)
public class ModHowlAttackEntity extends Entity implements IProjectile {
    public static final EntityType<ModHowlAttackEntity> howl_attack = null;

    private ModGetreidewolfEntity mOwner;
    private CompoundNBT mOwnerNBT;

    /**
     * Construct a howl attack from just an entity type.
     * @param type The type of the entity.
     * @param world The current world.
     */
    public ModHowlAttackEntity(EntityType<? extends ModHowlAttackEntity> type, World world) {
        super(type, world);
    }

    /**
     * Construct a howl attack at the owner's position.
     * @param world The current world.
     * @param owner The owner of the howl attack.
     */
    public ModHowlAttackEntity(World world, ModGetreidewolfEntity owner) {
        this(howl_attack, world);
        mOwner = owner;
                setPosition(
                owner.posX - (owner.getWidth() + 1.0d) * 0.5d * MathHelper.sin(owner.renderYawOffset * (float)(Math.PI / 180.0d)),
                owner.posY + owner.getEyeHeight() - 0.1d,
                owner.posZ + (owner.getWidth() + 1.0d) * 0.5d * MathHelper.cos(owner.renderYawOffset * (float)(Math.PI / 180F)));
    }

    /**
     * Client side construction. Creates a particle effect to render.
     * @param world The current world.
     * @param x The x-position.
     * @param y The y-position.
     * @param z The z-position.
     * @param xSpeed Speed along the x-axis.
     * @param ySpeed Speed along the y-axis.
     * @param zSpeed Speed along the z-axis.
     */
    @OnlyIn(Dist.CLIENT)
    public ModHowlAttackEntity(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this(howl_attack, world);
        setPosition(x, y, z);

        for (int i = 0; i < 7; ++i) {
            double d0 = 0.4d + 0.1d * i;
            world.addParticle(ParticleTypes.SWEEP_ATTACK, x, y, z, xSpeed * d0, ySpeed * d0, zSpeed *d0);
        }

        setMotion(xSpeed, ySpeed, zSpeed);
    }

    /**
     * Tick - check for collisions and update the projectile's position.
     */
    @Override
    public void tick() {
        super.tick();
        if (mOwnerNBT != null) {
            restoreOwnerFromSave();
        }

        Vec3d motion = getMotion();
        RayTraceResult rayTrace = ProjectileHelper.func_221267_a(this, getBoundingBox().expand(motion).grow(1.d), (x) -> !x.isSpectator() && x != mOwner, RayTraceContext.BlockMode.OUTLINE, true);
        if (rayTrace != null && rayTrace.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, rayTrace)) {
            onHit(rayTrace);
        }

        posX += motion.x;
        posY += motion.y;
        posZ += motion.z;
        float motionRoot = MathHelper.sqrt(func_213296_b(motion));
        rotationYaw = (float)(MathHelper.atan2(motion.x, motion.z) * (180.0d / Math.PI));

        for(rotationPitch = (float)(MathHelper.atan2(motion.y, motionRoot) * (180.0d / Math.PI));
            rotationPitch - prevRotationPitch < -180.0F;
            prevRotationPitch -= 360.0F);

        while(rotationPitch - prevRotationPitch >= 180.0F) {
            prevRotationPitch += 360.0F;
        }

        while(rotationYaw - prevRotationYaw < -180.0F) {
            prevRotationYaw -= 360.0F;
        }

        while(rotationYaw - prevRotationYaw >= 180.0F) {
            prevRotationYaw += 360.0F;
        }

        rotationPitch = MathHelper.lerp(0.2F, prevRotationPitch, rotationPitch);
        rotationYaw = MathHelper.lerp(0.2F, prevRotationYaw, rotationYaw);

        if (!world.isMaterialInBB(getBoundingBox(), Material.AIR)) {
            remove();
        } else if (isInWaterOrBubbleColumn()) {
            remove();
        } else {
            setMotion(motion.scale(0.99d));
            if (!hasNoGravity()) {
                setMotion(getMotion().add(0.0d, -0.06d, 0.0d));
            }

            setPosition(posX, posY, posZ);
        }
    }

    /**
     * Set the velocity of the projectile on the client.
     * @param x The x-speed.
     * @param y The y-speed.
     * @param z The z-speed.
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void setVelocity(double x, double y, double z) {
        setMotion(x, y, z);
        if (prevRotationPitch == 0.0f && prevRotationYaw == 0.0f) {
            float hypotenuse = MathHelper.sqrt(x * x + z * z);

            rotationPitch = (float)(MathHelper.atan2(y, hypotenuse) * (180.0d / Math.PI));
            rotationYaw = (float)(MathHelper.atan2(x, z) * (180.0d / (float)Math.PI));

            prevRotationPitch = rotationPitch;
            prevRotationYaw = rotationYaw;

            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
        }
    }

    /**
     * Launches the projectile.
     * @param x The x-direction.
     * @param y The y-direction.
     * @param z The z-direction.
     * @param speed The speed.
     * @param inaccuracy The inaccuracy of the projectile.
     */
    @Override
    public void shoot(double x, double y, double z, float speed, float inaccuracy) {
        Vec3d motion = (new Vec3d(x, y, z)).normalize().add(rand.nextGaussian() * 0.0075d * inaccuracy, rand.nextGaussian() * 0.0075d * inaccuracy, rand.nextGaussian() * 0.0075d * inaccuracy).scale(speed);
        setMotion(motion);
        float hypotenuse = MathHelper.sqrt(func_213296_b(motion));

        rotationYaw = (float)(MathHelper.atan2(motion.x, z) * (180.0d / Math.PI));
        rotationPitch = (float)(MathHelper.atan2(motion.y, hypotenuse) * (180.0d / Math.PI));

        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
    }

    /**
     * Create a packet to tell clients to spawn this object.
     * @return The packet to send to clients.
     */
    @Override
    public IPacket<?> createSpawnPacket() {
        return new SSpawnObjectPacket(this);
    }

    /**
     * No extra data for this projectile.
     */
    @Override
    protected void registerData() {
        //no-op
    }

    /**
     * Use NBT data to save state.
     * @param compound The NBT data.
     */
    @Override
    protected void readAdditional(CompoundNBT compound) {
        if (compound.contains("Owner", 10)) {
            mOwnerNBT = compound.getCompound("Owner");
        }
    }

    /**
     * Use NBT data to save state.
     * @param compound The NBT data.
     */
    @Override
    protected void writeAdditional(CompoundNBT compound) {
        if (mOwner != null) {
            CompoundNBT compoundnbt = new CompoundNBT();
            UUID uuid = mOwner.getUniqueID();
            compoundnbt.putUniqueId("OwnerUUID", uuid);
            compound.put("Owner", compoundnbt);
        }
    }

    /**
     * Handle projectile collisions.
     * @param rayTraceResult The result from the ray trace.
     */
    private void onHit(RayTraceResult rayTraceResult) {
        RayTraceResult.Type type = rayTraceResult.getType();
        if (type == RayTraceResult.Type.ENTITY && mOwner != null) {
            ((EntityRayTraceResult)rayTraceResult).getEntity().attackEntityFrom(DamageSource.causeIndirectDamage(this, mOwner).setProjectile(), 1.0f);
        } else if (type == RayTraceResult.Type.BLOCK && !world.isRemote()) {
            remove();
        }
    }

    /**
     * Restore the owner from a save after reloading the world.
     */
    private void restoreOwnerFromSave() {
        if (mOwnerNBT != null && mOwnerNBT.hasUniqueId("OwnerUUID")) {
            UUID uuid = mOwnerNBT.getUniqueId("OwnerUUID");

            for (ModGetreidewolfEntity entity : world.getEntitiesWithinAABB(ModGetreidewolfEntity.class, getBoundingBox().grow(15.0d))) {
                if (entity.getUniqueID().equals(uuid)) {
                    mOwner = entity;
                    break;
                }
            }
        }

        mOwnerNBT = null;
    }
}
