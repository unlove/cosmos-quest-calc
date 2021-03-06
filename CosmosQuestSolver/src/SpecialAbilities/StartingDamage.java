/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//deal a set amount damage at start of fight. not used, but the scaleable version is
public class StartingDamage extends SpecialAbility{
    
    protected int amount;

    public StartingDamage(Creature owner, int amount) {
        super(owner);
        this.amount = amount;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new StartingDamage(newOwner,amount);
    }
    
    @Override
    public void startOfFightAction2(Formation thisFormation, Formation enemyFormation) {
        enemyFormation.takeAOEDamage(amount);
    }

    
    
    @Override
    public String getDescription() {
        return "";
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * (owner.getBaseAtt() + Formation.MAX_MEMBERS * amount);
    }
    
}
