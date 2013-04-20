package Server;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;


public class Server {
	public static final int BUF_SIZE	= 4;
	public static final String IP		= "127.0.0.1";
	public static final int PORT		= 30000;
	public static HashMap<SelectionKey, UserEntry> userEntryMap;
	public static void accept(SelectionKey key) throws IOException {
		
		System.out.print("ACCEPT: ");
		
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		ByteBuffer buf = ByteBuffer.allocateDirect(Constants.BUF_SIZE);
		socketChannel.register(key.selector(), SelectionKey.OP_READ, buf);
		
		System.out.println("Connection from: " + socketChannel.socket().getRemoteSocketAddress());
	}
	
	public static void read(SelectionKey key) throws IOException {
		
		System.out.print("READ: ");
		
		int bytes = 0;
		ByteBuffer buf = (ByteBuffer)key.attachment();
                buf = ByteBuffer.allocate(Constants.BUF_SIZE);
                buf.clear();
		SocketChannel socketChannel = (SocketChannel)key.channel();
		
		try {
			while ((bytes = socketChannel.read(buf)) > 0);
			
			// check for EOF
			if (bytes == -1)
				throw new IOException("EOF");
			
			// if buffer is full, write it back, flipping it first
			if (! buf.hasRemaining()) {
				buf.flip();
				key.interestOps(SelectionKey.OP_WRITE);
				
				//Channels.newChannel(System.out).write(buf);
				//buf.clear();
			}
			
		} catch (IOException e) {
                        e.printStackTrace();
			System.out.println("Connection closed: " + e.getMessage());
			socketChannel.close();
			
		}
                buf.flip();
                ArrayList<String> message = ParseMessage.parseBytes(buf);
                int type = Integer.parseInt(message.get(0));
                message.remove(0);
                if(type == Constants.LOGIN)
                    login(key, message);
                if(type == Constants.OFFREQEUEST)
                    offerRequest(key, message);
                if(type == Constants.OFFSERVICE)
                    offerService(key, message);
                if(type == Constants.CANCELREQ)
                    cancelRequest(key, message);
                if(type == Constants.OFFACCEPT)
                    offerAccept(key, message);
                if(type == Constants.OFFEXCEED)
                    offerExceed(message);
                if(type == Constants.OFFREFUSED)
                    offerRefused(message);
	}
	public static void write(SelectionKey key, ByteBuffer buf) throws IOException {
		
		System.out.println("WRITE: ");
		
		int bytes;
		//ByteBuffer buf = (ByteBuffer)key.attachment();		
		SocketChannel socketChannel = (SocketChannel)key.channel();
		
		try {
			while ((bytes = socketChannel.write(buf)) > 0);
			
			if (! buf.hasRemaining()) {
				buf.clear();
				key.interestOps(SelectionKey.OP_READ);
			}
			
		} catch (IOException e) {
			System.out.println("Connection closed: " + e.getMessage());
			socketChannel.close();
			
		}
	}
	
	public static void main(String[] args) {
		userEntryMap = new HashMap<>();
		ServerSocketChannel serverSocketChannel	= null;
		Selector selector						= null;
		
		try {
			selector = Selector.open();
			
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(Constants.IP, Constants.PORT));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			while (true) {
				selector.select();
				for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
					SelectionKey key = it.next();
					it.remove();
                                        System.out.println("DDDD");
					if (key.isAcceptable()) {
                                            accept(key);
                                        }
					else if (key.isReadable()) {
						read(key);
                                                System.out.println("VVV");

                                        }
					else if (key.isWritable());
//						write(key);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			if (selector != null)
				try {
					selector.close();
				} catch (IOException e) {}
			
			if (serverSocketChannel != null)
				try {
					serverSocketChannel.close();
				} catch (IOException e) {}
		}

    }
        
    static void  login(SelectionKey key, ArrayList<String> message) {
        String username = message.get(0);
        String password = message.get(1);
        String type = message.get(2);
        UserEntry ue = new UserEntry(username, type, password, key);
        userEntryMap.put(key, ue);
    }
    static void offerRequest(SelectionKey key, ArrayList<String> message) {
       UserEntry ue = userEntryMap.get(key);
       if(ue == null)
           return;
       String sn = message.get(0);
       ue.services.add(sn);
       notifyProd(Constants.OFFREQEUEST, ue.userName, sn);
    }
    static void notifyProd(int type,String conName, String  serviceName) {
        Iterator it = userEntryMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            UserEntry ue = (UserEntry)pairs.getValue();
            if(ue.isOfInterest(serviceName))
                sendMessage(type, conName, serviceName, ue);
        }
    }
    static void sendMessage(int type, String conName, String servName, UserEntry ue) {
        try {
            ArrayList<String> message = new ArrayList<>();
            message.add("" + type);
            message.add(conName);
            message.add(servName);
            write(ue.sk, ParseMessage.constructMessage(message));
        } catch(Exception e) {
           e.printStackTrace(); 
        }
    } 
    static void cancelRequest(SelectionKey key, ArrayList<String> message) {
       UserEntry ue = userEntryMap.get(key);
       if(ue == null)
           return;
       String sn = message.get(0);
       ue.services.add(sn);
       notifyProd(Constants.CANCELREQ, ue.userName, sn);  
    }
    
    static void offerService(SelectionKey key, ArrayList<String> message) {
       UserEntry ue = userEntryMap.get(key);
       if(ue == null)
           return;
       String sn = message.get(0);
       ue.services.add(sn);
       notifyProd(Constants.OFFSERVICE, ue.userName, sn);
    }
    static void offerAccept(SelectionKey key, ArrayList<String> message) {
        UserEntry ue = userEntryMap.get(key);
        if(ue == null)
            return;
        
        
    }
    static void offerExceed(ArrayList<String> message) {
        
    }
    static void offerRefused(ArrayList<String> message) {
        
    }
}
