package com.damagesimulator.PlayerCharacter.feat;

import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.equipment.weapon.core.Polearm;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;

public interface PolearmMaster {
    default AttackResult polearmAttack(Polearm weapon, Target target, Advantage advantage, int attackBonus) {
        return weapon.getHaft().rollAttack(attackBonus, advantage, target.getAc());
    }
}
