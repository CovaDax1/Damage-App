package com.damagesimulator.PlayerCharacter.PlayerClass.warlock;

import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.equipment.weapon.core.Weapon;

public interface Hexblade extends Warlock {
    void refreshPactSlots();

    void curse(Target target);
    Weapon getHexWeapon();
    void bondHexWeapon(Weapon weapon);
    int pactSmite();
}
