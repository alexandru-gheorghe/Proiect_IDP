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
       dest = new byte[Constants.BUF_SIZE];
       while(bb.hasRemaining()) {
           dest[index] = bb.get();
           index ++;
       }
       prevIndex = 0;
       index = 0;
       while(index < dest.length) {
           if(dest[index] == Constants.Start) {
               message.add(new String(dest, prevIndex, index - prevIndex));
               prevIndex = index+1;
           }
           index++;
               
       }
       return message;
    
    }
    public static ByteBuffer constructMessage(ArrayList<String> message) {
        ByteBuffer bb = ByteBuffer.allocate(Constants.BUF_SIZE);
        bb.clear();
        for(int i = 0; i < message.size(); i++) {
            bb.put(message.get(i).getBytes());
            bb.put(Constants.Start);

        }
        bb.flip();
        return bb;
    }
    
}
