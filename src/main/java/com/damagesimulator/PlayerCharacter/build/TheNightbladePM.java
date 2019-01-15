package com.damagesimulator.PlayerCharacter.build;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.PlayerCharacter.PlayerClass.PactCaster;
import com.damagesimulator.PlayerCharacter.PlayerClass.SpellCaster;
import com.damagesimulator.PlayerCharacter.PlayerClass.warlock.Hexblade;
import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.PlayerCharacter.feat.PolearmMaster;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.equipment.weapon.core.Polearm;
import com.damagesimulator.global.Advantage;

public class TheNightbladePM extends TheNightblade<MeleeWeapon> implements Hexblade, PolearmMaster, SpellCaster, PactCaster {
    public TheNightbladePM(AbilityScore strength, AbilityScore dexterity, AbilityScore constitution, AbilityScore intelligence, AbilityScore wisdom, AbilityScore charisma) {
        super(strength, dexterity, constitution, intelligence, wisdom, charisma);
    }

    public static TheNightbladePM build(int s, int d, int c, int i, int w, int h) {
        return new TheNightbladePM(
                new AbilityScore(s),
                new AbilityScore(d),
                new AbilityScore(c),
                new AbilityScore(i),
                new AbilityScore(w),
                new AbilityScore(h));
    }

    @Override
    public int economicMultiAttack(MeleeWeapon weapon, Target target, Advantage advantage) {
        int damage = super.economicMultiAttack(weapon, target, advantage);
        if (bonusActionAvailable) {
            bonusActionAvailable = false;
            damage += rollEconomicAttack(((Polearm) weapon).getHaft(), target, advantage, getToAttackBonus(weapon), getToDamageBonus(weapon));
        }
        return damage;
    }

    public int liberalMultiAttack(MeleeWeapon weapon, Target target, Advantage advantage) {
        int damage = super.liberalMultiAttack(weapon, target, advantage);
        if (bonusActionAvailable) {
            bonusActionAvailable = false;
            damage += rollLiberalAttack(((Polearm) weapon).getHaft(), target, advantage, getToAttackBonus(weapon), getToDamageBonus(weapon));
        }
        return damage;
    }

    public int conservativeMultiAttack(MeleeWeapon weapon, Target target, Advantage advantage) {
        int damage = super.conservativeMultiAttack(weapon, target, advantage);
        if (bonusActionAvailable) {
            bonusActionAvailable = false;
            damage += rollConservativeAttack(((Polearm) weapon).getHaft(), target, advantage, getToAttackBonus(weapon), getToDamageBonus(weapon));
        }
        return damage;
    }

}
