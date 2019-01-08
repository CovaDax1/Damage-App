package com.damagesimulator.global;

import java.util.Random;

public class d10 {
    private static d10 d10_instance = null;
    private Random dieRoll;

    private d10() {
        dieRoll = new Random();
    }

    public static d10 getDie() {
        if(d10_instance == null)
            d10_instance = new d10();
        return d10_instance;
    }

    public int roll() {
        return dieRoll.nextInt(9) + 1;
    }

    public int roll(int num) {
        int total = 0;
        for(int i = 0; i < num; i++) {
            total += dieRoll.nextInt(9) + 1;
        }
        return total;
    }
}
