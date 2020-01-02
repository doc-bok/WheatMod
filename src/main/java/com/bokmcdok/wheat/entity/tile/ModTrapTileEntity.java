package com.bokmcdok.wheat.entity.tile;

import com.bokmcdok.wheat.block.ModTrapBlock;
import com.bokmcdok.wheat.entity.ModEntityUtils;
import com.bokmcdok.wheat.sound.ModSoundUtils;
import com.google.common.collect.Sets;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.List;
import java.util.Set;

public class ModTrapTileEntity extends ModInventoryTileEntity implements ITickableTileEntity {
    private Set<ResourceLocation> mTargets;
    private boolean mActivated = false;

    /**
     * Construction
     */
    public ModTrapTileEntity() {
        this(Sets.newHashSet(), 5);
    }

    /**
     * Construction
     *
     * @param targets       The entities this trap targets.
     * @param inventorySize The size of the inventory.
     */
    public ModTrapTileEntity(Set<ResourceLocation> targets, int inventorySize) {
        super(ModEntityUtils.trap, inventorySize);
        mTargets = targets;
    }

    /**
     * If a target comes near, kill it and collect its loot.
     */
    @Override
    public void tick() {
        if (getIsInventoryEmpty()) {
            AxisAlignedBB boundingBox = new AxisAlignedBB(pos);
            if (mActivated) {
                boundingBox = boundingBox.grow(1);
                List<ItemEntity> entities = world.getEntitiesWithinAABB(ItemEntity.class, boundingBox);
                for (ItemEntity i : entities) {
                    ItemStack copy = i.getItem().copy();
                    ItemStack remaining = addItemStack(copy);
                    if (remaining.isEmpty()) {
                        i.remove();
                    } else {
                        i.setItem(remaining);
                    }
                }
            } else {
                List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, boundingBox);
                for (LivingEntity i : entities) {
                    if (mTargets.contains(i.getType().getRegistryName())) {
                        i.onKillCommand();
                        mActivated = true;
                        IForgeRegistry<SoundEvent> registry = RegistryManager.ACTIVE.getRegistry(GameData.SOUNDEVENTS);
                        SoundEvent sound = registry.getValue(new ResourceLocation("docwheat:mouse_trap_activate"));
                        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        world.setBlockState(pos, getBlockState().with(ModTrapBlock.ACTIVATED, true));
                        break;
                    }
                }
            }
        }
    }

    /**
     * Read data from a save.
     * @param data The NBT data.
     */
    @Override
    public void read(CompoundNBT data) {
        super.read(data);
        mActivated = data.getBoolean("activated");
    }

    /**
     * Write the state to a save.
     * @param data The NBT data.
     * @return Updated NBT data with the inventory saved.
     */
    @Override
    public CompoundNBT write(CompoundNBT data) {
        super.write(data);
        data.putBoolean("activated", mActivated);
        return data;
    }
}