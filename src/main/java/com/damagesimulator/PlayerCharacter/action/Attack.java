package com.damagesimulator.PlayerCharacter.action;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;

public interface Attack {
    AttackResult rollAttack(int toAttackBonus, Advantage advantage, int targetAc);

    AttackResult rollAttack(int toAttackBonus, Advantage advantage, int targetAc, int critRange);

    int rollDamage();

    AbilityScore.AbilityScores getAttackAbilityScore();

    void setAttackAbilityScore(AbilityScore.AbilityScores scores);
}

