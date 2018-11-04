/*

 */
package GUI;

import AI.AISolver;
import Formations.CreatureFactory;
import Formations.Hero;
import Formations.Hero.Rarity;
import static GUI.AssetPanel.HERO_SELECTION_COLUMNS;
import static GUI.AssetPanel.TEXTBOX_HEIGHT;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import static javax.swing.text.StyleConstants.setBackground;


public class HeroesCustomizationPanel extends JPanel implements IFilterFrame, DocumentListener {
    
    private ISolverFrame frame;
    
    private String filter = "";
    
    private JTextField searchText;
    private JPanel searchPanel;
    
    private HeroCustomizationPanel[] heroPanelArray;
    private HashMap<String,HeroCustomizationPanel> map;//for finding the right hero panel when loading
    

    public HeroesCustomizationPanel(ISolverFrame frame, int numColumns, boolean facingRight, boolean includePrioritize) {//reference solver, not frame?***
        this.frame = frame;
        
        map = new HashMap<>();
        
        JLabel searchLabel = new JLabel("Search");
        searchText = new JTextField("");
        searchPanel = new JPanel();
        
        searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.Y_AXIS));
        searchPanel.add(searchLabel);
        searchPanel.add(searchText);
        
        setLayout(new GridLayout(0,numColumns));
        searchText.setMaximumSize(new Dimension(200,TEXTBOX_HEIGHT));
        searchText.getDocument().addDocumentListener(this);
        add(searchPanel);
        
        Hero[] heroes = CreatureFactory.getHeroes();
        heroPanelArray = new HeroCustomizationPanel[heroes.length];
        for (int i = 0; i < heroPanelArray.length; i++){
            heroes[i].setFacingRight(facingRight);
            heroPanelArray[i] = new HeroCustomizationPanel(frame,heroes[i],includePrioritize);
            add(heroPanelArray[i]);
            map.put(heroes[i].getName(),heroPanelArray[i]);
        }

    }

    public void disableAll() {
        for (HeroCustomizationPanel panel : heroPanelArray){
            panel.setHeroEnabled(false);
        }
    }
    
    public void deprioritizeAll(){
        for (HeroCustomizationPanel panel : heroPanelArray){
            panel.setPrioritizeHero(false);
        }
    }

    public void setLevelAll(int level) {
        for (HeroCustomizationPanel panel : heroPanelArray){
            panel.setLevel(level);
        }
    }
    
    public void enableRarity(Rarity rarity) {
        for (HeroCustomizationPanel panel : heroPanelArray){
            if (panel.getHero().getRarity() == rarity)
            {
                panel.setHeroEnabled(true);
            }
            else
            {
                panel.setHeroEnabled(false);
            }
        }
    }
    
    public void writeLevelString(PrintWriter file) {
        for (int i = 0; i < heroPanelArray.length - 1; i++){
            heroPanelArray[i].writeHeroLevelString(file);
            file.print("\n");
        }
        heroPanelArray[heroPanelArray.length - 1].writeHeroLevelString(file);
    }

    public void writeSelectString(PrintWriter file) {
        for (int i = 0; i < heroPanelArray.length - 1; i++){
            heroPanelArray[i].writeHeroSelectString(file);
            file.print("\n");
        }
        heroPanelArray[heroPanelArray.length - 1].writeHeroSelectString(file);
    }
/*
    public void setHeroStats(String token, int level, boolean heroEnabled, boolean heroPrioritized) {
        HeroCustomizationPanel p = map.get(token);
        if (p != null){
            p.setLevel(level);
            p.setHeroEnabled(heroEnabled);
            p.setPrioritizeHero(heroPrioritized);
        }
    }
   */ 
    public void setHeroLevel(String name, int level){
        HeroCustomizationPanel p = map.get(name);
        if (p != null){
            p.setLevel(level);
        }
    }
    
    public void setHeroSelect(String name, boolean enabled, boolean prioritized){
        HeroCustomizationPanel p = map.get(name);
        if (p != null){
            p.setHeroEnabled(enabled);
            p.setPrioritizeHero(prioritized);
        }
    }
    
    public Hero getHero(String name) {
        return map.get(name).getHero();
    }
    
    public Hero[] getHeroes() {
        LinkedList<Hero> heroes = new LinkedList<>();
        for(int i = 0; i < heroPanelArray.length; i++){
            if (heroPanelArray[i].heroEnabled()){
                heroes.add((Hero)heroPanelArray[i].getHero().getCopy());
            }
        }
        
        return convertToArray(heroes);
        
    }
    
    public Hero[] getHeroesWithoutPrioritization() {
        LinkedList<Hero> heroes = new LinkedList<>();
        for(int i = 0; i < heroPanelArray.length; i++){
            if (heroPanelArray[i].heroEnabled() && !heroPanelArray[i].heroPrioritized()){
                heroes.add((Hero)heroPanelArray[i].getHero().getCopy());
            }
        }
        
        return convertToArray(heroes);
        
    }
    
    public Hero[] getPrioritizedHeroes() {
        LinkedList<Hero> heroes = new LinkedList<>();
        for(int i = 0; i < heroPanelArray.length; i++){
            if (heroPanelArray[i].heroPrioritized()){
                heroes.add((Hero)heroPanelArray[i].getHero().getCopy());
            }
        }
        return convertToArray(heroes);
    }
    
    private Hero[] convertToArray(LinkedList<Hero> heroes){
        int i = 0;
        Hero[] ans = new Hero[heroes.size()];
        for (Hero h : heroes){
            ans[i] = h;//already copied
            i ++;
        }
        return ans;
    }

    public boolean heroEnabled(String heroName) {
        return map.get(heroName).heroEnabled();
    }

    public boolean heroPrioritized(String hName) {
        return map.get(hName).heroPrioritized();
    }

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    public void filterChanged() {
        for(int i = 0; i < heroPanelArray.length; i++){
            String heroName = heroPanelArray[i].getHero().getName();
            String heroNameLow = heroName.toLowerCase();
            if (!heroNameLow.startsWith(getFilter()))
            {
                remove(heroPanelArray[i]);
            }
        }
        repaint();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        textFieldChanged(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        textFieldChanged(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        textFieldChanged(e);
    }
    
    public void textFieldChanged(DocumentEvent e){
        if (e.getDocument() == searchText.getDocument()){
            searchTextChanged();
        }
        else{
            System.out.println("Unknown Document in heroesCustomizationPanel!");
        }
    }

    private void searchTextChanged() {
        try{
            String searchEntered = searchText.getText().toLowerCase();
            searchText.setForeground(Color.BLACK);
            this.filter = searchEntered;
        }
        catch (Exception ex){
            searchText.setForeground(Color.RED);
        }
        try{
            if (filter.equals("")){
                fillHeroes();
            }
            else
            {
                filterChanged();
            }
        }
        catch(Exception ex){
            
        }
    }
    
    
    private void fillHeroes(){
        add(searchPanel);
        for (int i = 0; i < heroPanelArray.length; i++){
            add(heroPanelArray[i]);
        }
        repaint();
    }

    

    

    
    
    
    
}
