package com.damagesimulator.PlayerCharacter.PlayerClass.warlock;

import com.damagesimulator.equipment.weapon.core.Weapon;

public interface PactOfTheBlade extends Warlock {
    Weapon conjurePactWeapon();
    void bondWeapon(Weapon weapon);
}
