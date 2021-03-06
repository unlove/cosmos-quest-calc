/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//multiplies the owner's base attack power by a specified amount every time
//the user attacks. Used by Aural, Geum, and Ascended Geum
public class MultiplyAttack extends SpecialAbility{
    
    private double multiplier;
    private double damage = 0;

    public MultiplyAttack(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    

    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (thisFormation.getFrontCreature() == owner){
            damage *= multiplier;
            owner.setCurrentAtt((long)Math.ceil(damage));
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new MultiplyAttack(newOwner,multiplier);
    }
    
    @Override
    public void startOfFightAction(Formation thisFormation, Formation enemyFormation) {
        damage = owner.getBaseAtt();
    }

    
    @Override
    public String getDescription() {
        String multiplierString = "";
        if (multiplier % 1 == 0){
            multiplierString = Integer.toString((int)multiplier);
        }
        else{
            multiplierString = Double.toString(multiplier);
        }
        return "Attacking multiplies attack by " + multiplierString;
    }
    
    @Override
    public int viability() {
        //int highest = owner.getBaseHP() > owner.getBaseAtt() ? owner.getBaseHP() : owner.getBaseAtt();
        return owner.getBaseHP() * owner.getBaseAtt() * (int)Math.pow(multiplier,2);
    }
    
}
