package GUI;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import playback.GridConfiguration;
import playback.InstrumentHolder;
import playback.Player;



public class ButtonPanel extends JPanel {
	private GridPanel gp;
	private BufferedImage settings,instrumenu = null;
	private BufferedImage pianoi,pianob,bassi,bassb,drumsi,drumsb,guitari,guitarb = null;
	private Player player;
	private BufferedImage[] instruments = new BufferedImage[4];


	public ButtonPanel(Player p, GridPanel gp) {
		this.player = p;
		this.gp = gp;
		this.setVisible(true);
		this.setOpaque(false);
		try {
			instrumenu = ImageIO.read(new File("bin\\resources\\piano_icon.png"));
			settings = ImageIO.read(new File("bin\\resources\\settings.png"));
			pianob = ImageIO.read(new File("bin\\resources\\piano_button.png"));
			bassb = ImageIO.read(new File("bin\\resources\\bass_button.png"));
			guitarb = ImageIO.read(new File("bin\\resources\\guitar_button.png"));
			drumsb = ImageIO.read(new File("bin\\resources\\drums_button.png"));

			instruments[0] = drumsi = ImageIO.read(new File("bin\\resources\\drums_icon.png"));
			instruments[1] = pianoi = ImageIO.read(new File("bin\\resources\\piano_icon.png"));
			instruments[2] = bassi = ImageIO.read(new File("bin\\resources\\bass_icon.png"));
			instruments[3] = guitari = ImageIO.read(new File("bin\\resources\\guitar_icon.png"));

			System.out.println(settings.toString());

		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(1,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));

		drawMenuButtons(g);
		for (int i = 0; i < player.getActiveGrids().size(); i++) {
			if (gp.getActiveMenus()[i] == GridPanel.INSTRUMENT_MENU) {
				drawInstrumentButtons(g,i);
			} else if (gp.getActiveMenus()[i] == GridPanel.INSTRUMENT_MENU2) {
				drawInstrument2Buttons(g,i);
			}
		}

	}

	public BufferedImage resize(BufferedImage original, int width, int height){
		BufferedImage resized = new BufferedImage(width, height, original.getType());
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(original, 0, 0, width, height, 0, 0, original.getWidth(), original.getHeight(), null);
		g.dispose();
		return resized;
	}


	public BufferedImage rotate(BufferedImage img, int x, int y) {
		double angle = Math.atan2(y-gp.getHeight()/2, x-gp.getWidth()/2)-.5*Math.PI;
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(w, h, img.getType());  
		Graphics2D g = dimg.createGraphics();  
		g.rotate(angle, w/2, h/2);  
		g.drawImage(img, null, 0, 0);  
		return dimg;  
	}  

	private void drawMenuButtons(Graphics g){
		int[] temp = new int[4];
		for(int i=0; i<4; i++){
			temp = gp.getButtonCoordinate(i, 0);
			g.drawImage(resize(rotate(instrumenu, (temp[2]+temp[0])/2, (temp[3]+temp[1])/2), temp[2]-temp[0],temp[3]-temp[1]), temp[0], temp[1], this);
			temp = gp.getButtonCoordinate(i, 1);
			g.drawImage(resize(rotate(settings, (temp[2]+temp[0])/2, (temp[3]+temp[1])/2), temp[2]-temp[0],temp[3]-temp[1]), temp[0], temp[1], this);
		}
	}

	private void drawInstrumentButtons(Graphics g, int personIndex){
		int[] temp = new int[4];
		for(int i=0; i<4;i++){
			temp = gp.getButtonCoordinate(personIndex, i+2);
			g.drawImage(resize(rotate(instruments[i], (temp[2]+temp[0])/2, (temp[3]+temp[1])/2), temp[2]-temp[0], temp[3]-temp[1]), temp[0], temp[1], this);
		}
		temp= gp.getButtonCoordinate(personIndex, 6);
	}

	private void drawInstrument2Buttons(Graphics g, int personIndex) {
		int[] temp = new int[4];
		for (int i = 0; i < 4; i++) {
			temp = gp.getButtonCoordinate(personIndex, i + 2);
			g.drawImage(resize(rotate(instruments[(i+1)%4],
					(temp[2]+temp[0])/2, (temp[3]+temp[1])/2),
					temp[2] - temp[0], temp[3] - temp[1]), temp[0], temp[1], this);
		}
		temp = gp.getButtonCoordinate(personIndex, 6);
	}

}
