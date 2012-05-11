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
public class GridPanel extends JPanel {
	
	private static final int DEFAULTWIDTH = 1024;
	private static final int DEFAULTHEIGHT = 768;
    
    private MouseHandler mouseHandler = new MouseHandler();
    private boolean drawing;
    private int squareHeight = 18;
    
    private static double radOffset = 0.25d * Math.PI;
    
    private Player p;

    public GridPanel(Player p) {
    	this(p, DEFAULTWIDTH, DEFAULTHEIGHT);
    }
    
    public GridPanel(Player p, int width, int height) {
        this.p = p;
        this.p.setGridPanel(this);
        this.setPreferredSize(new Dimension(width, height));
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
        drawGrids((Graphics2D)g);
//        drawActiveTones((Graphics2D)g);
//        drawCircles(g);
//        drawLines(g);
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
        	Point p = e.getPoint();
        	p.setLocation(p.getX(), translateY(p.getY()));
            processClick(p);
        }
        
        public void mouseDragged(MouseEvent e) {
        	Point p = e.getPoint();
        	p.setLocation(p.getX(), translateY(p.getY()));
        	processDrag(p);
        }

    }
    
    private void drawGrids(Graphics2D g){
    	int people = p.getActiveGrids().size();
    	for(int i=0; i<people; i++){
    		drawPlayerGrid(g, i);
    	}
    }
    private void drawPlayerGrid(Graphics2D g, int personIndex){
    	int columns = p.getWidth();
    	for(int i=0; i<columns; i++){
    		drawColumn(g, personIndex, i);
    	}
    }
    private void drawColumn(Graphics2D g, int personIndex, int colIndex){
    	// TODO: allow changing of square amount
    	int notes = 10;
    	for(int i=0; i<notes; i++){
    		drawSquare(g, personIndex, colIndex, i);
    	}
     	
    }
    private void drawSquare(Graphics2D g, int personIndex, int colIndex, int noteIndex){

    	double beginAngle = (double)(personIndex*p.getWidth())*radPerColumn() + (double)colIndex*radPerColumn() + radOffset;
    	double endAngle = (double)(personIndex*p.getWidth())*radPerColumn() + (double)(colIndex+1)*radPerColumn() + radOffset;
    	
    	double lowerRadius = getRadius() - noteIndex*squareHeight;
    	double upperRadius = getRadius() - (noteIndex+1)*squareHeight;
    	
    	GeneralPath gp = new GeneralPath();
    	gp.moveTo(lowerRadius*Math.cos(beginAngle) + getWidth()/2,						//x1 (lower left)
    			translateY((int)(lowerRadius*Math.sin(beginAngle) + getHeight()/2)));	//y1
    	gp.lineTo(upperRadius*Math.cos(beginAngle) + getWidth()/2,						//x2 (upper left)
    			translateY((int)(upperRadius*Math.sin(beginAngle) + getHeight()/2)));	//y2
    	
    	Point[] upperBP = generateBezierPoints(upperRadius, beginAngle, endAngle);		//upper curve
    	gp.curveTo(upperBP[1].getX() + getWidth()/2,
    			translateY((int)(upperBP[1].getY() + getHeight()/2)),
    			upperBP[2].getX() + getWidth()/2,
    			translateY((int)(upperBP[2].getY() + getHeight()/2)),
    			upperRadius*Math.cos(endAngle) + getWidth()/2,
    			translateY((int)(upperRadius*Math.sin(endAngle) + getHeight()/2)));
    	
//    	gp.lineTo(upperRadius*Math.cos(endAngle) + getWidth()/2,						//x3
//    			translateY((int)(upperRadius*Math.sin(endAngle) + getHeight()/2)));		//y3
    	
    	gp.lineTo(lowerRadius*Math.cos(endAngle) + getWidth()/2,						//x4
    			translateY((int)(lowerRadius*Math.sin(endAngle) + getHeight()/2)));		//y4

    	Point[] lowerBP = generateBezierPoints(lowerRadius, beginAngle, endAngle);
    	gp.curveTo(lowerBP[2].getX() + getWidth()/2,
    			translateY((int)(lowerBP[2].getY() + getHeight()/2)),
    			lowerBP[1].getX() + getWidth()/2,
    			translateY((int)(lowerBP[1].getY() + getHeight()/2)),
    			lowerRadius*Math.cos(beginAngle) + getWidth()/2,
    			translateY((int)(lowerRadius*Math.sin(beginAngle) + getHeight()/2)));
    	
//    	gp.lineTo(lowerRadius*Math.cos(beginAngle) + getWidth()/2,						//x1
//    			translateY((int)(lowerRadius*Math.sin(beginAngle) + getHeight()/2)));	//y1
    	
    	gp.closePath();

    	Color squareColour = getColorFor(personIndex);

    	if(p.getActiveGrids().get(personIndex).getTone(colIndex, noteIndex)) {
    		squareColour = Color.black;
    	}
    	if(p.getPosition() == colIndex) {
    		squareColour = Color.black;
        	if(p.getActiveGrids().get(personIndex).getTone(colIndex, noteIndex)) {
        		squareColour = Color.white;
        	}
    	}
	  	g.setPaint(squareColour);
    	
	  	g.fill(gp);
    }
    
    private Point[] generateBezierPoints(double radius, double beginAngle, double endAngle) {
    	
    	double sweepAngle = endAngle - beginAngle;
    	
    	double[] p0 = {Math.cos(sweepAngle/2d),
    			Math.sin(sweepAngle/-2d)};
    	double[] p1 = {(4d-p0[0])/3d,
    			((1d-p0[0])*(3d-p0[0]))/(3d*p0[1])};
    	double[] p2 = {p1[0],
    			-1d*p1[1]};
    	double[] p3 = {p0[0],
    			-1d*p0[1]};
    	
    	double[][] p = {p0, p1, p2, p3};
    	Point[] result = new Point[4];
    	
    	for(int i = 0; i < p.length; i++) {
    		p[i] = rotate(radius*p[i][0], radius*p[i][1], beginAngle+sweepAngle/2d);
    		result[i] = new Point();
    		result[i].setLocation(p[i][0], p[i][1]);
    	}
    	
    	return result;
    }
    
    private double radPerColumn() {
        return (Math.PI * 2d) / ((double)(p.getWidth() * p.getActiveGrids().size()));
    }
    
    private double[] rotate(double x, double y, double rotationAngle) {
    	return rotate(x, y, 0d, 0d, rotationAngle);
    }
    
    private double[] rotate(double x, double y, double centerX, double centerY, double rotationAngle) {
    	double diffX = x-centerX;
		double diffY = y-centerY;
		
		double radius = Math.sqrt(diffX*diffX+diffY*diffY);
		double angle = Math.atan2(diffY, diffX);
		
		angle += rotationAngle;
		
		x = radius*Math.cos(angle);
		y = radius*Math.sin(angle);
		x += centerX;
		y += centerY;
		
		return new double[]{x, y};
    }
    
    private Color getColorFor(int person) {
        switch(person) {
            case 0: return Color.RED;
            case 1: return Color.BLUE;
            case 2: return Color.GREEN;
            case 3: return Color.YELLOW;
            default: return Color.MAGENTA;
        }
    }
    
    private int getRadius(){
    	return Math.min(getWidth()/2, getHeight()/2);
    }
    
    private NoteIndex translatePointToNoteIndex(Point point) {
        double radPerCol = (Math.PI * 2d) / ((double)this.p.getWidth() * this.p.getActiveGrids().size());
        // centreer de punten
        int rx = point.x - this.getWidth()/2;
        int ry = point.y - this.getHeight()/2;
        
        int rr = (int)Math.sqrt(rx*rx+ry*ry);
        double radr = Math.atan(((double)ry)/((double)rx));
        if(rx < 0 && ry > 0) {
            radr += Math.PI;
        }
        else if(rx < 0 && ry < 0) {
            radr += Math.PI;
        }
        else if(rx > 0 && ry < 0) {
            radr += Math.PI * 2d;
        }
        radr -= radOffset;
        // nu weten we de hoek (radr) en de straal (rr)
        double sizePerPerson = (Math.PI * 2d) / (double)(p.getActiveGrids().size());
        int personIndex = (int)Math.floor(radr / sizePerPerson);
        int num = p.getActiveGrids().size();
        personIndex = (personIndex % num + num) % num;
        int colIndex = (int)Math.floor((radr - (double)personIndex * sizePerPerson) / radPerCol);
        int w = p.getWidth();
        colIndex = (colIndex % w + w) % w;
        int noteIndex = this.p.getHeight() - (int)Math.floor(((double)(rr - (getRadius() - squareHeight * this.p.getHeight())) / (double)squareHeight)) - 1;
        
    	return new NoteIndex(personIndex, colIndex, noteIndex);
    }
    
    private void processClick(Point click) {
        NoteIndex clickedNote = translatePointToNoteIndex(click);
        
        ToneGrid tg = this.p.getActiveGrids().get(clickedNote.getPerson());
        if(clickedNote.getNote() >= 0 && clickedNote.getNote() < this.p.getHeight()) {
            tg.toggleTone(clickedNote.getColumn(), clickedNote.getNote());
            drawing = tg.getTone(clickedNote.getColumn(), clickedNote.getNote());
        }
        else
        	drawing = true;
    }
    
    private void processDrag(Point point) {
        NoteIndex clickedNote = translatePointToNoteIndex(point);
        ToneGrid tg = this.p.getActiveGrids().get(clickedNote.getPerson());
        try{
        	if(clickedNote.getNote() >= 0 && clickedNote.getNote() < this.p.getHeight()) {
	        	if(drawing)
	        		tg.activateTone(clickedNote.getColumn(), clickedNote.getNote());
	        	else
	        		tg.deactivateTone(clickedNote.getColumn(), clickedNote.getNote());
        	}
        }
        catch(Exception e) {
        	System.out.println("Hoerenkanker");
        }
    }
    
    private double translateY(double y) {
        return getHeight() - y;
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
    
    private class NoteIndex {
    	private int person, column, note;
    	
    	public NoteIndex(int person, int column, int note) {
    		this.person = person;
    		this.column = column;
    		this.note = note;
    	}
    	
    	public int getPerson() {
    		return person;
    	}
    	public int getColumn() {
    		return column;
    	}
    	public int getNote() {
    		return note;
    	}
    }
}
