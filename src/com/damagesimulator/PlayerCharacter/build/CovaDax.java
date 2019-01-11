package com.damagesimulator.PlayerCharacter.build;

import com.damagesimulator.PlayerCharacter.AbilityScore;

import com.damagesimulator.PlayerCharacter.PlayerCharacter;
import com.damagesimulator.PlayerCharacter.PlayerClass.BaseClass;
import com.damagesimulator.PlayerCharacter.PlayerClass.SpellCaster;
import com.damagesimulator.PlayerCharacter.PlayerClass.paladin.OathOfConquest;
import com.damagesimulator.PlayerCharacter.PlayerClass.paladin.OathOfVengeance;
import com.damagesimulator.PlayerCharacter.SpellCasterSlots;
import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.PlayerCharacter.action.SpellAttack;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;
import com.damagesimulator.global.*;

import static com.damagesimulator.global.AttackResult.CRIT;
import static com.damagesimulator.global.AttackResult.MISS;

public class CovaDax<M extends MeleeWeapon> extends PlayerCharacter<M> implements SpellCaster, OathOfVengeance {
    protected int layOnHands;
    protected int channelDivinity;
    protected SpellCasterSlots spellSlots;

    public CovaDax(AbilityScore strength, AbilityScore dexterity, AbilityScore constitution, AbilityScore intelligence, AbilityScore wisdom, AbilityScore charisma) {
        super(strength, dexterity, constitution, intelligence, wisdom, charisma);
        init();
    }

    public static CovaDax build(int s, int d, int c, int i, int w, int h) {
        return new CovaDax(
                new AbilityScore(s),
                new AbilityScore(d),
                new AbilityScore(c),
                new AbilityScore(i),
                new AbilityScore(w),
                new AbilityScore(h));
    }

    protected void init() {
        super.init();
        this.spellSlots = new SpellCasterSlots(this);
        if(paladinLevels > 4 || fighterLevels > 4 || monkLevels > 4 || barbarianLevels > 4 || rangerLevels > 4) attackCount = 2;
        if(fighterLevels > 11 || (paladinLevels == 20 || this instanceof OathOfConquest)) attackCount = 3;
        longRest();
    }

    @Override
    public void levelUp(BaseClass baseClass) {
        super.levelUp(baseClass);
    }

    public void levelUp(BaseClass baseClass, int num) {
        super.levelUp(baseClass, num);
    }

    @Override
    public void longRest() {
        this.hp = this.getHp();
        this.layOnHands = paladinLevels * 5;
        getSpellSlots().refreshSpellSlots();
        shortRest();
    }

    @Override
    public void shortRest() {
        shortRest(0);
    }

    @Override
    public void shortRest(int hitDie) {
        //TODO: Implement HD Healing
        if (paladinLevels > 3 || clericLevels > 0)
            channelDivinity = (clericLevels > 5) ? 2 : 1;
    }

    @Override
    public int getLayOnHandsPool() {
        return this.layOnHands;
    }

    @Override
    public int getChannelDivinityCount() {
        return this.channelDivinity;
    }

    @Override
    public void vowOfEnmity(Target target) {
        target.setAdvOnHit(Advantage.ADVANTAGE);
    }

    @Override
    public int smite(int spellLevel, boolean undead) {
        if (spellLevel < 1 || paladinLevels < 2) return 0;
        getSpellSlots().spendSpellSlot(spellLevel - 1);
        return d8.getDie().roll(spellLevel + 1 + (undead ? 1 : 0));
    }

    public int maxSmiteDamage(int spellLevel, boolean undead) {
        if(spellLevel < 1 || paladinLevels < 2) return 0;
        return 8 * (spellLevel + 1 + (undead ? 1 : 0));
    }

    @Override
    public int improvedSmite() {
        if (paladinLevels < 11) return 0;
        return d8.getDie().roll();
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
        damage += improvedSmite();
        int smiteSlot = 0;
        if (target.isBloodied())
            smiteSlot = this.spellSlots.getLowestAvailableSpellSlot();
        damage += smite(smiteSlot, target.isUndead());
        if (crit) {
            damage += weapon.getMaxDamage();
            damage += maxSmiteDamage(smiteSlot, target.isUndead());
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
        damage += improvedSmite();
        int smiteSlot = 0;
        if(target.isBloodied())
            smiteSlot = getSpellSlots().getHighestAvailableSpellSlot();
        else
            smiteSlot = getSpellSlots().getLowestAvailableSpellSlot();
        damage += smite(smiteSlot, target.isUndead());
        if (crit) {
            damage += weapon.getMaxDamage();
            damage += maxSmiteDamage(smiteSlot, target.isUndead());
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
        damage += improvedSmite();
        if (crit)
            damage += weapon.getMaxDamage();
        return damage;
    }

    @Override
    public AttackResult weaponAttack(M weapon, Target target, Advantage advantage, int attackBonus) {
        return weapon.rollAttack(attackBonus, advantage, target.getAc());
    }

    @Override
    public int unarmedAttack(Target target, Advantage advantage, int attackBonus, int damageBonus) {
        int damage = 0;
        AttackResult attackRoll = unarmed.rollAttack(this.getStrength().getMod() + attackBonus, advantage, target.getAc());
        if (attackRoll != AttackResult.MISS)
            damage += this.unarmed.rollDamage() + this.getStrength().getMod() + damageBonus;
        return damage;
    }

    //Spellcaster Methods
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
    public int spellAttack(SpellAttack spellAttack) {
        return 0;
    }

    @Override
    public void castSpell(Spell spell) {

    }
}
