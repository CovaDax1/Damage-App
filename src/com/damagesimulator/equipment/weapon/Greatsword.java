package com.damagesimulator.equipment.weapon;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.equipment.weapon.attributes.DamageType;
import com.damagesimulator.equipment.weapon.core.GreatWeapon;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;
import com.damagesimulator.global.Die;
import com.damagesimulator.global.d20;

public class Greatsword extends MeleeWeapon implements GreatWeapon {
    public Greatsword() {
        super(AbilityScore.AbilityScores.STRENGTH, 0);
        this.damageDie = new Die(2, 6);
        this.heavy = true;
        this.twoHanded = true;
        this.damageType = DamageType.Slashing;

    }

    public Greatsword(int enchantment) {
        super(AbilityScore.AbilityScores.STRENGTH, enchantment);
        this.damageDie = new Die(2, 6);
        this.heavy = true;
        this.twoHanded = true;
        this.damageType = DamageType.Slashing;
    }

    @Override
    public AttackResult rollPowerAttack(int toAttackBonus, Advantage advantage, int targetAc) {
        int attackRoll = d20.getDie().roll(advantage);
        if (attackRoll == 20) return AttackResult.CRIT;
        else if (attackRoll == 1) return AttackResult.MISS;
        else return (attackRoll + toAttackBonus + getEnchantmentBonus() - 5 < targetAc) ? AttackResult.MISS : AttackResult.HIT;
    }

    @Override
    public int rollPowerDamage() {
        int bonusDamage = getEnchantmentDamage() + 10;
        if(bonusDamageDie != null) bonusDamage += bonusDamageDie.roll();
        return damageDie.roll() + bonusDamage;
    }
}
