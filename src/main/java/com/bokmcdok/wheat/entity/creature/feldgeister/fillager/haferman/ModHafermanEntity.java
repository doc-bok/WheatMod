package com.bokmcdok.wheat.entity.creature.feldgeister.fillager.haferman;

import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.ai.behaviour.ISpellcaster;
import com.bokmcdok.wheat.ai.goals.ModCastSpellOnAttackTargetGoal;
import com.bokmcdok.wheat.ai.goals.ModRaidFarmGoal;
import com.bokmcdok.wheat.entity.creature.feldgeister.ModFeldgeisterEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.ModFillagerEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter.ModWeizenmutterEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegistryManager;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.Nullable;

public class ModHafermanEntity extends ModFillagerEntity implements ISpellcaster {
    private static final DataParameter<Boolean> SPELL = EntityDataManager.createKey(ModWeizenmutterEntity.class, DataSerializers.BOOLEAN);
    private static final ResourceLocation TEXTURE = new ResourceLocation("docwheat:textures/entity/feldgeister/haferman.png");

    /**
     * Construction
     * @param type  The type of this entity.
     * @param world The current world.
     */
    public ModHafermanEntity(EntityType<? extends ModFeldgeisterEntity> type, World world) {
        super(type, world);
    }

    /**
     * Allow for spellcasting arm pose if casting a spell.
     * @return The arm pose to use.
     */
    @Override
    public AbstractIllagerEntity.ArmPose getArmPose() {
        if (isCastingSpell()) {
            return AbstractIllagerEntity.ArmPose.SPELLCASTING;
        }

        return super.getArmPose();
    }

    /**
     * Add spell particle effects if a spell is being cast.
     */
    @Override
    public void tick() {
        super.tick();

        if (world.isRemote && isCastingSpell()) {
            double d0 = 0.3d;
            double d1 = 0.35d;
            double d2 = 0.3d;
            float f = renderYawOffset * ((float)Math.PI / 180F) + MathHelper.cos((float)ticksExisted * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);
            world.addParticle(ParticleTypes.ENTITY_EFFECT, func_226277_ct_() + (double)f1 * 0.6D, func_226278_cu_() + 1.8D, func_226281_cx_() + (double)f2 * 0.6D, d0, d1, d2);
            world.addParticle(ParticleTypes.ENTITY_EFFECT, func_226277_ct_() - (double)f1 * 0.6D, func_226278_cu_() + 1.8D, func_226281_cx_() - (double)f2 * 0.6D, d0, d1, d2);
        }
    }

    /**
     * Set whether or not the Haferman is casting a spell.
     * @param castingSpell TRUE if a spell is being cast, false otherwise.
     */
    @Override
    public void setCastingSpell(boolean castingSpell) {
        dataManager.set(SPELL, castingSpell);
    }

    /**
     * Check if the Weizenmutter is casting a spell.
     * @return TRUE if a spell is being cast.
     */
    @Override
    public boolean isCastingSpell() {
        return dataManager.get(SPELL);
    }

    /**
     * Register a synced parameter for keeping track of spell use.
     */
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(SPELL, false);
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
     * Give the Haferman an iron shillelagh.
     * @param world The current world.
     * @param difficulty The current difficulty.
     * @param reason The reason for spawning.
     * @param spawnData The spawn data.
     * @param dataTag NBT tags.
     * @return The updated living entity data.
     */
    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag) {
        Item weapon = ForgeRegistries.ITEMS.getValue(new ResourceLocation("docwheat:wpn_shillelagh_iron"));
        setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(weapon));
        return super.onInitialSpawn(world, difficulty, reason, spawnData, dataTag);
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
     * Get the ambient sound.
     * @return The ambient sound of the entity.
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
     * Get the death sound.
     * @return The sound to play when the entity dies.
     */
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER;
    }

    /**
     * Register the custom goals for the Haferman
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(4, new ModCastSpellOnAttackTargetGoal(this, WheatMod.SPELL_REGISTRAR.getSpell("charm_person"), 1.0d, (caster, target) -> target instanceof VillagerEntity));
        goalSelector.addGoal(7, new ModRaidFarmGoal(this, "docwheat:crop", 1.0d, 16, 1));

        targetSelector.addGoal(2, new HurtByTargetGoal(this));
        targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, VillagerEntity.class, 10, true, true, x -> {
            if (x.isChild()) {
                ForgeRegistry<Effect> registry = RegistryManager.ACTIVE.getRegistry(GameData.POTIONS);
                Effect effect = registry.getValue(new ResourceLocation("docwheat:charm"));
                if (effect != null) {
                    return (x.getActivePotionEffect(effect) == null);
                }
            }

            return  false;
        }));
    }

    /**
     * Slow these feckers down.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(18d);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25d);
    }
}
