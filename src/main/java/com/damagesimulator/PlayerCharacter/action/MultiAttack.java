package com.damagesimulator.PlayerCharacter.action;

import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.global.Advantage;

import java.util.List;

public interface MultiAttack {
    int multiAttack(Attack attack, Target target, Advantage advantage, int attackBonus, int damageBonus);

    int multiAttack(List<Attack> attacks, Target target, Advantage advantage, int attackBonus, int damageBonus);
}
