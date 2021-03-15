package com.bokmcdok.wheat.entity.creature;

import com.bokmcdok.wheat.entity.creature.player.ModPlayerEntityEventHandler;
import com.bokmcdok.wheat.entity.creature.villager.ModVillagerEventHandler;
import com.bokmcdok.wheat.item.ModDamageType;
import com.bokmcdok.wheat.item.ModItemTrait;
import com.bokmcdok.wheat.item.ModWeaponItem;
import com.bokmcdok.wheat.tag.ModTagRegistrar;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;


public class ModLivingEntityEventHandler {
    private final ModPlayerEntityEventHandler mPlayerEntityEventHandler;
    private final ModVillagerEventHandler mVillagerEventHandler;
    private final ModTagRegistrar mTagRegistrar;

    /**
     * Construction
     *
     * @param tagRegistrar Registrar holding the item tags.
     */
    public ModLivingEntityEventHandler(ModTagRegistrar tagRegistrar) {
        mTagRegistrar = tagRegistrar;
        mPlayerEntityEventHandler = new ModPlayerEntityEventHandler();
        mVillagerEventHandler = new ModVillagerEventHandler(mTagRegistrar);
    }

    /**
     * Fired whenever an entity joins the world.
     *
     * @param event The event data.
     */
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        mVillagerEventHandler.onEntityJoinWorldEvent(event);
    }

    /**
     * Fired when a villager creates its trades.
     *
     * @param event The event data.
     */
    public void onVillagerTradesEvent(VillagerTradesEvent event) {
        mVillagerEventHandler.onVillagerTradesEvent(event);
    }

    /**
     * Fired when a living entity does an update.
     *
     * @param event The event data.
     */
    public void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        mPlayerEntityEventHandler.onLivingUpdateEvent(event);
        mVillagerEventHandler.onLivingUpdateEvent(event);
    }

    /**
     * Custom code for player attacks.
     *
     * @param event The attack event.
     */
    public void onAttackEntityEvent(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
        Item item = heldItem.getItem();
        if (item instanceof ModWeaponItem) {
            ModWeaponItem weapon = (ModWeaponItem) item;

            //	Prevent normal attack code from running.
            event.setCanceled(true);

            Entity targetEntity = event.getTarget();
            if (targetEntity.canBeAttackedWithItem()) {
                if (!targetEntity.hitByEntity(player)) {
                    float attackDamage = (float) player.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();

                    float modifier;
                    if (targetEntity instanceof LivingEntity) {

                        //  Bludgeoning weapons do extra damage against any kind of  skeleton.
                        if (weapon.getDamageType() == ModDamageType.BLUDGEONING) {
                            if (targetEntity instanceof AbstractSkeletonEntity) {

                                attackDamage *= 1.5;
                            }
                        }

                        //  Piercing weapons do extra damage against armoured opponents
                        if (weapon.getDamageType() == ModDamageType.PIERCING) {
                            if (targetEntity instanceof IronGolemEntity) {
                                attackDamage *= 1.5;
                            }

                            if (targetEntity instanceof PlayerEntity &&
                                    ((PlayerEntity)targetEntity).getTotalArmorValue() > 10) {
                                attackDamage *= 1.5;
                            }
                        }

                        //  Slashing weapons do extra damage against living opponents
                        if (weapon.getDamageType() == ModDamageType.SLASHING) {
                            if (targetEntity instanceof AgeableEntity ||
                                targetEntity instanceof PatrollerEntity ||
                                targetEntity instanceof WaterMobEntity) {
                                attackDamage *= 1.5;
                            }

                            if (targetEntity instanceof PlayerEntity &&
                                    ((PlayerEntity)targetEntity).getTotalArmorValue() <= 10) {
                                attackDamage *= 1.5;
                            }
                        }

                        //  Versatile weapons do more damage if offhand is empty.
                        if (weapon.hasTrait(ModItemTrait.VERSATILE)) {
                            ItemStack offhandItem = player.getHeldItem(Hand.OFF_HAND);
                            if (offhandItem.isEmpty()) {
                                attackDamage *= weapon.getTraitValue(ModItemTrait.VERSATILE);
                            }
                        }

                        modifier = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), ((LivingEntity) targetEntity).getCreatureAttribute());

                        //  Non-lethal weapons won't kill living opponents.
                        if (weapon.hasTrait(ModItemTrait.NONLETHAL)) {
                            if (attackDamage + modifier > ((LivingEntity) targetEntity).getHealth()) {
                                if (targetEntity instanceof AgeableEntity ||
                                    targetEntity instanceof PatrollerEntity ||
                                    targetEntity instanceof WaterMobEntity ||
                                    targetEntity instanceof PlayerEntity) {
                                    attackDamage = ((LivingEntity) targetEntity).getHealth() - 1;
                                    modifier = 0;
                                }
                            }
                        }
                    } else {
                        modifier = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), CreatureAttribute.UNDEFINED);
                    }

                    float cooledAttackStrength = player.getCooledAttackStrength(0.5F);

                    attackDamage = attackDamage * (0.2F + cooledAttackStrength * cooledAttackStrength * 0.8F);
                    modifier = modifier * cooledAttackStrength;
                    player.resetCooldown();

                    if (attackDamage > 0.0F || modifier > 0.0F) {
                        boolean isFullAttackStrength = cooledAttackStrength > 0.9F;

                        int knockbackModifier = EnchantmentHelper.getKnockbackModifier(player);
                        if (isFullAttackStrength) {
                            if (player.isSprinting()) {
                                playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK);
                                ++knockbackModifier;
                            }
                        }

                        boolean isCriticalHit = isFullAttackStrength &&
                                player.fallDistance > 0.0F &&
                                !player.onGround &&
                                !player.isOnLadder() &&
                                !player.isInWater() &&
                                !player.isPotionActive(Effects.BLINDNESS) &&
                                !player.isPassenger() &&
                                targetEntity instanceof LivingEntity &&
                                !player.isSprinting();

                        float criticalHitModifier = 1.0f;
                        if (isCriticalHit) {
                            criticalHitModifier = 1.5f;
                        }

                        net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(player, targetEntity, isCriticalHit, criticalHitModifier);
                        if (hitResult != null) {
                            attackDamage *= hitResult.getDamageModifier();
                        }

                        attackDamage = attackDamage + modifier;

                        float targetEntityHealth = 0.0F;
                        boolean startedBurning = false;
                        int fireAspectModifier = EnchantmentHelper.getFireAspectModifier(player);

                        if (targetEntity instanceof LivingEntity) {
                            targetEntityHealth = ((LivingEntity) targetEntity).getHealth();

                            if (fireAspectModifier > 0 && !targetEntity.isBurning()) {
                                startedBurning = true;
                                targetEntity.setFire(1);
                            }
                        }

                        Vec3d targetMotion = targetEntity.getMotion();
                        boolean isAttacked = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(player),
                                attackDamage);

                        if (isAttacked) {
                            if (isCriticalHit) {
                                player.onCriticalHit(targetEntity);

                                if (weapon.hasTrait(ModItemTrait.SWEEPING)) {
                                    float sweepingDamage =
                                            1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * attackDamage;

                                    AxisAlignedBB boundingBox =
                                            targetEntity.getBoundingBox().grow(1.0D, 0.25D, 1.0D);

                                    for (LivingEntity LivingEntity :
                                            player.world.getEntitiesWithinAABB(LivingEntity.class, boundingBox)) {
                                        if (LivingEntity != player && LivingEntity != targetEntity &&
                                                !player.isOnSameTeam(LivingEntity) &&
                                                player.getDistanceSq(LivingEntity) < 9.0D) {
                                            LivingEntity.knockBack(player, 0.4F, (double) MathHelper.sin(player.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                                            LivingEntity.attackEntityFrom(DamageSource.causePlayerDamage(player), sweepingDamage);
                                        }
                                    }

                                    playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP);
                                    player.spawnSweepParticles();
                                } else {
                                    playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT);
                                }

                                if (weapon.getDamageType() == ModDamageType.BLUDGEONING) {
                                    knockbackModifier = (knockbackModifier + 1) * 2;
                                }
                            } else {
                                if (isFullAttackStrength) {
                                    playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG);
                                } else {
                                    playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK);
                                }
                            }

                            if (knockbackModifier > 0) {
                                if (targetEntity instanceof LivingEntity) {
                                    ((LivingEntity) targetEntity).knockBack(player, (float) knockbackModifier * 0.5F, (double) MathHelper.sin(player.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                                } else {
                                    targetEntity.addVelocity((double) (-MathHelper.sin(player.rotationYaw * 0.017453292F) * (float) knockbackModifier * 0.5F), 0.1D, (double) (MathHelper.cos(player.rotationYaw * 0.017453292F) * (float) knockbackModifier * 0.5F));
                                }

                                player.setMotion(player.getMotion().mul(0.6, 1.0, 0.6));
                                player.setSprinting(false);
                            }

                            if (targetEntity instanceof ServerPlayerEntity && targetEntity.velocityChanged) {
                                ((ServerPlayerEntity) targetEntity).connection.sendPacket(new SEntityVelocityPacket(targetEntity));
                                targetEntity.velocityChanged = false;
                                targetEntity.setMotion(targetMotion);
                            }

                            if (modifier > 0.0F) {
                                player.onEnchantmentCritical(targetEntity);
                            }

                            player.setLastAttackedEntity(targetEntity);

                            if (targetEntity instanceof LivingEntity) {
                                EnchantmentHelper.applyThornEnchantments((LivingEntity) targetEntity, player);
                            }

                            EnchantmentHelper.applyArthropodEnchantments(player, targetEntity);
                            ItemStack mainhandItem = player.getHeldItemMainhand();
                            Entity entity = targetEntity;

                            if (targetEntity instanceof EnderDragonPartEntity) {
                                entity = ((EnderDragonPartEntity) targetEntity).dragon;
                            }

                            if (!mainhandItem.isEmpty() && entity instanceof LivingEntity) {
                                ItemStack beforeHitCopy = mainhandItem.copy();
                                mainhandItem.hitEntity((LivingEntity) entity, player);

                                if (mainhandItem.isEmpty()) {
                                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, beforeHitCopy, Hand.MAIN_HAND);
                                    player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                                }
                            }

                            if (targetEntity instanceof LivingEntity) {
                                float damageDealt = targetEntityHealth - ((LivingEntity) targetEntity).getHealth();
                                player.addStat(Stats.DAMAGE_DEALT, Math.round(damageDealt * 10.0F));

                                if (fireAspectModifier > 0) {
                                    targetEntity.setFire(fireAspectModifier * 4);
                                }

                                if (player.world instanceof ServerWorld && damageDealt > 2.0F) {
                                    int numParticles = (int) ((double) damageDealt * 0.5D);
                                    BlockPos targetPosition = targetEntity.getPosition();
                                    ((ServerWorld) player.world).spawnParticle(ParticleTypes.DAMAGE_INDICATOR,
                                            targetPosition.getX(),
                                            targetPosition.getY() + (double) (targetEntity.getHeight() * 0.5F),
                                            targetPosition.getZ(), numParticles,
                                            0.1D, 0.0D, 0.1D, 0.2D);
                                }
                            }

                            player.addExhaustion(0.1F);
                        } else {
                            playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE);

                            if (startedBurning) {
                                targetEntity.extinguish();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Apply weapon/armour traits to incoming damage.
     * @param event The event data.
     */
    public void OnLivingHurtEvent(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof  LivingEntity) {
            LivingEntity creature = (LivingEntity) entity;
            ItemStack mainhandItem = creature.getHeldItem(Hand.MAIN_HAND);
            Item item = mainhandItem.getItem();
            if (item instanceof ModWeaponItem) {
                ModWeaponItem weapon = (ModWeaponItem) item;
                if (weapon.hasTrait(ModItemTrait.DEFENSIVE)) {
                    if (weapon.hasTrait(ModItemTrait.VERSATILE) &&
                        !creature.getHeldItem(Hand.OFF_HAND).isEmpty()) {
                        return;
                    }

                    event.setAmount(event.getAmount() * weapon.getTraitValue(ModItemTrait.DEFENSIVE));
                }
            }
        }
    }

    private void playSound(PlayerEntity player, SoundEvent sound) {
        BlockPos playerPosition = player.getPosition();
        player.world.playSound(null,
                playerPosition.getX(), playerPosition.getY(), playerPosition.getZ(),
                sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}
