/*

 */
package GUI;

import Formations.BattleLog;
import Formations.BattleState;
import Formations.Formation;
import cosmosquestsolver.OtherThings;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class SimulationPanel extends JPanel implements ActionListener{
    
    protected JFrame frame;
    
    private JPanel battlePanel;
    private JPanel buttonPanel;
    
    private JPanel leftDamageLabelPanel;
    private JPanel rightDamageLabelPanel;
    private JPanel roundLabelPanel;
    private JLabel leftDamageTitleLabel;
    private JLabel rightDamageTitleLabel;
    private JLabel leftDamageLabel;
    private JLabel rightDamageLabel;
    private JLabel roundTitleLabel;
    private JLabel roundLabel;
    private SolutionFormationPanel leftFormationPanel;
    private SolutionFormationPanel rightFormationPanel;
    
    
    private JButton firstButton;
    private JButton lastButton;
    private JButton previousButton;
    private JButton nextButton;
    protected JButton menuButton;
    
    protected BattleLog log;
    private int roundNum;
    
    
    public SimulationPanel(JFrame frame){
        this.frame = frame;
        
        battlePanel = new JPanel();
        buttonPanel = new JPanel();
        
        leftDamageLabelPanel = new JPanel();
        rightDamageLabelPanel = new JPanel();
        leftDamageLabel = new JLabel("0");
        rightDamageLabel = new JLabel("0");
        roundLabelPanel = new JPanel();
        leftDamageTitleLabel = new JLabel("Damage Dealt");
        rightDamageTitleLabel = new JLabel("Damage Dealt");
        leftFormationPanel = new SolutionFormationPanel(true);
        rightFormationPanel = new SolutionFormationPanel(false);
        roundTitleLabel = new JLabel("Round");
        roundLabel = new JLabel("0");
        firstButton = new JButton("<<");
        lastButton = new JButton(">>");
        previousButton = new JButton("<");
        nextButton = new JButton(">");
        menuButton = new JButton("Menu");
        
        firstButton.addActionListener(this);
        lastButton.addActionListener(this);
        previousButton.addActionListener(this);
        nextButton.addActionListener(this);
        menuButton.addActionListener(this);
        
        firstButton.setActionCommand("first");
        lastButton.setActionCommand("last");
        previousButton.setActionCommand("previous");
        nextButton.setActionCommand("next");
        menuButton.setActionCommand("menu");
        
        
        leftDamageLabelPanel.add(leftDamageTitleLabel);
        leftDamageLabelPanel.add(leftDamageLabel);
        rightDamageLabelPanel.add(rightDamageTitleLabel);
        rightDamageLabelPanel.add(rightDamageLabel);
        roundLabelPanel.add(roundTitleLabel);
        roundLabelPanel.add(roundLabel);
        
        battlePanel.add(leftDamageLabelPanel);
        battlePanel.add(leftFormationPanel);
        battlePanel.add(roundLabelPanel);
        battlePanel.add(rightFormationPanel);
        battlePanel.add(rightDamageLabelPanel);
        
        buttonPanel.add(firstButton);
        buttonPanel.add(previousButton);
        buttonPanel.add(menuButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(lastButton);
        
        add(battlePanel);
        add(buttonPanel);
        
        
        setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_FRAME_WIDTH,150));
        setMinimumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_FRAME_WIDTH,150));
        setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_FRAME_WIDTH,150));
        leftFormationPanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        leftFormationPanel.setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        leftFormationPanel.setMinimumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        rightFormationPanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        rightFormationPanel.setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        rightFormationPanel.setMinimumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        leftDamageLabelPanel.setPreferredSize(new Dimension(100,100));
        leftDamageLabelPanel.setMaximumSize(new Dimension(100,100));
        leftDamageLabelPanel.setMinimumSize(new Dimension(100,100));
        rightDamageLabelPanel.setPreferredSize(new Dimension(100,100));
        rightDamageLabelPanel.setMaximumSize(new Dimension(100,100));
        rightDamageLabelPanel.setMinimumSize(new Dimension(100,100));
        roundLabelPanel.setPreferredSize(new Dimension(50,50));
        roundLabelPanel.setMaximumSize(new Dimension(50,50));
        roundLabelPanel.setMinimumSize(new Dimension(50,50));
        
        battlePanel.setLayout(new BoxLayout(battlePanel,BoxLayout.X_AXIS));
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
        leftDamageLabelPanel.setLayout(new BoxLayout(leftDamageLabelPanel,BoxLayout.Y_AXIS));
        rightDamageLabelPanel.setLayout(new BoxLayout(rightDamageLabelPanel,BoxLayout.Y_AXIS));
        roundLabelPanel.setLayout(new BoxLayout(roundLabelPanel,BoxLayout.Y_AXIS));
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        roundLabelPanel.setOpaque(false);
        leftDamageLabelPanel.setOpaque(false);
        rightDamageLabelPanel.setOpaque(false);
        battlePanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        setOpaque(false);
        
        log = new BattleLog();
        roundNum = 0;
        
    }
    
    public void recieveSimulation(BattleLog log){
        this.log = log;
        changeRound(log.length()-1);
    }
    
    protected void changeRound(int round){
        if (round < 0){
            round = 0;
        }
        else if (round >= log.length()){
            round = log.length() - 1;
        }
        
        roundNum = round;
        
        BattleState state = log.getState(round);
        
        leftFormationPanel.updateFormation(state.leftFormation);
        rightFormationPanel.updateFormation(state.rightFormation);
        leftDamageLabel.setText(OtherThings.intToCommaString(state.rightFormation.getDamageTaken()));
        rightDamageLabel.setText(OtherThings.intToCommaString(state.leftFormation.getDamageTaken()));
        roundLabel.setText(Integer.toString(round));
        
        revalidate();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "first":
                changeRound(0);
            break;
            case "last":
                changeRound(log.length()-1);
            break;
            case "previous":
                changeRound(roundNum - 1);
            break;
            case "next":
                changeRound(roundNum + 1);
            break;
            case "menu":
                exit();
            break;
            default:
                System.out.println("Error: unknown action command in SimulationPanel");
        }
        if (frame != null){
            frame.requestFocusInWindow();
        }
    }
    
    public void exit(){
        new MenuFrame();
        frame.setVisible(false);
        frame.dispose();
    }
    
}
