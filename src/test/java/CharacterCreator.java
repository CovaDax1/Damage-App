import com.damagesimulator.PlayerCharacter.PlayerClass.BaseClass;
import com.damagesimulator.PlayerCharacter.build.CovaDax;
import com.damagesimulator.equipment.weapon.Greatsword;
import org.junit.Assert;
import org.junit.Test;

public class CharacterCreator {
    @Test
    public void testCovaGenerate() {
        CovaDax covadax = CovaDax.build(8, 8, 8, 8, 8, 8);
        Assert.assertEquals(2, covadax.getProficiency());
        covadax.levelUp(BaseClass.PALADIN, 5);
        Assert.assertEquals(5, covadax.getLevel());
        Assert.assertEquals(3, covadax.getProficiency());
        covadax.equipWeapon(new Greatsword());
        Assert.assertEquals(
                -1 + covadax.getProficiency() + covadax.getMhWeapon().getEnchantmentBonus(),
                covadax.getToAttackBonus(covadax.getMhWeapon()));
    }
}
