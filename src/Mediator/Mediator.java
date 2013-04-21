/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mediator;

import GUI_Package.ServListModel;
import GUI_Package.ServTableModel;
import GUI_Package.ServiceListGUI;
import Network.Network;
import WebService_Client.WebService_Client;
import java.util.ArrayList;

/**
 *
 * @author alex
 */
public class Mediator {
    Network network;
    WebService_Client clt;
    ServTableModel guiModel;
    public Mediator() {
        this.network = null;
        this.clt = null;
        this.guiModel = null;
    }
    public String getUserName() {
        return guiModel.getUserName();
    }
    public boolean addNewUsers(ArrayList<String> users, String servName) {
        guiModel.addNewUsers(users, servName);
        return true;
    }
    public void registerNetwork(Network network) {
        this.network = network;
    }
    public void registerWebService_Client(WebService_Client clt) {
        this.clt = clt;
    }
    public void registerServiceListModel(ServTableModel guiModel) {
        this.guiModel = guiModel;
        System.err.print("DD");
    }
 
    public boolean login(String username, String password, String typeName) {
        return network.login(username, password, typeName);
    }
    public void startNetworkService() {
        network.startNetworkService();
    }
    public void updateTable() {
        guiModel.updateTable();
    }
    public boolean ConsActivateService(String servName) {
        return this.clt.ConsActivateService(servName);
    }
    public boolean ConsDropRequestService(String servName) {
        return this.clt.ConsDropRequestService(servName);
    }
    
    public boolean sendMakeOffer(String userName, String servName) {
        return clt.sendMakeOffer(userName, servName);
    }
    public boolean sendDropOffer(String userName, String servName) {
        return clt.sendDropOffer(userName, servName);
    }
    public void sendOfferRequest(String userName, String servName) {
        this.network.sendOfferRequest(userName, servName);
    }
    public boolean sendAcceptOffer(String userName, String servName) {
        return clt.sendAcceptOffer(userName, servName);
    }
    public boolean sendRefuseOffer(String userName, String servName) {
        return clt.sendRefuseOffer(userName, servName);
    } 
    public ArrayList<String> sendOfferService(String userName, String servName) {
        return network.sendOfferService(userName, servName);
    }
    public boolean receiveMakeOffer(String userName, String servName) {
       return guiModel.receiveMakeOffer(userName, servName);
    }
    public boolean receiveDropOffer(String userName, String servName) {
       return guiModel.receiveDropOffer(userName, servName);
   }
   public boolean receiveRequestOffer(String userName, String servName) {
        return guiModel.receiveRequestOffer(userName, servName);
   }
   public boolean receiveCancelOffer(String userName, String servName) {
        return guiModel.receiveCancelOffer(userName, servName);
   }
    public boolean logout() {
        return clt.logout();
    }
    public void getGroupOfInt() {
        clt.getGroupOfInt();
    }
    public boolean addNewUser(String username, String servName) {
        return this.guiModel.addNewUser(username, servName);
    }
 
    public boolean addNewUser(String username, ArrayList<String> servNames) {
        return this.guiModel.addNewUser(username, servNames);
    }
    public void startWebServiceListener() {
        clt.startWebServiceListener();
    }
    

    
}
