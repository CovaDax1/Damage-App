package com.damagesimulator.PlayerCharacter.build;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.PlayerCharacter.PlayerCharacter;
import com.damagesimulator.PlayerCharacter.PlayerClass.paladin.OathOfVengeance;
import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.PlayerCharacter.feat.GreatWeaponMastery;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;

public class CovaDaxImpl extends PlayerCharacter<MeleeWeapon> implements OathOfVengeance, GreatWeaponMastery {
    public CovaDaxImpl(AbilityScore strength, AbilityScore dexterity, AbilityScore constitution, AbilityScore intelligence, AbilityScore wisdom, AbilityScore charisma) {
        super(strength, dexterity, constitution, intelligence, wisdom, charisma);
    }

    public static CovaDaxImpl build(int s, int d, int c, int i, int w, int h) {
        return new CovaDaxImpl(
                new AbilityScore(s),
                new AbilityScore(d),
                new AbilityScore(c),
                new AbilityScore(i),
                new AbilityScore(w),
                new AbilityScore(h));
    }

    @Override
    public void longRest() {

    }

    @Override
    public void shortRest() {

    }

    @Override
    public void shortRest(int hitDie) {

    }

    public int conservativeMultiAttack(MeleeWeapon weapon, Target target, Advantage advantage) {
        this.bonusActionAvailable = true;
        int damage = 0;
        for (int i = 0; i < 2; i++)
            damage += rollConservativeAttack(weapon, target, advantage, getToAttackBonus(weapon), getToDamageBonus(weapon));
        return damage;
    }

    @Override
    protected int rollConservativeAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        return 0;
    }

    @Override
    protected int rollConservativeDamage(MeleeWeapon weapon, int damageBonus, boolean crit) {
        return 0;
    }

    @Override
    protected int rollEconomicAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        return 0;
    }

    @Override
    protected int rollEconomicDamage(MeleeWeapon weapon, Target target, int damageBonus, boolean crit) {
        return 0;
    }

    @Override
    protected int rollLiberalAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        return 0;
    }

    @Override
    protected int rollLiberalDamage(MeleeWeapon weapon, Target target, int damageBonus, boolean crit) {
        return 0;
    }

    @Override
    protected int unarmedAttack(Target target, Advantage advantage, int attackBonus, int damageBonus) {
        return 0;
    }

    @Override
    public AttackResult weaponAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus) {
        return null;
    }

    @Override
    public void vowOfEnmity(Target target) {

    }

    @Override
    public int getLayOnHandsPool() {
        return 0;
    }

    @Override
    public int getChannelDivinityCount() {
        return 0;
    }

    @Override
    public int smite(int spellLevel, boolean undead) {
        return 0;
    }

    @Override
    public int improvedSmite() {
        return 0;
    }

    @Override
    public boolean shouldIPowerAttack(Target t, Advantage adv, MeleeWeapon weapon, int atkBonus) {
        return false;
    }

    @Override
    public AttackResult powerAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus) {
        return null;
    }

    @Override
    public AttackResult cleave(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus) {
        return null;
    }

    @Override
    public AttackResult powerCleave(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus) {
        return null;
    }
}
