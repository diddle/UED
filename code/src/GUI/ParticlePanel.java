package GUI;
/*
 * Copyright Jerry Huxtable 1999
 *
 * Feel free to do anything you like with this code.
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import com.jhlabs.image.*;
import com.jhlabs.image.Gradient;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * An applet which animates a simple particle system.
 */
public class ParticlePanel extends JPanel implements Runnable {
	protected MemoryImageSource source;
	protected Image image;
	protected boolean newImage = true;
	protected ColorModel colorModel;
	protected Thread thread;
	protected byte[] pixels1, pixels2;
	protected Image offscreen;
	protected Graphics offscreenG;
	public Action[] actions;
	protected Filter[] filters;
	protected Object[] gradients;
	private boolean running = false;
	private boolean startAnimation = false;
	protected Particles particles;


	public static void main(String[] args) {
		ParticlePanel test = new ParticlePanel();
		//test.setBounds( 0 , 0 , 800 , 800 );
		JFrame frame = new JFrame("Particles!");
		frame.add(test);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setPreferredSize(new Dimension(300,300));
		frame.setBounds ( 0, 0, 800 , 800 );
		frame.setVisible(true);
		new Thread(test).start();

		//test.setVisible(true);
	}

	public void notePlayed(int pitch, int velocity, NoteIndex noteIndex) {
		int num = 1;
		int size = 3; // 1..3
		int color = 100; // 1..255
		Set<Particle> toUse = new HashSet<Particle>();
		if(this.particles.freeParticles.size() > 0) {
			synchronized(this.particles.freeParticles) {
				Iterator<Particle> i = this.particles.freeParticles.iterator();
				for(int j=0; j<num; j++) {
					Particle p = i.next();

					toUse.add(p);
				}
				i = toUse.iterator();
				while(i.hasNext()) {
					Particle p = i.next();
					this.particles.freeParticles.remove(p);
					p.size = size;
					p.color = color;
					this.particles.newParticle(p);
				}
			}
		}
	}

	private int toColor(int r, int g, int b) {
		return (r << 16) + (g << 8) + b;
	}

	public ParticlePanel() {
		this.setVisible(true);
		
		this.init();

		new Thread(this).start();
	}
	
	public void init() {
		gradients = new Object[1];
		Gradient g = new Gradient();

		g.addKnot(0, 0, Gradient.LINEAR );
		g.addKnot(30, toColor(255,0,0), Gradient.LINEAR );
		g.addKnot(100, toColor(0,255,0), Gradient.LINEAR );
		g.addKnot(240, toColor(0,0,255), Gradient.LINEAR );
		g.addKnot(255, toColor(255,255,255), Gradient.LINEAR );
		gradients[0] = g;


		setGradient(5);
		int numFilters = 14;
		filters = new Filter[numFilters];
		filters[0] = new ShiftDownFilter();
		filters[1] = new ShiftUpFilter();
		filters[2] = new ConvolveFilter();
		filters[3] = new BlurHFilter();
		filters[4] = new BlurVFilter();
		filters[5] = new ThingFilter();
		filters[6] = new WaterFilter();
		filters[7] = new ClearFilter();
		filters[8] = new ZoomInHFilter();
		filters[9] = new ZoomInVFilter();
		filters[10] = new TwirlFilter();
		filters[11] = new ZoomFilter(0.95,0.95);
		filters[12] = new FadeFilter(4);
		filters[13] = new BlurHVFilter();
		filters[10].setEnabled(true);
		filters[12].setEnabled(true);
		filters[13].setEnabled(true);

		int numActions = 1;
		actions = new Action[numActions];
		int numParticles = 100;
		particles = new Particles(numParticles, getWidth()/2, getHeight()/2, getWidth(), getHeight());
		actions[0] = particles;
		actions[0].setEnabled(true);
		particles.rate = 999;
		particles.speed = (100 << 8) / 10;
		particles.angle = 0;
		particles.spread = 360;
		particles.gravity = 0;
		particles.color = 255;
		particles.scatter = 50;
		particles.hscatter = 50;
		particles.vscatter = 50;
		particles.randomness = 50;
		particles.size = 2;
		particles.x = getWidth()/2;
		particles.y = getHeight()/2;
		particles.lifetime = 100;
		particles.speedVariation = (50 << 8) / 10;
		particles.decay = 0;
		startAnimation = 1 != 0;
		int colormap = 10;
		setGradient(colormap);
	}

