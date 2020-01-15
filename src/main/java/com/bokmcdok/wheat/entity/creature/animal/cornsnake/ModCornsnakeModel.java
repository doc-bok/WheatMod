package com.bokmcdok.wheat.entity.creature.animal.cornsnake;

import com.bokmcdok.wheat.entity.creature.ModSegmentedModel;
import net.minecraft.entity.Entity;

public class ModCornsnakeModel<T extends Entity> extends ModSegmentedModel<ModCornsnakeEntity> {
    private static final int[][] SEGMENT_SIZES = new int[][] {
            {1, 1, 1},
            {3, 2, 2},
            {4, 3, 3},
            {3, 3, 3},
            {3, 3, 3},
            {1, 1, 2},
            {1, 1, 2}
    };

    private static final int[][] TEXTURE_OFFSETS = new int[][] {
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
    public ModCornsnakeModel() {
        super(SEGMENT_SIZES, TEXTURE_OFFSETS);
    }
}
