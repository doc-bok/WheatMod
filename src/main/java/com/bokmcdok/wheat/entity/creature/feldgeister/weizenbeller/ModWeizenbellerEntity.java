package com.bokmcdok.wheat.entity.creature.feldgeister.weizenbeller;

import com.bokmcdok.wheat.ai.behaviour.IUsesTags;
import com.bokmcdok.wheat.entity.creature.animal.mouse.ModMouseEntity;
import com.bokmcdok.wheat.entity.creature.animal.widowbird.ModWidowbirdEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.ModFeldgeisterEntity;
import com.bokmcdok.wheat.tag.ModTagRegistrar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ModWeizenbellerEntity extends ModFeldgeisterEntity implements IUsesTags {
    private ModTagRegistrar mTagRegistrar;

    /**
     * Construction
     * @param type  The type of this entity.
     * @param world The current world.
     */
    public ModWeizenbellerEntity(EntityType<? extends ModFeldgeisterEntity> type, World world) {
        super(type, world);
    }

    /**
     * Getreidewolf attacks cause nausea.
     * @param target The target of the attack.
     * @return TRUE if the attack was successful.
     */
    @Override
    public boolean attackEntityAsMob(Entity target) {
        boolean result = target.attackEntityFrom(DamageSource.causeMobDamage(this), (float)getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());
        playSound(SoundEvents.ENTITY_FOX_BITE, 1.0F, 1.0F);
        return result;
    }

    /**
     * Provides the entity with access to the tag registrar.
     * @param tagRegistrar The global tag registrar.
     */
    @Override
    public void setTagRegistrar(ModTagRegistrar tagRegistrar) {
        mTagRegistrar = tagRegistrar;
    }

    /**
     * If a getreidewolf is fed meat they will start to pollinate crops instead of diseasing them.
     * @param player The player interacting with the entity.
     * @param hand The player's hand.
     * @return TRUE if the interaction is handled.
     */
    @Override
    protected boolean processInteract(PlayerEntity player, Hand hand) {
        if (!getIsFed()) {
            ItemStack itemStack = player.getHeldItem(hand);
            Item item = itemStack.getItem();

            if (mTagRegistrar.getItemTag("docwheat:flour").containsItem(item)) {
                if (!player.abilities.isCreativeMode) {
                    itemStack.shrink(1);
                }

                setIsFed(true);
            }

            return true;
        }

        return super.processInteract(player, hand);
    }

    /**
     * Get the height of the entity's eyes.
     * @param pose The current pose.
     * @param size The size of the entity.
     * @return The height of the entity's eyes.
     */
    @Override
    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return size.height * 0.4F;
    }

    /**
     * Register the behaviours of the entity.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();

        goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4f));

        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, 10, false, false, IS_CHILD));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, ChickenEntity.class, 10, false, false, null));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, ModMouseEntity.class, 10, false, false, null));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, ModWidowbirdEntity.class, 10, false, false, null));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, RabbitEntity.class, 10, false, false, null));
    }

    /**
     * Weizenbeller have more health and do more damage than a fox.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3d);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0d);
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0d);
    }

    /**
     * Get the ambient sound.
     * @return The ambient sound of the entity.
     */
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_FOX_AGGRO;
    }

    /**
     * Get the hurt sound.
     * @param source The source of the damage.
     * @return The sound to play when this entity is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_FOX_HURT;
    }

    /**
     * Get the death sound.
     * @return The sound to play when the entity dies.
     */
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER;
    }

    /**
     * Set whether or not the getreidewolf has been fed.
     * @param value TRUE if the getreidewolf has been fed.
     */
    protected void setIsFed(boolean value) {
        super.setIsFed(value);

        if (value) {
            goalSelector.removeGoal(mAttackGoal);
        }
    }
}
