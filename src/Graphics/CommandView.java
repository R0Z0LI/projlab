package Graphics;

import Agent.Agent;
import Collectible.Collectible;
import Field.Field;
import Virologist.Virologist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

public class CommandView extends JPanel implements ActionListener {
    private Virologist myVirologist;
    private Virologist chosenVirologist;
    private JLabel fieldName;
    private JLabel chosenViroName;
    private HashMap<JButton, Virologist> virButtons;
    //?? akkor végül is csak 1 lehet
    private HashMap<JButton, Collectible> thingButtons;
    private JButton applyButton;
    private JButton attackButton;
    private JButton stealButton;
    private JComboBox<String> agentChooser;
    private JComboBox<String> stealableChooser;
    private JButton backButton;

    public CommandView(Virologist myViro){
        myVirologist=myViro;
        applyButton=new JButton("Apply");
        applyButton.addActionListener(this);
        attackButton=new JButton("Attack");
        attackButton.addActionListener(this);
        stealButton=new JButton("Steal");
        stealButton.addActionListener(this);
        backButton=new JButton("Back");
        backButton.addActionListener(this);
        fieldName=new JLabel();
        chosenViroName=new JLabel();
        virButtons=new HashMap<>();
        thingButtons=new HashMap<>();
        update();
    }

    /**
     * displays fieldname, touchable virologists, collectible things
     */
    public void update(){
        this.removeAll();
        //asking for info about viro
        //fieldinfo
       /* Field field = myVirologist.getCurrentField();
        fieldName.setText(field.getName());

        //display labels
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(0,2,30,50));
        innerPanel.add(new JLabel("Field:"));
        innerPanel.add(fieldName);
        innerPanel.add(new JLabel("Vir:"),2);
        //otherviros info + display viro buttons
        int i=0;
        List<Virologist> viros=field.GetTouchableVirologists();
        for(Virologist v :viros){
            JButton vButton=new JButton(v.getName());
            virButtons.put(vButton, v);
            innerPanel.add(vButton);
            ++i;
        }
        //ez csak kitoltes
        if(i%2 ==0) innerPanel.add(new JLabel());
        innerPanel.add(new JLabel("Thing:"));
        //collectible info
        Collectible coll=field.getCollectible();
        JButton thing=null;
        if(coll!=null) {
            thing = new JButton(coll.getName());
            thingButtons.put(thing, coll);
        }
        //displaying collectible thing
        if(coll!=null)
            innerPanel.add(thing);
        this.add(innerPanel);*/
        this.add(new JLabel("itt vagyok"));
    }

    public void activateView(){
        GameFrame.Instance().setView(this);
    }
    private void virologistChosen(Virologist v){
        //chosen viro
        chosenVirologist=v;
        chosenViroName.setText(v.getName());
        //agents
        agentChooser=new JComboBox<>();
        List<Agent> agents= myVirologist.getPropertyHandler().getAgents();
        for(Agent a : agents){
            agentChooser.addItem(a.getName());
        }
        //stealables
        stealableChooser=new JComboBox<>();
        List<Collectible> stealables=myVirologist.getStealableThings();
        for(Collectible c: stealables){
            stealableChooser.addItem(c.getName());
        }
        //layout
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        innerPanel.add(chosenViroName,c);
        c.gridx=0;
        c.gridy=1;
        innerPanel.add(applyButton,3);
        c.gridx=0;
        c.gridy=2;
        innerPanel.add(stealButton,5);
        c.gridx=0;
        c.gridy=3;
        innerPanel.add(attackButton,7);
        c.gridx=1;
        c.gridy=1;
        innerPanel.add(agentChooser,4);
        c.gridx=1;
        c.gridy=2;
        innerPanel.add(stealableChooser,6);
        c.gridx=0;
        c.gridy=4;
        innerPanel.add(backButton,9);
        this.add(innerPanel);
    }
    public void actionPerformed(ActionEvent e){
        if (e.getSource().equals(attackButton)) {
            attackButtonPressed();
        } else if (e.getSource().equals(applyButton)) {
            applyButtonPressed();
        } else if (e.getSource().equals(stealButton)) {
            stealButtonPressed();
        }  else if (e.getSource().equals(backButton)) {
            backButtonPressed();
        } else if(virButtons.containsKey(e.getSource())){
            virologistChosen(virButtons.get(e.getSource()));
        }else if(thingButtons.containsKey(e.getSource())){
            thingButtonPressed(thingButtons.get(e.getSource()));
        }
    }
    private void attackButtonPressed(){
        myVirologist.attack(chosenVirologist);
    }
    private void stealButtonPressed(){
        String sName =(String)stealableChooser.getSelectedItem();
        Collectible stealable=null;
        List<Collectible> stealables=myVirologist.getStealableThings();
        for(Collectible c: stealables){
            if(c.getName().equals(sName))
                stealable=c;
        }
        if(stealable!=null)
             myVirologist.steal(stealable, chosenVirologist);
    }
    private void applyButtonPressed(){
        String aName=(String)agentChooser.getSelectedItem();
        List<Agent> agents= myVirologist.getPropertyHandler().getAgents();
        Agent agent=null;
        for(Agent a : agents){
            if(a.getName().equals(aName))
                agent=a;
        }
        if(agent!=null)
            myVirologist.applyAgent(agent, chosenVirologist);
    }
    private void backButtonPressed(){
        update();
    }
    private void thingButtonPressed(Collectible thing){
        myVirologist.collect(thing);
    }
}
