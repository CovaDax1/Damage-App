package com.damagesimulator.PlayerCharacter.build;

import com.damagesimulator.PlayerCharacter.*;
import com.damagesimulator.PlayerCharacter.PlayerClass.PactCaster;
import com.damagesimulator.PlayerCharacter.PlayerClass.SpellCaster;
import com.damagesimulator.PlayerCharacter.PlayerClass.warlock.Hexblade;
import com.damagesimulator.PlayerCharacter.action.SpellAttack;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.equipment.weapon.core.Weapon;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;
import com.damagesimulator.global.Die;
import com.damagesimulator.global.Spell;

import static com.damagesimulator.global.AttackResult.CRIT;
import static com.damagesimulator.global.AttackResult.MISS;

public class TheNightblade<M extends MeleeWeapon> extends PlayerCharacter<M> implements Hexblade, SpellCaster, PactCaster {
    SpellCasterSlots spellSlots;
    PactSlots pactSlots;

    public TheNightblade(AbilityScore strength, AbilityScore dexterity, AbilityScore constitution, AbilityScore intelligence, AbilityScore wisdom, AbilityScore charisma) {
        super(strength, dexterity, constitution, intelligence, wisdom, charisma);
    }

    public static TheNightblade build(int s, int d, int c, int i, int w, int h) {
        return new TheNightblade(
                new AbilityScore(s),
                new AbilityScore(d),
                new AbilityScore(c),
                new AbilityScore(i),
                new AbilityScore(w),
                new AbilityScore(h));
    }

    @Override
    public void longRest() {
        super.init();
        shortRest();
        this.spellSlots.refreshSpellSlots();
    }

    @Override
    public void shortRest() {
        shortRest(0);
    }

    @Override
    public void shortRest(int hitDie) {
        refreshPactSlots();
    }

    @Override
    protected int rollEconomicAttack(M weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        int damage = 0;
        AttackResult attackRoll = weaponAttack(weapon, target, advantage, attackBonus);
        if(attackRoll != MISS) damage += rollEconomicDamage(weapon, target, damageBonus, attackRoll == CRIT);
        return damage;
    }

    @Override
    protected int rollEconomicDamage(M weapon, Target target, int damageBonus, boolean crit) {
        int damage = 0;
        damage += weapon.rollDamage() + damageBonus;
        int smiteSlot = 0;
        if (target.isBloodied() && this.pactSlots.getAvailablePactSlots() > 0)
            smiteSlot = this.pactSlots.getPactSlotLevel();
        damage += pactSmite();
        if (crit) {
            damage += weapon.getMaxDamage();
            damage += (smiteSlot + 1) * 8;
        }
        return damage;
    }

    @Override
    protected int rollLiberalAttack(M weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        int damage = 0;
        AttackResult attackRoll = weaponAttack(weapon, target, advantage, attackBonus);
        if(attackRoll != MISS) damage += rollLiberalDamage(weapon, target, damageBonus, attackRoll == CRIT);
        return damage;
    }

    @Override
    protected int rollLiberalDamage(M weapon, Target target, int damageBonus, boolean crit) {
        int damage = 0;
        damage += weapon.rollDamage() + damageBonus;
        int smiteSlot = 0;
        if(target.isBloodied() && pactSlots.getAvailablePactSlots() > 0)
            smiteSlot = pactSlots.getPactSlotLevel();
        damage += pactSmite();
        if (crit) {
            damage += weapon.getMaxDamage();
            damage += (smiteSlot + 1) * 8;
        }
        return damage;
    }

    @Override
    protected int rollConservativeAttack(M weapon, Target target, Advantage advantage, int attackBonus, int damageBonus) {
        int damage = 0;
        AttackResult attackRoll = weaponAttack(weapon, target, advantage, attackBonus);
        if(attackRoll != MISS) damage +=  rollConservativeDamage(weapon, damageBonus, attackRoll == CRIT);
        return damage;
    }

    @Override
    protected int rollConservativeDamage(M weapon, int damageBonus, boolean crit) {
        int damage = 0;
        damage += weapon.rollDamage() + damageBonus;
        if (crit)
            damage += weapon.getMaxDamage();
        return damage;
    }

    @Override
    protected int unarmedAttack(Target target, Advantage advantage, int attackBonus, int damageBonus) {
        return 0;
    }

    @Override
    public AttackResult weaponAttack(M weapon, Target target, Advantage advantage, int attackBonus) {
        return weapon.rollAttack(attackBonus, advantage, target.getAc());
    }

    @Override
    public int spellAttack(SpellAttack spellAttack) {
        return 0;
    }

    @Override
    public void castSpell(Spell spell) {

    }

    @Override
    public void curse(Target target) {

    }

    @Override
    public Weapon getHexWeapon() {
        return getMhWeapon();
    }

    @Override
    public void bondHexWeapon(Weapon weapon) {
        weapon.setAttackAbilityScore(AbilityScore.AbilityScores.CHARISMA);
        this.equipWeapon(weapon);
    }

    @Override
    public int pactSmite() {
        if (getPactCasterLevel() < 1 || warlockLevels < 3) return 0;
        Die smiteDie = new Die(this.pactSlots.getPactSlotLevel() + 1, 8);
        getSpellSlots().spendSpellSlot(this.pactSlots.getPactSlotLevel() - 1);
        return smiteDie.roll();
    }

    @Override
    public int getPactSlotCount() {
        return this.pactSlots.getAvailablePactSlots();
    }

    @Override
    public int getPactSlotLevel() {
        return this.pactSlots.getPactSlotLevel();
    }

    @Override
    public void refreshPactSlots() {
        this.pactSlots.refreshSpellSlots();
    }

    @Override
    public int getSpellCasterLevel() {
        int cLevel = 0;
        cLevel += wizardLevels + sorcererLevels + bardLevels + clericLevels + druidLevels;
        cLevel += (paladinLevels + rangerLevels) / 2;
        return cLevel;
    }

    @Override
    public SpellCasterSlots getSpellSlots() {
        return this.spellSlots;
    }

    @Override
    public int getPactCasterLevel() {
        return warlockLevels;
    }

    @Override
    public PactSlots getPactSlots() {
        return this.pactSlots;
    }
}
