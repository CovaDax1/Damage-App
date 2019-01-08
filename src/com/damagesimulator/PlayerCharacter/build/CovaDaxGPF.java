package com.damagesimulator.PlayerCharacter.build;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.PlayerCharacter.PlayerClass.SpellCaster;
import com.damagesimulator.PlayerCharacter.PlayerClass.paladin.OathOfVengeance;
import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.PlayerCharacter.feat.GreatWeaponMastery;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.equipment.weapon.core.Polearm;
import com.damagesimulator.equipment.weapon.core.Weapon;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;
import com.damagesimulator.global.d4;

public class CovaDaxGPF extends CovaDax implements SpellCaster, OathOfVengeance, PolearmMaster, GreatWeaponMastery {
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

    //attack liberally
    //always power attack
    // if target hp > half, smite using highest otherwise smite using lowest
    // if kill or crit, cleave, otherwise polearm
    public int attackLiberally(MeleeWeapon weapon, Target target, Advantage advantage) {
        boolean cleave = false;
        int damage = 0;
        int toAttackBonus = getAttackAbsBonus(weapon) + (blessed ? d4.getDie().roll() : 0);
        int toDamageBonus = (weapon.finesse() && dexterity.getMod() > strength.getMod() ? dexterity.getMod() : strength.getMod());

        for (int i = 0; i < 2; i++) {
            int powerDamage = 10;
            AttackResult attackRoll = powerAttack(weapon, target, advantage, toAttackBonus);
            if (attackRoll != AttackResult.MISS) {
                damage += weapon.rollDamage() + toDamageBonus + powerDamage;
                damage += improvedSmite();
                int smiteSlot = 0;
                if (target.isBloodied()) {
                    smiteSlot = this.getHighestAvailableSpellSlot();
                } else {
                    smiteSlot = this.getLowestAvailableSpellSlot();
                }
                damage += smite(smiteSlot, target.isUndead());
                if (attackRoll == AttackResult.CRIT) {
                    cleave = true;
                    damage += weapon.getMaxDamage();
                    damage += smiteDie.maxDamage(smiteSlot, target.isUndead());
                }
            }
        }
        if (target.getHp() < 1) cleave = true;
        int powerDamage = 10;
        AttackResult bonusAttack = powerAttack(weapon, target, advantage, toAttackBonus);
        if (bonusAttack != null && bonusAttack != AttackResult.MISS) {
            if (cleave) { //you get a cleave if you crit
                damage += weapon.rollDamage() + toDamageBonus + 10;
            } else if (weapon instanceof Polearm) {
                damage += ((Polearm) weapon).rollPolearmMasterDamage() + toDamageBonus + powerDamage;
            }
            damage += improvedSmite();
            int smiteSlot = 0;
            if (target.isBloodied()) {
                smiteSlot = this.getHighestAvailableSpellSlot();
            } else {
                smiteSlot = this.getLowestAvailableSpellSlot();
            }
            damage += smite(smiteSlot, target.isUndead());
            if(bonusAttack == AttackResult.CRIT) {
                if(cleave)
                    damage += weapon.getMaxDamage();
                else if(weapon instanceof Polearm)
                    damage += ((Polearm) weapon).polearmMaxDamage();
                damage += smiteDie.maxDamage(smiteSlot, target.isUndead());
            }
        }
        return damage;
    }

