package com.damagesimulator.PlayerCharacter;

public class AbilityScore {
    private int value;

    public enum AbilityScores {
        STRENGTH,
        DEXTERITY,
        CONSTITUTION,
        INTELLIGENCE,
        WISDOM,
        CHARISMA
    }

    public AbilityScore(int value) {
        this.value = value;
    }

    public int getScore() {
        return value;
    }

    public int getMod() {
        return (int) Math.floor((value / 2) - 5);
    }
}
