package com.bokmcdok.wheat.item;

/**
 * Damage types for weapons
 */
public enum ModDamageType {
    BLUDGEONING,    //  Increases knockback, 1.5x damage vs skeletons
    PIERCING,       //  Ignores 15% of armour
    SLASHING        //  Can harvest webs, can cause extra damage vs opponents that can bleed (15% chance of 1.5x)
}
