/*

 */
package Formations;

import SpecialAbilities.AOE;
import SpecialAbilities.AddAttack;
import SpecialAbilities.AntiAOE;
import SpecialAbilities.Wither;
import SpecialAbilities.BloodBomb;
import SpecialAbilities.DeathStrike;
import SpecialAbilities.Leech;
import SpecialAbilities.Evolve;
import SpecialAbilities.Revenge;
import SpecialAbilities.CriticalHit;
import SpecialAbilities.DamageOverflow;
import SpecialAbilities.ElementDamageBoost;
import SpecialAbilities.Heal;
import SpecialAbilities.Intercept;
import SpecialAbilities.MonsterBuff;
import SpecialAbilities.MultiplyAttack;
import SpecialAbilities.OutnumberedPercentDamage;
import SpecialAbilities.Purity;
import SpecialAbilities.Rainbow;
import SpecialAbilities.RandomStatBoost;
import SpecialAbilities.RandomTarget;
import SpecialAbilities.Reflect;
import SpecialAbilities.Ricochet;
import SpecialAbilities.ScaleableAOE;
import SpecialAbilities.ScaleableAntiAOE;
import SpecialAbilities.ScaleableHeal;
import SpecialAbilities.ScaleableLifeSteal;
import SpecialAbilities.ScaleableStartingDamage;
import SpecialAbilities.ScaleableStatBoost;
import SpecialAbilities.StatBoost;
import SpecialAbilities.StatLevelBoost;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

//uses the flyweight and prototype patterns to generate new objects.
//these include monsters, heroes, world bosses, and images
//these assets are generated when loading a frame (not the main menu) for the 
//first time.
public class CreatureFactory {
    
    //singleton or have everything be static?
    private static CreatureFactory instance;// = new CreatureFactory();
    
    //for singleton, make these non-static***
    private static Monster[][] monsters;
    private static HashMap<String,Hero> heroes;
    private static HashMap<String,WorldBoss> worldBosses;
    private static ArrayList<String> monsterNames;
    private static ArrayList<String> heroNames;
    private static ArrayList<String> worldBossNames;
    private static HashMap<Integer,String> IDToNameMap;
    private static HashMap<String,BufferedImage> pictures;
    private static BufferedImage defaultImage;
    //private static int currentID;

    public static final int MAX_QUESTS = 160;

    
    
    private CreatureFactory(){
        IDToNameMap = new HashMap<>();
        initiateMonsters();
        initiateHeroes();
        initiateWorldBosses();
        initiatePictures();
    }
    
    public static String getCreatureName(int ID){
        if (instance == null){
            instance = new CreatureFactory();
        }
        return IDToNameMap.get(ID);
    }
    
    
    
    public static Monster getMonster(Creature.Element element, int tier){
        if (instance == null){
            instance = new CreatureFactory();
        }
        return (Monster) instance.monsters[element.ordinal()][tier - 1].getCopy();
    }
    
    //returns an array of all monsters of a givin element in order of tier.
    // if element is null, returns all monsters
    public static Monster[] getMonsters(Creature.Element element) {
        if (instance == null){
            instance = new CreatureFactory();
        }
        
        Monster[] monsterArray;
        
        if (element != null){
            monsterArray = new Monster[Monster.TOTAL_TIERS];
            for (int i = 0; i < monsterArray.length; i++){
                monsterArray[i] = (Monster) monsters[element.ordinal()][i].getCopy();
            }
            return monsterArray;
        }
        else{
            monsterArray = new Monster[Monster.TOTAL_TIERS * Creature.Element.values().length];
            for (int i = 0; i < Creature.Element.values().length; i++){
                for (int j = 0; j < Monster.TOTAL_TIERS; j++){
                    monsterArray[j + Monster.TOTAL_TIERS*i] = (Monster) monsters[i][j].getCopy();
                }
            }
            return monsterArray;
        }
        
    }
    
    public static long getMinFollowersForTier1Monsters() {
        if (instance == null){
            instance = new CreatureFactory();
        }
        long ans = 0;
        for (int i = 0; i < Creature.Element.values().length; i++){
            long followers = getMonster(Creature.Element.values()[i],1).getFollowers();
            if (followers > ans){
                ans = followers;
            }
        }
        return ans;
    }
    
    public static Monster getCheapestMonster() {
        if (instance == null){
            instance = new CreatureFactory();
        }
        LinkedList<Monster> monsters = new LinkedList<>();
        for (Creature.Element element : Creature.Element.values()){
            monsters.add(getMonster(element,1));
        }
        
        long cheapestCost = Long.MAX_VALUE;
        for (Monster m : monsters){
            if (m.getFollowers() < cheapestCost){
                cheapestCost = m.getFollowers();
            }
        }
        
        while(true){//this returns a random monster if there is a tie for the cheapest monster
            int randomIndex = (int)(Math.random() * monsters.size());
            if (monsters.get(randomIndex).getFollowers() == cheapestCost){
                return monsters.get(randomIndex).getCopy();
            }
            else{
                monsters.remove(randomIndex);
            }
        }
        
    }
    
    public static Monster getStrongestMonster() {//strongest monster is always the same. just return that?
        if (instance == null){
            instance = new CreatureFactory();
        }
        LinkedList<Monster> monsters = new LinkedList<>();
        for (Creature.Element element : Creature.Element.values()){
            monsters.add(getMonster(element,Monster.TOTAL_TIERS));
        }
        
        Monster strongestMonster = getMonster(Creature.Element.AIR,Monster.TOTAL_TIERS);
        for (Monster m : monsters){
            if (m.getFollowers() > strongestMonster.getFollowers()){
                strongestMonster = m;
            }
        }
        return strongestMonster;
        
    }
    
    //returns a list of all followers that are available with a given amount of followers
    public static LinkedList<Monster> getAvailableMonsters(long followers){
        if (instance == null){
            instance = new CreatureFactory();
        }
        LinkedList<Monster> list = new LinkedList<>();
        for (int i = 0; i < Creature.Element.values().length; i++){
            for (int j = 0; j < Monster.TOTAL_TIERS; j++){
                if (monsters[i][j].getFollowers() < followers){
                    list.add((Monster) monsters[i][j].getCopy());
                }
            }
        }
        return list;
    }
    
