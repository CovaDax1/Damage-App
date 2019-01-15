package com.damagesimulator.PlayerCharacter.PlayerClass;

import com.damagesimulator.PlayerCharacter.action.SpellAttack;
import com.damagesimulator.global.Spell;

public interface MagicAdept {
    int spellAttack(SpellAttack spellAttack);

    void castSpell(Spell spell);
}
