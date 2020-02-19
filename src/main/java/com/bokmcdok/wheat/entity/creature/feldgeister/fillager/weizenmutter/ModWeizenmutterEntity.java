package com.bokmcdok.wheat.entity.creature.feldgeister.fillager.weizenmutter;
import com.bokmcdok.wheat.WheatMod;
import com.bokmcdok.wheat.ai.behaviour.ISpellcaster;
import com.bokmcdok.wheat.ai.goals.ModCastSpellOnAttackTargetGoal;
import com.bokmcdok.wheat.ai.goals.ModCastSpellOnSelfGoal;
import com.bokmcdok.wheat.entity.creature.feldgeister.ModFeldgeisterEntity;
import com.bokmcdok.wheat.entity.creature.feldgeister.fillager.ModFillagerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class ModWeizenmutterEntity extends ModFillagerEntity implements ISpellcaster {
    private static final DataParameter<Boolean> SPELL = EntityDataManager.createKey(ModWeizenmutterEntity.class, DataSerializers.BOOLEAN);

    private static final ResourceLocation TEXTURE = new ResourceLocation("docwheat:textures/entity/feldgeister/weizenmutter.png");

    /**
     * Construction
     * @param type  The type of this entity.
     * @param world The current world.
     */
    public ModWeizenmutterEntity(EntityType<? extends ModFeldgeisterEntity> type, World world) {
        super(type, world);
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
     * Check if the Weizenmutter is casting a spell.
     * @return TRUE if a spell is being cast.
     */
    @Override
    public boolean isCastingSpell() {
        return dataManager.get(SPELL);
    }

    /**
     * Set whether or not the Weizenmutter is casting a spell.
     * @param castingSpell TRUE if a spell is being cast, false otherwise.
     */
    @Override
    public void setCastingSpell(boolean castingSpell) {
        dataManager.set(SPELL, castingSpell);
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
     * Give the Weizenmutter a lightning wand.
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
        Item lightningWand = ForgeRegistries.ITEMS.getValue(new ResourceLocation("docwheat:lightning_wand"));
        setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(lightningWand));
        return super.onInitialSpawn(world, difficulty, reason, spawnData, dataTag);
    }

    /**
     * Weizenmutters are similar to witches.
     */
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(26.0D);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    /**
     * Get the ambient sound.
     * @return The ambient sound of the entity.
     */
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITCH_AMBIENT;
    }

    /**
     * Get the hurt sound.
     * @param source The source of the damage.
     * @return The sound to play when this entity is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WITCH_HURT;
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
     * Register the custom goals for the Weizenmutter:
     *  - Convert children into Ahrenkind.
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();

        //  TODO: Some goals only execute when angry.
        goalSelector.addGoal(3, new ModCastSpellOnSelfGoal(this, WheatMod.SPELL_REGISTRAR.getSpell("true_polymorph_weizenmutter_cornsnake"), (caster, target) -> caster.getHealth() / caster.getMaxHealth() <= 0.2f));
        goalSelector.addGoal(3, new ModCastSpellOnSelfGoal(this, WheatMod.SPELL_REGISTRAR.getSpell("true_polymorph_weizenmutter_getreidewolf"), (caster, target) -> {
            LivingEntity attackTarget = caster.getAttackTarget();
            if (attackTarget != null) {
                return attackTarget.getHealth() / attackTarget.getMaxHealth() <= 0.2f;
            }

            return false;
        }));

        goalSelector.addGoal(3, new ModCastSpellOnAttackTargetGoal(this, WheatMod.SPELL_REGISTRAR.getSpell("conjure_getreidewolf"), 1.0d, null));
        goalSelector.addGoal(3, new ModCastSpellOnAttackTargetGoal(this, WheatMod.SPELL_REGISTRAR.getSpell("call_lightning"), 1.0d, null));

        goalSelector.addGoal(6, new ModCastSpellOnAttackTargetGoal(this, WheatMod.SPELL_REGISTRAR.getSpell("true_polymorph_ahrenkind"), 1.0d, (caster, target) -> target instanceof VillagerEntity));

        goalSelector.removeGoal(mAttackGoal);

        targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, VillagerEntity.class, 10, true, true, LivingEntity::isChild));
    }

    /**
     * Register a synced parameter for keeping track of spell use.
     */
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(SPELL, false);
    }
}
