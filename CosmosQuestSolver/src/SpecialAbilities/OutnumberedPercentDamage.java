/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//at the beginning of the fight, cuts a percentage of each enemy's hp by a certain
//percent. This percent is determined by how many creatures each formation has.
//if the hero is alone against a formation of 6, the ability reduces each
//enemy's hp to 1/6th its original value. has no effect if the user's formation
//has more creatures than the enemy's formation. Used by Leprechaun
public class OutnumberedPercentDamage extends SpecialAbility{

    public OutnumberedPercentDamage(Creature owner) {
        super(owner);
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new OutnumberedPercentDamage(newOwner);
    }
    
    @Override
    public void startOfFightAction(Formation thisFormation, Formation enemyFormation) {
        double percentDamage = 1-((double) thisFormation.size() / enemyFormation.size());
        if (percentDamage <= 0){
            return;
        }
        for (Creature creature : enemyFormation){
            double damageDelt = creature.getBaseHP() * percentDamage;
            creature.changeHP(-damageDelt,enemyFormation);//rounding?
        }
    }

    
    
    @Override
    public String getDescription() {
        return "At start, cuts enemy hp by a percent if outnumbered";
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt();
    }
    
}
