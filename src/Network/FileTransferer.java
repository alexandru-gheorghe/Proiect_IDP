/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import Mediator.Mediator;
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
    int quant;
    String fileName;
    String servName;
    Mediator med;
    public FileTransferer(String servName, Mediator med, String ipaddr, int port, int type, String fileName, int quant) {
        this.type = type;
        this.ipaddr = ipaddr;
        this.port =  port;
        this.fileName = fileName;
        this.quant = quant;
        this.servName = servName;
        this.med = med;
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
            socketChannel.connect(new InetSocketAddress(ipaddr, port));

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
			System.out.println("JUST SELECT");
		
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
            int totalBytes = 0;
            int bytes;
            while((bytes = rbc.read(bb)) > 0) {
                bb.flip();
                totalBytes += bytes;
                this.socketChannel.write(bb);
                if(totalBytes >= quant)
                    break;
                bb.clear();
                Thread.sleep(500);
                med.updateProgress(servName, totalBytes);
            }
            System.out.println("Write == " + totalBytes);
            rbc.close();
            //socketChannel.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
   }
   
    void readFile() {
    try {
            WritableByteChannel rbc = Channels.newChannel(new FileOutputStream(fileName));
            ByteBuffer bb = ByteBuffer.allocate(5);
            bb.clear();
            int totalBytes = 0;
            int bytes;
         
            while((bytes = this.socketChannel.read(bb)) >= 0) {
                
                bb.flip();
                if(bytes > 0)
                    rbc.write(bb);
                totalBytes += bytes;
                if(totalBytes >= quant)
                    break;
                bb.clear();
                if(bytes > 0) {
                    Thread.sleep(500);
                    med.updateProgress(servName, totalBytes);
                }
            }
            System.out.println("Read == " + totalBytes);
            rbc.close();
            socketChannel.close();
            serverSocketChannel.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
}