	public void setGradient(int n) {
		n = Math.max(0, Math.min(n, gradients.length-1));
		byte[] r = new byte[256];
		byte[] g = new byte[256];
		byte[] b = new byte[256];
		for (int i = 0; i < 256; i++) {
			int rgb = ((Gradient)gradients[n]).getColor((float)(i/255.0));
			r[i] = (byte)((rgb >> 16) & 0xff);
			g[i] = (byte)((rgb >> 8) & 0xff);
			b[i] = (byte)(rgb & 0xff);
		}
		colorModel = new IndexColorModel(8, 256, r, g, b);
	}

	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), getHeight());
	}

	public void start() {
		if (startAnimation && thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	public void run() {
		try {
			running = true;
			while (running) {
				repaintImage();
				//				synchronized(this) {
				//					wait();
				//				}
				Thread.sleep(5);
			}
		}
		catch (InterruptedException e) {
		}
		thread = null;
	}

	public void update(Graphics g) {
		paint(g);
	}

	public synchronized void paint(Graphics gr) {
		
		if (newImage || image == null)
			image = makeImage();
		//                byte[] pixels = new byte[100*100];
		//                for(int i=0; i<pixels.length; i++) {
		//                    pixels[i] = (byte)((int)Math.round(Math.random() * 255));
		//                }
		//                image = createImage(new MemoryImageSource(w, h, colorModel, pixels, 0, w));

		//                byte[] r,g,b;
		//                r = new byte[256];
		//                g = new byte[256];
		//                b = new byte[256];
		//                for(int i=0; i<256; i++) {
		//                    r[i] = (byte)i;
		//                    g[i] = (byte)i;
		//                    b[i] = (byte)i;
		//                }
		//                ColorModel colorModel = new IndexColorModel(8, 256, r, g, b);
		//                byte[] pixels = new byte[200*200];
		//                for(int i=0; i<pixels.length; i++) {
		//                    pixels[i] = (byte)((int)Math.round(Math.random() * 255));
		//                }
		//                Image image = createImage(new MemoryImageSource(200, 200, colorModel, pixels, 0, 200));

		gr.drawImage(image, 0, 0, this);
		if (!running) {
			String s = "Click to Start";
			FontMetrics fm = gr.getFontMetrics();
			int x = (getWidth()-fm.stringWidth(s))/2;
			int y = (getHeight()-fm.getAscent())/2;
			gr.setColor(Color.white);
			gr.drawString(s, x, y);
		}
		//                super.paint(g);
		notify();

	}

	private Image makeImage() {
		//            byte[] r,g,b;
		//                r = new byte[256];
		//                g = new byte[256];
		//                b = new byte[256];
		//                for(int i=0; i<256; i++) {
		//                    r[i] = (byte)i;
		//                    g[i] = (byte)i;
		//                    b[i] = (byte)i;
		//                }
		//                ColorModel colorModel = new IndexColorModel(8, 256, r, g, b);




		if (pixels1 == null) {
			int i = 0;
			pixels1 = new byte[getWidth()*getHeight()];
			pixels2 = new byte[getWidth()*getHeight()];
			for (i = 0; i < actions.length; i++)
				if (actions[i].isEnabled())
					actions[i].apply(pixels1, getWidth(), getHeight());
		} else {
			for (int i = 0; i < filters.length; i++) {
				if (filters[i].isEnabled()) {
					filters[i].apply(pixels1, pixels2, getWidth(), getHeight());
					byte[] t = pixels1;
					pixels1 = pixels2;
					pixels2 = t;
				}
			}
			for (int i = 0; i < actions.length; i++)
				if (actions[i].isEnabled())
					actions[i].apply(pixels1, getWidth(), getHeight());
		}
		newImage = false;
		if (image == null) {
			image = createImage(source = new MemoryImageSource(getWidth(), getHeight(), colorModel, pixels1, 0, getWidth()));
			source.setAnimated(true);
		} else
			source.newPixels(pixels1, colorModel, 0, getWidth());
		return image;
	}

	private void repaintImage() {
		newImage = true;
		repaint();
	}

	public void mousePressed(MouseEvent e) {
		requestFocus();
		startAnimation = true;
		if (thread == null && !running)
			start();
		else
			running = false;
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}
class Thing {
	private boolean enabled;

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}

class Action extends Thing {
	public void apply(byte[] pixels, int width, int height) {
	}
}

class Filter extends Action {
	public void apply(byte[] in, byte[] out, int width, int height) {
	}
}

/**
 * A particle system. We use fixed point integer maths with 8 fractional bits everywhere
 * for speed, hence the frequent shifts by 8.
 */
class Particles extends Action {
	public Particle[] particles;
	public int rate = 100;
	public int angle = 0;	// 0 degrees is north
	public int spread = 90;
	public int gravity = (1 << 8);
	public int lifetime = 65;
	public int scatter = 0;
	public int hscatter = 0;
	public int vscatter = 0;
	public int x;
	public int y;
	public int speed = 1;
	public int size;
	public int width;
	public int height;
	public int speedVariation = 0;
	public int decay = 0;
	public int randomness = (7 << 8);
	public int color;
	public int numParticles;
	private static int[] sinTable, cosTable;
	public Set<Particle> freeParticles;

	static {
		sinTable = new int[360];
		cosTable = new int[360];
		for (int i = 0; i < 360; i++) {
			double angle = 2*Math.PI*i/360;
			sinTable[i] = (int)(256 * Math.sin(angle));
			cosTable[i] = (int)(256 * Math.cos(angle));
		}
	}

	public Particles(int numParticles, int x, int y, int width, int height) {
		this.freeParticles = new HashSet<Particle>();
		this.numParticles = numParticles;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		particles = new Particle[numParticles];
		for (int i = 0; i < numParticles; i++) {
			particles[i] = new Particle(this, width, height);
			newParticle(particles[i]);
			this.freeParticles.add(particles[i]);
		}
	}

	public double gaussian() {
		double sum = 0;
		for (int i = 0; i < 12; i++) {
			sum += Math.random();
		}
		return (sum-6)/3.0;
	}

	public void newParticle(Particle particle) {
		particle.color = color;
		particle.size = size;
		particle.lifetime = (int)(Math.random()*lifetime);
		particle.randomness = randomness;
		particle.x = x;
		particle.y = y;
		if (scatter != 0) {
			int a = ((int)(Math.random() * 360)) % 360;
			double distance = scatter * Math.random() / 256;
			particle.x += (int)(cosTable[a] * distance);
			particle.y += (int)(-sinTable[a] * distance);
		}
		if (hscatter != 0)
			particle.x += (int)(hscatter * (Math.random()-0.5));
		if (vscatter != 0)
			particle.y += (int)(vscatter * (Math.random()-0.5));
		int a = (angle + 450 - spread/2 + (int)(Math.random() * spread)) % 360;
		int s = speed + (int)(speedVariation * gaussian());
		particle.vx = ((cosTable[a] * s) >> 8);
		particle.vy = -((sinTable[a] * s) >> 8);

		particle.x <<= 8;
		particle.y <<= 8;
	}

	public void apply(byte[] pixels, int width, int height) {
		for (int i = 0; i < particles.length; i++) {
			Particle p = particles[i];
			if (p.lifetime < 0) {
				//newParticle(p);
				synchronized(this.freeParticles) {
					this.freeParticles.add(p);
				}
			}
			else {
				p.paint(pixels, width, height);
				p.move(width, height);
				p.color -= decay;
				if (p.color < 0)
					p.color = 0;
			}
		}
	}

	public String toString() {
		return "Particles";
	}
}

class Particle {
	protected int x, y;
	protected int vx, vy;
	public int size;
	public int color = 255;
	public int randomness = 0;
	public int lifetime = -1;
	private Particles particles;

	public Particle(Particles particles, int width, int height) {
		this.particles = particles;
	}

	public void move(int width, int height) {
		if (randomness != 0) {
			vx += (int)(Math.random() * randomness)-randomness/2;
			vy += (int)(Math.random() * randomness)-randomness/2;
		}
		x += vx;
		y += vy;
		vy += particles.gravity;
		lifetime--;
	}

	/*
	 * How to draw circles of small sizes
	 */
	public int[] circle1 = { 0, 1 };
	public int[] circle3 = { 0, 1, -1, 3, 0, 1 };
	public int[] circle5 = { -1, 3, -2, 5, -2, 5, -2, 5, -1, 3 };
	public int[] circle7 = { -1, 3, -2, 5, -3, 7, -3, 7, -3, 7, -2, 5, -1, 3 };
	public int[][] circles = { circle1, circle3, circle5, circle7 };

	public void paint(byte[] pixels, int width, int height) {
		byte pixel = (byte)color;
		int[] c = circles[Math.min(size, circles.length)];
		int my = (y >> 8)-size;
		for (int i = 0; i < c.length; i += 2, my++) {
			if (my < 0)
				continue;
			else if (my >= height)
				break;
			int x1 = Math.max(0, (x >> 8)+c[i]);
			int x2 = Math.min(width-1, x1+c[i+1]);
			int j = my*width+x1;
			for (int mx = x1; mx <= x2; mx++)
				pixels[j++] = pixel;
		}
	}
}

/**
 * Scroll the image down
 */
class ShiftDownFilter extends Filter {
	public void apply(byte[] in, byte[] out, int width, int height) {
		int i = 0;
		int j = 0;
		for (int x = 0; x < width; x++)
			out[j++] = 0;
		for (int y = 1; y < height; y++) {
			for (int x = 0; x < width; x++) {
				out[j++] = in[i++];
			}
		}
	}

	public String toString() {
		return "Move Down";
	}
}

/**
 * Scroll the image up
 */
class ShiftUpFilter extends Filter {
	public void apply(byte[] in, byte[] out, int width, int height) {
		int i = width*height-1;
		int j = i;
		for (int x = 0; x < width; x++)
			out[j--] = 0;
		for (int y = 1; y < height; y++) {
			for (int x = 0; x < width; x++) {
				out[j--] = in[i--];
			}
		}
	}

	public String toString() {
		return "Move Up";
	}
}

/**
 * Zoom the image out to the left and right
 */
class ZoomInVFilter extends Filter {
	public void apply(byte[] in, byte[] out, int width, int height) {
		int i = 0;
		int j = 0;
		int height2 = height/2;
		for (int x = 0; x < width; x++)
			out[j++] = 0;
		j = width;
		for (int y = 1; y < height2; y++) {
			for (int x = 0; x < width; x++) {
				out[j++] = in[i++];
			}
		}
		i += 2*width;
		for (int y = 1; y < height2; y++) {
			for (int x = 0; x < width; x++) {
				out[j++] = in[i++];
			}
		}
		for (int x = 0; x < width; x++)
			out[j++] = 0;
	}

	public String toString() {
		return "Move In Vertical";
	}
}

/**
 * Zoom the image out sideways
 */
class ZoomInHFilter extends Filter {
	public void apply(byte[] in, byte[] out, int width, int height) {
		int i = 0;
		int j = 0;
		int height2 = height/2;
		for (int x = 0; x < width; x++)
			out[j++] = 0;
		j = width;
		for (int y = 1; y < height2; y++) {
			for (int x = 0; x < width; x++) {
				out[j++] = in[i++];
			}
		}
		i += 2*width;
		for (int y = 1; y < height2; y++) {
			for (int x = 0; x < width; x++) {
				out[j++] = in[i++];
			}
		}
		for (int x = 0; x < width; x++)
			out[j++] = 0;
	}

	public String toString() {
		return "Move In Horizontal";
	}
}

/**
 * Do a general convolution on the image (this is much slower than the blur filters)
 */
class ConvolveFilter extends Filter {
	protected int[] kernel = {
			-3, 0, 0, 0, -3,
			0, 0, 0, 0, 0,
			-3, 0, 50, 0, -3,
			0, 0, 0, 0, 0,
			-3, 0, 0, 0, -3,
	};
	int target = 40;

	public ConvolveFilter() {
	}

	public ConvolveFilter(int[] kernel, int target) {
		this.kernel = kernel;
		this.target = target;
	}

	public void apply(byte[] in, byte[] out, int width, int height) {
		int index = 0;
		int rows = 5;
		int cols = 5;
		int rows2 = rows/2;
		int cols2 = cols/2;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int t = 0;

				for (int row = -rows2; row <= rows2; row++) {
					int iy = y+row;
					int ioffset;
					if (0 <= iy && iy < height)
						ioffset = iy*width;
					else
						ioffset = y*width;
					int moffset = cols*(row+rows2)+cols2;
					for (int col = -cols2; col <= cols2; col++) {
						int f = kernel[moffset+col];

						if (f != 0) {
							int ix = x+col;
							if (!(0 <= ix && ix < width))
								ix = x;
							t += f * (in[ioffset+ix] & 0xff);
						}
					}
				}
				t /= target;
				if (t > 255)
					t = 255;
				out[index++] = (byte)t;
			}
		}
	}

	public String toString() {
		return "Convolve";
	}
}

