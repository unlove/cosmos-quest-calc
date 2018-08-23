/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//null object pattern for monsters
public class Nothing extends SpecialAbility{

    public Nothing(Creature owner) {
        super(owner);
    }

    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Nothing(newOwner);
    }
    
    
    @Override
    public String getDescription() {
        return "";
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt();
    }
    
}