/*

 */
package GUI;

import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Monster;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

//displays all monsters of a specified element.
public class ElementMonsterSelectionPanel extends JPanel{
    
    private EnemySelectFrame frame;
    private EnemyFormationMakerPanel enemyFormationMakerPanel;
    private Creature.Element element;
    
    private Monster[] monsters;

    public ElementMonsterSelectionPanel(EnemySelectFrame frame,EnemyFormationMakerPanel enemyFormationMakerPanel, Creature.Element element, boolean facingRight) {
        this.frame = frame;
        this.enemyFormationMakerPanel = enemyFormationMakerPanel;
        this.element = element;
        
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        
        monsters = CreatureFactory.getMonsters(element);
        
        for (Monster monster : monsters) {
            monster.setFacingRight(facingRight);
            CreaturePictureSelectionPanel creaturePicturePanel = new CreaturePictureSelectionPanel(frame,enemyFormationMakerPanel,monster);
            JPanel panel = new JPanel();
            panel.add(creaturePicturePanel);
            add(panel);
            panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
            //panel.setBackground(Color.RED);
            creaturePicturePanel.setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE,AssetPanel.CREATURE_PICTURE_SIZE));
        }
        //setBackground(Color.PINK);
        setPreferredSize(new Dimension(AssetPanel.CREATURE_PICTURE_SIZE * Monster.TOTAL_TIERS,AssetPanel.CREATURE_PICTURE_SIZE));
        setOpaque(false);
    }
    
    
    
}
