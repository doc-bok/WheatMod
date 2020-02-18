package com.bokmcdok.wheat.entity.creature.feldgeister.fillager.ahrenkind;

import com.bokmcdok.wheat.ai.behaviour.IGetsAngry;
import com.bokmcdok.wheat.ai.target.ModAttackIfAngryGoal;
import com.bokmcdok.wheat.ai.target.ModHurtByTargetGoal;
import com.bokmcdok.wheat.entity.creature.feldgeister.ModFeldgeisterEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.ModFillagerEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.getreidewolf.ModGetreidewolfEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter.ModWeizenmutterEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class ModAhrenkindEntity extends ModFillagerEntity implements IGetsAngry {
    private static final ResourceLocation TEXTURE = new ResourceLocation("docwheat:textures/entity/feldgeister/ahrenkind.png");

    private UUID mAngerTargetUUID;
    private int mAngerLevel;
    private int mRandomSoundDelay;

    /**
     * Construction
     * @param type  The type of this entity.
     * @param world The current world.
     */
    public ModAhrenkindEntity(EntityType<? extends ModFeldgeisterEntity> type, World world) {
        super(type, world);
    }

    /**
     * Update the target to get revenge upon.
     * @param target The entity to target.
     */
    @Override
    public void setRevengeTarget(@Nullable LivingEntity target) {
        super.setRevengeTarget(target);
        if (target != null) {
            mAngerLevel = 400 + getNewAngerLevel();
            mRandomSoundDelay = rand.nextInt(40);
            mAngerTargetUUID = target.getUniqueID();
        }
    }

    /**
     * Check whether the entity is angry or not.
     * @return TRUE if the entity is angry.
     */
    @Override
    public boolean isAngry() {
        return mAngerLevel > 0;
    }

    /**
     * Write additional data that needs to be saved.
     * @param data The NBT data.
     */
    @Override
    public void writeAdditional(CompoundNBT data) {
        super.writeAdditional(data);
        data.putShort("Anger", (short)mAngerLevel);
        if (mAngerTargetUUID != null) {
            data.putString("HurtBy", mAngerTargetUUID.toString());
        } else {
            data.putString("HurtBy", "");
        }
    }

    /**
     * Read any additonal saved data.
     * @param data The NBT data.
     */
    @Override
    public void readAdditional(CompoundNBT data) {
        super.readAdditional(data);
        mAngerLevel = data.getShort("Anger");
        String hurtBy = data.getString("HurtBy");
        if (!hurtBy.isEmpty()) {
            mAngerTargetUUID = UUID.fromString(hurtBy);
            PlayerEntity player = world.getPlayerByUuid(mAngerTargetUUID);
            setRevengeTarget(player);
            if (player != null) {
                attackingPlayer = player;
                recentlyHit = getRevengeTimer();
            }
        }
    }

    /**
     * Set the revenge target if a player attacks.
     * @param source The source of the damage.
     * @param amount The amount of damage.
     * @return TRUE if damage is inflicted.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getTrueSource();
            if (entity instanceof PlayerEntity && !((PlayerEntity)entity).isCreative() && canEntityBeSeen(entity)) {
                setRevengeTarget((LivingEntity)entity);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    /**
     * Prevent player rest if the Ahrenkind is angry.
     * @param player The player entity.
     * @return TRUE if the mob is angry.
     */
    @Override
    public boolean isPreventingPlayerRest(PlayerEntity player) {
        return isAngry();
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
     * Ahrenkind are children.
     * @return Always TRUE.
     */
    @Override
    public boolean isChild() {
        return true;
    }

    /**
     * Ahrenkind will attack in groups alongside Weizenmutters and Getreidewulfs.
     */
    @Override
    protected void registerGoals() {
        goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        goalSelector.addGoal(8, new LookRandomlyGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, getSpeed() * 2.0d, false));
        goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, getSpeed()));

        targetSelector.addGoal(1, new ModHurtByTargetGoal(this,
                ModAhrenkindEntity.class,
                ModGetreidewolfEntity.class,
                ModWeizenmutterEntity.class));

        targetSelector.addGoal(2, ModAttackIfAngryGoal.create(this, PlayerEntity.class, true));
    }

    /**
     * Register the ahrenkind attributes.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35F);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
    }

    @Override
    protected void updateAITasks() {
        LivingEntity revengeTarget = getRevengeTarget();
        if (isAngry()) {
            --mAngerLevel;

            LivingEntity target = revengeTarget != null ? revengeTarget : getAttackTarget();
            if (isAngry() && target != null) {
                if (!canEntityBeSeen(target)) {
                    setRevengeTarget(null);
                    setAttackTarget(null);
                } else {
                    mAngerLevel = getNewAngerLevel();
                }
            }
        }

        if (mRandomSoundDelay > 0 && --mRandomSoundDelay == 0) {
            playSound(SoundEvents.ENTITY_PILLAGER_AMBIENT, getSoundVolume() * 2.0f, ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }

        if (isAngry() && mAngerTargetUUID != null && revengeTarget == null) {
            PlayerEntity player = world.getPlayerByUuid(mAngerTargetUUID);
            setRevengeTarget(player);
            attackingPlayer = player;
            recentlyHit = getRevengeTimer();
        }

        super.updateAITasks();
    }

    /**
     * Get the ambient sound.
     * @return The entity's ambient sound.
     */
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PILLAGER_AMBIENT;
    }

    /**
     * Get the hurt sound.
     * @param source The source of the damage.
     * @return The sound to play when this entity is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PILLAGER_HURT;
    }

    /**
     * Generate a new anger level.
     * @return A randomised number of ticks for the entity to remain angry.
     */
    private int getNewAngerLevel() {
        return 400 + rand.nextInt(400);
    }
}