    //attack economically
    //power attack if adv, smite using lowest first
    public int attackEconomically(MeleeWeapon weapon, Target target, Advantage advantage) {
        boolean cleave = false;
        int damage = 0;
        int toAttackBonus = getAttackAbsBonus(weapon) + (blessed ? d4.getDie().roll() : 0);
        int toDamageBonus = (weapon.finesse() && dexterity.getMod() > strength.getMod() ? dexterity.getMod() : strength.getMod());

        for (int i = 0; i < 2; i++) {
            int powerDamage = 0;
            AttackResult attackRoll;
            if (shouldIPowerAttack(target, advantage, weapon, toAttackBonus)) {
                powerDamage += 10;
                attackRoll = powerAttack(weapon, target, advantage, toAttackBonus);
            } else {
                attackRoll = weaponAttack(weapon, target, advantage, toAttackBonus);
            }
            if (attackRoll != AttackResult.MISS) {
                damage += weapon.rollDamage() + toDamageBonus + powerDamage;
                damage += improvedSmite();
                int smiteSlot = 0;
                if (target.isBloodied()) {
                    smiteSlot = this.getLowestAvailableSpellSlot();
                }
                damage += smite(smiteSlot, target.isUndead());
                if (attackRoll == AttackResult.CRIT) {
                    cleave = true;
                    damage += weapon.getMaxDamage();
                    damage += smiteDie.maxDamage(smiteSlot, target.isUndead());
                }
            }
        }
        if (target.getHp() < 1) cleave = true;
        AttackResult bonusAttack;
        if (shouldIPowerAttack(target, advantage, weapon, toAttackBonus)) {
            damage += 10;
            bonusAttack = powerAttack(weapon, target, advantage, toAttackBonus);
        } else {
            bonusAttack = weaponAttack(weapon, target, advantage, toAttackBonus);
        }
        if (bonusAttack != null && bonusAttack != AttackResult.MISS) {
            if (cleave) { //you get a cleave if you crit
                damage += weapon.rollDamage() + toDamageBonus;
            } else if (weapon instanceof Polearm) {
                damage += ((Polearm) weapon).rollPolearmMasterDamage() + toDamageBonus;
            }
            damage += improvedSmite();
            int smiteSlot = 0;
            if (target.isBloodied())
                smiteSlot = this.getLowestAvailableSpellSlot();
            damage += smite(smiteSlot, target.isUndead());
            if(bonusAttack == AttackResult.CRIT) {
                if(cleave)
                    damage += weapon.getMaxDamage();
                else if(weapon instanceof Polearm)
                    damage += ((Polearm) weapon).polearmMaxDamage();
                damage += smiteDie.maxDamage(smiteSlot, target.isUndead());
            }
        }
        return damage;
    }

    //attack conservatively
    //power attack if adv, no smite
    public int attackConservatively(MeleeWeapon weapon, Target target, Advantage advantage) {
        boolean cleave = false;
        int damage = 0;
        int toAttackBonus = getAttackAbsBonus(weapon) + (blessed ? d4.getDie().roll() : 0);
        int toDamageBonus = (weapon.finesse() && dexterity.getMod() > strength.getMod() ? dexterity.getMod() : strength.getMod());

        for (int i = 0; i < 2; i++) {
            int powerDamage = 0;
            AttackResult attackRoll;
            if (shouldIPowerAttack(target, advantage, weapon, toAttackBonus)) {
                powerDamage += 10;
                attackRoll = powerAttack(weapon, target, advantage, toAttackBonus);
            } else {
                attackRoll = weaponAttack(weapon, target, advantage, toAttackBonus);
            }
            if (attackRoll != AttackResult.MISS) {
                damage += weapon.rollDamage() + toDamageBonus + powerDamage;
                damage += improvedSmite();
                if (attackRoll == AttackResult.CRIT) {
                    cleave = true;
                    damage += weapon.getMaxDamage();
                }
            }
        }
        if (target.getHp() < 1) cleave = true;
        AttackResult bonusAttack;
        if (shouldIPowerAttack(target, advantage, weapon, toAttackBonus)) {
            damage += 10;
            bonusAttack = powerAttack(weapon, target, advantage, toAttackBonus);
        } else {
            bonusAttack = weaponAttack(weapon, target, advantage, toAttackBonus);
        }
        if (bonusAttack != null && bonusAttack != AttackResult.MISS) {
            if (cleave) { //you get a cleave if you crit
                damage += weapon.rollDamage() + toDamageBonus;
            } else if (weapon instanceof Polearm) {
                damage += ((Polearm) weapon).rollPolearmMasterDamage() + toDamageBonus;
            }
            damage += improvedSmite();
            if(bonusAttack == AttackResult.CRIT) {
                if(cleave)
                    damage += weapon.getMaxDamage();
                else if(weapon instanceof Polearm)
                    damage += ((Polearm) weapon).polearmMaxDamage();
            }
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
        return weapon.polearmMasterAttack(attackBonus, advantage, target.getAc());
    }

    public AttackResult powerPolearmAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus) {
        if (weapon instanceof Polearm && weapon.isTwoHanded() && weapon.isHeavy())
            return polearmAttack((Polearm) weapon, target, advantage, attackBonus - 5);
        else
            return polearmAttack((Polearm) weapon, target, advantage, attackBonus);
    }

    @Override
    public boolean shouldIPowerAttack(Target t, Advantage adv, Weapon weapon, int atkBonus) {
        if(adv == Advantage.ADVANTAGE) return true;
        else if(adv == Advantage.DISADVANTAGE) return false;
        else {
            if(atkBonus + weapon.getEnchantmentBonus() - weapon.getAverageDamage() + 16 >= t.getAc()) {
                return true;
            }
            return false;
        }
    }
}
