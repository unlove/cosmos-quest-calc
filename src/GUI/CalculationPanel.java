/*

 */
package GUI;

import AI.AISolver;
import Formations.Creature;
import Formations.Formation;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

// gui for controlling when the solver starts and stops. also has a button to
// go back to the main menu
public class CalculationPanel extends JPanel implements ActionListener{
    
    private ISolverFrame frame;
    AISolver solver;
    
    private JPanel buttonAndInfoPanel;
    private JPanel buttonPanel;
    private JButton findButton;
    private JButton stopSearchButton;
    private JButton backButton;
    private JLabel searchingLabel;
    private JLabel messageLabel;
    private int resultStatus;// -1: no solution, 0: searching, 1: found solution. polling to change labels because they weren't updating with commands
    private ChatBox entireListChatBox;
    
    
    private Timer timer;
    
    public CalculationPanel(ISolverFrame frame){
        this.frame = frame;
        resultStatus = 0;
        
        solver = frame.makeSolver();
        
        buttonPanel = new JPanel();
        findButton = new JButton("Find");
        stopSearchButton = new JButton("Stop Searching");
        backButton = new JButton("Back to Menu");
        searchingLabel = new JLabel("");
        messageLabel = new JLabel("");
        buttonAndInfoPanel = new JPanel();
        entireListChatBox = new ChatBox();
        
        
        findButton.addActionListener(this);
        stopSearchButton.addActionListener(this);
        backButton.addActionListener(this);
        
        findButton.setActionCommand("find");
        stopSearchButton.setActionCommand("stop searching");
        backButton.setActionCommand("back");
        
        buttonPanel.add(findButton);
        buttonPanel.add(stopSearchButton);
        buttonPanel.add(backButton);
        
        buttonAndInfoPanel.add(buttonPanel);
        buttonAndInfoPanel.add(searchingLabel);
        buttonAndInfoPanel.add(messageLabel);
        
        buttonAndInfoPanel.setLayout(new BoxLayout(buttonAndInfoPanel,BoxLayout.Y_AXIS));
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        
        add(buttonAndInfoPanel);
        add(entireListChatBox);
        
        setOpaque(false);
        buttonPanel.setOpaque(false);
        buttonAndInfoPanel.setOpaque(false);
        buttonAndInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT );
        entireListChatBox.setPreferredSize(new Dimension(300,150));
        entireListChatBox.setMaximumSize(new Dimension(300,150));
        entireListChatBox.setMinimumSize(new Dimension(300,150));
        
        searchingLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        timer = new Timer(200,this);
        timer.setActionCommand("timer");
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "find": 
                if (!solver.isSearching()){
                    resultStatus = 0;
                    frame.recieveSolution(new Formation());//erases any previous solutions
                    frame.recieveStart();
                    entireListChatBox.clear();
                    solver = frame.makeSolver();//resets solver
                    new Thread(solver).start();
                    
                    revalidate();
                    repaint();
                }
                frame.requestFocusInWindow();
            break;
            case "stop searching": 
                recieveStopSearching();
                frame.requestFocusInWindow();
            break;
            case "back":
                solver.stopSearching();
                frame.backToMenuAction();
                new MenuFrame();
                frame.setVisible(false);
                frame.dispose();
            break;
            case "timer":
                
                if (!solver.isSearching()){
                    searchingLabel.setText("");
                }
                
                
                if(resultStatus == -1){
                    messageLabel.setText(frame.getDoneMessage());
                }
                else if (resultStatus == 0){
                    messageLabel.setText("");
                }
                else{
                    messageLabel.setText(frame.getSolutionMessage());
                }
                
                
                repaint();
                revalidate();
            break;
            default:
                System.out.println("CalculationPanel: unknown action command");
        }
        
        
    }

    public void recieveFailure() {
        resultStatus = -1;
        searchingLabel.setText("");
    }
    
    public void recieveStopSearching(){
        solver.stopSearching();
        searchingLabel.setText("");
        resultStatus = 0;
    }
    
    public void recieveSolution(){
        searchingLabel.setText("");
        resultStatus = 1;
    }

    //public void printSearchingLabelText() {
        //System.out.println("SearchingLabelText: " + messageLabel.getText());
    //}

    public void recieveProgressString(String text) {
        searchingLabel.setText(text);
    }
    
    public void recieveCreatureList(LinkedList<Creature> list) {
        entireListChatBox.addText("Search Order");
        int index = 0;
        for (Creature c : list){
            index ++;
            entireListChatBox.addText(Integer.toString(index) + ": " + c.getName());
        }
    }

    public void recieveRefine() {
        solver.recieveRefine();
    }
    
}
