/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author alex
 */
public class UserEntry {
    String userName;
    String type;
    ArrayList<String> services;
    HashMap<String, Integer> offers;
    
    public UserEntry(String userName) {
        this.userName = new String(userName);
    }
    
    
    
}
