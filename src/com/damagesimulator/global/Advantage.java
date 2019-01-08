package com.damagesimulator.global;

public enum Advantage {
    ADVANTAGE(1),
    STANDARD(0),
    DISADVANTAGE(-1);

    private int value;

    public int getValue() {
        return this.value;
    }

    Advantage(int value) {
        this.value = value;
    }
}
