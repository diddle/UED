/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Niels Visser
 */
public class TouchSocket extends Observable implements Runnable {
    public TouchSocket() {
        
    }
    
    public void startServer() {
        new Thread(this).start();
    }
    
    private void broadcast() {
        try {
            DatagramSocket socket = new DatagramSocket();
//            ByteBuffer dataBuffer = ByteBuffer.allocate(64);
//            dataBuffer.flip();
//            byte[] data = new byte[dataBuffer.limit()];
//            dataBuffer.get(data);
            byte[] data = "TOUCHKITTBC".getBytes();
            int udpPort = 3330;
            for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress address : Collections.list(iface.getInetAddresses())) {
                    if (!address.isSiteLocalAddress()) {
                        continue;
                    }
                    // Java 1.5 doesn't support getting the subnet mask, so try the two most common.
                    byte[] ip = address.getAddress();
                    ip[3] = -1; // 255.255.255.0
                    socket.send(new DatagramPacket(data, data.length, InetAddress.getByAddress(ip), udpPort));
                    ip[2] = -1; // 255.255.0.0
                    socket.send(new DatagramPacket(data, data.length, InetAddress.getByAddress(ip), udpPort));
                }
            }
            //        try {
            //            MulticastSocket s;
            //            InetAddress address = InetAddress.getByName("255.255.255.255");
            //            int port = 3330;
            //            String msg = "TOUCHKITTBC";
            //            Socket socket = new Socket(address, port);
            //            OutputStream out = socket.getOutputStream();
            //            byte [] output = msg.getBytes();
            //            out.write(output);
            //            out.flush();
            //        } catch (IOException ex) {
            //        }
            //        }
        } catch (IOException ex) {
            Logger.getLogger(TouchSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void listen() {
        while(true) {
            try {
                ServerSocket server = new ServerSocket(3333);
                System.out.println("Server started.");
                this.broadcast();
                System.out.println("Broadcast sent.");
                // one client at a time
                while(true) {
                    System.out.println("Waiting for clients...");
                    Socket client = server.accept();
                    System.out.println("Client accepted.");
                    DataInputStream in = new DataInputStream(client.getInputStream());
                    while(true) {
                        // stel pakket samen
                        TouchPacket p = new TouchPacket();
                        p.id = in.readInt();
                        System.out.println("Read: id (" + p.id + ")");
                        p.x = in.readInt();
                        System.out.println("Read: x (" + p.x + ")");
                        p.y = in.readInt();
                        System.out.println("Read: y (" + p.y + ")");
                        p.touch = in.readInt();
                        System.out.println("Read: touch (" + p.touch + ")");
                        this.notifyObservers(p);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(TouchSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void run() {
        this.listen();
    }
}
