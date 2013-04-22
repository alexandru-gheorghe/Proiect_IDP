/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mediator;

import GUI_Package.ProdState;
import GUI_Package.ServListModel;
import GUI_Package.ServTableModel;
import GUI_Package.ServiceListGUI;
import Network.Network;
import Server.Constants;
import WebService_Client.WebService_Client;
import java.util.ArrayList;
import org.apache.log4j.*;
import Server.Logare;

/**
 *
 * @author alex
 */
public class Mediator {
    Network network;
    WebService_Client clt;
    ServTableModel guiModel;
    public static Logger logger;
    
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
    public boolean removeUsers(ArrayList<String> users, String servName) {
        guiModel.removeUsers(users, servName);
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
        logger = Logare.initLogger(username + "-" + password + ".log");
        logger.info("Logged : " + username + "; pass:" + password + "; type:" + typeName);
        
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
    
    public boolean sendMakeOffer(String userName, String servName, String price) {
        logger.info("sendMakeOffer : to:" + userName + "; Service:" + servName + "; Price:" + price);
        
        return network.sendMakeOffer(userName, servName, price);
    }
    public boolean sendDropOffer(String userName, String servName) {
        logger.info("sendDropOffer : to:" + userName + "; Service:" + servName);
        
        return network.sendDropOffer(userName, servName);
    }
    public void sendOfferRequest(String userName, String servName) {
        logger.info("sendOfferRequest : to:" + userName + "; Service:" + servName);
        
        this.network.sendOfferRequest(userName, servName);
    }
    public void sendDropRequest(String servName) {
        logger.info("sendDropRequest : Service:" + servName);
        
        this.network.sendDropRequest(servName);
    }
    public boolean sendAcceptOffer(String userName, String servName, String quant) {
        logger.info("sendAcceptOffer : to:" + userName + "; Service:" + servName + "; Quant:" + quant);
         
        return network.sendAcceptOffer(userName, servName, quant);
    }
    public boolean sendRefuseOffer(String userName, String servName) {
        logger.info("sendRefuseOffer : to:" + userName + "; Service:" + servName);
        
        return network.sendRefuseOffer(userName, servName);
    } 
    public ArrayList<String> sendOfferService(String userName, String servName) {
        logger.info("sendOfferService : to:" + userName + "; Service:" + servName);
        
        return network.sendOfferService(userName, servName);
    }
    public boolean receiveMakeOffer(String userName, String servName, String price) {
       logger.info("receiveMakeOffer : from:" + userName + "; Service:" + servName + "; Price:" + price);
        
       return guiModel.receiveMakeOffer(userName, servName, price);
    }
    public boolean receiveDropOffer(String userName, String servName) {
       logger.info("receiveDropOffer : from:" + userName + "; Service:" + servName);
       
       return guiModel.receiveDropOffer(userName, servName);
   }
   public boolean receiveRequestOffer(String userName, String servName) {
       logger.info("receiveRequestOffer : from:" + userName + "; Service:" + servName);
       
       return guiModel.receiveRequestOffer(userName, servName);
   }
   public boolean receiveCancelOffer(String userName, String servName) {
       logger.info("receiveCancelOffer : from:" + userName + "; Service:" + servName);
       
        return guiModel.receiveCancelOffer(userName, servName);
   }
   public boolean receiveOfferRefused(String userName, String servName) {
       guiModel.receiveOfferRefused(userName, servName);
       logger.info("receiveOfferRefused : from:" + userName + "; Service:" + servName);
       
       return true;
   }
   public boolean receiveOfferAccept(String userName, String servName, String quant, int port) {
       guiModel.receiveOfferAccept(userName, servName, quant, port);
       logger.info("receiveOfferAccept : from:" + userName + "; Service:" + servName + "; Quant:" + quant + "; Port:" + port);
       
       return true;
   }
   public boolean receiveOfferExceed(String userName, String servName) {
       guiModel.changeState(userName, servName, ServListModel.offerExceeded);
       logger.info("receiveOfferExceed : from:" + userName + "; Service:" + servName);
       
       return true;
   }
    public boolean logout() {
        logger.info("____Logged out____");
        return network.logout();
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
    public void updateProgress(String servName, int progress) {
        guiModel.updateProgress(servName, progress);
    }

    
}
