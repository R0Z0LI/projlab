package Graphics;

import Field.Field;
import Game.Game;
import Virologist.Virologist;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FieldView extends JPanel implements ActionListener {
    private Field myField;
    private Virologist activeVirologist;
    private ArrayList<JButton> neighbours = new ArrayList<>();
    private ArrayList<JLabel> neighbourNames = new ArrayList<>();
    private JLabel virologistIcon, chosenVirologistIcon;

    /**
     * Empty constructor
     */
    public FieldView(Virologist v) {
        activeVirologist = v;
        update();
    }

    /**
     * Step on another field
     * @param pressedButton
     */
    private void neighborButtonPressed(JButton pressedButton) {
        removeChosenVirologistIcon();
        String fieldName = pressedButton.getText();
        for(Field field : myField.getNeighbours()) {
            if (fieldName.equals(field.getName())) {
                activeVirologist.step(field);
                break;
            }
        }
        this.update();

        activeVirologist.getMyCommandView().activateView();
        Game.actionHappened();
    }

    /**
     * Updates the shown FieldView
     */
    public void update() {
        neighbours.clear();
        this.removeAll();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 20, 20, 20);

        //viroicon
        c.gridx=4;
        c.gridy=1;
        c.gridwidth=3;
        if(virologistIcon!=null)
            this.add(virologistIcon,c);
        if(chosenVirologistIcon!=null) {
            c.gridx=0;
            c.gridy=1;
            this.add(chosenVirologistIcon,c);
        }
        c.gridwidth=1;

        // adding neighbour buttons and their icons
        this.myField = activeVirologist.getCurrentField();
        for (int i = 0; i < myField.getNeighbours().size(); i++) {
            // icon
            BufferedImage img = null;
            String path = "src/pictures/" + myField.getNeighbours().get(i).getName().subSequence(0,3) + "icon.png";
            try {
                img = ImageIO.read(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }

            JLabel label = new JLabel();
            label.setSize(50, 100);
            Image dimg = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            label.setIcon(imageIcon);

            c.gridx = i;
            c.gridy = 2;
            this.add(label, c);

            // button
            c.gridx = i;
            c.gridy = 4;
            JButton neighbourButton = new JButton(myField.getNeighbours().get(i).getName());
            neighbourButton.addActionListener(this);
            neighbours.add(neighbourButton);
            this.add(neighbourButton, c);
        }
        for(JButton jButton : neighbours) System.out.println(jButton.getText());

        // TODO - ICONS
        this.validate();
        this.repaint();
        this.myField.setView(this);
        this.activateView();
    }

    /**
     * Beállítja megjelenésre az aktuális View-t
     */
    public void activateView(){
        GameFrame.instance().setView(this);
    }

    /**
     * beállítja a mezőn álló aktív virológust és egyben update-eli magát
     * @param v
     */
    public void setActiveVirologist(Virologist v) {
        activeVirologist = v;
        //this.update();
    }

    public void setVirologistIcon(ImageIcon icon) {
        virologistIcon= new JLabel(icon);
        update();
    }
    public void setChosenVirologistIcon(ImageIcon icon) {
        chosenVirologistIcon=new JLabel(icon);
        update();
    }
    public void removeChosenVirologistIcon(){
        chosenVirologistIcon=null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(JButton jButton : neighbours){
            if(e.getSource().equals(jButton)){
                neighborButtonPressed(jButton);
                break;
            }
        }
    }
}
