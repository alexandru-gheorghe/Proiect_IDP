package Server;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;
import org.apache.log4j.*;
import Server.Logare;

public class Server {
	public static HashMap<SelectionKey, UserEntry> userEntryMap;
        public static HashMap<String, String>   accServiceMap;
        public static Logger logger;
        
	public static void accept(SelectionKey key) throws IOException {
		
		System.out.print("ACCEPT: ");
		
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		ByteBuffer buf = ByteBuffer.allocateDirect(Constants.BUF_SIZE);
		socketChannel.register(key.selector(), SelectionKey.OP_READ, buf);
		
		System.out.println("Connection from: " + socketChannel.socket().getRemoteSocketAddress());
	}
	
	public static void read(SelectionKey key) throws Exception {
		
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
                    offerRefused(key, message);
                if(type == Constants.DROPREQ)
                    dropRequest(key, message);
                if(type == Constants.OFFMAKE)
                    makeOffer(key, message);
                if(type == Constants.OFFDROP)
                    dropOffer(key, message);
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
                accServiceMap = new HashMap<>();
		ServerSocketChannel serverSocketChannel	= null;
		Selector selector						= null;
		logger = Logare.initLogger("server.log");
                
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
			
		} catch (Exception e) {
                        System.out.println("IOException");
			e.printStackTrace();
			
		} finally {
			if (selector != null)
				try {
					selector.close();
				} catch (IOException e) {
                                    e.printStackTrace();
                                }
			
			if (serverSocketChannel != null)
				try {
					serverSocketChannel.close();
				} catch (IOException e) {
                                    e.printStackTrace();
                                }
		}

    }
        
    static void  login(SelectionKey key, ArrayList<String> message) {
        String username = message.get(0);
        String password = message.get(1);
        String type = message.get(2);
        UserEntry ue = new UserEntry(username, type, password, key);
        userEntryMap.put(key, ue);
        ArrayList<String> reply = new ArrayList<>();
        reply.add(Constants.LOGINACCEPT + "");
        try {
            write(key, ParseMessage.constructMessage(reply));
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    static void offerRequest(SelectionKey key, ArrayList<String> message) throws Exception {
       UserEntry ue = userEntryMap.get(key);
       if(ue == null)
           return;
       String sn = message.get(0);
       ue.services.add(sn);
       ArrayList<String> reply = getGroup(sn, Constants.PROD);
       if(reply.size() > 0) {
            reply.add(0, Constants.OFFSERVICE + "");
            reply.add(1, sn);
       }
       write(key, ParseMessage.constructMessage(reply));
       notifyProd(Constants.OFFREQEUEST, ue.userName, sn, Constants.PROD);
       
        logger.info("offerRequest : from:" + ue.userName + "; Prod:" + sn);
    }
    static ArrayList<String> getGroup(String serviceName, String clientType) {
        Iterator it = userEntryMap.entrySet().iterator();
        ArrayList<String> prodList = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            UserEntry ue = (UserEntry)pairs.getValue();
            if(ue.isOfInterest(serviceName) && ue.hasType(clientType))
                prodList.add(ue.userName);
        }
        
        return prodList;
    }
    static void notifyProd(int type, String conName, String  serviceName, String clientType) {
        Iterator it = userEntryMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            UserEntry ue = (UserEntry)pairs.getValue();
            if(ue.isOfInterest(serviceName) && ue.hasType(clientType))
                sendMessage(type, conName, serviceName, ue);
        }
    }
    static void sendMessage(int type, String conName, String servName, UserEntry ue) {
        try {
            ArrayList<String> message = new ArrayList<>();
            message.add("" + type);
            message.add(servName);
            message.add(conName);
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
       notifyProd(Constants.CANCELREQ, ue.userName, sn, Constants.PROD);  
    }
    
    static void offerService(SelectionKey key, ArrayList<String> message) throws Exception {
       UserEntry ue = userEntryMap.get(key);
       if(ue == null)
           return;
       String sn = message.get(0);
       ue.services.add(sn);
       ArrayList<String> reply = getGroup(sn, Constants.CON);
       if(reply.size() > 0) {
            reply.add(0, Constants.OFFREQEUEST + "");
            reply.add(1, sn);
       }
       write(key, ParseMessage.constructMessage(reply));
       notifyProd(Constants.OFFSERVICE, ue.userName, sn, Constants.CON);
       
       logger.info("offerService : from:" + ue.userName + "; Service:" + sn);
    }
    static void offerAccept(SelectionKey key, ArrayList<String> message) throws Exception {
        UserEntry ue = userEntryMap.get(key);
        if(ue == null)
            return;
        UserEntry winUe;
        String serviceName = message.get(0);
        String winName = message.get(1);
        String quant = message.get(2);
        String port = message.get(3);
        String clientType = Constants.PROD;
        Iterator it = userEntryMap.entrySet().iterator();
        if(accServiceMap.containsKey(ue.userName + serviceName)) {
            sendMessage(Constants.INVALACC, "", "", ue);
            return;
        }
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            UserEntry ueIt = (UserEntry)pairs.getValue();
            if(ueIt.userName.compareTo(winName) != 0 &&
                ueIt.isOfInterest(serviceName) && ueIt.hasType(clientType))
                sendMessage(Constants.OFFREFUSED, ue.userName, serviceName, ueIt);
            if(ueIt.userName.compareTo(winName) == 0) {
                ArrayList<String> reply = new ArrayList<>();
                reply.add(Constants.OFFACCEPT + "");
                reply.add(serviceName);
                reply.add(ue.userName);
                reply.add(quant);
                reply.add(port);
                write(ueIt.sk, ParseMessage.constructMessage(reply));
            }
        }
        accServiceMap.put(ue.userName + serviceName, winName);
        
        logger.info("offerAccept : from:" + ue.userName + "; Service:" + serviceName + "; to:" + winName + "; Quant:" + quant);
    }
    static void dropRequest(SelectionKey key, ArrayList<String> message) {
        UserEntry ue = userEntryMap.get(key);
        if(ue == null)
            return;
        String sn = message.get(0);
        notifyProd(Constants.DROPREQ, ue.userName, sn, Constants.PROD);
        
        logger.info("dropRequest : from:" + ue.userName + "; Service:" + sn);
    }
    static void offerExceed(ArrayList<String> message) {
        
    }
    
    static void offerRefused(SelectionKey key, ArrayList<String> message) throws Exception{
        UserEntry ue = userEntryMap.get(key);
        notifyUser(Constants.OFFREFUSED, message.get(1), message.get(0), ue.userName, "");
        
        logger.info("dropRequest : from:" + ue.userName + "; For:" + message.get(1) + "; Service:" + message.get(0));
    }

    private static void makeOffer(SelectionKey key, ArrayList<String> message) throws Exception{
        UserEntry ue = userEntryMap.get(key);
        String sn = message.remove(0);
        String user = message.remove(0);
        int price = Integer.parseInt(message.remove(0));
        String servKey = user + sn;
        ue.hashServices.put(servKey, price);
        Iterator it = userEntryMap.entrySet().iterator();
        int maxOffer = -1;
        UserEntry actUe = null;
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            ue = (UserEntry)pairs.getValue();
            if(ue.offActive(servKey)) {
                actUe = ue;
            }
        }
        System.out.println(servKey);
        ue = userEntryMap.get(key);
        if(actUe == null || actUe.userName.compareTo(ue.userName) == 0) {
            ue.setState(servKey, Constants.ACTSTATE);
            notifyUser(Constants.OFFMAKE, user, sn, ue.userName, price + "");
            return;
        }
        System.out.println("ue " + ue.userName + "actue = " + actUe.userName);
        if(ue.getOffer(servKey) < actUe.getOffer(servKey)) {
            ue.setState(servKey, Constants.EXSTATE);
            System.out.println("Offer to low");
            sendMessage(Constants.OFFEXCEED, user, sn, ue);
            actUe.setState(servKey, Constants.ACTSTATE);
            
        } else {
            System.out.println("Offer to high");
            ue.setState(servKey, Constants.ACTSTATE);
            actUe.setState(servKey, Constants.EXSTATE);
            sendMessage(Constants.OFFEXCEED, user, sn, actUe);

        }
        notifyUser(Constants.OFFMAKE, user, sn, ue.userName, price + "");
        
        logger.info("makeOffer : from:" + ue.userName + "; To:" + user + "; Service:" + sn + "; Price:" + price);
    }
    
    public static void notifyUser(int action, String userName, String servName, String userProd, String price) throws Exception{
        Iterator it = userEntryMap.entrySet().iterator();
        System.out.println("Notify User" + userName + servName + price);
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            UserEntry   ue = (UserEntry)pairs.getValue();
            if(ue.userName.compareTo(userName) == 0) {
                ArrayList<String> message = new ArrayList<>();
                message.add(action + "");
                message.add(servName);
                message.add(userProd);
                message.add(price);
                write(ue.sk, ParseMessage.constructMessage(message));
                return;
            }
        }
    }
    public static void dropOffer(SelectionKey key, ArrayList<String> message) throws Exception{
        UserEntry ue = userEntryMap.get(key);
        ue.setState(message.get(0) + message.get(1), Constants.EXSTATE);
        notifyUser(Constants.OFFDROP, message.get(1), message.get(0), ue.userName, "");
        
        logger.info("dropOffer : from:" + ue.userName + "; To:" + message.get(1) + "; Service:" + message.get(0));
    }
}
