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
public class MockProdWebService_Client extends WebService_Client{
    String userName = "Test";
    String servFixName = "Vand";
    ArrayList<String> servNames;
    public MockProdWebService_Client(Mediator med) {
        super(med);
        servNames = new ArrayList<>();
    }
    public void activateServiceSimulation() {
        System.out.println("Activate");
        for(int i = 0; i < 9; i++) {
            servNames.add(servFixName+i);
        }
        addNewUser(userName, servNames);
    }
    
    public void requestOfferSimulation() {
        System.out.println("Request Offer");
        for(int i = 0; i < 9; i = i + 2) {
            receiveRequestOffer(userName+i, servNames.get(i));
        }
    }
    public void cancelOfferSimulation() {
        System.out.println(" Cancel Offer");
        for(int i = 0; i < 9; i = i + 2) {
            receiveCancelOffer(userName, servNames.get(i));
        }
    }
    protected Object doInBackground()  {
        while(true) {
            try {
                int delay = 3000;
                Thread.sleep(delay);
                activateServiceSimulation();
                Thread.sleep(2*delay);
                requestOfferSimulation();
                Thread.sleep(2*delay);
                cancelOfferSimulation();

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
