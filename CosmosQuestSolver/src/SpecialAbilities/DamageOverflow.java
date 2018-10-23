/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//Nullifies damage above a given amount.
//used by Doyenne
public class DamageOverflow extends SpecialAbility{
    
    private long damageCap;

    public DamageOverflow(Creature owner, long damageCap) {
        super(owner);
        this.damageCap = damageCap;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new DamageOverflow(newOwner,damageCap);
    }
    
    @Override
    public void takeHit(Creature attacker,  Formation thisFormation, Formation enemyFormation, double hit) {
        if((long)Math.ceil(hit) > damageCap){
            super.takeHit(attacker, thisFormation, enemyFormation, 0);
        }
        else{
            super.takeHit(attacker, thisFormation, enemyFormation, hit);
        }
    }
    
    
    @Override
    public String getDescription() {
        return "Ignores attacks over " + damageCap + " damage";
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * (owner.getBaseHP()/damageCap) * owner.getBaseAtt());
    }
    
}
