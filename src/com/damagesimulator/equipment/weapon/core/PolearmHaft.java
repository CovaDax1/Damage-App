package com.damagesimulator.equipment.weapon.core;

import com.damagesimulator.equipment.weapon.attributes.DamageType;
import com.damagesimulator.global.Die;

public class PolearmHaft extends MeleeWeapon implements Polearm, GreatWeapon {

    public PolearmHaft(Polearm polearm) {
        super(((MeleeWeapon) polearm).getAttackAbilityScore(), ((MeleeWeapon) polearm).getEnchantmentBonus());
        init();
    }

    private void init() {
        this.damageDie =  new Die(1, 4);
        this.heavy = true;
        this.twoHanded = true;
        this.reach = true;
        this.damageType = DamageType.Bludgeoning;
    }

    @Override
    public PolearmHaft getHaft() {
        return this;
    }
}
