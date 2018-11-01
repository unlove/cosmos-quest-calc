/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

/**
 * Heals % of damage dealt
 * Used by Sanqueen
 */
public class Leech extends SpecialAbility {
    protected double multiplier;
    protected boolean deadOnStart;
    
    public Leech(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }

    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Leech(newOwner,multiplier);
    }

    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation) {
        deadOnStart = owner.isDead();
        //System.out.println(owner.getName() + " has " + owner.getCurrentHP());
    }
    
    @Override
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        if (!deadOnStart){
            owner.changeHP(owner.getCurrentAtt() * multiplier, thisFormation);
        }
    }
    
    @Override
    public String getDescription() {
        String percentString = Integer.toString((int)(multiplier * 100));
        return "Heals " + percentString + "% of damage dealt";
    }

    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt() * (int)(owner.getBaseHP() * multiplier);
    }
    
}
