package com.bokmcdok.wheat.entity.creature.animal.cornsnake;

import com.bokmcdok.wheat.entity.creature.ModSegmentedModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * The model for a Cornsnake. Controls animation for the rattle defense.
 * @param <T> The entity this model is rendering.
 */
public class ModCornsnakeModel<T extends Entity> extends ModSegmentedModel<ModCornsnakeEntity> {

    /**
     * The sizes of each segment representing the snake.
     */
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

    /**
     * The texture offsets used to render the snake.
     */
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
        super(32, 16, SEGMENT_SIZES, TEXTURE_OFFSETS, 0.3f);
    }

    @Override
    public void func_225597_a_(ModCornsnakeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.func_225597_a_(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        int length = mSegments.length;
        if (entity.isAggressive()) {
            mSegments[length - 4].rotateAngleX = (float) Math.PI * 0.3f;
            mSegments[length - 3].rotateAngleX = (float) Math.PI * 0.2f;
            mSegments[length - 2].rotateAngleX = (float) Math.PI * 0.1f;
            mSegments[length - 1].rotateAngleY = MathHelper.cos(ageInTicks * 0.9f + (float) (length - 1) * 0.3F * (float) Math.PI) * (float) Math.PI * 0.05F * (float) (1 + Math.abs(length - 3));
        } else {
            mSegments[length - 4].rotateAngleX = 0f;
            mSegments[length - 3].rotateAngleX = 0f;
            mSegments[length - 2].rotateAngleX = 0f;
        }
    }
}
