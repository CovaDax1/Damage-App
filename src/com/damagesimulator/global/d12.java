package com.damagesimulator.global;

import java.util.Random;

public class d12 {
    private static d12 d12_instance = null;
    private Random dieRoll;

    private d12() {
        dieRoll = new Random();
    }

    public static d12 getInstance() {
        if(d12_instance == null)
            d12_instance = new d12();
        return d12_instance;
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
