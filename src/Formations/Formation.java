/*

 */
package Formations;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

//holds a list of creatures representing a battle line. Also simulates battles
public class Formation implements Iterable<Creature>{

    
    
    private long totalDamageTaken = 0;// for tiebreakers in pvp
    public static final int MAX_MEMBERS = 6;//current max creatures possible in one line
    private LinkedList<Creature> members;
    private double AOEResistance = 0;//1 is invincible
    
    //in rare circumstances, armor might outweigh attack power, resulting in an
    //infinite loop. After a set amount of rounds, end the battle
    public static final int STALEMATE_CUTOFF_POINT = 100;
    private long seed = -1;//used for random skills. seed should be positive. if seed is needed, generate it
    
    private static final boolean DEBUG = false;//prints info when fighting
    
    public static enum VictoryCondition{WIN,DRAW,LOSE};
    
    //empty formation
    public Formation(){
        members = new LinkedList<>();
    }
    
    public Formation(List<Creature> creatures){
        
        members = new LinkedList<>();
        
        for (Creature creature: creatures){
            members.add(creature);
        }
    }
    
    public Formation getCopy() {
        LinkedList list = new LinkedList<>();
        
        for(Creature c : members){
            list.add(c.getCopy());
        }
        Formation f = new Formation(list);
        f.totalDamageTaken = totalDamageTaken;
        f.AOEResistance = AOEResistance;
        return f;
    }
    
    public LinkedList<Creature> getMembers() {
        return members;
    }
    
    public long getSeed() {
        if (seed == -1){//lazy evaluation
            seed = generateSeed();
        }
        return seed;
    }
    
    private long generateSeed(){
        long seed = 1;
        for (int i = 0; i < members.size(); i++){
            seed = seed * Math.abs(members.get(i).getID()) + 1;
        }
        return seed;
    }
    
    public long getTurnSeed(Formation otherFormation, int turnNumber){
        long ans = (otherFormation.getSeed() + (long)Math.pow(101-turnNumber,3)) % ((int)Math.round((double)otherFormation.getSeed()/(101 - turnNumber) + (101 - turnNumber)*(101 - turnNumber)));
        return Math.abs(ans);
    }
    
    public void addDamageTaken(long hit) {
        totalDamageTaken += hit;
    }
    
    public long getDamageTaken(){
        return totalDamageTaken;
    }
    
    
    public void attack(Formation enemyFormation){
        members.getFirst().attack(this,enemyFormation);//only the first creature damages foes directly
    }
    
    public Creature getFrontCreature(){
        try{//is this needed?
            return members.getFirst();
        }
        catch (NoSuchElementException e){
            return null;
        }
    }
    
    public void addCreature(Monster m) {
        if (members.size() < MAX_MEMBERS){
            members.add(m);
        }
    }
    
    public Creature getEntry(int i) {
        try{
            return members.get(i);
        }
        catch (Exception e){
            return null;
        }
    }
    
    public boolean isEmpty() {
        return members.isEmpty();
    }
    
    public boolean containsMonsters(){
        for (Creature creature : members){
            if (creature instanceof Monster){
                return true;
            }
        }
        return false;
    }
    
    //for identifying repeat formations in the combination iterator.
    //there can be more than one of each monster, but the combination
    //iterator treats the same type of monsters as different creatures
    public boolean containsSameCreatures(Formation formation) {
        if (members.size() != formation.members.size()){
            return false;
        }
        LinkedList<Creature> thisList = getMembers();
        LinkedList<Creature> otherList = formation.getMembers();
        
        Collections.sort(thisList);
        Collections.sort(otherList);
        for (int i = 0; i < thisList.size(); i++){
            if (!thisList.get(i).isSameCreature(otherList.get(i))){
                return false;
            }
        }
        return true;
        
        
    }
    
    //returns true if the formation contains two or more of the same creature
    public boolean contains(Creature c) {
        for (Creature creature : members){
            if (c.getClass() == creature.getClass() && c.getID() == creature.getID()){
                return true;
            }
        }
        return false;
    }
    
    //returns a list of all monsters in the formation
    public LinkedList<Monster> getMonsters(){
        LinkedList<Monster> list = new LinkedList<>();
        for (Creature creature : members){
            if (creature instanceof Monster){
                Monster m = (Monster)creature;
                list.add(m);
            }
        }
        return list;
    }

    
    
    //returns the game's definition of a formation's strength
    //the sum of each creature's strength
    public int strength(){
        int sum = 0;
        for (Creature creature : members){
            sum += creature.strength();
        }
        return sum;
    }
    
