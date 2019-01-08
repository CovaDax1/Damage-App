package com.damagesimulator.global;

import java.util.Random;

public class Die {
    protected int num;
    protected int die;
    protected Random dieRoll;

    public Die(int num, int die) {
        this.dieRoll = new Random();
        this.num = num;
        this.die = die - 1;
    }

    public int getDie() {
        return die;
    }

    public int getNum() {
        return num;
    }

    public int roll() {
        int value = 0;
        for (int i = 0; i < num; i++) {
            value += dieRoll.nextInt(die) + 1;
        }
        return value;
    }
}
