package com.damagesimulator.equipment.armor;

import com.damagesimulator.PlayerCharacter.AbilityScore;

public abstract class Armor {
    private ArmorType armorType;
    private int ac;
    private int bonusAc;
    private int dexBonus;

    public Armor(int ac, int dexBonus, ArmorType armorType) {
        this.ac = ac;
        this.dexBonus = dexBonus;
        this.armorType = armorType;
    }

    public Armor(int ac, int dexBonus, ArmorType armorType, int bonusAc) {
        this.ac = ac;
        this.dexBonus = dexBonus;
        this.armorType = armorType;
        this.bonusAc = bonusAc;
    }

    public int getAc(AbilityScore dexterity) {
        int ac = this.ac + this.bonusAc;
        if (this.dexBonus > 0) {
            if (this.dexBonus < dexterity.getMod())
                ac += dexterity.getMod();
            else
                ac += dexBonus;
        }
        return ac;
    }
}

