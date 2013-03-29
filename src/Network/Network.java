/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import Mediator.Mediator;
import javax.swing.SwingWorker;

/**
 *
 * @author alex
 */
public class Network extends SwingWorker{
    Mediator med;
    public Network(Mediator med) {
        this.med = med;
        med.registerNetwork(this);
    }
    public void startNetworkService() {
        //this.execute();
    }
    @Override
    protected Object doInBackground()  {
        int i = 0;
        try {
            Thread.sleep(5000);
            for(i = 0 ; i < 100 ; i++) {
                System.out.println("Alex");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
            return null;
            
    }
    public void sendFile() {
        this.execute();
    }
    public void receiveFile() {
        this.execute();
    }
}
