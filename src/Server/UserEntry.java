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
    SelectionKey    sk;
    public UserEntry(String userName, String type, String password, SelectionKey sk) {
        this.userName = new String(userName);
        this.type = new String(type);
        this.password = new String(password);
        this.sk = sk;
        this.services = new ArrayList<>();
        hashServices  = new HashMap<>();
    }
    boolean isOfInterest(String servName) {
        for(int i = 0; i < services.size(); i++) {
            if(services.get(i).compareTo(servName) == 0)
                return true;
        }        
        return false;
    }
    boolean hasType(String type) {
        return this.type.compareTo(type) == 0;
    }
    boolean isProd() {
        return (type.compareTo(Constants.PROD) == 0);
    }
}
