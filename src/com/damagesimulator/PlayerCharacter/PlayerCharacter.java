package com.damagesimulator.PlayerCharacter;

import com.damagesimulator.PlayerCharacter.PlayerClass.BaseClass;

public abstract class PlayerCharacter extends BaseCharacter {

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


    public PlayerCharacter(AbilityScore strength, AbilityScore dexterity, AbilityScore constitution, AbilityScore intelligence, AbilityScore wisdom, AbilityScore charisma) {
        super(strength, dexterity, constitution, intelligence, wisdom, charisma);
    }

    protected void init() {
        this.hp = getHp();
        this.proficiency = getProficiency();
    }

    public void levelUp(BaseClass baseClass) {
        levelUp(baseClass, 1);
        init();
    }
    
    public void levelUp(BaseClass baseClass, int num) {
        switch (baseClass) {
            case BARBARIAN: barbarianLevels += num; break;
            case BARD: bardLevels += num; break;
            case CLERIC: clericLevels += num; break;
            case DRUID: druidLevels += num; break;
            case FIGHTER: fighterLevels += num; break;
            case MONK: monkLevels += num; break;
            case PALADIN: paladinLevels += num; break;
            case RANGER: rangerLevels += num; break;
            case ROGUE: rogueLevels += num; break;
            case SORCERER: sorcererLevels += num; break;
            case WARLOCK: warlockLevels += num; break;
            case WIZARD: wizardLevels += num; break;
        }
        init();
    }

    public int getLevel() {
        return barbarianLevels + bardLevels + clericLevels + druidLevels + fighterLevels + monkLevels + paladinLevels + rangerLevels + rogueLevels + sorcererLevels + warlockLevels + wizardLevels;
    }

    public int getHp() {
        int hp = getLevel() * this.getConstitution().getMod();
        hp += barbarianLevels * (12/2) + 2;
        hp += (paladinLevels + fighterLevels * rangerLevels) * (10 / 2) + 1;
        hp += (clericLevels + bardLevels + druidLevels + monkLevels + warlockLevels + rogueLevels) * (8 / 2) + 1;
        hp += (sorcererLevels + wizardLevels) * (6 / 2) + 1;
        return hp;
    }

    public int getProficiency() {
        return 2 + Math.floorDiv(getLevel(), 4);
    }
}
