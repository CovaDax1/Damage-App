package com.damagesimulator.PlayerCharacter.feat;

import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;

public interface GreatWeaponMastery {
    default boolean shouldIPowerAttack(Target t, Advantage adv, MeleeWeapon weapon, int atkBonus, int dmgBonus) {
        if(adv == Advantage.ADVANTAGE) return true;
        else if(adv == Advantage.DISADVANTAGE) return false;
        else
            return ((atkBonus + weapon.getEnchantmentBonus()) - ((weapon.getAverageDamage() + dmgBonus) / 2) + 16) >= t.getAc();
    }
    default AttackResult powerAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus) {
        if (weapon.isTwoHanded() && weapon.isHeavy())
            return weapon.rollAttack(attackBonus - 5, advantage, target.getAc());
        else
            return weapon.rollAttack(attackBonus, advantage, target.getAc());
    }
    default AttackResult cleave(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus) {
        return weapon.rollAttack(attackBonus, advantage, target.getAc());
    }
    default AttackResult powerCleave(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus) {
        if (weapon.isTwoHanded() && weapon.isHeavy())
            return weapon.rollAttack(attackBonus - 5, advantage, target.getAc());
        else
            return weapon.rollAttack(attackBonus, advantage, target.getAc());
    }
}
