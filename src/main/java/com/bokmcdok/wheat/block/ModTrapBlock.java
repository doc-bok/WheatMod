package com.bokmcdok.wheat.block;

import net.minecraft.state.BooleanProperty;
import net.minecraft.util.ResourceLocation;

public class ModTrapBlock extends ModBlock {
    private static final BooleanProperty HAS_CAUGHT_TARGET = BooleanProperty.create("caught_creature");
    private final ResourceLocation mTargetEntity;

    /**
     * Construction
     * @param properties The properties for the block.
     * @param creature The creature this block can trap.
     */
    public ModTrapBlock(ModBlockImpl.ModBlockProperties properties, ResourceLocation creature) {
        super(properties);
        mTargetEntity = creature;
        setDefaultState(stateContainer.getBaseState().with(HAS_CAUGHT_TARGET, false));
    }
}
