package com.damagesimulator.PlayerCharacter.feat;

import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.equipment.weapon.core.Weapon;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;

public interface GreatWeaponMastery {
    boolean shouldIPowerAttack(Target t, Advantage adv, Weapon weapon, int atkBonus);

    AttackResult powerAttack(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus);
    AttackResult cleave(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus);

    AttackResult powerCleave(MeleeWeapon weapon, Target target, Advantage advantage, int attackBonus);
}
