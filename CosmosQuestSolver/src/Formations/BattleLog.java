/*

 */
package Formations;

import static Formations.Formation.doBattlePrep;
import static Formations.Formation.doOneRound;
import java.util.LinkedList;


public class BattleLog {
    
    private LinkedList<BattleState> states;
    
    public BattleLog(Formation thisFormation, Formation enemyFormation) {
        states = new LinkedList<>();
        
        doBattlePrep(thisFormation,enemyFormation);
        
        int roundNumber = 0;
        states.add(new BattleState(thisFormation.getCopy(),enemyFormation.getCopy(),roundNumber));
        
        while(!(thisFormation.isEmpty() || enemyFormation.isEmpty()) && roundNumber < Formation.STALEMATE_CUTOFF_POINT){
            roundNumber ++;
            doOneRound(thisFormation,enemyFormation);
            states.add(new BattleState(thisFormation.getCopy(),enemyFormation.getCopy(),roundNumber));
        }
        
    }
    
    public BattleLog(){
        this(new Formation(),new Formation());
    }
    
    public BattleState getState(int i){
        return states.get(i);
    }

    public int length() {
        return states.size();
    }
    
    
}
