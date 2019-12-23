package com.bokmcdok.wheat.villager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.GiveHeroGiftsTask;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTables;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModGiveHeroGiftsTask extends GiveHeroGiftsTask {
    private static final Map<VillagerProfession, ResourceLocation> GIFTS = Util.make(Maps.newHashMap(), (lootTables) -> {
        lootTables.put(VillagerProfession.ARMORER, LootTables.GAMEPLAY_HERO_OF_THE_VILLAGE_ARMORER_GIFT);
        lootTables.put(VillagerProfession.BUTCHER, LootTables.GAMEPLAY_HERO_OF_THE_VILLAGE_BUTCHER_GIFT);
        lootTables.put(VillagerProfession.CARTOGRAPHER, LootTables.GAMEPLAY_HERO_OF_THE_VILLAGE_CARTOGRAPHER_GIFT);
        lootTables.put(VillagerProfession.CLERIC, LootTables.GAMEPLAY_HERO_OF_THE_VILLAGE_CLERIC_GIFT);
        lootTables.put(VillagerProfession.FARMER, LootTables.GAMEPLAY_HERO_OF_THE_VILLAGE_FARMER_GIFT);
        lootTables.put(VillagerProfession.FISHERMAN, LootTables.GAMEPLAY_HERO_OF_THE_VILLAGE_FISHERMAN_GIFT);
        lootTables.put(VillagerProfession.FLETCHER, LootTables.GAMEPLAY_HERO_OF_THE_VILLAGE_FLETCHER_GIFT);
        lootTables.put(VillagerProfession.LEATHERWORKER, LootTables.GAMEPLAY_HERO_OF_THE_VILLAGE_LEATHERWORKER_GIFT);
        lootTables.put(VillagerProfession.LIBRARIAN, LootTables.GAMEPLAY_HERO_OF_THE_VILLAGE_LIBRARIAN_GIFT);
        lootTables.put(VillagerProfession.MASON, LootTables.GAMEPLAY_HERO_OF_THE_VILLAGE_MASON_GIFT);
        lootTables.put(VillagerProfession.SHEPHERD, LootTables.GAMEPLAY_HERO_OF_THE_VILLAGE_SHEPHERD_GIFT);
        lootTables.put(VillagerProfession.TOOLSMITH, LootTables.GAMEPLAY_HERO_OF_THE_VILLAGE_TOOLSMITH_GIFT);
        lootTables.put(VillagerProfession.WEAPONSMITH, LootTables.GAMEPLAY_HERO_OF_THE_VILLAGE_WEAPONSMITH_GIFT);

        lootTables.put(ModVillagerProfessionHelper.BAKER, new ResourceLocation("docwheat", "gameplay/hero_of_the_village/baker_gift" ));
    });

    private long mStartTime;
    private boolean mDone;

    public ModGiveHeroGiftsTask(int duration) {
        super(duration);
    }

    @Override
    protected void startExecuting(ServerWorld worldIn, VillagerEntity entityIn, long gameTimeIn) {
        mDone = false;
        mStartTime = gameTimeIn;
        PlayerEntity playerentity = this.getNearestPlayer(entityIn).get();
        entityIn.getBrain().setMemory(MemoryModuleType.INTERACTION_TARGET, playerentity);
        BrainUtil.lookAt(entityIn, playerentity);
    }

    @Override
    protected boolean shouldContinueExecuting(ServerWorld worldIn, VillagerEntity entityIn, long gameTimeIn) {
        return this.hasNearestPlayer(entityIn) && !mDone;
    }

    @Override
    protected void updateTask(ServerWorld worldIn, VillagerEntity owner, long gameTime) {
        PlayerEntity playerentity = getNearestPlayer(owner).get();
        BrainUtil.lookAt(owner, playerentity);
        if (isCloseEnough(owner, playerentity)) {
            if (gameTime - mStartTime > 20L) {
                giveGifts(owner, playerentity);
                mDone = true;
            }
        } else {
            BrainUtil.approach(owner, playerentity, 5);
        }
    }

    private void giveGifts(VillagerEntity villager, LivingEntity player) {
        for(ItemStack itemstack : getGifts(villager)) {
            BrainUtil.throwItemAt(villager, itemstack, player);
        }
    }

    private List<ItemStack> getGifts(VillagerEntity villager) {
        if (villager.isChild()) {
            return ImmutableList.of(new ItemStack(Items.POPPY));
        } else {
            VillagerProfession villagerprofession = villager.getVillagerData().getProfession();
            if (GIFTS.containsKey(villagerprofession)) {
                LootTable loottable = villager.world.getServer().getLootTableManager().getLootTableFromLocation(GIFTS.get(villagerprofession));
                LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld)villager.world)).withParameter(LootParameters.POSITION, new BlockPos(villager)).withParameter(LootParameters.THIS_ENTITY, villager).withRandom(villager.getRNG());
                return loottable.generate(lootcontext$builder.build(LootParameterSets.GIFT));
            } else {
                return ImmutableList.of(new ItemStack(Items.WHEAT_SEEDS));
            }
        }
    }

    private boolean hasNearestPlayer(VillagerEntity p_220396_1_) {
        return this.getNearestPlayer(p_220396_1_).isPresent();
    }


    private Optional<PlayerEntity> getNearestPlayer(VillagerEntity villager) {
        return villager.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER).filter(this::isHero);
    }

    private boolean isHero(PlayerEntity player) {
        return player.isPotionActive(Effects.HERO_OF_THE_VILLAGE);
    }

    private boolean isCloseEnough(VillagerEntity villager, PlayerEntity player) {
        BlockPos blockpos = new BlockPos(player);
        BlockPos blockpos1 = new BlockPos(villager);
        return blockpos1.withinDistance(blockpos, 5.0D);
    }
}