/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

/**
 * Gains % damage taken as atk
 * Used by Cliodhna
 */
public class Evolve extends SpecialAbility {
    protected double multiplier;
    protected long damageTakenThisRound;
    
    public Evolve(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }

    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Evolve(newOwner,multiplier);
    }
    
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        damageTakenThisRound = 0;
    }
   
    @Override
    public void recordDamageTaken(long damage){//is this skill asymetric? (which side you're on matters)
        damageTakenThisRound = damage;
        
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (thisFormation.getFrontCreature() == owner){//can only reflect direct damage while in front
            owner.setCurrentAtt(owner.getCurrentAtt() + 
                    (damageTakenThisRound * (long)Math.ceil(multiplier)));
        }
    }

    @Override
    public String getDescription() {
        String percentString = Integer.toString((int)(multiplier * 100));
        return "+" + percentString + "% of damage taken as atk";
    }

    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt() * 2;
    }
    
}
