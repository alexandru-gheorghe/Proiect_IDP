/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI_Package;

import Mediator.Mediator;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alex
 */
public class ServTableModel extends DefaultTableModel {
    String colNames[] = {"Service", "User List", "Status", "Progress" };
    Mediator med;
    JTable table;
    String username;
    ArrayList<ServListModel> serviceList;
    public ServTableModel(Mediator med, JTable table, String username) {
        super();
        this.username = new String(username);
        this.table = table;
        this.setColumnIdentifiers(colNames);
        this.med = med;
        this.serviceList = new ArrayList<>();
        med.registerServiceListModel(this);
        this.addDataListener();
    }
    
    void addRow(ServListModel list) {
        Vector vct = new Vector();
        vct.add(list);
        vct.add(list);
        vct.add(list);
        vct.add(list);
        serviceList.add(list);
        list.registerTableModel(this);
        super.addRow(vct);
    }
    public boolean addNewUser(String userName, String servName) {
        for(int i = 0; i < serviceList.size(); i++) {
            if(serviceList.get(i).isSameService(servName) &&
               serviceList.get(i).isActive()) {
                serviceList.get(i).addUser(userName);
            }
        }
        updateTable();
        return true;
    }
   public boolean deleteUser(String userName, String servName) {
        for(int i = 0; i < serviceList.size(); i++) {
            if(serviceList.get(i).isSameService(servName)) {
                serviceList.get(i).deleteUser(userName);
            }
        }
        updateTable();
        return true;
    }
    public boolean receiveMakeOffer(String userName, String servName, String price) {
        changeState(userName, servName, ServListModel.offerMade, price);
        return true;
    }
    
   public boolean receiveDropOffer(String userName, String servName) {
        changeState(userName, servName, ServListModel.noOffer);
        return true;
    }
   public boolean receiveRequestOffer(String userName, String servName) {
       addNewUser(userName, servName);
       return true;
   }
   public boolean receiveCancelOffer(String userName, String servName) {
       deleteUser(userName, servName);
       return true;
   }
   public boolean receiveOfferRefused(String userName, String servName) {
       changeState(userName, servName, ServListModel.offerRefused);
       return true;
   }
   public boolean receiveOfferAccept(String userName, String servName, String quant) {
       changeState(userName, servName, ServListModel.offerAccept);
       return true;
   }
     public void changeState(String userName, String servName, String state) {
        for(int i = 0; i < serviceList.size(); i++) {
            if(serviceList.get(i).isSameService(servName) && 
                serviceList.get(i).isActive())
                    serviceList.get(i).changeState(userName, state);
        }
        updateTable();
     }
     public void changeState(String userName, String servName, String state, String price) {
        for(int i = 0; i < serviceList.size(); i++) {
            if(serviceList.get(i).isSameService(servName) && 
                serviceList.get(i).isActive())
                    serviceList.get(i).changeState(userName, state, price);
        }
        updateTable();
     }
   
    public boolean addNewUser(String userName, ArrayList<String> servNames) {
        for(int i = 0; i < servNames.size(); i++) {
            for(int k = 0; k < serviceList.size(); k++) {
                if(serviceList.get(k).isSameService(servNames.get(i)) && serviceList.get(k).isActive()) {
                    serviceList.get(k).addUser(userName);
                }
            }
        }
        updateTable();
        return true;
    }
    public boolean addNewUsers(ArrayList<String> users, String servName) {
        for(int i = 0; i < users.size(); i++) 
            this.addNewUser(users.get(i), servName);
        updateTable();
        return true;
    }
    public boolean removeUsers(ArrayList<String> users, String servName) {
        for(int i = 0; i < users.size(); i++) 
            this.deleteUser(users.get(i), servName);
        updateTable();
        return true;
    }
    public void updateTable() {
        this.fireTableDataChanged();
    }
    
    public void addDataListener() {
        this.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                for(int i = 0; i < serviceList.size(); i++) {
                    if(i < table.getRowCount())
                        table.setRowHeight(i, 20 * Math.max(1, serviceList.get(i).numUsers()));
                }
            }
        });        
    }
    public String getUserName() {
        return username;
    }
}
