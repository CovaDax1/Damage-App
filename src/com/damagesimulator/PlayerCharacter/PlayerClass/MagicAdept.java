package com.damagesimulator.PlayerCharacter.PlayerClass;

import com.damagesimulator.global.Spell;
import com.damagesimulator.PlayerCharacter.action.SpellAttack;

public interface MagicAdept {
    int spellAttack(SpellAttack spellAttack);
    void castSpell(Spell spell);
}
