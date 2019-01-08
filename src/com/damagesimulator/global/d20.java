package com.damagesimulator.global;

import java.util.Random;

public class d20 {
    private static d20 d20_instance = null;
    private Random dieRoll;

    private d20() {
        this.dieRoll = new Random();
    }

    public static d20 getDie() {
        if (d20_instance == null)
            d20_instance = new d20();
        return d20_instance;
    }

    public int roll(Advantage advantage) {
        if (advantage == Advantage.ADVANTAGE) return rollAdvantage();
        else if (advantage == Advantage.DISADVANTAGE) return rollDisadvantage();
        else return roll();
    }

    private int roll() {
        return dieRoll.nextInt(19) + 1;
    }

    public int roll(int num) {
        int total = 0;
        for (int i = 0; i < num; i++) {
            total += dieRoll.nextInt(19) + 1;
        }
        return total;
    }

    private int rollAdvantage() {
        int roll1 = roll();
        int roll2 = roll();
        if (roll2 > roll1) return roll2;
        else return roll1;
    }

    private int rollDisadvantage() {
        int roll1 = roll();
        int roll2 = roll();
        if (roll2 < roll1) return roll2;
        else return roll1;
    }
}
