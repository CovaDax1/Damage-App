package com.damagesimulator.PlayerCharacter;

import com.damagesimulator.PlayerCharacter.PlayerClass.BaseClass;
import com.damagesimulator.equipment.weapon.core.Weapon;
import com.damagesimulator.global.Advantage;

public abstract class PlayerCharacter<W extends Weapon> extends BaseCharacter<W> {

    protected int barbarianLevels;
    protected int bardLevels;
    protected int clericLevels;
    protected int druidLevels;
    protected int fighterLevels;
    protected int monkLevels;
    protected int paladinLevels;
    protected int rangerLevels;
    protected int rogueLevels;
    protected int sorcererLevels;
    protected int warlockLevels;
    protected int wizardLevels;
    protected int attackCount = 1;


    public PlayerCharacter(AbilityScore strength, AbilityScore dexterity, AbilityScore constitution, AbilityScore intelligence, AbilityScore wisdom, AbilityScore charisma) {
        super(strength, dexterity, constitution, intelligence, wisdom, charisma);
    }

    protected void init() {
        this.hp = getHp();
        this.proficiency = getProficiency();
    }

    public void levelUp(BaseClass baseClass) {
        levelUp(baseClass, 1);
    }

    public void levelUp(BaseClass baseClass, int num) {
        switch (baseClass) {
            case BARBARIAN:
                barbarianLevels += num;
                break;
            case BARD:
                bardLevels += num;
                break;
            case CLERIC:
                clericLevels += num;
                break;
            case DRUID:
                druidLevels += num;
                break;
            case FIGHTER:
                fighterLevels += num;
                break;
            case MONK:
                monkLevels += num;
                break;
            case PALADIN:
                paladinLevels += num;
                break;
            case RANGER:
                rangerLevels += num;
                break;
            case ROGUE:
                rogueLevels += num;
                break;
            case SORCERER:
                sorcererLevels += num;
                break;
            case WARLOCK:
                warlockLevels += num;
                break;
            case WIZARD:
                wizardLevels += num;
                break;
        }
        init();
    }

    public int getLevel() {
        return barbarianLevels + bardLevels + clericLevels + druidLevels + fighterLevels + monkLevels + paladinLevels + rangerLevels + rogueLevels + sorcererLevels + warlockLevels + wizardLevels;
    }

    public int getHp() {
        int hp = getLevel() * this.getConstitution().getMod();
        hp += barbarianLevels * (12 / 2) + 2;
        hp += (paladinLevels + fighterLevels * rangerLevels) * (10 / 2) + 1;
        hp += (clericLevels + bardLevels + druidLevels + monkLevels + warlockLevels + rogueLevels) * (8 / 2) + 1;
        hp += (sorcererLevels + wizardLevels) * (6 / 2) + 1;
        return hp;
    }

    public int getProficiency() {
        return 2 + Math.floorDiv(getLevel(), 4);
    }

    public abstract void longRest();

    public abstract void shortRest();

    public abstract void shortRest(int hitDie);

    public int conservativeMultiAttack(W weapon, Target target, Advantage advantage) {
        this.bonusActionAvailable = true;
        int damage = 0;
        for (int i = 0; i < this.attackCount; i++)
            damage += rollConservativeAttack(weapon, target, advantage, getToAttackBonus(weapon), getToDamageBonus(weapon));
        return damage;
    }

    protected abstract int rollConservativeAttack(W weapon, Target target, Advantage advantage, int attackBonus, int damageBonus);

    protected abstract int rollConservativeDamage(W weapon, int damageBonus, boolean crit);

    public int economicMultiAttack(W weapon, Target target, Advantage advantage) {
        this.bonusActionAvailable = true;
        int damage = 0;
        for (int i = 0; i < this.attackCount; i++)
            damage += rollEconomicAttack(weapon, target, advantage, getToAttackBonus(weapon), getToDamageBonus(weapon));
        return damage;
    }

    protected abstract int rollEconomicAttack(W weapon, Target target, Advantage advantage, int attackBonus, int damageBonus);

    protected abstract int rollEconomicDamage(W weapon, Target target, int damageBonus, boolean crit);

    public int liberalMultiAttack(W weapon, Target target, Advantage advantage) {
        this.bonusActionAvailable = true;
        int damage = 0;
        for (int i = 0; i < this.attackCount; i++)
            damage += rollLiberalAttack(weapon, target, advantage, getToAttackBonus(weapon), getToDamageBonus(weapon));
        return damage;
    }

    protected abstract int rollLiberalAttack(W weapon, Target target, Advantage advantage, int attackBonus, int damageBonus);

    protected abstract int rollLiberalDamage(W weapon, Target target, int damageBonus, boolean crit);
}
