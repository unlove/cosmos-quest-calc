/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Monster;
import cosmosquestsolver.OtherThings;

//multiplies attack by a specified amount for every living monster behind the
//owner (multiplicative). Used by Geror and Ascended Geror
public class MonsterBuff extends SpecialAbility{
    
    private double multiplier;

    public MonsterBuff(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public double extraDamage(Formation thisFormation, Formation enemyFormation) {
        return owner.getBaseAtt() * Math.pow(multiplier, monstersBehind(thisFormation)) - owner.getBaseAtt();
    }

    
    private int monstersBehind(Formation f){
        int numBehind = 0;
        boolean foundOwner = false;
        for (Creature creature : f){
            if (foundOwner && creature instanceof Monster && !creature.isDead()){
                numBehind ++;
            }
            
            if (creature == owner){//this goes last in case a monster has this ability(not possible currently)
                foundOwner = true;
            }
        }
        
        return numBehind;
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new MonsterBuff(newOwner,multiplier);
    }
    
    
    @Override
    public String getDescription() {
        return "Monsters behind increace attack by " + multiplier;
    }
    
    @Override
    public int viability() {
        int att = (int)(owner.getBaseAtt() * Math.pow(multiplier, Formation.MAX_MEMBERS/3.0));
        return owner.getBaseHP() * att;
    }
    
}

