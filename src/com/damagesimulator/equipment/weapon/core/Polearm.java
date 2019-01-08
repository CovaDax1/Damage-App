package com.damagesimulator.equipment.weapon.core;

import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;

public interface Polearm {
    AttackResult polearmMasterAttack(int toAttackBonus, Advantage advantage, int targetAc);
    int rollPolearmMasterDamage();
    int polearmMaxDamage();
}
