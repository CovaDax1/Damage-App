package com.damagesimulator.global;

import java.util.Random;

public class d6 {
    private static d6 d6_instance = null;
    private Random dieRoll;

    private d6() {
        dieRoll = new Random();
    }

    public static d6 getDie() {
        if(d6_instance == null)
            d6_instance = new d6();
        return d6_instance;
    }

    public int roll() { return dieRoll.nextInt(5) + 1; }

    public int roll(int num) {
        int total = 0;
        for(int i = 0; i < num; i++) {
            total += dieRoll.nextInt(5) + 1;
        }
        return total;
    }
}

