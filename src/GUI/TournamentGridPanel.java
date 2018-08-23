/*

 */
package GUI;

import AI.TournamentOptimizer;
import Formations.Formation;
import Formations.TournamentGrid;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

// displays a solution given by TournamentOptimizer
public class TournamentGridPanel extends JPanel{
    
    private TournamentOptimizerFrame frame;
    private int numRows;
    // field for columns if the game ever restricts creatures per row and hides the columns*
    
    private TournamentGrid grid;
    
    public SolutionFormationPanel[] rows;

    public TournamentGridPanel(TournamentOptimizerFrame frame, int numRows) {
        this.frame = frame;
        
        this.numRows = numRows;
        rows = new SolutionFormationPanel[numRows];
        
        createGrid();
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setOpaque(false);
        
    }
    
    public void setRows(int newNumRows){
        numRows = newNumRows;
        rows = new SolutionFormationPanel[newNumRows];
        createGrid();
    }
    
    private void createGrid(){
        removeAll();
        for (int i = 0; i < rows.length; i++){
            rows[i] = new SolutionFormationPanel(true);
            rows[i].setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
            rows[i].setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
            rows[i].setMinimumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
            add(rows[i]);
        }
        
        setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE * numRows));
        setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE * numRows));
        setMinimumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE * numRows));
        revalidate();
        repaint();
    }
    
    
    
    

    public void recieveGrid(TournamentGrid g) {
        if (g == grid){
            return;// same grid, no point updating
        }
        
        this.grid = g;
        int index = 0;
        for (Formation f : g.getFormations()){
            if (index >= numRows){
                break;
            }
            rows[index].updateFormation(f);
            index ++;
        }
        
        //clear any unused rows
        while(index < numRows){
            rows[index].updateFormation(new Formation());
            index ++;
        }
        
        
        repaint();
    }
    
    public void resetGrid(){
        for (SolutionFormationPanel panel : rows){
            panel.updateFormation(new Formation());
        }
    }
    
}
