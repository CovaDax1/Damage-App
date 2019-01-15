package com.damagesimulator.equipment.armor;

public class Scale extends Armor {
    public Scale() {
        super(14, 2, ArmorType.Medium, 0);
    }

    public Scale(int bonusAc) {
        super(14, 2, ArmorType.Medium, bonusAc);
    }
}
