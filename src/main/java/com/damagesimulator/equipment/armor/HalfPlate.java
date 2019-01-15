package com.damagesimulator.equipment.armor;

public class HalfPlate extends Armor {
    public HalfPlate() {
        super(15, 2, ArmorType.Medium);
    }

    public HalfPlate(int bonusAc) {
        super(15, 2, ArmorType.Medium, bonusAc);
    }
}

