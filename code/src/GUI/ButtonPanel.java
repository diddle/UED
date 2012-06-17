package GUI;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import playback.GridConfiguration;
import playback.InstrumentHolder;
import playback.Player;



public class ButtonPanel extends JPanel {
	private GridPanel gp;
	private BufferedImage settings,instrumenu;
	private BufferedImage pianob,bassb,drumsb,guitarb;
	private Player player;
	private BufferedImage[] instruments;


	public ButtonPanel(Player p, GridPanel gp) {
		this.player = p;
		this.gp = gp;
		this.setVisible(true);
		this.setOpaque(false);
		instruments = new BufferedImage[5];
		try {
                    ClassLoader cl = this.getClass().getClassLoader();
			settings = ImageIO.read(cl.getResource("resources/settings.png"));
			pianob = ImageIO.read(cl.getResource("resources/piano_button.png"));
			bassb = ImageIO.read(cl.getResource("resources/bass_button.png"));
			guitarb = ImageIO.read(cl.getResource("resources/guitar_button.png"));
			drumsb = ImageIO.read(cl.getResource("resources/drums_button.png"));

			instruments[0] = ImageIO.read(cl.getResource("resources/drums_icon.png"));
			instruments[1] = ImageIO.read(cl.getResource("resources/piano_icon.png"));
			instruments[2] = ImageIO.read(cl.getResource("resources/bass_icon.png"));
			instruments[3] = ImageIO.read(cl.getResource("resources/guitar_icon.png"));
			instruments[4] = ImageIO.read(cl.getResource("resources/misc_icon.png"));

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
			} 
		}

	}

	public BufferedImage scale(BufferedImage original, double scale){
		int newWidth = (int)(((double)original.getWidth())*scale);
		int newHeight = (int)(((double)original.getHeight())*scale);
		BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(), original.getHeight(), null);
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
		double tempScale;
		BufferedImage tempImage;
		for(int i=0; i<player.getActiveGrids().size(); i++){
			temp = gp.getButtonCoordinates(i, 0);
			List<GridConfiguration> gridConfigs = InstrumentHolder.getInstance().getAvailableConfigurations();
			for(int j = 0; j < gridConfigs.size(); j++) {
				if(player.getActiveGrids().get(i).getGridConfiguration().equals(gridConfigs.get(j))) {
					instrumenu = instruments[j];
				}
			}
			tempScale = (temp[2]<temp[3])?(double)(temp[2])/(double)instrumenu.getWidth():(double)(temp[3])/(double)instrumenu.getHeight();
			tempImage = scale(rotate(instrumenu, temp[0], temp[1]), tempScale);
			g.drawImage(tempImage, temp[0]-tempImage.getWidth()/2, temp[1]-tempImage.getHeight()/2, this);
			temp = gp.getButtonCoordinates(i, 1);
			tempScale = (temp[2]<temp[3])?(double)(temp[2])/(double)settings.getWidth():(double)(temp[3])/(double)settings.getHeight();
			tempImage = scale(rotate(settings, temp[0], temp[1]), tempScale);
			g.drawImage(tempImage, temp[0]-tempImage.getWidth()/2, temp[1]-tempImage.getHeight()/2, this);
		}
	}

	private void drawInstrumentButtons(Graphics g, int personIndex){
		int[] tempCoords = new int[4];
		double tempScale;
		BufferedImage tempImage;
		List<GridConfiguration> gridConfigs = InstrumentHolder.getInstance().getAvailableConfigurations();
		for(int i = 0; i < 4; i++){
			tempCoords = gp.getButtonCoordinates(personIndex, i+2);
			tempScale = (tempCoords[2]<tempCoords[3])?(double)(tempCoords[2])/(double)instruments[i].getWidth():(double)(tempCoords[3])/(double)instruments[i].getHeight();
			tempImage = scale(rotate(instruments[(i+gp.getInstrMenuIndex(personIndex))%gridConfigs.size()], tempCoords[0], tempCoords[1]), tempScale);
			g.drawImage(tempImage, tempCoords[0]-tempImage.getWidth()/2, tempCoords[1]-tempImage.getHeight()/2, this);
		}
	}

}
