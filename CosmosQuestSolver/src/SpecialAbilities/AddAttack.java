/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//increases attack by a specified amount every turn. Used by Spyke, Boor, Putrid, and Kryton
public class AddAttack extends SpecialAbility{
    
    private long amount;

    public AddAttack(Creature owner, long amount) {
        super(owner);
        this.amount = amount;
    }

    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        owner.setCurrentAtt(owner.getCurrentAtt() + amount);
        if (owner.getCurrentAtt() < 0){
            owner.setCurrentAtt(0);
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new AddAttack(newOwner,amount);
    }
    
    
    
    @Override
    public String getDescription() {
        if (amount > 0){
            return "Gains " + amount + " attack every turn";
        }
        else if (amount < 0){
            return "Loses " + -amount + " attack every turn";
        }
        else{
            return "";
        }
    }
    
    @Override
    public int viability() {
        int att = (int)(owner.getBaseAtt() + (owner.getBaseHP() * amount/75.0));//calculate odds of boost applying assuming random placement**
        return owner.getBaseHP() * att;
    }
    
}
