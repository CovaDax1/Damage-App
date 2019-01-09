package com.damagesimulator.PlayerCharacter.build;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.PlayerCharacter.PlayerCharacter;
import com.damagesimulator.PlayerCharacter.PlayerClass.BaseClass;
import com.damagesimulator.PlayerCharacter.PlayerClass.SpellCaster;
import com.damagesimulator.PlayerCharacter.PlayerClass.paladin.OathOfVengeance;
import com.damagesimulator.PlayerCharacter.PlayerClass.paladin.SmiteDie;
import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.PlayerCharacter.action.SpellAttack;
import com.damagesimulator.PlayerCharacter.feat.GreatWeaponMastery;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.equipment.weapon.core.Polearm;
import com.damagesimulator.equipment.weapon.core.Weapon;
import com.damagesimulator.global.*;

import static com.damagesimulator.global.AttackResult.CRIT;
import static com.damagesimulator.global.AttackResult.MISS;

public class CovaDaxGWF extends CovaDax implements SpellCaster, OathOfVengeance, GreatWeaponMastery {
    private int layOnHands;
    private int channelDivinity;
    private int[] spellSlots = new int[9];
    private SmiteDie smiteDie;
    private volatile boolean bonusActionAvailable;

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

    @Override
    public void levelUp(BaseClass baseClass) {
        super.levelUp(baseClass);
    }

    public void levelUp(BaseClass baseClass, int num) {
        super.levelUp(baseClass, num);
    }

    public void longRest() {
        hp = this.getHp();
        layOnHands = paladinLevels * 5;
        if (paladinLevels > 3 || clericLevels > 0)
            channelDivinity = (clericLevels > 5) ? 2 : 1;
        refreshSpellSlots();
    }

    @Override
    public int getLayOnHandsPool() {
        return this.layOnHands;
    }

    @Override
    public int getChannelDivinityCount() {
        return this.channelDivinity;
    }

    @Override
    public void vowOfEnmity(Target target) {
        target.setAdvOnHit(Advantage.ADVANTAGE);
    }

    @Override
    public int smite(int spellLevel, boolean undead) {
        if (spellLevel < 1) return 0;
        Die smiteDie = new Die(spellLevel + 1 + (undead ? 1 : 0), 8);
        spellSlots[spellLevel - 1]--;
        return smiteDie.roll();
    }

    @Override
    public int improvedSmite() {
        if (paladinLevels > 10)
            return smiteDie.improvedSmite();
        return 0;
    }

    //attack liberally
    //power attack, smite using highest first, spell smite as BA?
    @Override
    public int liberalMultiAttack(MeleeWeapon weapon, Target target, Advantage advantage) {
        this.bonusActionAvailable = true;
        int damage = 0;
        int toAttackBonus = getAttackAbsBonus(weapon) +  (blessed ? d4.getDie().roll() : 0);
        int toDamageBonus = (weapon.finesse() && dexterity.getMod() > strength.getMod() ? dexterity.getMod() : strength.getMod());
        for (int i = 0; i < 2; i++)
            damage += rollLiberalAttack(weapon, target, advantage, toAttackBonus, toDamageBonus);
        return damage;
    }

    //attack economically
    //power attack if adv, smite using lowest first
    // Max AC = attackBonus - damage/2 + 16
    @Override
    public int economicMultiAttack(MeleeWeapon weapon, Target target, Advantage advantage) {
        this.bonusActionAvailable = true;
        int damage = 0;
        int toAttackBonus = getAttackAbsBonus(weapon) +  (blessed ? d4.getDie().roll() : 0);
        int toDamageBonus = (weapon.finesse() && dexterity.getMod() > strength.getMod() ? dexterity.getMod() : strength.getMod());
        for (int i = 0; i < 2; i++)
            damage += rollEconomicAttack(weapon, target, advantage, toAttackBonus, toDamageBonus);
        return damage;
    }

    //attack conservatively
    //power attack if adv, no smite
    @Override
    public int conservativeMultiAttack(MeleeWeapon weapon, Target target, Advantage advantage) {
        this.bonusActionAvailable = true;
        int damage = 0;
        int toAttackBonus = getAttackAbsBonus(weapon) + (blessed ? d4.getDie().roll() : 0);
        int toDamageBonus = (weapon.finesse() && dexterity.getMod() > strength.getMod() ? dexterity.getMod() : strength.getMod());
        for (int i = 0; i < 2; i++)
            damage += rollConservativeAttack(weapon, target, advantage, toAttackBonus, toDamageBonus);
        return damage;
    }

    private int rollLiberalAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        int damage = 0;
        AttackResult attackRoll = powerAttack(weapon, target, advantage, attackBonus);
        if(attackRoll != MISS) damage += rollLiberalDamage(weapon, target, damageBonus + 10, attackRoll == CRIT);
        if(bonusActionAvailable && (attackRoll == CRIT || target.getHp() < 1)) {
            bonusActionAvailable = false;
            damage += rollLiberalAttack(weapon, target, advantage, attackBonus, damageBonus);
        }
        return damage;
    }

    private int rollLiberalDamage(MeleeWeapon weapon, Target target, int damageBonus, boolean crit) {
        int damage = 0;
        damage += weapon.rollDamage() + damageBonus;
        damage += improvedSmite();
        int smiteSlot = 0;
        if(target.isBloodied())
            smiteSlot = this.getHighestAvailableSpellSlot();
        else
            smiteSlot = this.getLowestAvailableSpellSlot();
        damage += smite(smiteSlot, target.isUndead());
        if (crit) {
            damage += weapon.getMaxDamage();
            damage += smiteDie.maxDamage(smiteSlot, target.isUndead());
        }
        return damage;
    }

    private int rollEconomicAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        int damage = 0;
        AttackResult attackRoll;
        if (shouldIPowerAttack(target, advantage, weapon, attackBonus)) {
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

    private int rollEconomicDamage(MeleeWeapon weapon, Target target, int damageBonus, boolean crit) {
        int damage = 0;
            damage += weapon.rollDamage() + damageBonus;
            damage += improvedSmite();
            int smiteSlot = 0;
            if (target.isBloodied())
                smiteSlot = this.getLowestAvailableSpellSlot();
            damage += smite(smiteSlot, target.isUndead());
            if (crit) {
                damage += weapon.getMaxDamage();
                damage += smiteDie.maxDamage(smiteSlot, target.isUndead());
            }
        return damage;
    }

    private int  rollConservativeAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        AttackResult attackRoll;
        int damage = 0;
        if (shouldIPowerAttack(target, advantage, weapon, attackBonus)) {
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

    private int rollConservativeDamage(Weapon weapon, int damageBonus, boolean crit) {
        int damage = 0;
        damage += weapon.rollDamage() + damageBonus;
        damage += improvedSmite();
        if (crit)
            damage += weapon.getMaxDamage();
        return damage;
    }

    @Override
    public AttackResult weaponAttack(Weapon weapon, Target target, Advantage advantage, int attackBonus) {
        return weapon.rollAttack(attackBonus, advantage, target.getAc());
    }

    // Max AC = attackBonus - damage/2 + 16
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
