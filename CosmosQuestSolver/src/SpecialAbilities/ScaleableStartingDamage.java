/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Levelable;

//deal a set amount damage at start of fight that scales with level. Used by Dr Hawking
public class ScaleableStartingDamage extends StartingDamage{
    
    private int levelMilestone;
    
    public ScaleableStartingDamage(Creature owner, int amount, int levelMilestone){
        super(owner,amount);
        this.levelMilestone = levelMilestone;
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleableStartingDamage(newOwner,amount,levelMilestone);
    }
    
    @Override
    public void startOfFightAction2(Formation thisFormation, Formation enemyFormation) {
        //enemyFormation.takeAOEDamage((amount * (int)(owner.getLevel() / levelMilestone)));//not affected by bubbles
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            for (Creature creature : enemyFormation.getMembers()){
                creature.takeAOEDamage((amount * (int)(levelable.getLevel() / levelMilestone)),thisFormation);
            }
        }
    }
    
    @Override
    public String getDescription() {
        if (levelMilestone == 1){
            return "At start, deal " + amount + " aoe per level";
        }
        else{
            return "At start, deal " + amount + " aoe every " + levelMilestone + " levels";
        }
    }
    
    @Override
    public int viability() {
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            return owner.getBaseHP() * (owner.getBaseAtt() + Formation.MAX_MEMBERS * (amount * (int)(levelable.getLevel() / levelMilestone)));
        }
        else{
            return owner.getBaseAtt() * owner.getBaseHP();
        }
    }
    
}
