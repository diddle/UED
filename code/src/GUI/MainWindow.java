/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import playback.Player;

/**
 *
 * @author Niels Visser
 */
public class MainWindow extends JFrame {
    public MainWindow(Player p) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.fullScreen();
        //this.setBounds ( 10 , 10 , 200 , 200 );
        //this.setVisible(true);
        GridPanel gf = new GridPanel(p);
        this.add(gf);
        this.fullScreen();
        
    }
    
    private void fullScreen() {
        this.setResizable(false);
        if (!this.isDisplayable()) {
            // Can only do this when the frame is not visible
            this.setUndecorated(true);
        }
        //GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        GraphicsDevice gd = devices[devices.length-1];
        try {
            if (gd.isFullScreenSupported()) {
                gd.setFullScreenWindow(this);
            } else {
                // Can't run fullscreen, need to bodge around it (setSize to screen size, etc)
            }
            this.setVisible(true);
            // Your business logic here
        }
        finally {
            //gd.setFullScreenWindow(null);
        }
        
    }
}
