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
	public HashMap<SelectionKey, UserEntry> userEntryMap;
	public static void accept(SelectionKey key) throws IOException {
		
		System.out.print("ACCEPT: ");
		
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		ByteBuffer buf = ByteBuffer.allocateDirect(BUF_SIZE);
		socketChannel.register(key.selector(), SelectionKey.OP_READ, buf);
		
		System.out.println("Connection from: " + socketChannel.socket().getRemoteSocketAddress());
	}
	
	public static void read(SelectionKey key) throws IOException {
		
		System.out.print("READ: ");
		
		int bytes = 0;
		ByteBuffer buf = (ByteBuffer)key.attachment();
                buf = ByteBuffer.allocate(Constants.BUF_SIZE);
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
			System.out.println("Connection closed: " + e.getMessage());
			socketChannel.close();
			
		}
                ArrayList<String> message = ParseMessage.parseBytes(buf);
                int type = Integer.parseInt(message.get(0));
                message.remove(0);
                if(type == Constants.LOGIN)
                    login(message);
                if(type == Constants.OFFREQEUEST)
                    offerRequest(message);
                if(type == Constants.OFFSERVICE)
                    offerService(message);
                if(type == Constants.CANCELREQ)
                    cancelRequest(message);
                if(type == Constants.OFFACCEPT)
                    offerAccept(message);
                if(type == Constants.OFFEXCEED)
                    offerExceed(message);
                if(type == Constants.OFFREFUSED)
                    offerRefused(message);
	}
	public static void write(SelectionKey key) throws IOException {
		
		System.out.println("WRITE: ");
		
		int bytes;
		ByteBuffer buf = (ByteBuffer)key.attachment();		
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
		
		ServerSocketChannel serverSocketChannel	= null;
		Selector selector						= null;
		
		try {
			selector = Selector.open();
			
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(IP, PORT));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			while (true) {
				selector.select();
				for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
					SelectionKey key = it.next();
					it.remove();
					
					if (key.isAcceptable())
						accept(key);
					else if (key.isReadable())
						read(key);
					else if (key.isWritable())
						write(key);
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
        
    static void  login(ArrayList<String> message) {
        
    }
    static void offerRequest(ArrayList<String> message) {
        
    }
    static void cancelRequest(ArrayList<String> message) {
        
    }
    static void offerService(ArrayList<String> message) {
        
    }
    static void offerAccept(ArrayList<String> message) {
        
    }
    static void offerExceed(ArrayList<String> message) {
        
    }
    static void offerRefused(ArrayList<String> message) {
        
    }
}