    public void prepareForFight(Formation enemyFormation){
       for (Creature creature : members){
            creature.prepareForFight(this,enemyFormation);
        } 
    }
    
    //activates each creature's special ability's start of fight action
    public void startOfFightAction(Formation enemyFormation){
        for (Creature creature : members){
            creature.startOfFightAction(this,enemyFormation);
        }
    }
    
    //activates each creature's special ability's start of fight action
    public void startOfFightAction2(Formation enemyFormation){
        for (Creature creature : members){
            creature.startOfFightAction2(this,enemyFormation);
        }
    }
    
    
    public void preRoundAction(Formation enemyFormation){
        for (int i = members.size() - 1; i >= 0; i--){
            members.get(i).preRoundAction(this,enemyFormation);
        }
    }
    
    public void postRoundAction(Formation enemyFormation){
        for (int i = members.size() - 1; i >= 0; i--){
            members.get(i).postRoundAction(this,enemyFormation);
        }
    }
    
    public void postRoundAction2(Formation enemyFormation){//healing
        for (int i = members.size() - 1; i >= 0; i--){
            members.get(i).postRoundAction2(this,enemyFormation);
        }
    }
    
    // at the end of each round, delete dead creatures at the front of the formation
    // the creatures behind, if any, will take their place at the front
    public void handleCreatureDeaths(Formation enemyFormation){
        for (Creature creature : members){
            if (creature.isDead()){
                creature.actionOnDeath(this,enemyFormation);
            }
        }
        
        while(getFrontCreature()!= null && getFrontCreature().isDead()){
            
            members.removeFirst();
        }
    }
    
    public void takeHit(Creature attacker, Formation enemyFormation) {
        takeHit(attacker,enemyFormation,0);//creature in front usually takes the main hit
    }
    
    public void takeHit(Creature attacker, Formation enemyFormation,int position) {//position: which creature to take the hit
        double hit = attacker.determineDamage(members.get(position),this,enemyFormation);//check for array out of bounds?
        hit = alterIncomingDamage(hit,enemyFormation);//if abilities ever have %reduction armor, do it either ^ or V depending on what gets reduced first. prefer ^
        members.get(position).takeHit(attacker,this,enemyFormation,hit);
    }
    
    private double alterIncomingDamage(double hit, Formation enemyFormation){
        double startingDamage = hit;
        for (Creature c : members){
            hit = c.getSpecialAbility().alterIncomingDamage(hit,startingDamage,this,enemyFormation);
        }
        return hit;
    }
    
    public void takeAOEDamage(double damage){
        double newDamage = damage * (1 - AOEResistance);//test***
        for (Creature creature : members){
            creature.takeAOEDamage(newDamage,this);
        }
    }
    
    public void AOEHeal(double amount, Formation enemyFormation){//anti AOE rounding?
        double newAmount = amount * (1 - enemyFormation.getAOEResistance());
        for (Creature creature : members){
            creature.changeHP(newAmount,this);
        }
    }
    
    public double getAOEResistance() {
        return AOEResistance;
    }
    
    public void setAOEResistance(double m){
        AOEResistance = m;
    }

    @Override
    public Iterator<Creature> iterator() {
        return new FormationIterator();
    }
    
    public void setFacingRight(boolean facingRight) {
        for (Creature creature : members){
            creature.setFacingRight(facingRight);
        }
    }

    public int size() {
        return members.size();
    }

    //for iterating each creature inf the formation
    private class FormationIterator implements Iterator<Creature>{
        
        Iterator<Creature> iterator;
        
        public FormationIterator(){
            iterator = members.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Creature next() {
            return iterator.next();
        }
        
    }
    
    
    
    
    public static VictoryCondition determineOutcome(Formation thisFormation, Formation enemyFormation){//should these be in Formation class?
        battle(thisFormation,enemyFormation);
        
        boolean thisDead = thisFormation.isEmpty();
        boolean enemyDead = enemyFormation.isEmpty();
        
        if (thisDead && !enemyDead){
            return VictoryCondition.LOSE;
        }
        else if (thisDead && enemyDead){
            return VictoryCondition.DRAW;
        }
        else if (!thisDead && enemyDead){
            return VictoryCondition.WIN;
        }
        else{//if both sides still have followers, something went wrong
            return VictoryCondition.DRAW;//damage based win?
        }
    }
    
