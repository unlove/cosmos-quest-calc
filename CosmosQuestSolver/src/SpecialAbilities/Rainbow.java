/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

// increases attack by a specified amount if a specified number of unique elements
// are alive and behind the user. Used by Aoyuki
public class Rainbow extends SpecialAbility{
    
    private boolean activated = false;
    private int damageBoost;
    private int numTypesRequired;

    public Rainbow(Creature owner, int damageBoost, int numTypesRequired) {
        super(owner);
        this.damageBoost = damageBoost;
        this.numTypesRequired = numTypesRequired;
    }
    
    @Override
    public double extraDamage(Formation thisFormation, Formation enemyFormation) {
        if (meetsRequirements(thisFormation)){
            return damageBoost;
        }
        else{
            return 0;
        }
    }

    

    private boolean meetsRequirements(Formation thisFormation) {
        boolean[] elements = new boolean[Creature.Element.values().length];
        
        boolean foundOwner = false;
        for (Creature creature : thisFormation){
            if (foundOwner && !creature.isDead()){
                elements[creature.getElement().ordinal()] = true;
            }
            if (creature == owner){
                foundOwner = true;
            }
        }
        
        int numElements = 0;
        for (boolean b : elements){
            if (b){
                numElements ++;
            }
        }
        
        return numElements >= numTypesRequired;
        
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Rainbow(newOwner,damageBoost,numTypesRequired);
    }
    
    
    
    @Override
    public String getDescription() {
        return "+" + damageBoost + " attack when " + numTypesRequired + " elemental types alive";
    }
    
    @Override
    public int viability() {
        int att = (int)(owner.getBaseAtt() + (damageBoost * 0.25));//calculate odds of boost applying assuming random placement**
        return owner.getBaseHP() * att;
    }
    
}
