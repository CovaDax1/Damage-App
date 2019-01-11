package com.damagesimulator;

import com.damagesimulator.PlayerCharacter.Target;
import com.damagesimulator.PlayerCharacter.build.*;
import com.damagesimulator.PlayerCharacter.PlayerClass.BaseClass;
import com.damagesimulator.equipment.armor.Plate;
import com.damagesimulator.equipment.weapon.Glaive;
import com.damagesimulator.equipment.weapon.Greatsword;
import com.damagesimulator.equipment.weapon.core.MeleeWeapon;

public class Main {
    private static final int count = 1000 * 1000;
    private static final int numOfRounds = 10;

    public static void main(String[] args) {

        testCova();
    }

    public static void testTestCova() {
        CovaDax cova = CovaDaxGWF.build(16,12,14,8,8,14);
        cova.levelUp(BaseClass.PALADIN, 4);
        cova.levelUp(BaseClass.PALADIN, 3);
        cova.equipWeapon(new Greatsword());
        cova.equipArmor(new Plate());
//        cova.bless();

        Target t = Target.generate();
        testCovaLiberal(cova, t);
        t = Target.generate();
        testCovaEconomical(cova, t);
        t = Target.generate();
        testCovaConservative(cova, t);
    }

    public static void testCova() {
        CovaDax cova = CovaDax.build(16,12,14,8,8,14);
        cova.levelUp(BaseClass.PALADIN, 3);
        cova.equipWeapon(new Greatsword());
        cova.equipArmor(new Plate());
//        cova.bless();

        Target t = Target.generate();
        testCovaLiberal(cova, t);
        t = Target.generate();
        testCovaEconomical(cova, t);
        t = Target.generate();
        testCovaConservative(cova, t);

        CovaDax covaAsi = CovaDaxASI.build(20,12,14,8,8,14);
        covaAsi.levelUp(BaseClass.PALADIN, 8);
        covaAsi.equipWeapon(new Greatsword());
        covaAsi.equipArmor(new Plate());
//        covaAsi.bless();

        System.out.println("\n\n -------ASI-------");

        t = Target.generate();
        testCovaLiberal(covaAsi, t);
        t = Target.generate();
        testCovaEconomical(covaAsi, t);
        t = Target.generate();
        testCovaConservative(covaAsi, t);

        CovaDax covaGwm = CovaDaxGWF.build(18,12,14,8,8,14);
        covaGwm.levelUp(BaseClass.PALADIN, 8);
        covaGwm.equipWeapon(new Greatsword());
        covaGwm.equipArmor(new Plate());
//        covaGwm.bless();

        System.out.println("\n\n -------GREAT WEAPON MASTER-------");

        t = Target.generate();
        testCovaLiberal(covaGwm, t);
        t = Target.generate();
        testCovaEconomical(covaGwm, t);
        t = Target.generate();
        testCovaConservative(covaGwm, t);

        CovaDax covaPM = CovaDaxPM.build(18,12,14,8,8,14);
        covaPM.levelUp(BaseClass.PALADIN, 8);
        covaPM.equipWeapon(new Glaive());
        covaPM.equipArmor(new Plate());
//        covaPM.bless();

        System.out.println("\n\n -------POLEARM MASTER-------");

        t = Target.generate();
        testCovaLiberal(covaPM, t);
        t = Target.generate();
        testCovaEconomical(covaPM, t);
        t = Target.generate();
        testCovaConservative(covaPM, t);

        CovaDax covaGPM = CovaDaxGPF.build(16,12,14,8,8,14);
        covaGPM.levelUp(BaseClass.PALADIN, 8);
        covaGPM.equipWeapon(new Glaive());
        covaGPM.equipArmor(new Plate());
//        covaPM.bless();

        System.out.println("\n\n -------GREAT POLEARM MASTER-------");

        t = Target.generate();
        testCovaLiberal(covaGPM, t);
        t = Target.generate();
        testCovaEconomical(covaGPM, t);
        t = Target.generate();
        testCovaConservative(covaGPM, t);
    }

    public static void testCovaLiberal(CovaDax cova, Target t) {
        double totalDamage = 0;
        double misses = 0;
        int kills = 0;
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < numOfRounds; j++) {
                int damage = cova.liberalMultiAttack(cova.getMhWeapon(), t, t.getAdvOnHit());
                t.takeDamage(damage);
                totalDamage += damage;
                if(damage == 0) misses++;
                if (t.getHp() < 1) {
                    t = Target.generate();
                    kills++;
                }
            }
            cova.longRest();
        }
        double hits = (count * numOfRounds) - misses;
        System.out.println("Liberal Cova has an acurassy of " + (hits / (hits + misses)) * 100 + "%" );
        System.out.println("Liberal Cova deals " + totalDamage / (count * numOfRounds) + " damage per round!");
    }

    public static void testCovaEconomical(CovaDax cova, Target t) {
        double totalDamage = 0;
        int kills = 0;
        int misses = 0;
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < numOfRounds; j++) {
                int damage = cova.economicMultiAttack((MeleeWeapon) cova.getMhWeapon(), t, t.getAdvOnHit());
                t.takeDamage(damage);
                if(damage == 0) misses++;
                totalDamage += damage;
                if (t.getHp() < 1) {
                    t = Target.generate();
                    kills++;
                }
            }
            cova.longRest();
        }

        double hits = (count * numOfRounds) - misses;
        System.out.println("Economic Cova has an acurassy of " + (hits / (hits + misses)) * 100 + "%" );
        System.out.println("Economic Cova deals " + totalDamage / (count * numOfRounds) + " damage per round!");
    }


    public static void testCovaConservative(CovaDax cova, Target t) {
        double totalDamage = 0;
        int kills = 0;
        int misses = 0;
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < numOfRounds; j++) {
                int damage = cova.conservativeMultiAttack((MeleeWeapon) cova.getMhWeapon(), t, t.getAdvOnHit());
                t.takeDamage(damage);
                totalDamage += damage;
                if(damage == 0) misses++;
                if (t.getHp() < 1) {
                    t = Target.generate();
                    kills++;
                }
            }
            cova.longRest();
        }

        double hits = (count * numOfRounds) - misses;
        System.out.println("Conservative Cova has an acurassy of " + (hits / (hits + misses)) * 100 + "%" );
        System.out.println("Conservative Cova deals " + totalDamage / (count * numOfRounds) + " damage per round!");
    }
}