/**
 * Blur horizontally
 */
class BlurHFilter extends Filter {
	public void apply(byte[] in, byte[] out, int width, int height) {
		for (int y = 0; y < height; y++) {
			int index = y*width;
			out[index] = (byte)(((in[index] & 0xff) + (in[index+1] & 0xff))/3);
			index++;
			for (int x = 1; x < width-1; x++) {
				out[index] = (byte)(((in[index-1] & 0xff) + (in[index] & 0xff) + (in[index+1] & 0xff))/3);
				index++;
			}
			out[index] = (byte)(((in[index-1] & 0xff) + (in[index] & 0xff))/3);
		}
	}

	public String toString() {
		return "Blur Horizontally";
	}
}

/**
 * Blur vertically
 */
class BlurVFilter extends Filter {
	public void apply(byte[] in, byte[] out, int width, int height) {
		for (int x = 0; x < width; x++) {
			int index = x;
			out[index] = (byte)(((in[index] & 0xff) + (in[index+width] & 0xff))/3);
			index += width;
			for (int y = 1; y < height-1; y++) {
				out[index] = (byte)(((in[index-width] & 0xff) + (in[index] & 0xff) + (in[index+width] & 0xff))/3);
				index += width;
			}
			out[index] = (byte)(((in[index-width] & 0xff) + (in[index] & 0xff))/3);
		}
	}