    public static Hero getHero(String name, int level){
        if (instance == null){
            instance = new CreatureFactory();
        }
        Hero hero = (Hero) instance.heroes.get(name).getCopy();
        hero.levelUp(level);
        return hero;
    }
    
    public static Hero[] getHeroes() {
        if (instance == null){
            instance = new CreatureFactory();
        }
        Hero[] heroArray = new Hero[heroNames.size()];
        int i = 0;
        for (String str : heroNames){
            heroArray[i] = (Hero) heroes.get(str).getCopy();
            i ++;
        }
        return heroArray;
    }
    
    public static WorldBoss getWorldBoss(String name){
        if (instance == null){
            instance = new CreatureFactory();
        }
        WorldBoss wb = (WorldBoss) instance.worldBosses.get(name).getCopy();
        return wb;
    }
    
    public static WorldBoss getDefaultBoss() {
        if (instance == null){
            instance = new CreatureFactory();
        }
        return getWorldBoss("Lord of Chaos");
    }
    
    public static WorldBoss[] getWorldBosses(){
        if (instance == null){
            instance = new CreatureFactory();
        }
        
        WorldBoss[] bossArray = new WorldBoss[worldBossNames.size()];
        int i = 0;
        for (String str : worldBossNames){
            bossArray[i] = (WorldBoss) worldBosses.get(str).getCopy();
            i ++;
        }
        return bossArray;
        
    }
    
    public static Formation loadFormation(String address, boolean facingRight) {
        if (instance == null){
            instance = new CreatureFactory();
        }
        try{
            LinkedList<Creature> list = new LinkedList<>();
            Scanner sc = new Scanner(new File(address + ".txt"));
            String[] tokens;
            while (sc.hasNext()){
                tokens = sc.nextLine().split(",");
                if(tokens[0].equals("M")){//signifies monsters
                    Monster m = getMonster(charToElement(tokens[1].charAt(0)),Integer.parseInt(tokens[2]));
                    m.setFacingRight(facingRight);
                    list.add(m);
                }
                else if (tokens[0].equals("H")){//heroes
                    Hero h = getHero(tokens[1],Integer.parseInt(tokens[2]));
                    h.setFacingRight(facingRight);
                    list.add(h);
                }
            }
            return new Formation(list);
        }
        catch(Exception e){
            return new Formation();
        }
        
    }
    
    private static Creature.Element charToElement(char c){
        switch(c){
            case 'A': return Creature.Element.AIR;
            case 'W': return Creature.Element.WATER;
            case 'E': return Creature.Element.EARTH;
            case 'F': return Creature.Element.FIRE;
            default: return null;
        }
    }
    
