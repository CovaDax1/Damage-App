package com.damagesimulator.PlayerCharacter;

import com.damagesimulator.equipment.weapon.core.Weapon;
import com.damagesimulator.global.Advantage;
import com.damagesimulator.global.AttackResult;

public class Target extends BaseCharacter {
    private final int maxHp;
    private int ac;
    private int hp;
    private boolean undead;
    private Advantage advOnHit = Advantage.STANDARD;

    private Target(int ac, int hp, int prof, int s, int d, int c, int i, int w, int a) {
        super(new AbilityScore(s),
                new AbilityScore(d),
                new AbilityScore(c),
                new AbilityScore(i),
                new AbilityScore(w),
                new AbilityScore(a));
        this.ac = ac;
        this.hp = hp;
        this.maxHp = hp;
        this.proficiency = prof;
        this.undead = false;
    }

    public static Target generate() {
        Target target = new Target(16, 30, 2, 8, 8, 8, 8, 8, 8);
        return target;
    }

    public int getAc() {
        return ac;
    }

    @Override
    protected int unarmedAttack(Target target, Advantage advantage, int attackBonus, int damagebonus) {
        return 0;
    }

    @Override
    public AttackResult weaponAttack(Weapon weapon, Target target, Advantage advantage, int attackBonus) {
        return null;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public boolean isBloodied() {
        return getHp() < (getMaxHp() / 2);
    }

    @Override
    public int getProficiency() {
        return proficiency;
    }

    public Advantage getAdvOnHit() {
        return this.advOnHit;
    }

    public void setAdvOnHit(Advantage adv) {
        this.advOnHit = adv;
    }

    public boolean isUndead() {
        return undead;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
    }
}
