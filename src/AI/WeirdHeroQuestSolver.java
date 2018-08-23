/*

 */
package AI;

import Formations.Creature;
import Formations.Hero;
import GUI.QuestSolverFrame;
import cosmosquestsolver.OtherThings;
import java.util.Iterator;
import java.util.LinkedList;

//uses composition to search for solution including those without the maximum number of followers
public class WeirdHeroQuestSolver extends AISolver{// if more than one hero, solve higher up? currently broken because of prioritizeHeroes
    
    //private String weirdHeroName;//use ID instead?
    private QuestSolverFrame frame;
    private Hero weirdHero;
    private LinkedList<SpecialQuestSolver> solverList;
    
    public WeirdHeroQuestSolver(QuestSolverFrame frame, String weirdHeroName){//parameter for certain heroes? Or make specifically for one hero (leprechaun)?
        this.frame = frame;
        
        //make sure weird hero is not null. get from frame
        weirdHero = frame.getHero(weirdHeroName);
    }
    
    @Override
    protected void search() {
        solverList = new LinkedList<>();
        for (int i = frame.getMaxCreatures(); i > 0; i--){
            SpecialQuestSolver solver = new SpecialQuestSolver(frame,this,i,weirdHero);
            solverList.add(solver);
            new Thread(solver).start();
        }
        
        
    }
    
    @Override
    public void stopSearching() {
        for (SpecialQuestSolver solver: solverList){
            solver.stopSearching();
        }
        
        searching = false;
    }
    
}
