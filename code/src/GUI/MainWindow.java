/*	Copyright Niels Kamp 2012
 *	Copyright Kasper Vaessen 2012
 *	Copyright Niels Visser 2012
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
package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import playback.Player;

public class MainWindow extends JFrame {
	public MainWindow(Player p) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridPanel gf = new GridPanel(p);
		ButtonPanel bp = new ButtonPanel(p, gf);
		CalibrationPanel cp = CalibrationPanel.getInstance();
		this.fullScreen();
		gf.setBounds(0, 0, this.getWidth(), this.getHeight());
		bp.setBounds(0, 0, this.getWidth(), this.getHeight());
		cp.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.getLayeredPane().add(gf, new Integer(2));
		this.getLayeredPane().add(bp, new Integer(3));
		this.getLayeredPane().add(cp, new Integer(4));
	}

	private void fullScreen() {
		this.setResizable(false);
		if (!this.isDisplayable()) {
			// Can only do this when the frame is not visible
			this.setUndecorated(true);
		}
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
		}

	}

	public void setParticlePanel(ParticlePanel pp) {
		pp.setBounds(0, 0, getWidth(), getHeight());
		pp.init();
		this.getLayeredPane().add(pp, new Integer(1));
	}
}
