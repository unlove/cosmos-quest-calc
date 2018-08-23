/*

 */
package Formations;

import GUI.AssetPanel;
import GUI.CreatureDrawer;
import SpecialAbilities.Nothing;
import SpecialAbilities.SpecialAbility;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Monster extends Creature {

    public static final int TOTAL_UNIQUE_TIERS = 15;
    public static final int REPEATS = 2;
    public static final int TOTAL_TIERS = TOTAL_UNIQUE_TIERS * REPEATS;
    
    public static final String[] REPEAT_TIER_STRING = new String[]{"","Furious "};//prefex names are aready hard coded into creatureFactory. Repeat?
    
    private long followers;
    private int tier;
    
    protected Monster(Element element, int baseAtt, int baseHP, long followers, int tier){
        super(element,baseAtt,baseHP);
        this.followers = followers;
        specialAbility = new Nothing(this);
        this.tier = tier;
    }

    @Override
    public Monster getCopy() {
        Monster m = new Monster(element,baseAtt,baseHP,followers,tier);
        m.setID(ID);
        m.currentAtt = currentAtt;
        m.currentHP = currentHP;
        m.specialAbility = specialAbility.getCopyForNewOwner(m);
        return m;
    }
    
    public int getTier(){
        return tier;
    }
    
    @Override
    public int getID(){
        int elementNum = 0;
        switch(element){
            case AIR: elementNum = 0;break;
            case EARTH: elementNum = 1;break;
            case FIRE: elementNum = 2;break;
            case WATER: elementNum = 3;break;
        }
        return tier * Element.values().length + elementNum;
    }
    
    @Override
    public long getFollowers(){
        return followers;
    }
    
    @Override
    public String getImageAddress() {
        int strIndex = (tier-1)/TOTAL_UNIQUE_TIERS;
        
        return "Creatures/Monsters/" + getName().substring(REPEAT_TIER_STRING[strIndex].length());
        
    }
    
    public int sortingValue(){
        return element.ordinal()*TOTAL_TIERS + tier;
    }
    
    @Override
    public Hero.Rarity getRarity() {
        return null;
    }

    @Override
    public void draw(Graphics g) {
        CreatureDrawer.drawCreature(this, g);
    }
    
}