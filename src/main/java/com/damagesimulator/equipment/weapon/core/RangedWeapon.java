package com.damagesimulator.equipment.weapon.core;

import com.damagesimulator.PlayerCharacter.AbilityScore;

public abstract class RangedWeapon extends Weapon {
    private int minRange;
    private int maxRange;
    private boolean loading;

    public RangedWeapon(AbilityScore.AbilityScores abs) {
        super(abs);
    }

    public RangedWeapon(AbilityScore.AbilityScores abs, int enchantment) {
        super(abs, enchantment);
    }
}
