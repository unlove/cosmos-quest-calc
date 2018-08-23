/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//deals a set amount of damage to all creatures in the enemy formation after
//every turn while the owner is alive. Damage is done before healing. Used by
//Alpha, TR0N1X, and Lord of Chaos
public class AOE extends SpecialAbility{
    
    protected int damage;
    
    public AOE(Creature owner, int damage){
        super(owner);
        this.damage = damage;
    }

    @Override
    public double extraDamage(Formation thisFormation, Formation enemyFormation) {
        return 0;
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        enemyFormation.takeAOEDamage(damage);
    }

    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new AOE(newOwner,damage);
    }

    @Override
    public String getDescription() {
        return "Deal " + damage + " before every turn";
    }

    @Override
    public int viability() {
        return (owner.getBaseHP() * owner.getBaseAtt()) + (owner.getBaseHP() * damage * Formation.MAX_MEMBERS);
    }
    
    
    
}
