/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 *
 * @author alex
 */
public class ParseMessage {

    public static ArrayList<String> parseBytes(ByteBuffer bb) {
       ArrayList<String> message = new ArrayList<>();
       int index = 0;
       int prevIndex;
       byte []dest;
       while( Constants.Start != bb.get(index) || Constants.End != bb.get(index + 1))
            index++;
       dest = new byte[index - 1];
       bb.get(dest, 0, index - 1);
       message.add(new String(dest));
       while(true) {
            prevIndex = index + 2;
            index = index + 2;
            if(index >= bb.limit())
                return message;
            while( Constants.Start != bb.get(index) || Constants.End != bb.get(index + 1))
                index++;
            dest = new byte[index - prevIndex];
            message.add(new String(dest));
       }
       //return message;
    
    }
    public static ByteBuffer constructMessage(ArrayList<String> message) {
        ByteBuffer bb = ByteBuffer.allocate(Constants.BUF_SIZE);
        for(int i = 0; i < message.size(); i++) {
            bb.put(message.get(i).getBytes());
            bb.put(Constants.Start);
            bb.put(Constants.End);
        }
        return bb;
    }
    
}
