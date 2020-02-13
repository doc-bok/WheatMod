package com.bokmcdok.wheat.ai.tasks;

import com.bokmcdok.wheat.entity.creature.villager.ModVillagerItems;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class ModPickupFoodTask extends Task<VillagerEntity> {
    private final ModVillagerItems mVillagerItems;
    private List<ItemEntity> nearbyItems = new ArrayList<>();

    /**
     * Construction
     */
    public ModPickupFoodTask(ModVillagerItems villager) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT));
        mVillagerItems = villager;
    }

    /**
     * Shall we execute this task?
     * @param worldIn
     * @param owner
     * @return
     */
    protected boolean shouldExecute(ServerWorld worldIn, VillagerEntity owner) {
        this.nearbyItems = worldIn.getEntitiesWithinAABB(ItemEntity.class, owner.getBoundingBox().grow(4.0D, 2.0D, 4.0D));
        return !nearbyItems.isEmpty();
    }

    /**
     * Execution - this allows villagers to pickup our custom items.
     * @param worldIn
     * @param entityIn
     * @param gameTimeIn
     */
    protected void startExecuting(ServerWorld worldIn, VillagerEntity entityIn, long gameTimeIn) {
        ItemEntity itementity = nearbyItems.get(worldIn.rand.nextInt(nearbyItems.size()));
        ItemStack item = itementity.getItem();
        if (mVillagerItems.canPickupItem(entityIn.getVillagerData().getProfession(), item)) {
            Vec3d vec3d = itementity.getPositionVec();
            entityIn.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosWrapper(new BlockPos(vec3d)));
            entityIn.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3d, 0.5F, 0));
        }
    }
}
