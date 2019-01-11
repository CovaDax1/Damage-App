package com.damagesimulator.PlayerCharacter.PlayerClass.paladin;

public interface Paladin {
    int getLayOnHandsPool();
    int getChannelDivinityCount();
    int smite(int spellLevel, boolean undead);
    int improvedSmite();
}

