package com.damagesimulator.equipment.weapon.core;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.PlayerCharacter.action.MeleeWeaponAttack;

public abstract class MeleeWeapon extends Weapon implements MeleeWeaponAttack {
    protected boolean finesse;
    protected boolean versatile;
    protected boolean reach;

    public MeleeWeapon(AbilityScore.AbilityScores abs) {
        super(abs);
    }

    public MeleeWeapon(AbilityScore.AbilityScores abs, int enchantment) {
        super(abs, enchantment);
    }

    public boolean finesse() {
        return finesse;
    }

    public boolean isVersatile() {
        return versatile;
    }

    public boolean hasReach() {
        return reach;
    }
}
