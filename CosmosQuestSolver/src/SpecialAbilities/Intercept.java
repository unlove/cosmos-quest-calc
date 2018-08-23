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
    public double alterIncomingDamage(double hit, double initialHit, Formation thisFormation, Formation enemyFormation) {//is this how it works?
        if (owner != thisFormation.getFrontCreature()){//&& !dead?
            double damageIntercepted = hit * interceptPercent;
            if (damageIntercepted <= owner.getCurrentHP()){
                owner.takeHit(owner, thisFormation, enemyFormation, damageIntercepted);
                return hit - damageIntercepted;
            }
            else{//if cannot absorb all the damage
                double hp = owner.getCurrentHP();
                owner.takeHit(owner, thisFormation, enemyFormation, owner.getCurrentHP());
                return hit - hp;
            }
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
        return (int)(interceptPercent * Formation.MAX_MEMBERS * owner.getBaseHP() * owner.getBaseHP());
    }
    
}