	public String toString() {
		return "Blur Vertically";
	}
}

/**
 * A sort of water-ripple type effect
 */
class WaterFilter extends Filter {
	public void apply(byte[] in, byte[] out, int width, int height) {
		for (int y = 1; y < height-1; y++) {
			int index = y*width;
			index++;
			for (int x = 1; x < width-1; x++) {
				int n = (byte)(
						((in[index-1] & 0xff) + (in[index+1] & 0xff) + (in[index-width] & 0xff) + (in[index+width] & 0xff))/2
						- (out[index] & 0xff));
				n -= (byte)(n >> 6);
				out[index] = (byte)ImageMath.clamp(n, 0, 255);
				index++;
			}
			index++;
		}
	}

	public String toString() {
		return "Ripple";
	}
}

/**
 * Clears the image to black - you only see the moving particles with this one.
 */
class ClearFilter extends Filter {
	public void apply(byte[] in, byte[] out, int width, int height) {
		int index = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				out[index] = 0;
				index++;
			}
		}
	}

	public String toString() {
		return "Clear";
	}
}

class ThingFilter extends ConvolveFilter {
	/*
	protected static int[] kernel = {
		10, 0, 10, 0, 10,
		0, 0, 0, 0, 0,
		0, 10, 0, 10, 0,
		0, 0, 0, 0, 0,
		10, 0, 10, 0, 10,
	};
	 */
	protected static int[] kernel = {
		10, 0, 0, 0, 10,
		0, 10, 0, 10, 0,
		0, 0, 10, 0, 0,
		0, 10, 0, 10, 0,
		10, 0, 0, 0, 10,
	};

