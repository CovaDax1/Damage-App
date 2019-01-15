package com.damagesimulator.PlayerCharacter.build;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.PlayerCharacter.PlayerClass.SpellCaster;
import com.damagesimulator.PlayerCharacter.PlayerClass.paladin.OathOfVengeance;

public class CovaDaxASI extends CovaDax implements SpellCaster, OathOfVengeance {
    public CovaDaxASI(AbilityScore strength, AbilityScore dexterity, AbilityScore constitution, AbilityScore intelligence, AbilityScore wisdom, AbilityScore charisma) {
        super(strength, dexterity, constitution, intelligence, wisdom, charisma);
        init();
    }

    public static CovaDaxASI build(int s, int d, int c, int i, int w, int h) {
        return new CovaDaxASI(
                new AbilityScore(s),
                new AbilityScore(d),
                new AbilityScore(c),
                new AbilityScore(i),
                new AbilityScore(w),
                new AbilityScore(h));
    }
}
