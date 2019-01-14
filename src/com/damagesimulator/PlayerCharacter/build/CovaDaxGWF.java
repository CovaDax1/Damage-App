package com.damagesimulator.PlayerCharacter.build;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.PlayerCharacter.PlayerClass.SpellCaster;
import com.damagesimulator.PlayerCharacter.PlayerClass.paladin.OathOfVengeance;
import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.PlayerCharacter.feat.GreatWeaponMastery;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;

import static com.damagesimulator.global.AttackResult.CRIT;
import static com.damagesimulator.global.AttackResult.MISS;

public class CovaDaxGWF extends CovaDax<MeleeWeapon> implements SpellCaster, OathOfVengeance, GreatWeaponMastery {
    public CovaDaxGWF(AbilityScore strength, AbilityScore dexterity, AbilityScore constitution, AbilityScore intelligence, AbilityScore wisdom, AbilityScore charisma) {
        super(strength, dexterity, constitution, intelligence, wisdom, charisma);
        init();
    }

    public static CovaDaxGWF build(int s, int d, int c, int i, int w, int h) {
        return new CovaDaxGWF(
                new AbilityScore(s),
                new AbilityScore(d),
                new AbilityScore(c),
                new AbilityScore(i),
                new AbilityScore(w),
                new AbilityScore(h));
    }

    protected int rollLiberalAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        int damage = 0;
        AttackResult attackRoll = powerAttack(weapon, target, advantage, attackBonus);
        if (attackRoll != MISS) damage += rollLiberalDamage(weapon, target, damageBonus + 10, attackRoll == CRIT);
        if (bonusActionAvailable && (attackRoll == CRIT || target.getHp() < 1)) { //cleave
            bonusActionAvailable = false;
            damage += rollLiberalAttack(weapon, target, advantage, attackBonus, damageBonus);
        }
        return damage;
    }

    protected int rollEconomicAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        int damage = 0;
        AttackResult attackRoll;
        if (shouldIPowerAttack(target, advantage, weapon, attackBonus, damageBonus)) {
            attackRoll = powerAttack(weapon, target, advantage, attackBonus);
            if(attackRoll != MISS) damage += rollEconomicDamage(weapon, target, damageBonus + 10, attackRoll == CRIT);
        } else {
            attackRoll = weaponAttack(weapon, target, advantage, attackBonus);
            if(attackRoll != MISS) damage += rollEconomicDamage(weapon, target, damageBonus, attackRoll == CRIT);
        }
        if(bonusActionAvailable && (attackRoll == CRIT || target.getHp() < 1)) {
            bonusActionAvailable = false;
            damage += rollEconomicAttack(weapon, target, advantage, attackBonus, damageBonus);
        }
        return damage;
    }

    protected int  rollConservativeAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        AttackResult attackRoll;
        int damage = 0;
        if (shouldIPowerAttack(target, advantage, weapon, attackBonus, damageBonus)) {
            attackRoll = powerAttack(weapon, target, advantage, attackBonus);
            if(attackRoll != MISS) damage += rollConservativeDamage(weapon, damageBonus + 10, attackRoll == CRIT);
        } else {
            attackRoll = weaponAttack(weapon, target, advantage, attackBonus);
            if(attackRoll != MISS) damage +=  rollConservativeDamage(weapon, damageBonus, attackRoll == CRIT);
        }
        if(bonusActionAvailable && (attackRoll == CRIT || target.getHp() < 1)) {
            bonusActionAvailable = false;
            damage += rollConservativeAttack(weapon, target, advantage, attackBonus, damageBonus);
        }
        return damage;
    }

    // Max AC = attackBonus - damage/2 + 16
    @Override
    public AttackResult powerAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus) {
        if (weapon.isTwoHanded() && weapon.isHeavy())
            return weapon.rollAttack(attackBonus - 5, advantage, target.getAc());
        else
            return weapon.rollAttack(attackBonus, advantage, target.getAc());
    }

    @Override
    public AttackResult cleave(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus) {
        return weapon.rollAttack(attackBonus, advantage, target.getAc());
    }

    @Override
    public AttackResult powerCleave(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus) {
        if (weapon.isTwoHanded() && weapon.isHeavy())
            return weapon.rollAttack(attackBonus - 5, advantage, target.getAc());
        else
            return weaponAttack(weapon, target, advantage, attackBonus);
    }
}