	public ThingFilter() {
		super(kernel, 120);
	}

	public String toString() {
		return "Thing";
	}
}


class TwirlFilter extends Filter
{

	public TwirlFilter()
	{
		angle = 0.10000000000000001D;
	}

	public void setAngle(double angle)
	{
		this.angle = angle;
	}

	public double getAngle()
	{
		return angle;
	}

	public void apply(byte in[], byte out[], int width, int height)
	{
		int i = 0;
		int cx = width / 2;
		int cy = height / 2;
		double r2 = cx * cx + cy * cy;
		double d = angle / r2;
		for(int y = 0; y < height; y++)
		{
			int dy = y - cy;
			for(int x = 0; x < width; x++)
			{
				int dx = x - cx;
				double distance = dx * dx + dy * dy;
				distance = r2 - distance;
				distance *= d;
				int newX = (int)(((double)x - (double)dy * distance) + 0.5D);
				int newY = (int)((double)y + (double)dx * distance + 0.5D);
				if(newX < 0)
					newX = 0;
				else
					if(newX >= width)
						newX = width - 1;
				if(newY < 0)
					newY = 0;
				else
					if(newY >= height)
						newY = height - 1;
				out[i++] = in[newY * width + newX];
			}

		}

	}

	public String toString()
	{
		return "Twirl";
	}

