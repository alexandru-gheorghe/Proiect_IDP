/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.ArrayList;

/**
 *
 * @author alex
 */
public class UserEntry {
    String userName;
    String type;
    ArrayList<String> services;
    public UserEntry(String userName) {
        this.userName = new String(userName);
    }
}
