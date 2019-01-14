package com.damagesimulator;

import com.damagesimulator.PlayerCharacter.PlayerCharacter;
import com.damagesimulator.PlayerCharacter.PlayerClass.BaseClass;
import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.PlayerCharacter.build.*;
import com.damagesimulator.equipment.armor.Plate;
import com.damagesimulator.equipment.armor.Scale;
import com.damagesimulator.equipment.weapon.Glaive;
import com.damagesimulator.equipment.weapon.Greatsword;
import com.damagesimulator.global.Advantage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {
    private static final int count = 1000 * 1000;
    private static final int numOfRounds = 10;

    public static void main(String[] args) {
        testCova();
        System.out.println("\n\n\n");
        testTheNightblade();
    }

    public static void testCharacter(Map<String, PlayerCharacter> builds) {
        Target t = Target.generate();
        String leftFormat = "| %-50s | %-5s | %-13s | %-11s | %-11s |%n";
        System.out.format("+----------------------------------------------------+-------+---------------+-------------+-------------+%n");
        System.out.format("| Build Name                                         | Level | Conservative  | Economic    | Liberal     |%n");
        System.out.format("+----------------------------------------------------+-------+---------------+-------------+-------------+%n");

        Iterator iterator = builds.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PlayerCharacter> pair = (Map.Entry<String, PlayerCharacter>) iterator.next();
            PlayerCharacter pc = pair.getValue();

            System.out.format(leftFormat,
                    pair.getKey(),
                    pc.getLevel(),
                    testConservative(pc, t, false, Advantage.STANDARD),
                    testEconomic(pc, t, false, Advantage.STANDARD),
                    testLiberal(pc, t, false, Advantage.STANDARD)
            );

            System.out.format(leftFormat,
                    pair.getKey() + " (Bless)",
                    pc.getLevel(),
                    testConservative(pc, t, true, Advantage.STANDARD),
                    testEconomic(pc, t, true, Advantage.STANDARD),
                    testLiberal(pc, t, true, Advantage.STANDARD)
            );

            System.out.format(leftFormat,
                    pair.getKey() + " (Advantage)",
                    pc.getLevel(),
                    testConservative(pc, t, false, Advantage.ADVANTAGE),
                    testEconomic(pc, t, false, Advantage.ADVANTAGE),
                    testLiberal(pc, t, false, Advantage.ADVANTAGE)
            );

            System.out.format("+----------------------------------------------------+-------+---------------+-------------+-------------+%n");
        }
    }


    public static void testTheNightblade() {
        Map<String, PlayerCharacter> nbList = new HashMap<>();
        TheNightbladeGWM nightbladeGWM = TheNightbladeGWM.build(8, 14, 16, 10, 8, 18);
        nightbladeGWM.levelUp(BaseClass.WARLOCK, 5);
        nightbladeGWM.levelUp(BaseClass.SORCERER, 3);
        nightbladeGWM.bondHexWeapon(new Greatsword(1));
        nightbladeGWM.equipArmor(new Scale());
        nbList.put("The Nightblade - Great Weapon Master", nightbladeGWM);
        TheNightbladePM nightbladePM = TheNightbladePM.build(8, 14, 16, 10, 8, 18);
        nightbladePM.levelUp(BaseClass.WARLOCK, 5);
        nightbladePM.levelUp(BaseClass.SORCERER, 3);
        nightbladePM.bondHexWeapon(new Glaive(1));
        nightbladePM.equipArmor(new Scale());
        nbList.put("The Nightblade - Polearm Master", nightbladePM);
        TheNightbladeGPM nightbladeGPM = TheNightbladeGPM.build(8, 14, 16, 10, 8, 18);
        nightbladeGPM.levelUp(BaseClass.WARLOCK, 5);
        nightbladeGPM.levelUp(BaseClass.SORCERER, 3);
        nightbladeGPM.bondHexWeapon(new Glaive(1));
        nightbladeGPM.equipArmor(new Scale());
        nbList.put("The Nightblade - Great Polearm Master", nightbladeGPM);
        testCharacter(nbList);
    }

    public static void testCova() {
        Map<String, PlayerCharacter> covaList = new HashMap<>();
        CovaDax cova = CovaDax.build(16,12,14,8,8,14);
        cova.levelUp(BaseClass.PALADIN, 3);
        cova.equipWeapon(new Greatsword());
        cova.equipArmor(new Plate());
        covaList.put("CovaDax - Control", cova);
        CovaDax covaAsi = CovaDaxASI.build(20,12,14,8,8,14);
        covaAsi.levelUp(BaseClass.PALADIN, 8);
        covaAsi.equipWeapon(new Greatsword());
        covaAsi.equipArmor(new Plate());
        covaList.put("Covadax - ASI", covaAsi);
        CovaDax covaGwm = CovaDaxGWF.build(18,12,14,8,8,14);
        covaGwm.levelUp(BaseClass.PALADIN, 8);
        covaGwm.equipWeapon(new Greatsword());
        covaGwm.equipArmor(new Plate());
        covaList.put("CovaDax - Great Weapon Master", covaGwm);
        CovaDax covaPM = CovaDaxPM.build(18,12,14,8,8,14);
        covaPM.levelUp(BaseClass.PALADIN, 8);
        covaPM.equipWeapon(new Glaive());
        covaPM.equipArmor(new Plate());
        covaList.put("CovaDax - Polearm Master", covaPM);
        CovaDax covaGPM = CovaDaxGPF.build(16,12,14,8,8,14);
        covaGPM.levelUp(BaseClass.PALADIN, 8);
        covaGPM.equipWeapon(new Glaive());
        covaGPM.equipArmor(new Plate());
        covaList.put("CovaDax - Great Polearm Master", covaGPM);
        testCharacter(covaList);
    }

    public static double testLiberal(PlayerCharacter pc, Target t, boolean bless, Advantage adv) {
        pc.bless(bless);
        t.setAdvOnHit(adv);
        double totalDamage = 0;
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < numOfRounds; j++) {
                int damage = pc.liberalMultiAttack(pc.getMhWeapon(), t, t.getAdvOnHit());
                t.takeDamage(damage);
                totalDamage += damage;
                if (t.getHp() < 1) {
                    t = Target.generate();
                }
            }
            pc.longRest();
        }
        return (totalDamage / (count * numOfRounds));
    }

    public static double testEconomic(PlayerCharacter pc, Target t, boolean bless, Advantage adv) {
        pc.bless(bless);
        t.setAdvOnHit(adv);
        double totalDamage = 0;
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < numOfRounds; j++) {
                int damage = pc.economicMultiAttack(pc.getMhWeapon(), t, t.getAdvOnHit());
                t.takeDamage(damage);
                totalDamage += damage;
                if (t.getHp() < 1) {
                    t = Target.generate();
                }
            }
            pc.longRest();
        }
        return (totalDamage / (count * numOfRounds));
    }

    public static double testConservative(PlayerCharacter pc, Target t, boolean bless, Advantage adv) {
        pc.bless(bless);
        t.setAdvOnHit(adv);
        double totalDamage = 0;
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < numOfRounds; j++) {
                int damage = pc.conservativeMultiAttack(pc.getMhWeapon(), t, t.getAdvOnHit());
                t.takeDamage(damage);
                totalDamage += damage;
                if (t.getHp() < 1) {
                    t = Target.generate();
                }
            }
            pc.longRest();
        }
        return (totalDamage / (count * numOfRounds));
    }
}
