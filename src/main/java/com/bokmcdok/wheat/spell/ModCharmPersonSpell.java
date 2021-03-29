package com.bokmcdok.wheat.spell;

import com.bokmcdok.wheat.ai.goals.ModFollowEntityGoal;
import com.bokmcdok.wheat.ai.goals.ModTemporaryGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegistryManager;

/**
 * Implementation of a Charm Person Spell
 */
public class ModCharmPersonSpell extends ModSpell {

    @Override
    public boolean cast(LivingEntity caster, Entity target) {
        if (target instanceof MobEntity) {
            MobEntity mob = (MobEntity) target;
            Goal followGoal = new ModFollowEntityGoal(mob, caster, 1d, 7f);
            Goal tempGoal = new ModTemporaryGoal(mob, followGoal, getDuration());
            mob.goalSelector.addGoal(0, tempGoal);

            ForgeRegistry<Effect> registry = RegistryManager.ACTIVE.getRegistry(GameData.POTIONS);
            Effect effect = registry.getValue(new ResourceLocation("docwheat:charm"));

            if (effect != null) {
                mob.addPotionEffect(new EffectInstance(effect, getDuration()));
            }

            return true;
        }

        return super.cast(caster);
    }

    @Override
    public double getRange() {
        return 30;
    }

    @Override
    public int getDuration() {
        return 36000;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
