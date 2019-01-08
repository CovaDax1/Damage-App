package com.damagesimulator.equipment.weapon;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.equipment.weapon.attributes.DamageType;
import com.damagesimulator.equipment.weapon.attributes.Versatile;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.global.Die;

public class Longsword extends MeleeWeapon implements Versatile {
    private Die versatileDamageDie;

    public Longsword() {
        super(AbilityScore.AbilityScores.STRENGTH, 0);
        this.damageDie = new Die(1,8);
        this.versatileDamageDie = new Die(1,10);
        this.versatile = true;
        this.damageType = DamageType.Slashing;
    }

    public Longsword(int enchantment) {
        super(AbilityScore.AbilityScores.STRENGTH, enchantment);
        this.damageDie = new Die(1,8);
        this.versatileDamageDie = new Die(1,10);
        this.versatile = true;
        this.damageType = DamageType.Slashing;
    }

    @Override
    public int rollVersatile() {
        int bonusDamage = getEnchantmentDamage();
        if(bonusDamageDie != null) bonusDamage += bonusDamageDie.roll();
        return versatileDamageDie.roll() + bonusDamage;
    }

    @Override
    public int getMaxVersatileDamage() {
        int bonusDamage = getEnchantmentDamage();
        if(bonusDamageDie != null ) bonusDamage = this.bonusDamageDie.getNum() * this.bonusDamageDie.getDie();
        return (this.versatileDamageDie.getNum() * this.versatileDamageDie.getDie()) + bonusDamage;
    }
}
