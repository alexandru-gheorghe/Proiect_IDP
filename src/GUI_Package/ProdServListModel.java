package GUI_Package;


import Mediator.Mediator;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
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
public class ProdServListModel extends ServListModel {
    public ProdServListModel(String servName, Mediator med) {
        this.med = med;
        this.progressBar = new JProgressBar(MIN, MAX);
        this.servName = new JLabel(servName);
        users = new DefaultListModel<State>();
        usersList = new JList(users);
        state = new JLabel(ServListModel.activState);
        addMouseListener();
    }

    @Override
    void addUser(String userName) {
        if(userNotExists(userName))
            this.users.addElement(new ProdState(userName, med, this));
    }
}