	private double angle;
}


class ZoomFilter extends Filter
{

	public ZoomFilter(double hAmount, double vAmount)
	{
		this.hAmount = hAmount;
		this.vAmount = vAmount;
	}

	public void apply(byte in[], byte out[], int width, int height)
	{
		int i = 0;
		int j = 0;
		int w2 = width / 2;
		int h2 = height / 2;
		if(vAmount == 0.0D)
		{
			for(int y = 0; y < height; y++)
			{
				i = y * width;
				for(int x = 0; x < width; x++)
				{
					int k = w2 + (int)((double)(x - w2) * hAmount);
					if(k >= 0 && k < width)
						out[j++] = in[i + k];
					else
						out[j++] = 0;
				}

			}

		} else
			if(hAmount == 0.0D)
			{
				for(int y = 0; y < height; y++)
				{
					int k = h2 + (int)((double)(y - h2) * vAmount);
					for(int x = 0; x < width; x++)
						if(k >= 0 && k < height)
							out[j++] = in[x + k * width];
						else
							out[j++] = 0;

				}

			} else
			{
				for(int y = 0; y < height; y++)
				{
					int ky = h2 + (int)((double)(y - h2) * vAmount);
					for(int x = 0; x < width; x++)
					{
						int kx = w2 + (int)((double)(x - w2) * hAmount);
						if(kx >= 0 && kx < width && ky >= 0 && ky < height)
							out[j++] = in[ky * width + kx];
						else
							out[j++] = 0;
					}

				}

			}
	}

	public String toString()
	{
		String io = "In";
		String dir = "";
		if(vAmount == 0.0D)
			dir = "Horizontal";
		else
			if(hAmount == 0.0D)
				dir = "Vertical";
		if(hAmount < 1.0D || vAmount < 1.0D)
			io = "Out";
		return (new StringBuilder()).append("Move ").append(io).append(" ").append(dir).toString();
	}

	private double hAmount;
	private double vAmount;
}


class FadeFilter extends Filter
{

	public FadeFilter(int amount)
	{
		this.amount = amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	public int getAmount()
	{
		return amount;
	}

	public void apply(byte in[], byte out[], int width, int height)
	{
		int i = 0;
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				int p = (in[i] & 0xff) - amount;
				if(p < 0)
					p = 0;
				out[i++] = (byte)p;
			}

		}

	}

	public String toString()
	{
		return (new StringBuilder()).append("Fade ").append(amount).toString();
	}

	private int amount;
}


class BlurHVFilter extends Filter
{

	BlurHVFilter()
	{
	}

	public void apply(byte in[], byte out[], int width, int height)
	{
		int index = 0;
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				int t = 0;
				if(y != 0)
					t += in[index - width] & 0xff;
				if(y != height - 1)
					t += in[index + width] & 0xff;
				if(x != 0)
					t += in[index - 1] & 0xff;
				if(x != width - 1)
					t += in[index + 1] & 0xff;
				t += in[index] & 0xff;
				t /= 5;
				if(t > 255)
					t = 255;
				out[index++] = (byte)t;
			}

		}

	}

	public String toString()
	{
		return "Blur";
	}
}
