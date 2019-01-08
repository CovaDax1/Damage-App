package com.damagesimulator.PlayerCharacter.PlayerClass;

public interface SpellCaster extends MagicAdept {
    int getCasterLevel();
    int getHighestAvailableSpellSlot();
    int getLowestAvailableSpellSlot();
    int getSpellSlotsByLevel(int i);
}

