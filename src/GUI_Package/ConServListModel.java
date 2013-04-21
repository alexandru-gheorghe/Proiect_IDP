package GUI_Package;


import Mediator.Mediator;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 */
public class ConServListModel extends ServListModel {
    public ConServListModel(String servName, Mediator med) {
        this.med = med;
        progressBar  = new JProgressBar(MIN, MAX);
        this.servName = new JLabel(servName);
        state = new JLabel(ServListModel.inactivState);
        addMouseListener(this.servName);
        users = new DefaultListModel<State>();
        usersList = new JList(users);
        addMouseListener();
            
    }
    JMenuItem getOptions() {
        if(state.getText().compareTo(ServListModel.inactivState) == 0) {
             JMenuItem jm = new JMenuItem("Launch Offer request");
             addLaunchActionListener(jm);
             return jm;
        }
        else 
            if(state.getText().compareTo(ServListModel.activState) == 0 && state.getText().compareTo(ServListModel.offerAccept) != 0) { 
                JMenuItem jm = new JMenuItem("Drop Offer request");
                addDropActionListener(jm);
                return jm;
            }
            else {
                return new JMenuItem("No Option Available");
            }
            
    }
    
    public void addLaunchActionListener(JMenuItem jm) {
        jm.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String inputValue = JOptionPane.showInputDialog("Quantaty for offer request:");
                
                if(med.ConsActivateService(servName.getText()))
                    changeState(ServListModel.activState);
                    //ArrayList<String> reply = med.sendOfferRequest(med.getUserName(), servName.getText());
                    //med.addNewUsers(reply, servName.getText());
                    
             }
        });
    }
    
    public void addDropActionListener(JMenuItem jm) {
        jm.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(med.ConsDropRequestService(servName.getText()))
                        changeState(ServListModel.inactivState);
              }
        });
    }
    
    public void addMouseListener(JLabel servName) {
        servName.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                    int button = e.getButton();
                    int index;
                    State us;
                    if(button == MouseEvent.BUTTON3) {
                        JPopupMenu jp = new JPopupMenu("Menu");
                        jp.add(getOptions());
                        jp.show((JComponent)e.getSource(), e.getX(), e.getY());
                    }
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

    @Override
    void addUser(String userName) {
        if(userNotExists(userName))
            this.users.addElement(new ConState(userName, med, this));
    }
}
