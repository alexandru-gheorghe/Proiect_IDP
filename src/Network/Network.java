/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import Mediator.Mediator;
import javax.swing.SwingWorker;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;
import Server.Constants;
import Server.ParseMessage;

/**
 *
 * @author alex
 */
public class Network extends SwingWorker{
    Mediator med;
    static SelectionKey serverKey;
    static Selector selector;
    public Network(Mediator med) {
        this.med = med;
        med.registerNetwork(this);
    }
    
    public boolean login(String username, String password, String typeName) {
        connect();
        
        ArrayList<String> message = new ArrayList<String>();
        message.add(Constants.LOGIN + "");
        message.add(username);
        message.add(password);
        message.add(typeName);                           
        ByteBuffer bb = ParseMessage.constructMessage(message);
        
        try {
            write(bb);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        try {
         bb = read();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
       message = ParseMessage.parseBytes(bb);
       System.out.println("Login reply " + message.get(0));
       message.get(0).compareTo("8");
       return true;
    }
    
     public void sendOfferRequest(String userName, String servName) {
         try {
             ArrayList<String> message = new ArrayList<>();
             message.add(Constants.OFFREQEUEST + "");
             message.add(servName);
             write(ParseMessage.constructMessage(message));
             /*
             ByteBuffer bb = read();
             message = ParseMessage.parseBytes(bb);
             return message;
             * */
         } catch(Exception e) {
             e.printStackTrace();
         }
     }
     public ArrayList<String> sendOfferService(String userName, String servName) {
         try {
             ArrayList<String> message = new ArrayList<>();
             message.add(Constants.OFFSERVICE + "");
             message.add(servName);
             write(ParseMessage.constructMessage(message));
             ByteBuffer bb = read();
             message = ParseMessage.parseBytes(bb);
             return message;
         } catch(Exception e) {
             e.printStackTrace();
         }
         return null;
     }
    
    public void startNetworkService() {
        this.execute();
    }
    @Override
    protected Object doInBackground()  {

        while(true) {
            try {
                ByteBuffer bb = read();
                ArrayList<String> message;
                message = ParseMessage.parseBytes(bb);
                int type = Integer.parseInt(message.remove(0));
                if(type == Constants.OFFREQEUEST) {
                    String servName = message.remove(0);
                    med.addNewUsers(message, servName);
                }
                if(type == Constants.OFFSERVICE) {
                    String servName = message.remove(0);
                    med.addNewUsers(message, servName);
                }
                    
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void sendFile() {
        this.execute();
    }
    public void receiveFile() {
        this.execute();
    }
    
    public void write(ByteBuffer bb) throws IOException {

        System.out.println("WRITE: ");

        
        SocketChannel socketChannel = (SocketChannel)serverKey.channel();

        while (socketChannel.write(bb) > 0);

        if (! bb.hasRemaining()) {
            //socketChannel.close();
            
        }
    }
    
    public ByteBuffer read() throws IOException {
		
        System.out.print("READ: ");

        int bytes = 0;
        ByteBuffer buf = ByteBuffer.allocateDirect(Constants.BUF_SIZE);
        buf.clear();
        SocketChannel socketChannel = (SocketChannel)serverKey.channel();
        selector.select();
        try {
            while ((bytes = socketChannel.read(buf)) > 0);

            // check for EOF
            if (bytes == -1)
                throw new IOException("EOF");

            // if buffer is full, write it back, flipping it first
            if (! buf.hasRemaining()) {
                buf.flip();

                //Channels.newChannel(System.out).write(buf);
                //buf.clear();
            }

        } catch (IOException e) {
            System.out.println("Connection closed: " + e.getMessage());
            socketChannel.close();

        }
        buf.flip();
        return buf;
    }
    
    public static void connect(){
	selector = null;
        SocketChannel socketChannel = null;
        boolean running = true;
        
        try {
            selector = Selector.open();

            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(Constants.IP, Constants.PORT));


            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            while (running) {
                selector.select();

                for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
                    SelectionKey key = it.next();
                    it.remove();

                    if (key.isConnectable()) {
                        
                        System.out.print("CONNECT: ");

                        if (! socketChannel.finishConnect()) {
                            System.err.println("Eroare finishConnect");
                            running = false;
                        }

                        //socketChannel.close();
                        serverKey = key;
                        key.interestOps(SelectionKey.OP_READ);
                        
                        running = false;
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        } 
        
    }
}
