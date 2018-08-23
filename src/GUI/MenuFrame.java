/*

 */
package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//main menu. Has buttons to go to the GUI for each solver
public class MenuFrame extends JFrame implements ActionListener{
    
    private JPanel titlePanel;
    private JPanel buttonPanel;
    
    private JLabel titleLabel;
    
    private JButton sandBoxButton;
    private JButton questButton;
    private JButton worldBossButton;
    private JButton tournamentButton;
    
    public static final int FRAME_WIDTH = 440;
    public static final int FRAME_HEIGHT = 140;
    public static BufferedImage frameBackground;
    
    public MenuFrame(){
        
        try{//not using image pool because it would trigger loading everything else
            frameBackground = ImageIO.read(new File("pictures/Creator Background.png"));
        }
        catch(IOException e){
            System.out.println("error");
        }
        
        setContentPane(new DrawPane());
        titlePanel = new JPanel();
        buttonPanel = new JPanel();
        titleLabel = new JLabel("Cosmos Quest Solver");
        
        sandBoxButton = new JButton("Sand Box");
        questButton = new JButton("Quests");
        worldBossButton = new JButton("World Bosses");
        tournamentButton = new JButton("Tournaments");
        
        sandBoxButton.addActionListener(this);
        questButton.addActionListener(this);
        worldBossButton.addActionListener(this);
        tournamentButton.addActionListener(this);
        
        sandBoxButton.setActionCommand("sand box");
        questButton.setActionCommand("quest");
        worldBossButton.setActionCommand("world boss");
        tournamentButton.setActionCommand("tournament");
        
        titlePanel.add(titleLabel);
        
        buttonPanel.add(sandBoxButton);
        buttonPanel.add(questButton);
        buttonPanel.add(worldBossButton);
        buttonPanel.add(tournamentButton);
        
        add(titlePanel);
        add(buttonPanel);
        
        setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));
        setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        titleLabel.setFont(new Font("Courier", Font.PLAIN, 30));
        
        setTitle("Cosmos Quest Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        getContentPane().setBackground(Color.YELLOW);
        
        titlePanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        
        ImageIcon img = new ImageIcon("pictures/Followers Icon.png");
        setIconImage(img.getImage());
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "sand box":
                new SandBoxFrame();
                setVisible(false);
                dispose();
            break;
            case "quest": 
                new QuestSolverFrame();
                setVisible(false);
                dispose();
            break;
            case "world boss": 
                new WorldBossOptimizerFrame();
                setVisible(false);
                dispose();
            break;
            case "tournament": 
                new TournamentOptimizerFrame();
                setVisible(false);
                dispose();
            break;
            
            default: System.out.println("MenuPanel encountered unknown actionCommand");
        }
    }

    private static class DrawPane extends JPanel {//for drawing a background on a JFrame

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(frameBackground, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null);
            g.setColor(new Color(255,255,180,128));
            g.fillRect(0, 0, getWidth(), getHeight());//does not scale**
         }
        
    }
    
    
    
}
