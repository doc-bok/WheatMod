package com.bokmcdok.wheat.entity.creature.animal.cornsnake;

import com.bokmcdok.wheat.entity.creature.ModSegmentedModel;
import net.minecraft.entity.Entity;

public class ModCornsnakeModel<T extends Entity> extends ModSegmentedModel<ModCornsnakeEntity> {
    private static final float[][] SEGMENT_SIZES = new float[][] {
            {2, 1, 3},
            {3, 2, 4},
            {3, 2, 4},
            {3, 2, 4},
            {3, 2, 4},
            {3, 2, 4},
            {3, 2, 4},
            {3, 2, 4},
            {3, 2, 4},
            {3, 2, 4},
            {3, 2, 4},
            {2, 1, 3},
            {1, 1, 2}
    };

    private static final int[][] TEXTURE_OFFSETS = new int[][] {
            {10, 6},
            {0, 6},
            {0, 0},
            {0, 6},
            {0, 0},
            {0, 6},
            {0, 0},
            {0, 6},
            {0, 0},
            {0, 6},
            {0, 0},
            {10, 0},
            {0, 12}
    };

    /**
     * Construction
     */
    public ModCornsnakeModel() {
        super(32, 16, SEGMENT_SIZES, TEXTURE_OFFSETS, 0.4f);
    }
}
