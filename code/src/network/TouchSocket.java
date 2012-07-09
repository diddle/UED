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

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TouchSocket extends Observable implements Runnable {
    public TouchSocket() {
        
    }
    
    public void startServer() {
        new Thread(this).start();
    }
    
    private void listen() {
        TouchBroadcaster tb  = new TouchBroadcaster();
        try {
            ServerSocket server = new ServerSocket(3333);
            while (true) {
                System.out.println("Server started.");
                System.out.println("Broadcast sent.");
                // one client at a time
                System.out.println("Waiting for clients...");
                tb.startBroadcast();
                Socket client = server.accept();
                tb.stopBroadcast();
                System.out.println("Client accepted.");
                DataInputStream in = new DataInputStream(client.getInputStream());
                boolean connected = true;
                while (connected) {
                    try {
                        // stel pakket samen
                        TouchPacket p = new TouchPacket();
                        p.id = readUnsignedInt(in);
                        //System.out.println("Read: id (" + p.id + ")");
                        p.x = readUnsignedInt(in);
                        //System.out.println("Read: x (" + p.x + ")");
                        p.y = readUnsignedInt(in);
                        //System.out.println("Read: y (" + p.y + ")");
                        p.touch = readUnsignedInt(in);
                        //System.out.println("Read: touch (" + p.touch + ")");
                        this.setChanged();
                        this.notifyObservers(p);
                    } catch (IOException ex) {
                        connected = false;
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TouchSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private long readUnsignedInt(DataInputStream in) throws IOException{
        byte[] val = new byte[4];
        val[0] = in.readByte();
        val[1] = in.readByte();
        val[2] = in.readByte();
        val[3] = in.readByte();
//        data[offset+0]=(value>>0) & 0xFF;
//        data[offset+1]=(value>>8) & 0xFF;
//        data[offset+3]=(value>>24) & 0xFF;
//        data[offset+3]=(value>>24) & 0xFF;
        long result = ((long)val[0]) + (((long)val[1]) << 8) + (((long)val[2]) << 16) + (((long)val[3]) << 24);
        return result;

    }

    @Override
    public void run() {
        this.listen();
    }
}