    private void initiateMonsters(){
        //currentID = 0;
        monsters = new Monster[Creature.Element.values().length][Monster.TOTAL_UNIQUE_TIERS * Monster.getNumTimesRepeat()];
        monsterNames = new ArrayList();
        
        //make shortened references to element numbers
        int air = Creature.Element.AIR.ordinal();
        int water = Creature.Element.WATER.ordinal();
        int earth = Creature.Element.EARTH.ordinal();
        int fire = Creature.Element.FIRE.ordinal();
        
        //code to get names of repeat creatures? have arrays of monster names in this class
        addMonster("Kodama",air,1,new Monster(Creature.Element.AIR,8,20,1000,1));
        addMonster("Harpy",air,2,new Monster(Creature.Element.AIR,6,48,3900,2));
        addMonster("Griffin",air,3,new Monster(Creature.Element.AIR,12,36,8000,3));
        addMonster("Quetzalcoatl",air,4,new Monster(Creature.Element.AIR,26,24,15000,4));
        addMonster("Bailong",air,5,new Monster(Creature.Element.AIR,20,60,41000,5));
        addMonster("Manticore",air,6,new Monster(Creature.Element.AIR,34,62,96000,6));
        addMonster("Anka",air,7,new Monster(Creature.Element.AIR,26,106,144000,7));
        addMonster("Typhon",air,8,new Monster(Creature.Element.AIR,52,78,257000,8));
        addMonster("Hecatoncheires",air,9,new Monster(Creature.Element.AIR,54,116,495000,9));
        addMonster("Ophion",air,10,new Monster(Creature.Element.AIR,60,142,785000,10));
        addMonster("The Norns",air,11,new Monster(Creature.Element.AIR,110,114,1403000,11));
        addMonster("World Egg",air,12,new Monster(Creature.Element.AIR,88,164,1733000,12));
        addMonster("Chronos",air,13,new Monster(Creature.Element.AIR,94,210,2772000,13));
        addMonster("Brahma",air,14,new Monster(Creature.Element.AIR,142,200,4785000,14));
        addMonster("Ginnun",air,15,new Monster(Creature.Element.AIR,190,226,8897000,15));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Kodama",air,16,new Monster(Creature.Element.AIR,196,280,12855000,16));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Harpy",air,17,new Monster(Creature.Element.AIR,206,318,16765000,17));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Griffin",air,18,new Monster(Creature.Element.AIR,280,280,21951000,18));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Quetzalcoatl",air,19,new Monster(Creature.Element.AIR,206,440,27288000,19));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Bailong",air,20,new Monster(Creature.Element.AIR,268,378,32242000,20));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Manticore",air,21,new Monster(Creature.Element.AIR,286,428,42826000,21));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Anka",air,22,new Monster(Creature.Element.AIR,320,454,55373000,22));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Typhon",air,23,new Monster(Creature.Element.AIR,348,500,72580000,23));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Hecatoncheires",air,24,new Monster(Creature.Element.AIR,374,554,94312000,24));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Ophion",air,25,new Monster(Creature.Element.AIR,430,580,124549000,25));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "The Norns",air,26,new Monster(Creature.Element.AIR,582,496,155097000,26));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "World Egg",air,27,new Monster(Creature.Element.AIR,484,712,202295000,27));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Chronos",air,28,new Monster(Creature.Element.AIR,642,644,265846000,28));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Brahma",air,29,new Monster(Creature.Element.AIR,616,834,368230000,29));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Ginnun",air,30,new Monster(Creature.Element.AIR,906,700,505055000,30));
        
        addMonster("Navi",water,1,new Monster(Creature.Element.WATER,6,30,1400,1));
        addMonster("Dakuwaqa",water,2,new Monster(Creature.Element.WATER,12,24,3900,2));
        addMonster("Mermaid",water,3,new Monster(Creature.Element.WATER,24,18,8000,3));
        addMonster("Yeti",water,4,new Monster(Creature.Element.WATER,20,36,18000,4));
        addMonster("Hippocampus",water,5,new Monster(Creature.Element.WATER,18,78,52000,5));
        addMonster("Hydra",water,6,new Monster(Creature.Element.WATER,44,44,84000,6));
        addMonster("Kraken",water,7,new Monster(Creature.Element.WATER,32,92,159000,7));
        addMonster("Leviathan",water,8,new Monster(Creature.Element.WATER,36,108,241000,8));
        addMonster("Poseidon",water,9,new Monster(Creature.Element.WATER,70,80,418000,9));
        addMonster("Cthulhu",water,10,new Monster(Creature.Element.WATER,70,110,675000,10));
        addMonster("Jormungandr",water,11,new Monster(Creature.Element.WATER,79,152,1315000,11));
        addMonster("Cailleach",water,12,new Monster(Creature.Element.WATER,78,188,1775000,12));
        addMonster("Ogenos",water,13,new Monster(Creature.Element.WATER,128,140,2398000,13));
        addMonster("Niflheim",water,14,new Monster(Creature.Element.WATER,122,212,4159000,14));
        addMonster("Nun",water,15,new Monster(Creature.Element.WATER,142,276,7758000,15));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Navi",water,16,new Monster(Creature.Element.WATER,198,286,13475000,16));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Dakuwaqa",water,17,new Monster(Creature.Element.WATER,258,262,17573000,17));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Mermaid",water,18,new Monster(Creature.Element.WATER,230,330,20909000,18));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Yeti",water,19,new Monster(Creature.Element.WATER,238,360,25079000,19));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Hippocampus",water,20,new Monster(Creature.Element.WATER,232,454,34182000,20));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Hydra",water,21,new Monster(Creature.Element.WATER,290,416,41901000,21));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Kraken",water,22,new Monster(Creature.Element.WATER,340,440,55877000,22));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Leviathan",water,23,new Monster(Creature.Element.WATER,354,490,72243000,23));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Poseidon",water,24,new Monster(Creature.Element.WATER,388,540,95903000,24));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Cthulhu",water,25,new Monster(Creature.Element.WATER,506,500,127256000,25));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Jormungandr",water,26,new Monster(Creature.Element.WATER,416,700,157140000,26));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Cailleach",water,27,new Monster(Creature.Element.WATER,500,682,199344000,27));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Ogenos",water,28,new Monster(Creature.Element.WATER,536,762,261023000,28));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Niflheim",water,29,new Monster(Creature.Element.WATER,512,1008,370761000,29));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Nun",water,30,new Monster(Creature.Element.WATER,802,802,515849000,30));
        
        addMonster("Sapling",earth,1,new Monster(Creature.Element.EARTH,4,44,1300,1));
        addMonster("Alux",earth,2,new Monster(Creature.Element.EARTH,8,30,2700,2));
        addMonster("Centaur",earth,3,new Monster(Creature.Element.EARTH,16,26,7500,3));
        addMonster("Ent",earth,4,new Monster(Creature.Element.EARTH,10,72,18000,4));
        addMonster("Sphinx",earth,5,new Monster(Creature.Element.EARTH,40,36,54000,5));
        addMonster("Alraune",earth,6,new Monster(Creature.Element.EARTH,24,72,71000,6));
        addMonster("Chimera",earth,7,new Monster(Creature.Element.EARTH,36,66,115000,7));
        addMonster("Ammit",earth,8,new Monster(Creature.Element.EARTH,60,60,215000,8));
        addMonster("Moai Golem",earth,9,new Monster(Creature.Element.EARTH,48,120,436000,9));
        addMonster("Akupara",earth,10,new Monster(Creature.Element.EARTH,64,122,689000,10));
        addMonster("Fenrir",earth,11,new Monster(Creature.Element.EARTH,81,134,1130000,11));
        addMonster("Liche",earth,12,new Monster(Creature.Element.EARTH,120,128,1903000,12));
        addMonster("Gea",earth,13,new Monster(Creature.Element.EARTH,132,190,3971000,13));
        addMonster("Yggdrasil",earth,14,new Monster(Creature.Element.EARTH,136,244,6044000,14));
        addMonster("Kailas",earth,15,new Monster(Creature.Element.EARTH,186,200,7173000,15));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Sapling",earth,16,new Monster(Creature.Element.EARTH,190,284,12534000,16));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Alux",earth,17,new Monster(Creature.Element.EARTH,192,338,16531000,17));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Centaur",earth,18,new Monster(Creature.Element.EARTH,242,330,22567000,18));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Ent",earth,19,new Monster(Creature.Element.EARTH,282,320,27107000,19));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Sphinx",earth,20,new Monster(Creature.Element.EARTH,264,382,32025000,20));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Alraune",earth,21,new Monster(Creature.Element.EARTH,272,446,42252000,21));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Chimera",earth,22,new Monster(Creature.Element.EARTH,324,450,55671000,22));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Ammit",earth,23,new Monster(Creature.Element.EARTH,340,516,73483000,23));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Moai Golem",earth,24,new Monster(Creature.Element.EARTH,458,458,96071000,24));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Akupara",earth,25,new Monster(Creature.Element.EARTH,418,592,123096000,25));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Fenrir",earth,26,new Monster(Creature.Element.EARTH,468,622,157055000,26));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Liche",earth,27,new Monster(Creature.Element.EARTH,602,580,206317000,27));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Gea",earth,28,new Monster(Creature.Element.EARTH,540,770,268117000,28));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Yggdrasil",earth,29,new Monster(Creature.Element.EARTH,614,830,363805000,29));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Kailas",earth,30,new Monster(Creature.Element.EARTH,614,1022,497082000,30));
        
        addMonster("Will-o-the-whisp",fire,1,new Monster(Creature.Element.FIRE,10,16,1000,1));
        addMonster("Fox Spirit",fire,2,new Monster(Creature.Element.FIRE,16,18,3900,2));
        addMonster("Hellhound",fire,3,new Monster(Creature.Element.FIRE,8,54,8000,3));
        addMonster("Wyvern",fire,4,new Monster(Creature.Element.FIRE,16,52,23000,4));
        addMonster("Cherufe",fire,5,new Monster(Creature.Element.FIRE,24,42,31000,5));
        addMonster("Gargoyle",fire,6,new Monster(Creature.Element.FIRE,20,104,94000,6));
        addMonster("Ifrit",fire,7,new Monster(Creature.Element.FIRE,44,54,115000,7));
        addMonster("Phoenix",fire,8,new Monster(Creature.Element.FIRE,50,94,321000,8));
        addMonster("Balrog",fire,9,new Monster(Creature.Element.FIRE,58,102,454000,9));
        addMonster("Beelzebub",fire,10,new Monster(Creature.Element.FIRE,82,104,787000,10));
        addMonster("Huitzilopochtli",fire,11,new Monster(Creature.Element.FIRE,70,164,1229000,11));
        addMonster("Surtur",fire,12,new Monster(Creature.Element.FIRE,92,156,1718000,12));
        addMonster("Bahamut",fire,13,new Monster(Creature.Element.FIRE,130,166,3169000,13));
        addMonster("Ra",fire,14,new Monster(Creature.Element.FIRE,168,168,4741000,14));
        addMonster("Chaos",fire,15,new Monster(Creature.Element.FIRE,136,234,5676000,15));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Will-o-the-whisp",fire,16,new Monster(Creature.Element.FIRE,192,288,13001000,16));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Fox Spirit",fire,17,new Monster(Creature.Element.FIRE,292,236,18090000,17));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Hellhound",fire,18,new Monster(Creature.Element.FIRE,200,392,21951000,18));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Wyvern",fire,19,new Monster(Creature.Element.FIRE,244,352,25170000,19));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Cherufe",fire,20,new Monster(Creature.Element.FIRE,266,388,33155000,20));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Gargoyle",fire,21,new Monster(Creature.Element.FIRE,338,362,42798000,21));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Ifrit",fire,22,new Monster(Creature.Element.FIRE,318,458,55582000,22));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Phoenix",fire,23,new Monster(Creature.Element.FIRE,410,424,72480000,23));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Balrog",fire,24,new Monster(Creature.Element.FIRE,392,534,95772000,24));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Beelzebub",fire,25,new Monster(Creature.Element.FIRE,328,764,125443000,25));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Huitzilopochtli",fire,26,new Monster(Creature.Element.FIRE,462,638,160026000,26));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Surtur",fire,27,new Monster(Creature.Element.FIRE,498,690,201426000,27));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Bahamut",fire,28,new Monster(Creature.Element.FIRE,552,746,264250000,28));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Ra",fire,29,new Monster(Creature.Element.FIRE,676,746,358119000,29));
        addMonster(Monster.REPEAT_TIER_STRING[1] + "Chaos",fire,30,new Monster(Creature.Element.FIRE,690,930,514040000,30));
    }

    private void initiateHeroes() {
        //currentID = 0;
        heroes = new HashMap<>();
        heroNames = new ArrayList<>();
        
        //chest heroes
        addHero("Hunter",new Hero(Creature.Element.AIR,14,22,Hero.Rarity.COMMON,7,new StatBoost(null,2,0,Creature.Element.AIR)));//get rid of owner in constructor? no. copying.
        addHero("Shaman",new Hero(Creature.Element.EARTH,20,40,Hero.Rarity.RARE,8,new StatBoost(null,0,2,Creature.Element.EARTH)));
        addHero("Alpha",new Hero(Creature.Element.FIRE,22,82,Hero.Rarity.LEGENDARY,9,new AOE(null,1)));
        addHero("Carl",new Hero(Creature.Element.WATER,12,28,Hero.Rarity.COMMON,10,new StatBoost(null,2,0,Creature.Element.WATER)));
        addHero("Nimue",new Hero(Creature.Element.AIR,22,38,Hero.Rarity.RARE,11,new StatBoost(null,0,2,Creature.Element.AIR)));
        addHero("Athos",new Hero(Creature.Element.EARTH,26,70,Hero.Rarity.LEGENDARY,12,new StatBoost(null,0,2,null)));
        addHero("Jet",new Hero(Creature.Element.FIRE,16,24,Hero.Rarity.COMMON,13,new StatBoost(null,2,0,Creature.Element.FIRE)));
        addHero("Geron",new Hero(Creature.Element.WATER,24,36,Hero.Rarity.RARE,14,new StatBoost(null,0,2,Creature.Element.WATER)));
        addHero("Rei",new Hero(Creature.Element.AIR,40,46,Hero.Rarity.LEGENDARY,15,new StatBoost(null,2,0,null)));
        addHero("Ailen",new Hero(Creature.Element.EARTH,22,19,Hero.Rarity.COMMON,16,new StatBoost(null,2,0,Creature.Element.EARTH)));
        addHero("Faefyr",new Hero(Creature.Element.FIRE,18,50,Hero.Rarity.RARE,17,new StatBoost(null,0,2,Creature.Element.FIRE)));
        addHero("Auri",new Hero(Creature.Element.WATER,32,60,Hero.Rarity.LEGENDARY,18,new Heal(null,2)));
        addHero("K41RY",new Hero(Creature.Element.AIR,16,28,Hero.Rarity.COMMON,21,new StatBoost(null,3,0,Creature.Element.AIR)));
        addHero("T4URUS",new Hero(Creature.Element.EARTH,20,46,Hero.Rarity.RARE,22,new StatBoost(null,1,0,null)));
        addHero("TR0N1X",new Hero(Creature.Element.FIRE,20,100,Hero.Rarity.LEGENDARY,23,new AOE(null,3)));
        addHero("Aquortis",new Hero(Creature.Element.WATER,8,58,Hero.Rarity.COMMON,24,new StatBoost(null,3,0,Creature.Element.WATER)));
        addHero("Aeris",new Hero(Creature.Element.AIR,32,30,Hero.Rarity.RARE,25,new Heal(null,1)));
        addHero("Geum",new Hero(Creature.Element.EARTH,2,75,Hero.Rarity.LEGENDARY,26,new MultiplyAttack(null,2)));
        addHero("Rudean",new Hero(Creature.Element.FIRE,12,38,Hero.Rarity.COMMON,30,new StatBoost(null,3,0,Creature.Element.FIRE)));
        addHero("Aural",new Hero(Creature.Element.WATER,50,18,Hero.Rarity.RARE,31,new MultiplyAttack(null,1.2)));
        addHero("Geror",new Hero(Creature.Element.AIR,46,46,Hero.Rarity.LEGENDARY,32,new MonsterBuff(null,1.2)));
        addHero("Ourea",new Hero(Creature.Element.EARTH,16,30,Hero.Rarity.COMMON,36,new StatBoost(null,3,0,Creature.Element.EARTH)));
        addHero("Erebus",new Hero(Creature.Element.FIRE,20,48,Hero.Rarity.RARE,37,new StatBoost(null,2,2,Creature.Element.FIRE)));
        addHero("Pontus",new Hero(Creature.Element.WATER,36,62,Hero.Rarity.LEGENDARY,38,new Purity(null,2)));
        addHero("Oymos",new Hero(Creature.Element.AIR,14,36,Hero.Rarity.COMMON,45,new StatBoost(null,4,0,Creature.Element.AIR)));
        addHero("Xarth",new Hero(Creature.Element.EARTH,32,32,Hero.Rarity.RARE,46,new StatBoost(null,2,2,Creature.Element.EARTH)));
        addHero("Atzar",new Hero(Creature.Element.FIRE,32,76,Hero.Rarity.LEGENDARY,47,new Purity(null,2)));
        addHero("Hama",new Hero(Creature.Element.WATER,18,30,Hero.Rarity.COMMON,62,new StatBoost(null,4,0,Creature.Element.WATER)));
        addHero("Hallinskidi",new Hero(Creature.Element.AIR,34,34,Hero.Rarity.RARE,63,new StatBoost(null,2,2,Creature.Element.AIR)));
        addHero("Rigr",new Hero(Creature.Element.EARTH,42,60,Hero.Rarity.LEGENDARY,64,new Purity(null,2)));
        addHero("Toth",new Hero(Creature.Element.FIRE,24,24,Hero.Rarity.COMMON,77,new StatBoost(null,4,0,Creature.Element.FIRE)));
        addHero("Ganah",new Hero(Creature.Element.WATER,30,40,Hero.Rarity.RARE,78,new StatBoost(null,2,2,Creature.Element.WATER)));
        addHero("Dagda",new Hero(Creature.Element.AIR,46,58,Hero.Rarity.LEGENDARY,79,new Purity(null,2)));
        addHero("Bylar",new Hero(Creature.Element.EARTH,20,30,Hero.Rarity.COMMON,93,new StatBoost(null,4,0,Creature.Element.EARTH)));
        addHero("Boor",new Hero(Creature.Element.FIRE,36,36,Hero.Rarity.RARE,94,new AddAttack(null,3)));
        addHero("Bavah",new Hero(Creature.Element.WATER,52,52,Hero.Rarity.LEGENDARY,95,new StatBoost(null,2,2,null)));
        addHero("Taint",new Hero(Creature.Element.AIR,25,25,Hero.Rarity.COMMON,110,new Ricochet(null,0.5,5)));
        addHero("Putrid",new Hero(Creature.Element.EARTH,50,48,Hero.Rarity.RARE,111,new AddAttack(null,-3)));
        addHero("Defile",new Hero(Creature.Element.FIRE,48,52,Hero.Rarity.LEGENDARY,112,new BloodBomb(null,50)));
        
        //ascended chest heroes
        addHero("Ascended Alpha",new Hero(Creature.Element.FIRE,46,174,Hero.Rarity.ASCENDED,65,new ScaleableAOE(null,1,3.3)));
        addHero("Ascended Athos",new Hero(Creature.Element.EARTH,60,162,Hero.Rarity.ASCENDED,66,new ScaleableStatBoost(null,0,1,null,3.3)));
        addHero("Ascended Rei",new Hero(Creature.Element.AIR,104,120,Hero.Rarity.ASCENDED,67,new ScaleableStatBoost(null,1,0,null,3.3)));
        addHero("Ascended Auri",new Hero(Creature.Element.WATER,78,148,Hero.Rarity.ASCENDED,68,new ScaleableHeal(null,1,6.58)));
        addHero("Ascended TR0N1X",new Hero(Creature.Element.FIRE,38,190,Hero.Rarity.ASCENDED,69,new Ricochet(null,0.75,5)));
        addHero("Ascended Geum",new Hero(Creature.Element.EARTH,8,222,Hero.Rarity.ASCENDED,70,new MultiplyAttack(null,2)));
        addHero("Ascended Geror",new Hero(Creature.Element.AIR,116,116,Hero.Rarity.ASCENDED,71,new MonsterBuff(null,1.3)));
        addHero("Ascended Pontus",new Hero(Creature.Element.WATER,86,150,Hero.Rarity.ASCENDED,81,new Purity(null,3)));
        addHero("Ascended Atzar",new Hero(Creature.Element.FIRE,81,162,Hero.Rarity.ASCENDED,82,new Purity(null,3)));
        addHero("Ascended Rigr",new Hero(Creature.Element.EARTH,99,141,Hero.Rarity.ASCENDED,86,new Purity(null,3)));
        addHero("Ascended Dagda",new Hero(Creature.Element.AIR,107,135,Hero.Rarity.ASCENDED,92,new Purity(null,3)));
        addHero("Ascended Bavah",new Hero(Creature.Element.WATER,122,122,Hero.Rarity.ASCENDED,100,new ScaleableStatBoost(null,1,1,null,6.58)));
        
        //quest heroes
        addHero("Valor",new Hero(Creature.Element.AIR,10,20,Hero.Rarity.COMMON,3,new StatBoost(null,0,1,Creature.Element.AIR)));
        addHero("Rokka",new Hero(Creature.Element.EARTH,8,30,Hero.Rarity.COMMON,4,new StatBoost(null,0,1,Creature.Element.EARTH)));
        addHero("Pyromancer",new Hero(Creature.Element.FIRE,12,24,Hero.Rarity.COMMON,5,new StatBoost(null,0,1,Creature.Element.FIRE)));
        addHero("Bewat",new Hero(Creature.Element.WATER,6,50,Hero.Rarity.COMMON,6,new StatBoost(null,0,1,Creature.Element.WATER)));
        addHero("Nicte",new Hero(Creature.Element.AIR,32,22,Hero.Rarity.RARE,19,new StatBoost(null,4,0,Creature.Element.AIR)));
        addHero("Forest Druid",new Hero(Creature.Element.EARTH,16,46,Hero.Rarity.RARE,27,new StatBoost(null,4,0,Creature.Element.EARTH)));
        addHero("Ignitor",new Hero(Creature.Element.FIRE,24,32,Hero.Rarity.RARE,28,new StatBoost(null,4,0,Creature.Element.FIRE)));
        addHero("Undine",new Hero(Creature.Element.WATER,14,58,Hero.Rarity.RARE,29,new StatBoost(null,4,0,Creature.Element.WATER)));
        addHero("Chroma",new Hero(Creature.Element.AIR,20,52,Hero.Rarity.RARE,39,new StatBoost(null,0,4,Creature.Element.AIR)));
        addHero("Petry",new Hero(Creature.Element.EARTH,44,26,Hero.Rarity.RARE,40,new StatBoost(null,0,4,Creature.Element.EARTH)));
        addHero("Zaytus",new Hero(Creature.Element.FIRE,22,58,Hero.Rarity.RARE,41,new StatBoost(null,0,4,Creature.Element.FIRE)));
        addHero("Lady Odelith",new Hero(Creature.Element.WATER,36,36,Hero.Rarity.RARE,54,new StatBoost(null,0,4,Creature.Element.WATER)));
        addHero("Shygu",new Hero(Creature.Element.AIR,54,34,Hero.Rarity.LEGENDARY,55,new ScaleableStatBoost(null,0,1,Creature.Element.AIR,9)));
        addHero("Thert",new Hero(Creature.Element.EARTH,28,72,Hero.Rarity.LEGENDARY,56,new ScaleableStatBoost(null,0,1,Creature.Element.EARTH,9)));
        addHero("Lord Kirk",new Hero(Creature.Element.FIRE,64,32,Hero.Rarity.LEGENDARY,57,new ScaleableStatBoost(null,0,1,Creature.Element.FIRE,9)));
        addHero("Neptunius",new Hero(Creature.Element.WATER,70,30,Hero.Rarity.LEGENDARY,58,new ScaleableStatBoost(null,0,1,Creature.Element.WATER,9)));
        addHero("Hosokawa",new Hero(Creature.Element.AIR,50,42,Hero.Rarity.LEGENDARY,88,new ScaleableStatBoost(null,1,0,Creature.Element.AIR,9)));
        addHero("Takeda",new Hero(Creature.Element.EARTH,66,32,Hero.Rarity.LEGENDARY,89,new ScaleableStatBoost(null,1,0,Creature.Element.EARTH,9)));
        addHero("Hirate",new Hero(Creature.Element.FIRE,56,38,Hero.Rarity.LEGENDARY,90,new ScaleableStatBoost(null,1,0,Creature.Element.FIRE,9)));
        addHero("Hattori",new Hero(Creature.Element.WATER,48,44,Hero.Rarity.LEGENDARY,91,new ScaleableStatBoost(null,1,0,Creature.Element.WATER,9)));
        addHero("Mahatma",new Hero(Creature.Element.AIR,26,78,Hero.Rarity.LEGENDARY,114,new ElementDamageBoost(null,0.75)));
        addHero("Jade",new Hero(Creature.Element.EARTH,30,76,Hero.Rarity.LEGENDARY,115,new ElementDamageBoost(null,0.75)));
        addHero("Edana",new Hero(Creature.Element.FIRE,36,72,Hero.Rarity.LEGENDARY,116,new ElementDamageBoost(null,0.75)));
        addHero("Dybbuk",new Hero(Creature.Element.WATER,30,80,Hero.Rarity.LEGENDARY,117,new ElementDamageBoost(null,0.75)));
        addHero("Ascended Shygu",new Hero(Creature.Element.AIR,135,85,Hero.Rarity.ASCENDED,118,new ScaleableStatBoost(null,0,1,Creature.Element.AIR,5.5)));
        addHero("Ascended Thert",new Hero(Creature.Element.EARTH,70,180,Hero.Rarity.ASCENDED,119,new ScaleableStatBoost(null,0,1,Creature.Element.EARTH,5.5)));
        addHero("Ascended Lord Kirk",new Hero(Creature.Element.FIRE,160,80,Hero.Rarity.ASCENDED,120,new ScaleableStatBoost(null,0,1,Creature.Element.FIRE,5.5)));
        addHero("Ascended Neptunius",new Hero(Creature.Element.WATER,175,75,Hero.Rarity.ASCENDED,121,new ScaleableStatBoost(null,0,1,Creature.Element.WATER,5.5)));
        addHero("Ascended Hosokawa",new Hero(Creature.Element.AIR,124,106,Hero.Rarity.ASCENDED,122,new ScaleableStatBoost(null,1,0,Creature.Element.AIR,5.5)));
        addHero("Ascended Takeda",new Hero(Creature.Element.EARTH,164,82,Hero.Rarity.ASCENDED,123,new ScaleableStatBoost(null,1,0,Creature.Element.EARTH,5.5)));
        addHero("Ascended Hirate",new Hero(Creature.Element.FIRE,144,96,Hero.Rarity.ASCENDED,124,new ScaleableStatBoost(null,1,0,Creature.Element.FIRE,5.5)));
        addHero("Ascended Hattori",new Hero(Creature.Element.WATER,126,114,Hero.Rarity.ASCENDED,125,new ScaleableStatBoost(null,1,0,Creature.Element.WATER,5.5)));
        
        //season heroes
        addHero("Veildur",new Hero(Creature.Element.EARTH,44,66,Hero.Rarity.LEGENDARY,33,new StatBoost(null,3,3,null)));
        addHero("Brynhildr",new Hero(Creature.Element.AIR,48,72,Hero.Rarity.LEGENDARY,34,new StatBoost(null,4,4,null)));
        addHero("Groth",new Hero(Creature.Element.FIRE,52,78,Hero.Rarity.LEGENDARY,35,new StatBoost(null,5,5,null)));
        addHero("Zeth",new Hero(Creature.Element.WATER,42,70,Hero.Rarity.LEGENDARY,48,new Revenge(null,0.1)));
        addHero("Koth",new Hero(Creature.Element.EARTH,46,76,Hero.Rarity.LEGENDARY,49,new Revenge(null,0.15)));
        addHero("Gurth",new Hero(Creature.Element.AIR,50,82,Hero.Rarity.LEGENDARY,50,new Revenge(null,0.2)));
        addHero("Sigrun",new Hero(Creature.Element.FIRE,12,65,Hero.Rarity.LEGENDARY,59,new Ricochet(null,0.5,5)));
        addHero("Koldis",new Hero(Creature.Element.WATER,14,70,Hero.Rarity.LEGENDARY,60,new Ricochet(null,0.5,5)));
        addHero("Alvitr",new Hero(Creature.Element.EARTH,16,75,Hero.Rarity.LEGENDARY,61,new Ricochet(null,0.5,5)));
        addHero("Arshen",new Hero(Creature.Element.AIR,36,74,Hero.Rarity.LEGENDARY,83,new Ricochet(null,1,1)));
        addHero("Rua",new Hero(Creature.Element.FIRE,40,78,Hero.Rarity.LEGENDARY,84,new Ricochet(null,1,1)));
        addHero("Dorth",new Hero(Creature.Element.WATER,44,82,Hero.Rarity.LEGENDARY,85,new Ricochet(null,1,1)));
        addHero("Kumu san", new Hero(Creature.Element.FIRE,38,70,Hero.Rarity.LEGENDARY,103,new Reflect(null,0.2)));
        addHero("Liu Cheng", new Hero(Creature.Element.WATER,42,78,Hero.Rarity.LEGENDARY,104,new Reflect(null,0.25)));
        addHero("Hidoka", new Hero(Creature.Element.EARTH,44,86,Hero.Rarity.LEGENDARY,105,new Reflect(null,0.3)));
        addHero("Master Lee", new Hero(Creature.Element.AIR,90,150,Hero.Rarity.ASCENDED,102,new Reflect(null,0.5)));
        
        //holiday heroes
        addHero("James",new Hero(Creature.Element.EARTH,12,50,Hero.Rarity.LEGENDARY,20,new Ricochet(null,0.75,5)));
        addHero("Werewolf",new Hero(Creature.Element.EARTH,25,35,Hero.Rarity.COMMON,51,new ScaleableStatBoost(null,0,1,null,9)));
        addHero("Jack'o Knight",new Hero(Creature.Element.AIR,35,55,Hero.Rarity.RARE,52,new ScaleableStatBoost(null,1,0,null,9)));
        addHero("Dullahan",new Hero(Creature.Element.FIRE,45,75,Hero.Rarity.LEGENDARY,53,new ScaleableStatBoost(null,1,1,null,9)));
        addHero("Christmas Elf",new Hero(Creature.Element.WATER,24,38,Hero.Rarity.COMMON,73,new ScaleableHeal(null,1,9)));
        addHero("Reindeer",new Hero(Creature.Element.AIR,36,54,Hero.Rarity.RARE,74,new ScaleableAOE(null,1,9)));
        addHero("Santa Claus",new Hero(Creature.Element.FIRE,48,72,Hero.Rarity.LEGENDARY,75,new ScaleableLifeSteal(null,1,9)));
        addHero("Sexy Santa",new Hero(Creature.Element.EARTH,44,44,Hero.Rarity.RARE,76,new Ricochet(null,0.66,5)));
        addHero("Leprechaun", new Hero(Creature.Element.EARTH,25,75,Hero.Rarity.LEGENDARY,96,new OutnumberedPercentDamage(null)));
        addHero("Sparks",new Hero(Creature.Element.FIRE,30,30,Hero.Rarity.COMMON,97,new StatLevelBoost(null,2)));
        addHero("Leaf",new Hero(Creature.Element.EARTH,42,48,Hero.Rarity.RARE,98,new StatLevelBoost(null,2)));
        addHero("Flynn",new Hero(Creature.Element.AIR,48,70,Hero.Rarity.LEGENDARY,99,new StatLevelBoost(null,2)));
        addHero("Billy",new Hero(Creature.Element.EARTH,40,30,Hero.Rarity.COMMON,127,new DeathStrike(null,100)));
        addHero("Sanqueen",new Hero(Creature.Element.WATER,20,88,Hero.Rarity.RARE,128,new Leech(null,0.8)));
        addHero("Cliodhna",new Hero(Creature.Element.AIR,60,150,Hero.Rarity.LEGENDARY,129,new Evolve(null,1.0)));
        
        //other heroes
        addHero("Lady of Twilight",new Hero(Creature.Element.AIR,20,45,Hero.Rarity.COMMON,0,new StatBoost(null,3,3,null)));
        addHero("Tiny",new Hero(Creature.Element.EARTH,30,70,Hero.Rarity.RARE,1,new ScaleableLifeSteal(null,1,24)));
        addHero("Nebra",new Hero(Creature.Element.FIRE,40,110,Hero.Rarity.LEGENDARY,2,new StatBoost(null,20,0,null)));
        addHero("Bubbles",new Hero(Creature.Element.WATER,110,300,Hero.Rarity.ASCENDED,80,new ScaleableAntiAOE(null,0.005)));
        addHero("Dr Hawking", new Hero(Creature.Element.AIR,60,66,Hero.Rarity.LEGENDARY,101,new ScaleableStartingDamage(null,1,1)));
        addHero("Neil", new Hero(Creature.Element.WATER,15,150,Hero.Rarity.LEGENDARY,113,new Intercept(null,0.3)));
        addHero("Dicemaster", new Hero(Creature.Element.WATER,26,25,Hero.Rarity.COMMON,107,new RandomStatBoost(null,20)));
        addHero("Luxurius Maximus", new Hero(Creature.Element.FIRE,60,28,Hero.Rarity.RARE,108,new RandomTarget(null)));
        addHero("Pokerface", new Hero(Creature.Element.EARTH,70,70,Hero.Rarity.LEGENDARY,109,new CriticalHit(null,3)));
        
        //developer heroes
        addHero("Spyke",new Hero(Creature.Element.AIR,45,75,Hero.Rarity.LEGENDARY,42,new AddAttack(null,10)));
        addHero("Aoyuki",new Hero(Creature.Element.WATER,55,70,Hero.Rarity.LEGENDARY,43,new Rainbow(null,100,4)));
        addHero("GaiaByte",new Hero(Creature.Element.EARTH,150,75,Hero.Rarity.LEGENDARY,44,new Wither(null,2)));
        
    }
    
    private void initiateWorldBosses() {
        //currentID = 0;
        worldBosses = new HashMap<>();
        worldBossNames = new ArrayList<>();
        
        addWorldBoss("Lord of Chaos",new WorldBoss(Creature.Element.FIRE,73,72,new AOE(null,20)));
        addWorldBoss("Mother of all Kodamas",new WorldBoss(Creature.Element.EARTH,125,87,new AntiAOE(null,0.5)));
        addWorldBoss("Kryton",new WorldBoss(Creature.Element.AIR,11,106,new AddAttack(null,10)));
        addWorldBoss("Doyenne",new WorldBoss(Creature.Element.WATER,110,126,new DamageOverflow(null,5000)));
    }
    
    private void addMonster(String name, int elementNum, int tier, Monster monster){
        monsterNames.add(name);
        monsters[elementNum][tier-1] = monster;
        IDToNameMap.put(monster.getID(),name);
        //monster.setID(currentID);//change to adding IDs directly in constructor for new seeded random heroes?
        //currentID ++;
    }
    
    private void addHero(String name, Hero hero){
        hero.attatchSpecialAbility();
        hero.levelUp(1);
        heroes.put(name,hero);
        heroNames.add(name);
        IDToNameMap.put(hero.getID(),name);
        //hero.setID(currentID);//disable once ids are in
        //currentID ++;
    }

    
    
    private void addWorldBoss(String name, WorldBoss wb){
        wb.attatchSpecialAbility();
        worldBosses.put(name,wb);
        worldBossNames.add(name);
        IDToNameMap.put(wb.getID(),name);
        //wb.setID(currentID);//hash map for names***
        //currentID ++;
    }
    
    public static BufferedImage getPicture(String address){
        if (instance == null){
            instance = new CreatureFactory();
        }
        BufferedImage img = instance.pictures.get("pictures/" + address + ".png");
        if (img == null){
            return instance.defaultImage;
        }
        else{
            return img;
        }
    }
    
    public static BufferedImage getDefaultImage(){
        if (instance == null){
            instance = new CreatureFactory();
        }
        return instance.defaultImage;
    }

    private void initiatePictures() {
        pictures = new HashMap<>();
        
        addDefaultImage();
        
        attemptToAddPictures("pictures/Creatures/Monsters");
        attemptToAddPictures("pictures/Creatures/Heroes");
        attemptToAddPictures("pictures/Creatures/World Bosses");
        attemptToAddPictures("pictures/Stands");
        attemptToAddPictures("pictures/Backgrounds");
        attemptToAddPictures("pictures/Others");
    }
    
    private void attemptToAddPictures(String directory){
        File[] files = new File(directory).listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null. 

        for (File file : files) {
            if (file.isFile()) {
                attemptToAddPicture(directory + "/" + file.getName());
            }
        }
    }
    
    
    
    private void attemptToAddPicture(String address){
        try {
            pictures.put(address, ImageIO.read(new File(address)));
        } catch (IOException ex) {
            
        }
    }
    
    
    private void addDefaultImage(){
        try {
            defaultImage = ImageIO.read(new File("pictures/Default Image.png"));
        } catch (IOException ex) {
            Logger.getLogger(CreatureFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean drawFuriousAura(Hero h){
        String name = h.getName();
        if (name.equals("Ascended Shygu") || 
                name.equals("Ascended Thert") || 
                name.equals("Ascended Lord Kirk") || 
                name.equals("Ascended Neptunius") || 
                name.equals("Ascended Hosokawa") || 
                name.equals("Ascended Takeda") || 
                name.equals("Ascended Hirate") || 
                name.equals("Ascended Hattori")){
            return true;
        }
        else{
            return false;
        }
    }
    
}
