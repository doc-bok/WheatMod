package com.bokmcdok.wheat.spell;

public class ModConjureFeySpell extends ModSpell {
    @Override
    public int getCastingTime() {
        return 600;
    }

    @Override
    public int getLevel() {
        return 6;
    }

    @Override
    public double getRange() {
        return 30;
    }
}
