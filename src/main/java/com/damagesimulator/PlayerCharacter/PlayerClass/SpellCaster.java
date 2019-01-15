package com.damagesimulator.PlayerCharacter.PlayerClass;

import com.damagesimulator.PlayerCharacter.SpellCasterSlots;

public interface SpellCaster extends MagicAdept {
    int getSpellCasterLevel();

    SpellCasterSlots getSpellSlots();
}

