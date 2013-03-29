/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService_Client;

import Mediator.Mediator;
import java.util.ArrayList;
import javax.swing.SwingWorker;

/**
 *
 * @author alex
 */
public class WebService_Client extends SwingWorker {
    Mediator med;
    public WebService_Client(Mediator med) {
        this.med = med;
        med.registerWebService_Client(this);
    }
    public boolean login(String username, String password) {
        return true;
    }
    public boolean logout() {
        return true;
    }
    public void sendServiceList() {
        
    }
    public void getGroupOfInt() {
        return;
    }
    public void sendServiceRequest() {
        
    }
    public void sendServiceCancel() {
        
    }
    public boolean sendAcceptOffer(String userName, String servName) {
        return true;
    }
    public boolean sendRefuseOffer(String userName, String servName) {
        return true;
        
    }
    public void sendServiceOffer() {
        
    }
    public boolean sendMakeOffer(String userName, String servName) {
        return true;
    }
    public boolean sendDropOffer(String userName, String servName) {
        return true;
        
    }
   public boolean ConsActivateService(String servName) {
       return true;
   }
   public boolean ConsDropRequestService(String servName) {
       return true;
   }
   public boolean receiveMakeOffer(String userName, String servName) {
       return med.receiveMakeOffer(userName, servName);
   }
   
   public boolean receiveDropOffer(String userName, String servName) {
       return med.receiveDropOffer(userName, servName);
   }
   public boolean receiveRequestOffer(String userName, String servName) {
        return med.receiveRequestOffer(userName, servName);
   }
   public boolean receiveCancelOffer(String userName, String servName) {
        return med.receiveCancelOffer(userName, servName);
   }
  
   public void startWebServiceListener() {
       this.execute();
   }

    @Override
        protected Object doInBackground()  {
            return null;
            
    }
    public boolean addNewUser(String username, ArrayList<String> servNames) {
        return this.med.addNewUser(username, servNames);
    }
   
   
}
