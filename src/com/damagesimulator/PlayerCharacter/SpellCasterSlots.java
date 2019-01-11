package com.damagesimulator.PlayerCharacter;

import com.damagesimulator.PlayerCharacter.PlayerClass.SpellCaster;

public class SpellCasterSlots extends SpellSlots<SpellCaster> {
    private int casterLevel = 0;
    private int[] spellSlots = new int[9];

    public SpellCasterSlots(SpellCaster spellCaster) {
        this.casterLevel = spellCaster.getSpellCasterLevel();
    }

    public int getHighestAvailableSpellSlot() {
        int highestPossibleSpellSlot = (int) Math.ceil(casterLevel / 2) - 1;
        for (int i = highestPossibleSpellSlot; i >= 0; i--)
            if (this.spellSlots[i] > 0)
                return i + 1;
        return 0;
    }

    public int getLowestAvailableSpellSlot() {
        for (int i = 0; i < (int) Math.ceil(casterLevel / 2); i++) {
            if (this.spellSlots[i] > 0) return i;
        }
        return 0;
    }

    public int getSpellSlotsByLevel(int i) {
        return this.spellSlots[i];
    }

    public void spendSpellSlot(int spellSlot) {
        if(getSpellSlotsByLevel(spellSlot) > 0) {
            this.spellSlots[spellSlot]--;
        }
    }

    public void refreshSpellSlots() {
        if (casterLevel > 0) {
            spellSlots[0] = 2;
        }

        if (casterLevel > 1) {
            spellSlots[0]++;
        }

        if (casterLevel > 2) {
            spellSlots[0]++;
            spellSlots[1] = 2;
        }

        if (casterLevel > 3) {
            spellSlots[1]++;
        }

        if (casterLevel > 4) {
            spellSlots[2] = 2;
        }

        if (casterLevel > 5) {
            spellSlots[2]++;
        }

        if (casterLevel > 6) {
            spellSlots[3] = 1;
        }

        if (casterLevel > 7) {
            spellSlots[3]++;
        }

        if (casterLevel > 8) {
            spellSlots[3]++;
            spellSlots[4] = 1;
        }

        if (casterLevel > 9) {
            spellSlots[4]++;
        }

        if (casterLevel > 10) {
            spellSlots[5] = 1;
        }

        if (casterLevel > 12) {
            spellSlots[6] = 1;
        }

        if (casterLevel > 14) {
            spellSlots[7] = 1;
        }

        if (casterLevel > 16) {
            spellSlots[8] = 1;
        }

        if (casterLevel > 17) {
            spellSlots[4]++;
        }

        if (casterLevel > 18) {
            spellSlots[5]++;
        }

        if (casterLevel > 19) {
            spellSlots[6]++;
        }
    }
}