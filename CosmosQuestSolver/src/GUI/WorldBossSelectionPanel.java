/*

 */
package GUI;

import Formations.CreatureFactory;
import Formations.Formation;
import Formations.WorldBoss;
import cosmosquestsolver.OtherThings;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

//displays and lets you select a world boss
public class WorldBossSelectionPanel extends JPanel implements ActionListener{
    
    private WorldBossOptimizerFrame frame;
    
    private JPanel titlePanel;
    private JLabel worldBossLabel;
    private WorldBossPicturePanel worldBossPicturePanel;
    private JPanel buttonPanel;
    private JPanel solutionTitlePanel;
    private JLabel solutionLabel;
    private JLabel damageLabel;
    private SolutionFormationPanel solutionFormationPanel;
    
    private HashMap<String,WorldBoss> map;
    
    public WorldBossSelectionPanel(WorldBossOptimizerFrame frame){
        this.frame = frame;
        
        titlePanel = new JPanel();
        worldBossLabel = new JLabel("World Boss",SwingConstants.CENTER);
        worldBossPicturePanel = new WorldBossPicturePanel();
        buttonPanel = new JPanel();
        solutionTitlePanel = new JPanel();
        solutionLabel = new JLabel("Solution");
        damageLabel = new JLabel("Damage: 0");
        solutionFormationPanel = new SolutionFormationPanel(true);
        
        setBoss(CreatureFactory.getDefaultBoss());
        
        titlePanel.add(worldBossLabel);
        solutionTitlePanel.add(solutionLabel);
        solutionTitlePanel.add(damageLabel);
        add(titlePanel);
        add(worldBossPicturePanel);
        add(buttonPanel);
        add(solutionTitlePanel);
        add(solutionFormationPanel);
        
        initiateButtons();
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        solutionTitlePanel.setLayout(new BoxLayout(solutionTitlePanel,BoxLayout.Y_AXIS));
        worldBossPicturePanel.setPreferredSize(new Dimension(WorldBoss.DRAW_WIDTH,WorldBoss.DRAW_HEIGHT));
        worldBossPicturePanel.setMaximumSize(new Dimension(WorldBoss.DRAW_WIDTH,WorldBoss.DRAW_HEIGHT));
        worldBossPicturePanel.setMinimumSize(new Dimension(WorldBoss.DRAW_WIDTH,WorldBoss.DRAW_HEIGHT));
        solutionFormationPanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        solutionFormationPanel.setMaximumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        solutionFormationPanel.setMinimumSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Formation.MAX_MEMBERS,AssetPanel.CREATURE_PICTURE_SIZE));
        buttonPanel.setPreferredSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2 - 100,100));
        buttonPanel.setMaximumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2 - 100,100));
        buttonPanel.setMinimumSize(new Dimension(QuestSolverFrame.QUEST_SOLVER_TOP_PANEL_WIDTH/2 - 100,100));
        
        worldBossLabel.setFont(new Font("Courier", Font.PLAIN, 30));
        solutionLabel.setFont(new Font("Courier", Font.PLAIN, 22));
        damageLabel.setFont(new Font("Courier", Font.PLAIN, 20));
        //worldBossLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //solutionLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        titlePanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        solutionTitlePanel.setOpaque(false);
        //solutionFormationPanel.setOpaque(false);
    }
    
    public WorldBoss getBoss(){
        return worldBossPicturePanel.boss;
    }
    
    public void setBoss(WorldBoss boss){
        worldBossPicturePanel.setBoss(boss);
        worldBossLabel.setText(boss.getName());
        repaint();
    }
    
    private void initiateButtons(){
        map = new HashMap<>();
        for (WorldBoss boss : CreatureFactory.getWorldBosses()){
            map.put(boss.getName(),boss);
            JButton button = new JButton(boss.getName());
            button.addActionListener(this);
            button.setActionCommand(boss.getName());
            buttonPanel.add(button);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!worldBossPicturePanel.getBoss().getName().equals(e.getActionCommand())){
            frame.parametersChanged();
            setBoss(map.get(e.getActionCommand()));
        }
        
    }

    void setDamage(long damage) {
        damageLabel.setText("Damage: " + OtherThings.intToCommaString(damage));
        revalidate();
        repaint();
    }

    public void recieveSolution(Formation f) {
        solutionFormationPanel.updateFormation(f);
    }

    public void parametersChanged() {
        solutionFormationPanel.updateFormation(new Formation());
        damageLabel.setText("Damage: 0");
    }
    
    private class WorldBossPicturePanel extends JPanel{
        
        private WorldBoss boss;
        
        public WorldBossPicturePanel(){
            
        }
        
        public WorldBossPicturePanel(WorldBoss boss){
            this.boss = boss;
            setMouseOverText();
        }
        
        public WorldBoss getBoss(){
            return boss;
        }
        
        public void setBoss(WorldBoss boss){
            this.boss = boss;
            setMouseOverText();
            repaint();
        }
        
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(CreatureFactory.getPicture("Backgrounds/World Boss Background"), 0, 0, WorldBoss.DRAW_WIDTH, WorldBoss.DRAW_HEIGHT, null);
            if (boss != null){
                if (!boss.isFacingRight()){
                    g.drawImage(CreatureFactory.getPicture(boss.getImageAddress()), WorldBoss.DRAW_WIDTH, 0, -WorldBoss.DRAW_WIDTH, WorldBoss.DRAW_HEIGHT, null);
                }
                else{
                    g.drawImage(CreatureFactory.getPicture(boss.getImageAddress()), 0, 0, WorldBoss.DRAW_WIDTH, WorldBoss.DRAW_HEIGHT, null);
                }
            }
        }
        
        private void setMouseOverText(){
            if (boss != null){
                setToolTipText(boss.toolTipText());
            }
        }
        
    }
    
}
