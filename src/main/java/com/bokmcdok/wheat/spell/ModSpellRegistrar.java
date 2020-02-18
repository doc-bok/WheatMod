package com.bokmcdok.wheat.spell;

import com.bokmcdok.wheat.entity.ModEntityUtils;
import com.google.common.collect.Maps;

import java.util.Map;

public class ModSpellRegistrar {
    private final Map<String, ModSpell> mSpells;

    /**
     * Construction
     */
    public ModSpellRegistrar() {
        mSpells = Maps.newHashMap();
        mSpells.put("call_lightning", new ModCallLightningSpell());
        mSpells.put("true_polymorph_other_ahrenkind", new ModTruePolymorphOtherSpell("docwheat:ahrenkind"));
    }

    /**
     * Get a spell by a string name.
     * @param id The ID of the spell.
     * @return An instance of the spell.
     */
    public ModSpell getSpell(String id) {
        if (mSpells.containsKey(id)) {
            return mSpells.get(id);
        } else {
            return null;
        }
    }
}
