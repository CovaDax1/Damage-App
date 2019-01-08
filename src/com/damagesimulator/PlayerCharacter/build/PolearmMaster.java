package com.damagesimulator.PlayerCharacter.build;

import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.equipment.weapon.core.Polearm;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;

public interface PolearmMaster {
    AttackResult polearmAttack(Polearm weapon, Target target, Advantage advantage, int attackBonus);
}
