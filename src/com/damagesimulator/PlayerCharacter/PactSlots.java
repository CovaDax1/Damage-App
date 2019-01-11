package com.damagesimulator.PlayerCharacter;

import com.damagesimulator.PlayerCharacter.PlayerClass.PactCaster;

public class PactSlots extends SpellSlots<PactCaster> {
    private int pactSlots;
    private int pactSlotLevel;
    private int casterLevel = 0;

    public PactSlots(PactCaster pactCaster) {
        this.casterLevel = pactCaster.getPactCasterLevel();
    }

    public int getAvailablePactSlots() {
        return pactSlots;
    }

    public int getPactSlotLevel() {
        return pactSlotLevel;
    }

    public void spendPactSlot() {
        if(pactSlots > 0)
            pactSlots--;
    }

    public void refreshSpellSlots() {
        if (casterLevel > 0) {
            pactSlots = 1;
            pactSlotLevel = 1;
        }

        if (casterLevel > 1) {
            pactSlots++;
        }

        if (casterLevel > 2) {
            pactSlotLevel++;
        }

        if (casterLevel > 4) {
            pactSlotLevel++;
        }

        if (casterLevel > 6) {
            pactSlotLevel++;
        }

        if (casterLevel > 8) {
            pactSlotLevel++;
        }

        if (casterLevel > 10) {
            pactSlots++;
        }

        if (casterLevel > 16) {
            pactSlots++;
        }
    }
}
