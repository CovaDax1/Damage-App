package com.damagesimulator.PlayerCharacter;

import com.damagesimulator.PlayerCharacter.action.Attack;
import com.damagesimulator.equipment.armor.Armor;
import com.damagesimulator.equipment.armor.Shield;
import com.damagesimulator.equipment.weapon.Unarmed;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.equipment.weapon.core.Weapon;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;
import com.damagesimulator.global.d4;

public abstract class BaseCharacter {
    protected int hp;
    protected int proficiency;

    protected AbilityScore strength;
    protected AbilityScore dexterity;
    protected AbilityScore constitution;
    protected AbilityScore intelligence;
    protected AbilityScore wisdom;
    protected AbilityScore charisma;

    protected boolean strengthSave;
    protected boolean dexteritySave;
    protected boolean constitutionSave;
    protected boolean intelligenceSave;
    protected boolean wisdomSave;
    protected boolean charismaSave;

    protected Armor armor;
    protected Shield shield;
    protected Weapon mhWeapon;
    protected Weapon ohWeapon;
    protected Unarmed unarmed = new Unarmed();

    protected boolean blessed;

    public BaseCharacter(AbilityScore strength, AbilityScore dexterity, AbilityScore constitution, AbilityScore intelligence, AbilityScore wisdom, AbilityScore charisma) {
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
    }

    public abstract int getHp();

    public abstract int getProficiency();

    public AbilityScore getStrength() {
        return strength;
    }

    public int getStrengthSave() { return getStrength().getMod() + (strengthSave ? getProficiency() : 0); }

    public AbilityScore getDexterity() {
        return dexterity;
    }

    public int getDexteritySave() { return getDexterity().getMod() + (dexteritySave ? getProficiency() : 0); }

    public AbilityScore getConstitution() {
        return constitution;
    }

    public int getConstitutionSave() { return getConstitution().getMod() + (constitutionSave ? getProficiency() : 0); }

    public AbilityScore getIntelligence() {
        return intelligence;
    }

    public int getIntelligenceSave() { return getIntelligence().getMod() + (intelligenceSave ? getProficiency() : 0); }

    public AbilityScore getWisdom() {
        return wisdom;
    }

    public int getWisdomSave() { return getWisdom().getMod() + (wisdomSave ? getProficiency() : 0); }

    public AbilityScore getCharisma() {
        return charisma;
    }

    public int getCharismaSave() { return getCharisma().getMod() + (charismaSave ? getProficiency() : 0); }

    public int getAc() {
        return armor.getAc(dexterity) + shield.getAc();
    }

    public void bless() {
        this.blessed = true;
    }

    public Armor getArmor() {
        return armor;
    }

    public Shield getShield() {
        return shield;
    }

    public Weapon getMhWeapon() {
        return mhWeapon;
    }

    public Weapon getOhWeapon() {
        return ohWeapon;
    }

    public void equipWeapon(Weapon weapon) {
        this.mhWeapon = weapon;
    }

    public void equipOhWeapon(Weapon weapon) {
        this.ohWeapon = weapon;
    }

    public void equipArmor(Armor armor) { this.armor = armor; }

    public int getMhWeaponToHitBonus() {
        int absMod = 0;
        if (mhWeapon instanceof MeleeWeapon) {
            if (!((MeleeWeapon) mhWeapon).finesse())
                absMod = strength.getMod();
            else
                absMod = dexterity.getMod();
        } else {
            absMod = dexterity.getMod();
        }
        int bless = 0;
        if(blessed) bless += d4.getDie().roll();

        return absMod + getProficiency() + bless;
    }

    public int getAttackAbsBonus(Attack attack) {
        int total = 0;
        switch(attack.getAttackAbilityScore()) {
            case STRENGTH:
                total += this.strength.getMod(); break;
            case DEXTERITY:
                total += this.dexterity.getMod(); break;
            case CONSTITUTION:
                total += this.constitution.getMod(); break;
            case INTELLIGENCE:
                total += this.intelligence.getMod(); break;
            case WISDOM:
                total += this.wisdom.getMod(); break;
            case CHARISMA:
                total += this.charisma.getMod(); break;
        }

        return total + getProficiency();
    }

    protected abstract int unarmedAttack(Target target, Advantage advantage, int attackBonus, int damageBonus);

    public abstract AttackResult weaponAttack(Weapon weapon, Target target, Advantage advantage, int attackBonus);
}
