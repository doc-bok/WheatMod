package com.bokmcdok.wheat.effect;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

/**
 * An effect applied when a creature is charmed.
 */
public class ModCharmEffect extends Effect {

    /**
     * Create a charm effect.
     * @param liquidColor The color of a potion with the effect.
     */
    public ModCharmEffect(int liquidColor) {
        super(EffectType.HARMFUL, liquidColor);
    }
}
