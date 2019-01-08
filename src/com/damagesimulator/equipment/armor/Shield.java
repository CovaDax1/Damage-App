package com.damagesimulator.equipment.armor;

public class Shield {
    private int ac;
    private int bonus;

    public Shield() {
        this.ac = 2;
        this.bonus = 0;
    }

    public Shield(int bonus) {
        this.ac = 2;
        this.bonus = bonus;
    }

    public int getAc() {
        return ac + bonus;
    }
}
