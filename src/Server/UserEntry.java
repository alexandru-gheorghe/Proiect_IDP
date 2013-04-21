/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author alex
 */
public class UserEntry {
    String userName;
    String type;
    String password;
    ArrayList<String> services;
    HashMap<String, Integer> hashServices;
    HashMap<String, Integer>  offState;
    SelectionKey    sk;
    public UserEntry(String userName, String type, String password, SelectionKey sk) {
        this.userName = new String(userName);
        this.type = new String(type);
        this.password = new String(password);
        this.sk = sk;
        this.services = new ArrayList<>();
        hashServices  = new HashMap<>();
        offState      = new HashMap<>();
    }
    boolean isOfInterest(String servName) {
        for(int i = 0; i < services.size(); i++) {
            if(services.get(i).compareTo(servName) == 0)
                return true;
        }        
        return false;
    }
    boolean hasOffer(String servKey) {
        return hashServices.containsKey(servKey);
    }
    int getOffer(String servKey) {
        if(hasOffer(servKey)) 
            return (Integer)hashServices.get(servKey);
        return -1;
    }
    boolean hasType(String type) {
        return this.type.compareTo(type) == 0;
    }
    boolean isProd() {
        return (type.compareTo(Constants.PROD) == 0);
    }
    boolean offActive(String servKey) {
        if(offState.containsKey(servKey)) {
            int state = (Integer)offState.get(servKey);
            return state == Constants.ACTSTATE;
        }
        return false;
    }
    void setState(String servKey, int state) {
        offState.put(servKey, state);
    }
}
