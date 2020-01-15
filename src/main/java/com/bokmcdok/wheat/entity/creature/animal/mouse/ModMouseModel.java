package com.bokmcdok.wheat.entity.creature.animal.mouse;

import com.bokmcdok.wheat.entity.creature.ModSegmentedModel;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModMouseModel<T extends Entity> extends ModSegmentedModel<ModMouseEntity> {
    private static final int[][] SEGMENT_SIZES = new int[][]{
            {1, 1, 1},
            {3, 2, 2},
            {4, 3, 3},
            {3, 3, 3},
            {3, 3, 3},
            {1, 1, 2},
            {1, 1, 2}
    };

    private static final int[][] TEXTURE_OFFSETS = new int[][]{
            {13, 4},
            {0, 0},
            {0, 9},
            {0, 16},
            {0, 16},
            {11, 0},
            {13, 4}
    };

    /**
     * Construction
     */
    public ModMouseModel() {
        super(SEGMENT_SIZES, TEXTURE_OFFSETS, 0.9f);
    }
}
