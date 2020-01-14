package com.bokmcdok.wheat.spell;

import com.google.common.collect.Maps;

import java.util.Map;

public class ModSpellManager {
    private final Map<String, IModSpell> mSpells;

    /**
     * Construction
     */
    public ModSpellManager() {
        mSpells = Maps.newHashMap();
        mSpells.put("call_lightning", new ModCallLightningSpell());
    }

    /**
     * Get a spell by a string name.
     * @param id The ID of the spell.
     * @return An instance of the spell.
     */
    public IModSpell getSpell(String id) {
        if (mSpells.containsKey(id)) {
            return mSpells.get(id);
        } else {
            return null;
        }
    }
}
