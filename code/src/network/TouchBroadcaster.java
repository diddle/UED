/*	Copyright Niels Visser 2012
 *	
 *	This file is part of MusicTable.
 *	
 *	MusicTable is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	MusicTable is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with MusicTable.  If not, see <http://www.gnu.org/licenses/>.
 */
package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TouchBroadcaster implements Runnable {
    
    private boolean broadcasting;
    
    public TouchBroadcaster() {
        this.broadcasting = false;
        new Thread(this).start();
    }
    
    public void startBroadcast() {
        this.broadcasting = true;
    }
    
    public void stopBroadcast() {
        this.broadcasting = false;
    }
    
    private void broadcast() {
        try {
            DatagramSocket socket = new DatagramSocket();
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
        } catch (IOException ex) {
            Logger.getLogger(TouchSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                if(this.broadcasting) {
                    this.broadcast();
                    //System.out.println("Broadcasting...");
                }
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TouchBroadcaster.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
