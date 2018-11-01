/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

/**
 * Deals damage when dying
 * Used by Billy
 */
public class DeathStrike extends SpecialAbility {
    protected int amount;
    
    public DeathStrike(Creature owner, int amount){
        super(owner);
        this.amount = amount;
    }

    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new DeathStrike(newOwner,amount);
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        if (thisFormation.getFrontCreature() == owner){
            activateAbility(enemyFormation);
        }
    }
    
    private void activateAbility(Formation enemyFormation) {
        enemyFormation.addDamageTaken(amount);
    }
    
    @Override
    public String getDescription() {
        return "Deals " + amount + " damage when dying";
    }

    @Override
    public int viability() {
        return owner.getBaseHP() * (owner.getBaseAtt() + amount);
    }
}
