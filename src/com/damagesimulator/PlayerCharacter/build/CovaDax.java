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
import com.damagesimulator.equipment.weapon.core.Weapon;
import com.damagesimulator.global.*;

public class CovaDax extends PlayerCharacter implements SpellCaster, OathOfVengeance {
    protected int layOnHands;
    protected int channelDivinity;
    protected int[] spellSlots = new int[9];
    protected SmiteDie smiteDie;

    public CovaDax(AbilityScore strength, AbilityScore dexterity, AbilityScore constitution, AbilityScore intelligence, AbilityScore wisdom, AbilityScore charisma) {
        super(strength, dexterity, constitution, intelligence, wisdom, charisma);
        init();
    }

    public static CovaDax build(int s, int d, int c, int i, int w, int h) {
        return new CovaDax(
                new AbilityScore(s),
                new AbilityScore(d),
                new AbilityScore(c),
                new AbilityScore(i),
                new AbilityScore(w),
                new AbilityScore(h));
    }

    protected void init() {
        super.init();
        smiteDie = new SmiteDie();
        longRest();
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
    //spell smite as BA?
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
                if(target.getHp() > target.getMaxHp() / 2) {
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
        return damage;
    }

    //attack economically
    //smite using lowest first
    public int attackEconomically(MeleeWeapon weapon, Target target, Advantage advantage) {
        int damage = 0;
        int toAttackBonus = getAttackAbsBonus(weapon) + (blessed ? d4.getDie().roll() : 0);
        int toDamageBonus = (weapon.finesse() && dexterity.getMod() > strength.getMod() ? dexterity.getMod() : strength.getMod());

        for (int i = 0; i < 2; i++) {
            AttackResult  attackRoll = weaponAttack(weapon, target, advantage, toAttackBonus);
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
        return damage;
    }

    //attack conservatively
    //no smite
    public int attackConservatively(MeleeWeapon weapon, Target target, Advantage advantage) {
        int damage = 0;
        int toAttackBonus = getAttackAbsBonus(weapon) + (blessed ? d4.getDie().roll() : 0);
        int toDamageBonus = (weapon.finesse() && dexterity.getMod() > strength.getMod() ? dexterity.getMod() : strength.getMod());

        for (int i = 0; i < 2; i++) {
            AttackResult attackRoll = weaponAttack(weapon, target, advantage, toAttackBonus);

            if (attackRoll != AttackResult.MISS) {
                damage += weapon.rollDamage() + toDamageBonus;
                damage += improvedSmite();
                if (attackRoll == AttackResult.CRIT) {
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
    public int unarmedAttack(Target target, Advantage advantage, int attackbonus, int damageBonus) {
        int damage = 0;
        int toAttackBonus = strength.getMod() + (blessed ? d4.getDie().roll() : 0);
        int toDamageBonus = strength.getMod();
        AttackResult attackRoll = unarmed.rollAttack(toAttackBonus, advantage, target.getAc());
        if (attackRoll != AttackResult.MISS)
            damage += this.unarmed.rollDamage() + toDamageBonus;
        return damage;
    }

    //Spellcaster Methods

    @Override
    public void refreshSpellSlots() {
        int casterLevel = getCasterLevel();
        if (casterLevel > 0) {
            spellSlots[0] = 2;
        }

        if (casterLevel > 1) {
            spellSlots[0]++;
        }

        if (casterLevel > 2) {
            spellSlots[0]++;
            spellSlots[1] = 2;
        }

        if (casterLevel > 3) {
            spellSlots[1]++;
        }

        if (casterLevel > 4) {
            spellSlots[2] = 2;
        }

        if (casterLevel > 5) {
            spellSlots[2]++;
        }

        if (casterLevel > 6) {
            spellSlots[3] = 1;
        }

        if (casterLevel > 7) {
            spellSlots[3]++;
        }

        if (casterLevel > 8) {
            spellSlots[3]++;
            spellSlots[4] = 1;
        }

        if (casterLevel > 9) {
            spellSlots[4]++;
        }

        if (casterLevel > 10) {
            spellSlots[5] = 1;
        }

        if (casterLevel > 12) {
            spellSlots[6] = 1;
        }

        if (casterLevel > 14) {
            spellSlots[7] = 1;
        }

        if (casterLevel > 16) {
            spellSlots[8] = 1;
        }

        if (casterLevel > 17) {
            spellSlots[4]++;
        }

        if (casterLevel > 18) {
            spellSlots[5]++;
        }

        if (casterLevel > 19) {
            spellSlots[6]++;
        }
    }

    @Override
    public int getCasterLevel() {
        int cLevel = 0;
        cLevel += wizardLevels + sorcererLevels + bardLevels + clericLevels + druidLevels;
        cLevel += (paladinLevels + rangerLevels) / 2;
        return cLevel;
    }

    @Override
    public int getHighestAvailableSpellSlot() {
        int cLevel = getCasterLevel();
        int highestPossibleSpellSlot = (int) Math.ceil(cLevel / 2) - 1;
        for (int i = highestPossibleSpellSlot; i >= 0; i--)
            if (this.spellSlots[i] > 0)
                return i+1;
        return 0;
    }

    @Override
    public int getLowestAvailableSpellSlot() {
        int cLevel = getCasterLevel();
        for (int i = 0; i < (int) Math.ceil(cLevel / 2); i++) {
            if (this.spellSlots[i] > 0) return i;
        }
        return 0;
    }

    @Override
    public int getSpellSlotsByLevel(int i) {
        return this.spellSlots[i];
    }

    @Override
    public int spellAttack(SpellAttack spellAttack) {
        return 0;
    }

    @Override
    public void castSpell(Spell spell) {

    }
}
