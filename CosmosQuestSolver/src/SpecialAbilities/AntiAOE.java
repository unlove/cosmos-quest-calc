/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//decreaces AOE ability damage on formation by a specified percentage.
//used by MOAK
public class AntiAOE extends SpecialAbility{
    
    protected double percent;

    public AntiAOE(Creature owner, double percent) {
        super(owner);
        this.percent = percent;
        if (percent > 1){
            this.percent = 1;
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new AntiAOE(newOwner,percent);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        double tempPer = percent;
        if (tempPer > 1){
            tempPer = 1;
        }
        thisFormation.setAOEResistance(tempPer);
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        double tempPer = percent;
        if (tempPer > 1){
            tempPer = 1;
        }
        thisFormation.setAOEResistance(thisFormation.getAOEResistance() - tempPer);
    }
    
    @Override
    public String getDescription() {
        String percentStr = "";
        double times100 = percent * 100;
        if (times100 % 1 == 0){
            percentStr = Integer.toString((int) times100);
        }
        else{
            percentStr = Double.toString(times100);
        }
        return "-" + percentStr + "% to area skills";
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt();
    }
    
}
