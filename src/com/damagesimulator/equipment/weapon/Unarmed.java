package com.damagesimulator.equipment.weapon;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.PlayerCharacter.action.Attack;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;
import com.damagesimulator.global.d20;

public class Unarmed implements Attack {
    private AbilityScore.AbilityScores abs = AbilityScore.AbilityScores.STRENGTH;

    public AttackResult rollAttack(int toAttackBonus, Advantage advantage, int targetAc) {
        int attackRoll = d20.getDie().roll(advantage);
        if (attackRoll == 20) return AttackResult.CRIT;
        else if (attackRoll == 1) return AttackResult.MISS;
        else return (attackRoll + toAttackBonus < targetAc) ? AttackResult.MISS : AttackResult.HIT;
    }

    public int rollDamage() {
        return 1;
    }

    public int getMaxDamage() {
        return 1;
    }

    @Override
    public AbilityScore.AbilityScores getAttackAbilityScore() {
        return abs;
    }

    @Override
    public void setAttackAbilityScore(AbilityScore.AbilityScores abs) {
        this.abs = abs;
    }
}
