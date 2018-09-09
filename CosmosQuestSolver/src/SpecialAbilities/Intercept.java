/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//takes a percentage of the front creature's damage.
//used by Neil
public class Intercept extends SpecialAbility{
    
    private double interceptPercent;

    public Intercept(Creature owner, double interceptPercent) {
        super(owner);
        this.interceptPercent = interceptPercent;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Intercept(newOwner,interceptPercent);
    }
    
    @Override
    public double alterIncomingDamage(double hit, double initialHit, Formation thisFormation, Formation enemyFormation) {
        if (owner != thisFormation.getFrontCreature() && !owner.isDead()){
            double damageIntercepted = hit * interceptPercent;
            owner.takeHit(owner, thisFormation, enemyFormation, damageIntercepted);
            return hit - damageIntercepted;
        }
        else{
            return hit;
        }
    }
    
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(interceptPercent * 100));
        return "Absorbs " + percent + "% damage from first unit";
    }
    
    @Override
    public int viability() {
        return (int)(interceptPercent * owner.getBaseHP() * owner.getBaseHP());
    }
    
}
