package com.damagesimulator.PlayerCharacter.PlayerClass.paladin;

import com.damagesimulator.global.Die;

public class SmiteDie extends Die {
    public SmiteDie() {
        super(0, 8);
    }

    @Override
    public int roll() {
        return smite(1, false);
    }

    public int smite(int spellLevel, boolean undead) {
        int value = 0;
        for (int i = 0; i < spellLevel + 1 + (undead ? 1 : 0); i++) {
            value += dieRoll.nextInt(die) + 1;
        }
        return value;
    }

    public int improvedSmite() {
        return this.dieRoll.nextInt(this.die) + 1;
    }

    public int maxDamage(int spellLevel, boolean undead) {
        return this.die * (spellLevel + (undead ? 1 : 0));
    }
}
