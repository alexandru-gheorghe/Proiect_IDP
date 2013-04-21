/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import Server.Constants;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.util.Iterator;
import javax.swing.SwingWorker;

/**
 *
 * @author alex
 */
public class FileTransferer extends SwingWorker{
    static int type;
    ServerSocketChannel serverSocketChannel;
    SocketChannel socketChannel;
    String ipaddr;
    int port;
    String fileName;
    FileTransferer(String ipaddr, int port, int type, String fileName) {
        this.type = type;
        this.ipaddr = ipaddr;
        this.port =  port;
        this.fileName = fileName;
    }
    @Override
    protected Object doInBackground() throws Exception {
        if(type == Constants.READOP) {
            this.connect();
            writeFile();
        } else {
            this.accept();
            readFile();
        }
        
        return null;
    }
    public void connect(){
	Selector selector = null;
        socketChannel = null;
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
                        key.interestOps(SelectionKey.OP_READ);
                        running = false;
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        } 
    }
    public void accept() throws IOException {
		serverSocketChannel	= null;
		Selector selector						= null;
		
		try {
			selector = Selector.open();
			
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(this.ipaddr, this.port));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
		
                        selector.select();
			
		SelectionKey key = selector.selectedKeys().iterator().next();
		System.out.print("ACCEPT: ");
		
		socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		ByteBuffer buf = ByteBuffer.allocateDirect(Constants.BUF_SIZE);
		socketChannel.register(key.selector(), SelectionKey.OP_WRITE, buf);
		
		System.out.println("Connection from: " + socketChannel.socket().getRemoteSocketAddress());
	} catch(Exception e) {
            e.printStackTrace();
        }
    }
    void writeFile() {
        try {
            ReadableByteChannel rbc = Channels.newChannel(new FileInputStream(fileName));
            ByteBuffer bb = ByteBuffer.allocate(5);
            bb.clear();
            while(rbc.read(bb) > 0) {
                bb.flip();
                this.socketChannel.write(bb);
                bb.clear();
            }
            rbc.close();
            socketChannel.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
   }
   
    void readFile() {
    try {
            WritableByteChannel rbc = Channels.newChannel(new FileOutputStream(fileName));
            ByteBuffer bb = ByteBuffer.allocate(5);
            bb.clear();
            while(this.socketChannel.read(bb) > 0) {
                bb.flip();
                rbc.write(bb);
                bb.clear();
            }
            rbc.close();
            socketChannel.close();
            serverSocketChannel.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
}
