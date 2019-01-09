package com.damagesimulator.PlayerCharacter.build;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.PlayerCharacter.PlayerClass.SpellCaster;
import com.damagesimulator.PlayerCharacter.PlayerClass.paladin.OathOfVengeance;
import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.PlayerCharacter.feat.GreatWeaponMastery;
import com.damagesimulator.PlayerCharacter.feat.PolearmMaster;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.equipment.weapon.core.Polearm;
import com.damagesimulator.equipment.weapon.core.Weapon;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;
import com.damagesimulator.global.d4;

import static com.damagesimulator.global.AttackResult.CRIT;
import static com.damagesimulator.global.AttackResult.MISS;

public class CovaDaxGPF extends CovaDaxGWF implements SpellCaster, OathOfVengeance, PolearmMaster, GreatWeaponMastery {
    public CovaDaxGPF(AbilityScore strength, AbilityScore dexterity, AbilityScore constitution, AbilityScore intelligence, AbilityScore wisdom, AbilityScore charisma) {
        super(strength, dexterity, constitution, intelligence, wisdom, charisma);
        init();
    }

    public static CovaDaxGPF build(int s, int d, int c, int i, int w, int h) {
        return new CovaDaxGPF(
                new AbilityScore(s),
                new AbilityScore(d),
                new AbilityScore(c),
                new AbilityScore(i),
                new AbilityScore(w),
                new AbilityScore(h));
    }

    //attack economically
    //power attack if adv, smite using lowest first
    // Max AC = attackBonus - damage/2 + 16
    @Override
    public int economicMultiAttack(MeleeWeapon weapon, Target target, Advantage advantage) {
        int damage = super.economicMultiAttack(weapon, target, advantage);
        if (bonusActionAvailable) {
            bonusActionAvailable = false;
            damage += rollEconomicAttack(((Polearm) weapon).getHaft(), target, advantage, getToAttackBonus(weapon), getToDamageBonus(weapon));
        }
        return damage;
    }

    protected int rollEconomicAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        int damage = 0;
        AttackResult attackRoll;
        if (shouldIPowerAttack(target, advantage, weapon, attackBonus)) {
            attackRoll = powerAttack(weapon, target, advantage, attackBonus);
            if (attackRoll != MISS) damage += rollEconomicDamage(weapon, target, damageBonus + 10, attackRoll == CRIT);
        } else {
            attackRoll = weaponAttack(weapon, target, advantage, attackBonus);
            if (attackRoll != MISS) damage += rollEconomicDamage(weapon, target, damageBonus, attackRoll == CRIT);
        }
        if (bonusActionAvailable && (attackRoll == CRIT || target.getHp() < 1)) { //cleave
            bonusActionAvailable = false;
            damage += rollEconomicAttack(weapon, target, advantage, attackBonus, damageBonus);
        }
        return damage;
    }

    //attack liberally
    //always power attack
    // if target hp > half, smite using highest otherwise smite using lowest
    // if kill or crit, cleave, otherwise polearm
    public int liberalMultiAttack(MeleeWeapon weapon, Target target, Advantage advantage) {
        int damage = super.liberalMultiAttack(weapon, target, advantage);
        if (bonusActionAvailable) {
            bonusActionAvailable = false;
            damage += rollLiberalAttack(((Polearm) weapon).getHaft(), target, advantage, getToAttackBonus(weapon), getToDamageBonus(weapon));
        }
        return damage;
    }

    protected int rollLiberalAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        int damage = 0;
        AttackResult attackRoll = powerAttack(weapon, target, advantage, attackBonus);
        if (attackRoll != MISS) damage += rollEconomicDamage(weapon, target, damageBonus + 10, attackRoll == CRIT);

        if (bonusActionAvailable && (attackRoll == CRIT || target.getHp() < 1)) { //cleave
            bonusActionAvailable = false;
            damage += rollLiberalAttack(weapon, target, advantage, attackBonus, damageBonus);
        }
        return damage;
    }

    //attack conservatively
    //power attack if adv, no smite
    public int conservativeMultiAttack(MeleeWeapon weapon, Target target, Advantage advantage) {
        int damage = super.conservativeMultiAttack(weapon, target, advantage);
        if (bonusActionAvailable) {
            bonusActionAvailable = false;
            damage += rollConservativeAttack(((Polearm) weapon).getHaft(), target, advantage, getToAttackBonus(weapon), getToDamageBonus(weapon));
        }
        return damage;
    }

    protected int rollConservativeAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        AttackResult attackRoll;
        int damage = 0;
        if (shouldIPowerAttack(target, advantage, weapon, attackBonus)) {
            attackRoll = powerAttack(weapon, target, advantage, attackBonus);
            if (attackRoll != MISS) damage += rollConservativeDamage(weapon, damageBonus + 10, attackRoll == CRIT);
        } else {
            attackRoll = weaponAttack(weapon, target, advantage, attackBonus);
            if (attackRoll != MISS) damage += rollConservativeDamage(weapon, damageBonus, attackRoll == CRIT);
        }
        if (bonusActionAvailable && (attackRoll == CRIT || target.getHp() < 1)) {
            bonusActionAvailable = false;
            damage += rollConservativeAttack(weapon, target, advantage, attackBonus, damageBonus);
        }
        return damage;
    }

    @Override
    public AttackResult powerAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus) {
        if (weapon.isTwoHanded() && weapon.isHeavy())
            return weapon.rollAttack(attackBonus - 5, advantage, target.getAc());
        else
            return weaponAttack(weapon, target, advantage, attackBonus);
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

    @Override
    public AttackResult polearmAttack(Polearm weapon, Target target, Advantage advantage, int attackBonus) {
        return weapon.getHaft().rollAttack(attackBonus, advantage, target.getAc());
    }

    public AttackResult powerPolearmAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus) {
        if (weapon instanceof Polearm && weapon.isTwoHanded() && weapon.isHeavy())
            return polearmAttack((Polearm) weapon, target, advantage, attackBonus - 5);
        else
            return polearmAttack((Polearm) weapon, target, advantage, attackBonus);
    }

    @Override
    public boolean shouldIPowerAttack(Target t, Advantage adv, Weapon weapon, int atkBonus) {
        if (adv == Advantage.ADVANTAGE) return true;
        else if (adv == Advantage.DISADVANTAGE) return false;
        else {
            if (atkBonus + weapon.getEnchantmentBonus() - weapon.getAverageDamage() + 16 >= t.getAc()) {
                return true;
            }
            return false;
        }
    }
}
