package com.damagesimulator.global;

import java.util.Random;

public class d4 {
    private static d4 d4_instance = null;
    private Random dieRoll;

    private d4() {
        dieRoll = new Random();
    }

    public static d4 getDie() {
        if(d4_instance == null)
            d4_instance = new d4();
        return d4_instance;
    }

    public int roll() {
        return dieRoll.nextInt(3) + 1;
    }

    public int roll(int num) {
        int total = 0;
        for(int i = 0; i < num; i++) {
            total += dieRoll.nextInt(3) + 1;
        }
        return total;
    }
}
