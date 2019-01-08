package com.damagesimulator.PlayerCharacter.build;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.PlayerCharacter.PlayerClass.SpellCaster;
import com.damagesimulator.PlayerCharacter.PlayerClass.paladin.OathOfVengeance;
import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.equipment.weapon.core.Polearm;
import com.damagesimulator.equipment.weapon.core.Weapon;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;
import com.damagesimulator.global.d4;

public class CovaDaxPM extends CovaDax implements SpellCaster, OathOfVengeance, PolearmMaster {
    public CovaDaxPM(AbilityScore strength, AbilityScore dexterity, AbilityScore constitution, AbilityScore intelligence, AbilityScore wisdom, AbilityScore charisma) {
        super(strength, dexterity, constitution, intelligence, wisdom, charisma);
        init();
    }

    public static CovaDaxPM build(int s, int d, int c, int i, int w, int h) {
        return new CovaDaxPM(
                new AbilityScore(s),
                new AbilityScore(d),
                new AbilityScore(c),
                new AbilityScore(i),
                new AbilityScore(w),
                new AbilityScore(h));
    }

    //attack liberally
    //power attack, smite using highest first, spell smite as BA?
    public int attackLiberally(MeleeWeapon weapon, Target target, Advantage advantage) {
        int damage = 0;
        int toAttackBonus = getAttackAbsBonus(weapon) + (blessed ? d4.getDie().roll() : 0);
        int toDamageBonus = (weapon.finesse() && dexterity.getMod() > strength.getMod() ? dexterity.getMod() : strength.getMod());

        for (int i = 0; i < 2; i++) {
            AttackResult attackRoll = weaponAttack(weapon, target, advantage, toAttackBonus);
            if (attackRoll != AttackResult.MISS) {
                damage += weapon.rollDamage() + toDamageBonus;
                damage += improvedSmite();
                int smiteSlot = 0;
                if(target.isBloodied()) {
                    smiteSlot = this.getHighestAvailableSpellSlot();
                } else {
                    smiteSlot = this.getLowestAvailableSpellSlot();
                }
                damage += smite(smiteSlot, target.isUndead());
                if (attackRoll == AttackResult.CRIT) {
                    damage += weapon.getMaxDamage();
                    damage += smiteDie.maxDamage(smiteSlot, target.isUndead());
                }
            }
        }

        if(weapon instanceof Polearm) {
            AttackResult attackRoll2 = polearmAttack(((Polearm) weapon), target, advantage, toAttackBonus);
            if (attackRoll2 != AttackResult.MISS) {
                damage += ((Polearm) weapon).rollPolearmMasterDamage() + toDamageBonus;
                damage += improvedSmite();
                int smiteSlot = 0;
                if(target.isBloodied()) {
                    smiteSlot = this.getHighestAvailableSpellSlot();
                } else {
                    smiteSlot = this.getLowestAvailableSpellSlot();
                }
                damage += smite(smiteSlot, target.isUndead());
                if (attackRoll2 == AttackResult.CRIT) {
                    damage += weapon.getMaxDamage();
                    damage += smiteDie.maxDamage(smiteSlot, target.isUndead());
                }
            }
        }

        return damage;
    }

    //attack economically
    //power attack if adv, smite using lowest first
    public int attackEconomically(MeleeWeapon weapon, Target target, Advantage advantage) {
        int damage = 0;
        int toAttackBonus = getAttackAbsBonus(weapon) + (blessed ? d4.getDie().roll() : 0);
        int toDamageBonus = (weapon.finesse() && dexterity.getMod() > strength.getMod() ? dexterity.getMod() : strength.getMod());

        for (int i = 0; i < 2; i++) {
            AttackResult attackRoll = weaponAttack(weapon, target, advantage, toAttackBonus);
            if (attackRoll != AttackResult.MISS) {
                damage += weapon.rollDamage() + toDamageBonus;
                damage += improvedSmite();
                int smiteSlot = 0;
                if(target.isBloodied()) {
                    smiteSlot = this.getLowestAvailableSpellSlot();
                }
                damage += smite(smiteSlot, target.isUndead());
                if (attackRoll == AttackResult.CRIT) {
                    damage += weapon.getMaxDamage();
                    damage += smiteDie.maxDamage(smiteSlot, target.isUndead());
                }
            }
        }
        if(weapon instanceof Polearm) {
            AttackResult attackRoll2 = polearmAttack(((Polearm) weapon), target, advantage, toAttackBonus);
            if (attackRoll2 != AttackResult.MISS) {
                damage += ((Polearm) weapon).rollPolearmMasterDamage() + toDamageBonus;
                damage += improvedSmite();
                int smiteSlot = 0;
                if(target.isBloodied()) {
                    smiteSlot = this.getLowestAvailableSpellSlot();
                }
                damage += smite(smiteSlot, target.isUndead());
                if (attackRoll2 == AttackResult.CRIT) { //if the CLEAVE crits as well
                    damage += weapon.getMaxDamage();
                    damage += smiteDie.maxDamage(smiteSlot, target.isUndead());
                }
            }
        }
        return damage;
    }

    //attack conservatively
    //power attack if adv, no smite
    public int attackConservatively(MeleeWeapon weapon, Target target, Advantage advantage) {
        int damage = 0;
        int toAttackBonus = getAttackAbsBonus(weapon) + (blessed ? d4.getDie().roll() : 0);
        int toDamageBonus = (weapon.finesse() && dexterity.getMod() > strength.getMod() ? dexterity.getMod() : strength.getMod());

        for (int i = 0; i < 2; i++) {
            AttackResult attackRoll = weaponAttack(weapon, target, advantage, toAttackBonus);
            if (attackRoll != AttackResult.MISS) {
                damage += weapon.rollDamage() + toDamageBonus;
                damage += improvedSmite();
                if (attackRoll == AttackResult.CRIT)
                    damage += weapon.getMaxDamage();
            }
        }
        if(weapon instanceof Polearm) {
            AttackResult attackRoll2 = polearmAttack(((Polearm) weapon), target, advantage, toAttackBonus);
            if (attackRoll2 != AttackResult.MISS) {
                damage += ((Polearm) weapon).rollPolearmMasterDamage() + toDamageBonus;
                damage += improvedSmite();
                if (attackRoll2 == AttackResult.CRIT) { //if the CLEAVE crits as well
                    damage += weapon.getMaxDamage();
                }
            }
        }
        return damage;
    }

    @Override
    public AttackResult weaponAttack(Weapon weapon, Target target, Advantage advantage, int attackBonus) {
        return weapon.rollAttack(attackBonus, advantage, target.getAc());
    }

    @Override
    public AttackResult polearmAttack(Polearm weapon, Target target, Advantage advantage, int attackBonus) {
        return weapon.polearmMasterAttack(attackBonus, advantage, target.getAc());
    }
}
