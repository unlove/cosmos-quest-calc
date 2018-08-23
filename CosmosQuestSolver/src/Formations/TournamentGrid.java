/*

 */
package Formations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;


public class TournamentGrid {
    
    private LinkedList<Formation> formations;
    
    
    public TournamentGrid(){
        formations = new LinkedList();
    }
    
    public TournamentGrid(LinkedList<Formation> formations){//argument should be a copy
        this.formations = formations;
    }
    
    public TournamentGrid getCopy(){
        TournamentGrid copy = new TournamentGrid();
        for (Formation f : formations){
            copy.addFormation(f.getCopy());
        }
        return copy;
    }
    
    public LinkedList<Formation> getFormations(){
        return formations;
    }
    
    public void setFormations(LinkedList<Formation> formations){
        this.formations = formations;
    }
    
    public void addFormation(Formation f){
        formations.add(f);
    }
    
    public int numFormations(){
        return formations.size();
    }
    
    //returns the total win points (win = 2, draw = 1, lose = 0) of the otherGrid's formations vs. all of a given
    //grid's formations. all combinations are tested.
    public int numWins(TournamentGrid otherGrid){
        int numWins = 0;
        //all grids vs all other grids
        for (Formation thisFormation : formations){
            for (Formation enemyFormation : otherGrid.formations){
                numWins += Formation.wonInPvP(thisFormation.getCopy(), enemyFormation.getCopy());
            }
        }

        
        //System.out.println(numWins);
        return numWins;
    }
    
    public int bestOfWin(TournamentGrid otherGrid){
        //each row fights agains the corrisponding row
        int numRows = formations.size() < otherGrid.formations.size() ? formations.size() : otherGrid.formations.size();//minimum
        int wins = 0;
        int losses = 0;
        long thisDamageTaken = 0;
        long enemyDamageTaken = 0;
        for (int i = 0; i < numRows; i ++){
            Formation leftSide = formations.get(i).getCopy();
            Formation rightSide = otherGrid.formations.get(i).getCopy();
            Formation.VictoryCondition winPoint = Formation.determineOutcome(leftSide, rightSide);
            switch (winPoint){
                case WIN: wins ++; break;
                case LOSE: losses ++; break;
                default://number of ties don't matter
            }
            thisDamageTaken += leftSide.getDamageTaken();
            enemyDamageTaken += rightSide.getDamageTaken();     
        }
        if (wins > losses){
            return 1;
        }
        else if (wins < losses){
            return 0;
        }
        else{//damage done will break a tie
            if (thisDamageTaken < enemyDamageTaken){
                return 1;
            }
            else{
                return 0;
            }
        }
    }
    
    public void shuffleRows() {
        Collections.shuffle(formations);
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("___________________________________________________________________\n");
        for (Formation formation : formations){
            sb.append(formation.toString()).append("\n");
        }
        sb.append("___________________________________________________________________");
        
        return sb.toString();
    }

    
    
}
