/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//attacks a random enemy each turn (only one hit, enemy in first is not guarenteed to get hit)
//used by Pokerface
public class CriticalHit extends SpecialAbility{
    
    private double multiplier;
    private int turn = 0;

    public CriticalHit(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new CriticalHit(newOwner,multiplier);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        enemyFormation.getTurnSeed(enemyFormation, turn);//gets formation to generate seed at the beginning of the fight. seed might change if called mid-fight
    }
    
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        turn ++;
    }
    
    @Override
    public void attack(Formation thisFormation, Formation enemyFormation) {
        if (Math.abs(thisFormation.getTurnSeed(enemyFormation,turn)) % 2 == 1){
            long originalAtt = owner.getCurrentAtt();
            owner.setCurrentAtt((long)(Math.ceil(originalAtt * multiplier)));
            super.attack(thisFormation,enemyFormation);
            owner.setCurrentAtt(originalAtt);
            //System.out.println("crit, seed = " + thisFormation.getTurnSeed(enemyFormation, turn));
        }
        else{
            super.attack(thisFormation,enemyFormation);
            //System.out.println(" no crit, seed = " + thisFormation.getTurnSeed(enemyFormation, turn));
        }
    }
    
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(multiplier * 100));
        return "Has a chance to deal " + percent + "% damage";
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt() * 2;// average of double damage
    }
    
}
