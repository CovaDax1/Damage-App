package com.damagesimulator.equipment.weapon.core;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.PlayerCharacter.action.Attack;
import com.damagesimulator.equipment.weapon.attributes.DamageType;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;
import com.damagesimulator.global.Die;
import com.damagesimulator.global.d20;

public abstract class Weapon implements Attack {
    protected int enchantmentBonus;
    protected int enchantmentDamage;

    protected DamageType damageType;
    protected DamageType bonusDamageType;

    protected AbilityScore.AbilityScores abilityScore;

    protected boolean twoHanded;
    protected boolean heavy;
    protected boolean light;

    protected Die damageDie;
    protected Die bonusDamageDie;

    public Weapon(AbilityScore.AbilityScores abs) {
        this.abilityScore = abs;
        this.enchantmentBonus = 0;
        this.enchantmentDamage = 0;
    }

    public Weapon(AbilityScore.AbilityScores abs, int enchantment) {
        this.abilityScore = abs;
        this.enchantmentBonus = enchantment;
        this.enchantmentDamage = enchantment;
    }

    public int getEnchantmentBonus() {
        return enchantmentBonus;
    }

    public int getEnchantmentDamage() {
        return enchantmentDamage;
    }

    public boolean isTwoHanded() {
        return twoHanded;
    }

    public boolean isHeavy() {
        return heavy;
    }

    public boolean isLight() {
        return light;
    }

    @Override
    public AttackResult rollAttack(int toAttackBonus, Advantage advantage, int targetAc) {
        int attackRoll = d20.getDie().roll(advantage);
        if (attackRoll == 20) return AttackResult.CRIT;
        else if (attackRoll == 1) return AttackResult.MISS;
        else
            return (attackRoll + toAttackBonus + getEnchantmentBonus() < targetAc) ? AttackResult.MISS : AttackResult.HIT;
    }

    @Override
    public AttackResult rollAttack(int toAttackBonus, Advantage advantage, int targetAc, int critRange) {
        int attackRoll = d20.getDie().roll(advantage);
        if (attackRoll == critRange) return AttackResult.CRIT;
        else if (attackRoll == 1) return AttackResult.MISS;
        else
            return (attackRoll + toAttackBonus + getEnchantmentBonus() < targetAc) ? AttackResult.MISS : AttackResult.HIT;
    }

    public int rollDamage() {
        int bonusDamage = getEnchantmentDamage();
        if (bonusDamageDie != null) bonusDamage += bonusDamageDie.roll();
        return damageDie.roll() + bonusDamage;
    }

    public int getMaxDamage() {
        return getMaxWeaponDamage() + getMaxEnchantmentDamage();
    }

    private int getMaxWeaponDamage() {
        return this.damageDie.getNum() * this.damageDie.getDie();
    }

    private int getMaxEnchantmentDamage() {
        int bonusDamage = getEnchantmentDamage();
        if (bonusDamageDie != null) bonusDamage = this.bonusDamageDie.getNum() * this.bonusDamageDie.getDie();
        return bonusDamage;
    }

    public int getAverageDamage() {
        int weaponDamage = this.damageDie.getNum() * ((1 + getMaxWeaponDamage()) / 2);
        if (bonusDamageDie != null)
            weaponDamage += this.bonusDamageDie.getNum() * ((1 + getMaxEnchantmentDamage() / 2));
        return weaponDamage;
    }

    @Override
    public AbilityScore.AbilityScores getAttackAbilityScore() {
        return abilityScore;
    }

    @Override
    public void setAttackAbilityScore(AbilityScore.AbilityScores abilityScore) {
        this.abilityScore = abilityScore;
    }
}