    public static int wonInPvP(Formation thisFormation, Formation enemyFormation){//0 for lose, 2 for win, 1 for tie
        battle(thisFormation,enemyFormation);
        
        boolean thisDead = thisFormation.isEmpty();
        boolean enemyDead = enemyFormation.isEmpty();
        
        
        if (thisDead && !enemyDead){
            return 0;
        }
        else if (thisDead && enemyDead){
            if (thisFormation.getDamageTaken() < enemyFormation.getDamageTaken()){
                return 2;
            }
            else return 0;
        }
        else if (!thisDead && enemyDead){
            return 2;
        }
        else{//if there's a stalemate
            return 1;//damage based win?
        }
    }
    
    public static boolean passed(Formation thisFormation, Formation enemyFormation){//for quests, where draws don't count
        VictoryCondition v = determineOutcome(thisFormation,enemyFormation);
        return v == VictoryCondition.WIN;
    }
    
    public static long damageDealt(Formation thisFormation, Formation enemyFormation){//for world bosses
        battle(thisFormation,enemyFormation);
        return enemyFormation.getDamageTaken();
    }
    
    
    //primary method for simulating battles
    public static void battle(Formation thisFormation, Formation enemyFormation){
        thisFormation.prepareForFight(enemyFormation);
        enemyFormation.prepareForFight(thisFormation);
        thisFormation.startOfFightAction(enemyFormation);
        enemyFormation.startOfFightAction(thisFormation);
        thisFormation.startOfFightAction2(enemyFormation);
        enemyFormation.startOfFightAction2(thisFormation);
        
        int roundNumber = 0;
        
        if (DEBUG){
            System.out.println(thisFormation);
        }
        
        while(!(thisFormation.isEmpty() || enemyFormation.isEmpty()) && roundNumber < STALEMATE_CUTOFF_POINT){
            roundNumber ++;
            doOneRound(thisFormation,enemyFormation);
            if (DEBUG){
                System.out.println(thisFormation);
            }
        }
    }
    
    public static void doOneRound(Formation thisFormation, Formation enemyFormation){
        thisFormation.preRoundAction(enemyFormation);
        enemyFormation.preRoundAction(thisFormation);
        
        thisFormation.attack(enemyFormation);
        enemyFormation.attack(thisFormation);
        
        thisFormation.postRoundAction(enemyFormation);
        enemyFormation.postRoundAction(thisFormation);
        
        thisFormation.postRoundAction2(enemyFormation);//for healing
        enemyFormation.postRoundAction2(thisFormation);
        
        thisFormation.handleCreatureDeaths(enemyFormation);
        enemyFormation.handleCreatureDeaths(thisFormation);
    }
    
    public static LinkedList<BattleState> getBattleStates(Formation thisFormation, Formation enemyFormation) {
        LinkedList<BattleState> stats = new LinkedList<>();
        
        thisFormation.prepareForFight(enemyFormation);
        enemyFormation.prepareForFight(thisFormation);
        thisFormation.startOfFightAction(enemyFormation);
        enemyFormation.startOfFightAction(thisFormation);
        thisFormation.startOfFightAction2(enemyFormation);
        enemyFormation.startOfFightAction2(thisFormation);
        
        int roundNumber = 0;
        stats.add(new BattleState(thisFormation.getCopy(),enemyFormation.getCopy(),roundNumber));
        
        while(!(thisFormation.isEmpty() || enemyFormation.isEmpty()) && roundNumber < STALEMATE_CUTOFF_POINT){
            roundNumber ++;
            doOneRound(thisFormation,enemyFormation);
            stats.add(new BattleState(thisFormation.getCopy(),enemyFormation.getCopy(),roundNumber));
        }
        
        //if (thisFormation.isEmpty() || enemyFormation.isEmpty()){
            //stats.add(new BattleState(thisFormation.getCopy(),enemyFormation.getCopy()));
        //}
        
        return stats;
    }
    
    //prints a text log of the battle for debugging
    private static void printBattleStatus(Formation thisFormation, Formation enemyFormation, int roundNumber){
        if (roundNumber == 0){
            System.out.println("Start");
        }
        else{
            System.out.println("After Round " + roundNumber + ":");
        }
        System.out.println("    Left Formation\n" + thisFormation);
        System.out.println("    Right Formation\n" + enemyFormation);
        System.out.println("\n\n");
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("\t___________________________________________________________________\n");
        for (Creature creature : members){
            sb.append("\t").append(creature.toString()).append("\n");
        }
        sb.append("\t___________________________________________________________________");
        
        return sb.toString();
    }
    
}
