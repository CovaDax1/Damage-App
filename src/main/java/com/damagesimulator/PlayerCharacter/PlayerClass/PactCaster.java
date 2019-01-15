package com.damagesimulator.PlayerCharacter.PlayerClass;

import com.damagesimulator.PlayerCharacter.PactSlots;

public interface PactCaster extends MagicAdept {
    int getPactCasterLevel();

    PactSlots getPactSlots();
}
