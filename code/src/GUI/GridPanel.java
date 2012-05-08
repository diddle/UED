/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.util.List;
import javax.swing.*;
import playback.Player;
import playback.ToneGrid;

/**
 *
 * @author Niels Visser
 */
public class GridPanel extends JPanel{
    
    private MouseHandler mouseHandler = new MouseHandler();
//    private Point p1 = new Point(100, 100);
//    private Point p2 = new Point(540, 380);
    private Point click = new Point(10,10);
    private boolean drawing;
    
    private Player p;

    public GridPanel(Player p) {
        this.p = p;
        this.setPreferredSize(new Dimension(1024, 768));
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.blue);
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(1,
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        drawActiveTones((Graphics2D)g);
        drawCircles(g);
        drawLines(g);
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            drawing = true;
            click = e.getPoint();
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
//            drawing = false;
//            click = e.getPoint();
//            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
//            if (drawing) {
//                click = e.getPoint();
//                repaint();
//            }
        }
    }
    
    private void drawCircles(Graphics g) {
        g.setColor(Color.BLACK);
        int x = 512;
        int y = 384;
        int step = 18;
        int r = 375;
        for(int i=0; i<this.p.getHeight()+1; i++) {
            int[] xywh = this.toXYWH(x, y, r - i * step);
            g.drawOval(xywh[0], xywh[1], xywh[2], xywh[3]);
        }
    }
    
    private int[] toXYWH(int x, int y, int r) {
        int[] result = new int[4];
        result[0] = x-r;
        result[1] = y-r;
        result[2] = 2*r;
        result[3] = 2*r;
        return result;
    }
    
    private void drawLines(Graphics g) {
        int x = 512;
        int y = 384;
        int step = 18;
        int r = 375;
        int numLines = this.p.getWidth() * this.p.getActiveGrids().size();
        double radPerLine = (Math.PI * 2d) / ((double)numLines);
        for(int i=0; i<numLines; i++) {
            int startR = r;
            int endR = r - step * this.p.getHeight();
            int startX = (int)(startR * Math.cos(i * radPerLine)) + x;
            int startY = (int)(startR * Math.sin(i * radPerLine)) + y;
            int endX = (int)(endR * Math.cos(i * radPerLine)) + x;
            int endY = (int)(endR * Math.sin(i * radPerLine)) + y;
            g.drawLine(startX, startY, endX, endY);
        }
    }
    
    private void drawActiveTones(Graphics2D g) {
        int x = 512;
        int y = 384;
        int step = 18;
        int r = 375;
        int numLines = this.p.getWidth() * this.p.getActiveGrids().size();
        double radPerLine = (Math.PI * 2d) / ((double)numLines);
        int i = 0;
        int w = this.p.getWidth();
        for(ToneGrid tg : this.p.getActiveGrids()) {
            int start = i * w;
            List<List<Boolean>> tones = tg.getAllTones();
            int tx = 0;
            for(List<Boolean> col : tones) {
                for(int ty=0; ty<col.size(); ty++) {
                    if(col.get(ty)) {
                        int tr = r - ty * step;
                        double trad = (double)(i * this.p.getWidth()) * radPerLine + (double)tx * radPerLine;
                        int px = (int)(tr * Math.cos(trad)) + x;
                        int py = (int)(tr * Math.sin(trad)) + y;
                        Point p1 = new Point(px, py);
                        
                        double trad2 = (double)(i * this.p.getWidth()) * radPerLine + (double)(tx+1) * radPerLine;
                        px = (int)(tr * Math.cos(trad2)) + x;
                        py = (int)(tr * Math.sin(trad2)) + y;
                        Point p2 = new Point(px, py);
                        
                        int tr2 = r - (ty+1) * step;
                        px = (int)(tr2 * Math.cos(trad2)) + x;
                        py = (int)(tr2 * Math.sin(trad2)) + y;
                        Point p3 = new Point(px, py);
                        
                        px = (int)(tr2 * Math.cos(trad)) + x;
                        py = (int)(tr2 * Math.sin(trad)) + y;
                        Point p4 = new Point(px, py);
                        
                        GeneralPath gp = new GeneralPath();
                        gp.moveTo(p1.x, p1.y);
                        gp.lineTo(p2.x, p2.y);
                        gp.lineTo(p3.x, p3.y);
                        gp.lineTo(p4.x, p4.y);
                        gp.lineTo(p1.x, p1.y);
                        gp.closePath();
                        g.setPaint(this.getColorFor(i));
                        g.fill(gp);
                        
                    }
                }
                tx++;
            }
            i++;
        }
    }
    
    private Color getColorFor(int person) {
        switch(person) {
            case 0: return Color.RED;
            case 1: return Color.BLUE;
            case 2: return Color.GREEN;
            case 3: return Color.YELLOW;
            default: return Color.BLACK;
        }
    }
    
//
//    private class ControlPanel extends JPanel {
//
//        private static final int DELTA = 10;
//
//        public ControlPanel() {
//            this.add(new MoveButton("\u2190", KeyEvent.VK_LEFT, -DELTA, 0));
//            this.add(new MoveButton("\u2191", KeyEvent.VK_UP, 0, -DELTA));
//            this.add(new MoveButton("\u2192", KeyEvent.VK_RIGHT, DELTA, 0));
//            this.add(new MoveButton("\u2193", KeyEvent.VK_DOWN, 0, DELTA));
//        }
//
//        private class MoveButton extends JButton {
//
//            KeyStroke k;
//            int dx, dy;
//
//            public MoveButton(String name, int code,
//                    final int dx, final int dy) {
//                super(name);
//                this.k = KeyStroke.getKeyStroke(code, 0);
//                this.dx = dx;
//                this.dy = dy;
//                this.setAction(new AbstractAction(this.getText()) {
//
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        GridPanel.this.p1.translate(dx, dy);
//                        GridPanel.this.p2.translate(dx, dy);
//                        GridPanel.this.repaint();
//                    }
//                });
//                ControlPanel.this.getInputMap(WHEN_IN_FOCUSED_WINDOW)
//                    .put(k, k.toString());
//                ControlPanel.this.getActionMap()
//                    .put(k.toString(), new AbstractAction() {
//
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        MoveButton.this.doClick();
//                    }
//                });
//            }
//        }
//    }

    public void display() {
        JFrame f = new JFrame("LinePanel");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        //f.add(new ControlPanel(), BorderLayout.SOUTH);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                //new GridPanel().display();
            }
        });
    }
}