/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService_Client;

import Mediator.Mediator;
import java.util.ArrayList;

/**
 *
 * @author alex
 */
public class MockConsWebService_Client  extends WebService_Client{

    String userName = "Test";
    String servFixName = "Cumpar";
    ArrayList<String> servNames;
    public MockConsWebService_Client(Mediator med) {
        super(med);
        servNames = new ArrayList<>();
    }
    public void loginNewUserSimulation() {
        System.out.println("Login");
        for(int i = 0; i < 9; i++) {
            servNames.add(servFixName+i);
        }
        addNewUser(userName, servNames);
    }
    
    public void makeOfferSimulation() {
        System.out.println("Make Offer");
        for(int i = 0; i < 9; i = i + 2) {
            receiveMakeOffer(userName, servNames.get(i));
        }
    }
    public void dropOfferSimulation() {
        System.out.println("Drop Offer");
        for(int i = 0; i < 9; i = i + 2) {
            receiveDropOffer(userName, servNames.get(i));
        }
    }
    
    protected Object doInBackground()  {
        while(true) {
            try {
                int delay = 3000;
                Thread.sleep(4*delay);
                loginNewUserSimulation();
                Thread.sleep(delay);
                makeOfferSimulation();
                Thread.sleep(4*delay);
                dropOfferSimulation();
                break;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
            
    }
    public void getGroupOfInt() {
        System.out.println("VVVV");
        med.addNewUser(userName, servNames);
    }
    
}
