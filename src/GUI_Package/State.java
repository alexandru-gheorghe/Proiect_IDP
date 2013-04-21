package GUI_Package;


import Mediator.Mediator;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.event.ListSelectionEvent;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 */
public abstract class State {
    String price;
    String state;
    JLabel name;
    String servName;
    ServListModel servList;
    Mediator med;
    public String getState() {
        return state;
    }
    
    public String getPrice() {
        return price;
    }
    
    public JLabel getUSer() {
        return name;
    }
    
    public String toString() {
        return name.getText();
    }
    
    public void changeState(String newState) {
        state = newState;
        servList.updateTable();
        if(newState.compareTo(ServListModel.noOffer) == 0)
            price = "";
    }
    public void changeState(String newState, String price) {
        state = newState;
        this.price = price;
        servList.updateTable();
    }
    public boolean isSameUser(String username) {
        return this.name.getText().compareTo(username) == 0;
    }

    abstract public void showPopup(int x, int y, MouseEvent lie);
}
