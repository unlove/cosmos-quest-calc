/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Levelable;

//decreaces AOE ability damage on formation by a specified percentage based on
//level. Used by Bubbles and MOAK
public class ScaleableAntiAOE extends AntiAOE{
    
    private double levelMilestone;
    
    public ScaleableAntiAOE(Creature owner, double amount, double levelMilestone) {
        super(owner,amount);
        this.levelMilestone = levelMilestone;
    }
    
    

    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleableAntiAOE(newOwner,percent,levelMilestone);
    }
    
    @Override
    public void startOfFightAction(Formation thisFormation, Formation enemyFormation) {
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            double AOEResistance = percent * (int)(levelable.getLevel() / levelMilestone);
            if (AOEResistance > 1){
                AOEResistance = 1;
            }
            thisFormation.setAOEResistance(AOEResistance);
        }
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            thisFormation.setAOEResistance(thisFormation.getAOEResistance() - percent * (int)(levelable.getLevel() / levelMilestone));
        }
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
        
        if (levelMilestone == 1){
            return "-" + percentStr + "% per level to area skills";
        }
        else{
            return "-" + percentStr + " per " + levelMilestone + " per level to area skills";
        }
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt();
    }
    
}
