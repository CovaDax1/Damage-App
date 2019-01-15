package com.damagesimulator.equipment.weapon;

import com.damagesimulator.PlayerCharacter.AbilityScore;
import com.damagesimulator.equipment.weapon.attributes.DamageType;
import com.damagesimulator.equipment.weapon.core.GreatWeapon;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.equipment.weapon.core.Polearm;
import com.damagesimulator.equipment.weapon.core.PolearmHaft;
import com.damagesimulator.global.Die;

public class Glaive extends MeleeWeapon implements Polearm, GreatWeapon {
    private PolearmHaft haft;

    public Glaive() {
        super(AbilityScore.AbilityScores.STRENGTH, 0);
        init();
    }

    public Glaive(int enchantment) {
        super(AbilityScore.AbilityScores.STRENGTH, enchantment);
        init();
    }

    private void init() {
        this.damageDie = new Die(1, 10);
        this.heavy = true;
        this.twoHanded = true;
        this.reach = true;
        this.damageType = DamageType.Slashing;
        this.haft = new PolearmHaft(this);
    }

    public PolearmHaft getHaft() {
        return haft;
    }
//    @Override
//    public AttackResult polearmMasterAttack(int toAttackBonus, Advantage advantage, int targetAc) {
//        return this.haft.polearmMasterAttack(toAttackBonus, advantage, targetAc);
//    }
//
//    @Override
//    public int rollPolearmMasterDamage() {
//        return this.haft.rollPolearmMasterDamage();
//    }
//
//    @Override
//    public int polearmMaxDamage() {
//        return this.haft.polearmMaxDamage();
//    }
//
//    @Override
//    public AttackResult rollPowerAttack(int toAttackBonus, Advantage advantage, int targetAc) {
//        int attackRoll = d20.getDie().roll(advantage);
//        if (attackRoll == 20) return AttackResult.CRIT;
//        else if (attackRoll == 1) return AttackResult.MISS;
//        else return (attackRoll + toAttackBonus + getEnchantmentBonus() - 5 < targetAc) ? AttackResult.MISS : AttackResult.HIT;
//    }
//
//    @Override
//    public int rollPowerDamage() {
//        int bonusDamage = getEnchantmentDamage() + 10;
//        if(bonusDamageDie != null) bonusDamage += bonusDamageDie.roll();
//        return damageDie.roll() + bonusDamage;
//    }
}
