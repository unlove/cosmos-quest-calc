/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Levelable;

//Increases the attack and armor of all creatures
//of a specified element (everyone if element is null) (including the owner)
//while creature is alive. Increases lineraly as level increaces. Used by
//Halloween heroes, quest heroes 13-20, Ascended Athos, Ascended Rei, and
//Ascended Bavah
public class ScaleableStatBoost extends StatBoost{
        
    private double levelMilestone;
    
    public ScaleableStatBoost(Creature owner, int attBoost, int armorBoost, Creature.Element element, double levelMilestone) {//if elsment is null, apply to all creatures
        super(owner,attBoost,armorBoost,element);
        this.levelMilestone = levelMilestone;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleableStatBoost(newOwner,attBoost,armorBoost,element,levelMilestone);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            for (Creature creature : thisFormation){
                if (element == null || creature.getElement() == element){
                    creature.addAttBoost(roundedScaleMilestone(levelable,attBoost,levelMilestone));//round up?***
                    creature.addArmorBoost(roundedScaleMilestone(levelable,armorBoost,levelMilestone));
                }
            }
        }
        
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            for (Creature creature : thisFormation){
                if (element == null || creature.getElement() == element){
                    creature.addAttBoost(-roundedScaleMilestone(levelable,attBoost,levelMilestone));//round up?***
                    creature.addArmorBoost(-roundedScaleMilestone(levelable,armorBoost,levelMilestone));
                }
            }
        }
    }

    
    
    @Override
    public String getDescription() {
        if (!(owner instanceof Levelable)){
            return "";
        }
        String elementStr = "";
        if (element == null){
            elementStr = "all";
        }
        else{
            switch(element){//method for this?
                case AIR: elementStr = "air"; break;
                case WATER: elementStr = "water"; break;
                case EARTH: elementStr = "earth"; break;
                case FIRE: elementStr = "fire"; break;
                default: elementStr = "all";
            }
        }
        
        String milestoneStr = "";
        if (levelMilestone % 1 == 0){
            if (levelMilestone == 1){
                milestoneStr = "level";
            }
            else{
                milestoneStr = Integer.toString((int)levelMilestone) + " levels";
            }
        }
        else{
            if (levelMilestone == 1){
                milestoneStr = "level";
            }
            else{
                milestoneStr = Double.toString(levelMilestone) + " levels";
            }
        }
        
        if (attBoost != 0 && armorBoost == 0){
            return "+" + attBoost + " attack to " + elementStr + " creatures every " + milestoneStr + " " + roundedScaleMilestoneStr((Levelable)owner,attBoost,levelMilestone);
        }
        else if (attBoost == 0 && armorBoost != 0){
            return "+" + armorBoost + " armor to " + elementStr + " creatures every " + milestoneStr + " " + roundedScaleMilestoneStr((Levelable)owner,armorBoost,levelMilestone);
        }
        else if (attBoost != 0 && armorBoost != 0){
            if (attBoost == armorBoost){
                return "+" + attBoost + " armor & atk to " + elementStr + " creatures every " + milestoneStr + " " + roundedScaleMilestoneStr((Levelable)owner,attBoost,levelMilestone);
            }
            else{
                return ""; //todo. no creature has this yet
            }
        }
        else{//no boost
            return "";
        }
    }
    
    @Override
    public int viability() {
        if (owner instanceof Levelable){
            Levelable levelable = (Levelable) owner;
            int actualAttBoost = (attBoost * (int)(levelable.getLevel() / levelMilestone));
            int actualArmorBoost = (armorBoost * (int)(levelable.getLevel() / levelMilestone));

            return (owner.getBaseAtt() + (Formation.MAX_MEMBERS/2) * actualAttBoost) * (owner.getBaseHP() + (Formation.MAX_MEMBERS/2) * actualArmorBoost);
        }
        else{
            return owner.getBaseAtt() * owner.getBaseHP();
        }
    }
    
}
