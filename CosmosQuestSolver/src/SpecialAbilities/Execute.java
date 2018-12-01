/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

/**
 *
 * @author sterling
 */
public class Execute extends SpecialAbility {
    
    private double multiplier;
    
    public Execute(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (thisFormation.getFrontCreature() == owner){//can only reflect direct damage while in front
            long baseHP = enemyFormation.getFrontCreature().getBaseHP();
            long currHP = enemyFormation.getFrontCreature().getCurrentHP();
            double percentage = (double) currHP / baseHP;
            if (percentage <= multiplier)
            {
                enemyFormation.getFrontCreature().changeHP(-currHP, enemyFormation);
            }
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Execute (newOwner,multiplier);
    }

    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(multiplier * 100));
        return "Kills enemies below " + percent + "% HP after attacking them";
    }

    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+multiplier));
    }
    
    
}
