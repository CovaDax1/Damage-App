package com.damagesimulator.global;

import java.util.Random;

public class d8 {
    private static d8 d8_instance = null;
    private Random dieRoll;

    private d8() {
        dieRoll = new Random();
    }

    public static d8 getDie() {
        if(d8_instance == null)
            d8_instance = new d8();
        return d8_instance;
    }

    public int roll() { return dieRoll.nextInt(7) + 1; }

    public int roll(int num) {
        int total = 0;
        for(int i = 0; i < num; i++) {
            total += dieRoll.nextInt(7) + 1;
        }
        return total;
    }
}
