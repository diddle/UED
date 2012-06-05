/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import playback.Player;

/**
 *
 * @author Niels Visser
 */
public class MainWindow extends JFrame {
    public MainWindow(Player p, ParticlePanel vp) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.fullScreen();
        //this.setBounds ( 10 , 10 , 200 , 200 );
        //this.setVisible(true);
        GridPanel gf = new GridPanel(p);
//        JLayeredPane layers = getLayeredPane();
        JLayeredPane layers = new JLayeredPane();
//        layers.setPreferredSize(new Dimension(300, 310));
        layers.add(vp, new Integer(1));
        vp.setBounds(0, -128, 1024, 1024);
        layers.add(gf, new Integer(2));
        gf.setBounds(0, 0, 1024, 768);
        this.add(layers);
        
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
