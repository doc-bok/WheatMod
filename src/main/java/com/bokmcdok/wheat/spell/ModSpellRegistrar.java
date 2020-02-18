package com.bokmcdok.wheat.spell;

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
        mSpells.put("true_polymorph_ahrenkind", new ModTruePolymorphSpell("docwheat:ahrenkind"));
        mSpells.put("true_polymorph_weizenmutter", new ModTruePolymorphSpell("docwheat:weizenmutter"));
        mSpells.put("true_polymorph_weizenmutter_cornsnake", new ModTruePolymorphSpell("docwheat:weizenmutter_cornsnake"));
        mSpells.put("true_polymorph_weizenmutter_getreidewulf", new ModTruePolymorphSpell("docwheat:weizenmutter_getreidewulf"));
        mSpells.put("true_polymorph_weizenmutter_turtle", new ModTruePolymorphSpell("minecraft:weizenmutter_turtle"));
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
