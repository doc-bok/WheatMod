package com.bokmcdok.wheat.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ThrownItemEntity extends ProjectileItemEntity {

    private ItemStack stone = null;

    /**
     * Construction
     * @param type The type of entity to create
     * @param world The current world
     */
    ThrownItemEntity(EntityType<? extends ThrownItemEntity> type, World world) {
        super(type, world);
    }

    /**
     * Construction
     * @param worldIn The current world
     * @param throwerIn The entity that threw the stone
     */
    public ThrownItemEntity(World worldIn, LivingEntity throwerIn) {
        super(EntityType.SNOWBALL, throwerIn, worldIn);
    }

    /**
     * Set the item used by this entity
     * @param stack The item stack to use
     */
    @Override
    public void setItem(ItemStack stack) {
        super.setItem(stack);
        stone = stack;
    }

    /**
     * Handler for {@link World#setEntityState}
     * @param id ???
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            IParticleData iparticledata = getParticleData();

            Vec3d position = getPositionVec();
            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(iparticledata, position.x, position.y, position.z, 0.0D, 0.0D, 0.0D);
            }
        }

    }

    /**
     * Get the item being thrown
     * @return The type of stone being thrown
     */
    @Override
    protected Item getDefaultItem() {
        return stone != null ? stone.getItem() : Items.SNOWBALL;
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     * @param result The result of the raytrace
     */
    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult)result).getEntity();
            entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()),1.0f);
        }

        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }

    /**
     * Get the particle data for impacts
     * @return The particles to render on impact
     */
    @OnlyIn(Dist.CLIENT)
    private IParticleData getParticleData() {
        ItemStack itemstack = this.func_213882_k();
        return (itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleData(ParticleTypes.ITEM, itemstack));
    }
}
