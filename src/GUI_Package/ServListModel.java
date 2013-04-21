package GUI_Package;


import Mediator.Mediator;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 */
public abstract class ServListModel {
    DefaultListModel<State> users;
    public static final String activState = "Activ";
    public static final String noOffer = "No Offer";
    public static final String offerMade = "Offer Made";
    public static final String offerExceeded = "Offer Exceeded";
    public static final String inactivState = "Inactiv";
    public static final String offerAccept = "Offer Accepted";
    public static final String offerRefused = "Offer Refused";
    public static final String transferStarted = "Transfer started";
    public static final String transferProg  = "Transfer in progress";
    public static final String transferComp = "Transfer Completed";
    public static final String transferFail = "Transfer Failed";
    static final int MIN =  0;
    static final int MAX = 10;
    Mediator med;
    JLabel servName;
    JList usersList;
    JLabel state;
    ServTableModel tableModel;
    JProgressBar progressBar;
    public String getUsers() {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        for(int i = 0; i < users.size(); i++) {
            sb.append(users.get(i).getUSer());
            sb.append("<br>");
        }
        sb.append("</html>");
        System.out.println(sb.toString());
        return sb.toString();
    }
    public void registerTableModel(ServTableModel tableModel) {
        this.tableModel = tableModel;
    }
    public void updateTable() {
        tableModel.updateTable();
    }
    public boolean addUser(State user) {
        users.addElement(user);
        return true;
    }
    public boolean deleteUser(String userName) {
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).isSameUser(userName)) {
                users.remove(i);
                return true;
            }
        }
        return true;
    }
    public JLabel getState() {
        if(state.getText().compareTo(inactivState) == 0)
            return new JLabel(inactivState);
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        for(int i = 0; i < users.size(); i++) {
            sb.append(users.get(i).getState());
            sb.append(";");
            sb.append(users.get(i).getPrice());
            sb.append("<br>");
        }
        sb.append("</html>");
        if(users.size() == 0) {
            return new JLabel(ServListModel.noOffer);
        }
        return new JLabel(sb.toString());
        
    }
    public int numUsers() {
        return users.size();
    }
    
    public JProgressBar getProgress() {
        return progressBar;
    }
    JList getJList() {
        return usersList;
    }
    JLabel getServName() {
        return servName;
    }
    
    void changeState(String newState) {
        state.setText(newState);
        med.updateTable();
    }
    void flush() {
        this.users.clear();
        med.updateTable();
    }
   public boolean isActive() {
       return state.getText().compareTo(inactivState) != 0;
   } 
        
    boolean isMakeOfferAvailable() {
        return (state.getText().compareTo(offerAccept) != 0 && 
                state.getText().compareTo(inactivState)  != 0 );            
    }
    
    boolean isDropActionAvailable() {
        return (state.getText().compareTo(offerAccept) != 0 && 
                state.getText().compareTo(inactivState)  != 0 );
    }
    
    boolean isAcceptOfferAvailable() {
        return (state.getText().compareTo(offerAccept) != 0 && 
                state.getText().compareTo(inactivState)  != 0 );            
    } 
    boolean isRefuseOfferAvailable() {
        return (state.getText().compareTo(offerAccept) != 0 && 
                state.getText().compareTo(inactivState)  != 0 );            
    }
    
    void addMouseListener() {
     usersList.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                    int button = e.getButton();
                    int index;
                    State us;
                    if(button == MouseEvent.BUTTON3) {
                       index = usersList.getSelectedIndex();
                       us = users.get(index);
                       us.showPopup(e.getX(), e.getY(), e);
                    }
                    if(button == MouseEvent.BUTTON3)
                        System.out.println("Buto3");
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });   
    }
   
    boolean isSameService(String name) {
        return this.servName.getText().compareTo(name) == 0;
    }
    boolean userNotExists(String username) {
        for(int i = 0; i < this.users.size(); i++) {
            if(users.get(i).isSameUser(username))
                    return false;
        }
        return true;
    }
    void changeState(String userName, String state) {
        for(int i = 0; i < this.users.size(); i++) {
            if(users.get(i).isSameUser(userName)) {
                users.get(i).changeState(state);
            }
        }
    }
    
    abstract void addUser(String userName);
}

