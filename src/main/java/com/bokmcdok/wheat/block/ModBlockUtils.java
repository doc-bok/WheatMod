package com.bokmcdok.wheat.block;

import com.bokmcdok.wheat.WheatMod;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Registers all the blocks used in this mod.
 */
@Mod.EventBusSubscriber(modid = WheatMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(WheatMod.MOD_ID)
public class ModBlockUtils {
    public static final ModCropsBlock wild_einkorn = null;
    public static final ModCropsBlock common_wheat = null;
    public static final ModCropsBlock einkorn = null;
    public static final ModCropsBlock wild_emmer = null;
    public static final ModCropsBlock emmer = null;
    public static final ModCropsBlock durum = null;
    public static final ModCropsBlock spelt = null;
    public static final ModCropsBlock diseased_wheat = null;

    public static final ModCropsBlock tomato = null;

    public static final FlourMillBlock flour_mill = null;

    public static final ModBlock cornsnake_egg = null;
    public static final ModBlock widowbird_nest = null;

    public static final ModTrapBlock mouse_trap = null;
    public static final ModTrapBlock seeded_mouse_trap = null;

    public static final ModCampfireBlock campfire = null;

    /**
     * Check if the specified block is present within a certain radius.
     * @param world The current world.
     * @param position The position to check.
     * @param block The block to look for.
     * @param radius The radius to search.
     * @return TRUE if the block is within the specified radius.
     */
    public static boolean isBlockPresent(World world, BlockPos position, Block block, int radius) {
        for (int x = (radius * -1); x < radius + 1; x++) {
            for (int z = (radius * -1); z < radius + 1; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }

                BlockPos posToCheck = position.add(x, 0, z);
                if(world.getBlockState(posToCheck).getBlock() == block) {
                    return true;
                }
            }
        }

        return false;
    }
}
