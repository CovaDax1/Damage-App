package com.damagesimulator.PlayerCharacter;

import com.damagesimulator.PlayerCharacter.action.Attack;
import com.damagesimulator.equipment.armor.Armor;
import com.damagesimulator.equipment.armor.Shield;
import com.damagesimulator.equipment.weapon.Unarmed;
import com.damagesimulator.equipment.weapon.core.Weapon;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;
import com.damagesimulator.global.d4;

public abstract class BaseCharacter<W extends Weapon> {
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
    protected boolean bonusActionAvailable;

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

    public void bless(boolean bless) {
        this.blessed = bless;
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

    public int getToAttackBonus(W weapon) {
        return getAttackAbsBonus(weapon) + getProficiency() + weapon.getEnchantmentBonus() + (blessed ? d4.getDie().roll() : 0);
    }

    public int getToDamageBonus(W weapon) {
        return getAttackAbsBonus(weapon) + weapon.getEnchantmentBonus();
    }

    public int getAttackAbsBonus(Attack attack) {
        switch(attack.getAttackAbilityScore()) {
            case STRENGTH:
                return this.strength.getMod();
            case DEXTERITY:
                return this.dexterity.getMod();
            case CONSTITUTION:
                return this.constitution.getMod();
            case INTELLIGENCE:
                return this.intelligence.getMod();
            case WISDOM:
                return this.wisdom.getMod();
            case CHARISMA:
                return this.charisma.getMod();
        }
        return 0;
    }

    protected abstract int unarmedAttack(Target target, Advantage advantage, int attackBonus, int damageBonus);

    public abstract AttackResult weaponAttack(W weapon, Target target, Advantage advantage, int attackBonus);
}
