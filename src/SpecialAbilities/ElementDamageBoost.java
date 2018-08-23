/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//attacks a random enemy each turn (only one hit, enemy in first is not guarenteed to get hit)
//used by Quest heroes 21-24
public class ElementDamageBoost extends SpecialAbility{
    
    private double percentBoost;

    public ElementDamageBoost(Creature owner, double multiplier) {
        super(owner);
        this.percentBoost = multiplier;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ElementDamageBoost(newOwner,percentBoost);
    }
    

    
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(percentBoost * 100));
        return "Elemental bouns +" + percent + "%";
    }
    
    public double getElementDamageBoost() {
        return percentBoost;
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt() * (int)(1+percentBoost);
    }
    
}
