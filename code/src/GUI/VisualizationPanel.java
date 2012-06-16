/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author Niels Visser
 */
public class VisualizationPanel extends JPanel {
    
    private List<Circle> circles;

    public VisualizationPanel(int width, int height) {
        this.circles = new ArrayList<Circle>();
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.setVisible(true);
    }
    
    public void notePlayed(int pitch, int velocity, NoteIndex noteIndex) {
        int r = velocity;
        Gradient g = new Gradient();
        g.setColorAt(0, Color.blue);
        g.setColorAt(255, Color.red);
        System.out.println(pitch);
        Color c = g.getColorAt((int)Math.round(((double)pitch/255d)*255d));
        int rangeX = this.getWidth() - 2*r;
        int rangeY = this.getHeight() - 2*r;
        int x = (int)Math.round(Math.random() * (double)rangeX);
        int y = (int)Math.round(Math.random() * (double)rangeY);
        // draw circle
        Circle cir = new Circle();
        cir.x = x;
        cir.y = y;
        cir.c = c;
        cir.r = r;
        synchronized(this.circles) {
            if(this.circles.size() > 15) {
                Circle first = this.circles.get(0);
                this.circles.remove(first);
            }
            this.circles.add(cir);
        }
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(1,
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        synchronized(this.circles) {
            for(Circle c : this.circles) {
                g.setColor(c.c);
                g.fillOval(c.x+c.r, c.y+c.r, 2*c.r, 2*c.r);
            }
        }
    }
    
    private static class Circle {
        public int x;
        public int y;
        public int r;
        public Color c;
    }
    
}
