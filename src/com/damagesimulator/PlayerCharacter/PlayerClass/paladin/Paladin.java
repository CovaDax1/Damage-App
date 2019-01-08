package com.damagesimulator.PlayerCharacter.PlayerClass.paladin;

import com.damagesimulator.PlayerCharacter.PlayerCharacter;
import com.damagesimulator.global.Advantage;

public interface Paladin {
    int getLayOnHandsPool();
    int getChannelDivinityCount();
    int smite(int spellLevel, boolean undead);
    int improvedSmite();
}

