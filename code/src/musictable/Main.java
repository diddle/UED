/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musictable;

import GUI.MainWindow;
import GUI.ParticlePanel;
//import GUI.VisualizationPanel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;
import playback.*;

/**
 *
 * @author Niels Visser
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException, InvalidMidiDataException {

		ParticlePanel pp = new ParticlePanel();
		Player p = Player.getInstance();
		p.init(120, 16, pp);

		// voeg drums toe
		List<Integer> drumkit = new ArrayList<Integer>();
		drumkit.add(DrumGridConfiguration.d35_Acoustic_Bass_Drum);
		drumkit.add(DrumGridConfiguration.d36_Bass_Drum_1);
		drumkit.add(DrumGridConfiguration.d37_Side_Stick);
		drumkit.add(DrumGridConfiguration.d38_Acoustic_Snare);
		drumkit.add(DrumGridConfiguration.d41_Low_Floor_Tom);
		drumkit.add(DrumGridConfiguration.d42_Closed_Hi_Hat);
		drumkit.add(DrumGridConfiguration.d44_Pedal_Hi_Hat);
		drumkit.add(DrumGridConfiguration.d49_Crash_Cymbal_1);
		drumkit.add(DrumGridConfiguration.d56_Cowbell);
		drumkit.add(DrumGridConfiguration.d54_Tambourine);
		DrumGridConfiguration drums = new DrumGridConfiguration(drumkit, 120);
		InstrumentHolder.getInstance().addConfiguration(drums);
		// voeg piano toe
		InstrumentGridConfiguration piano = new InstrumentGridConfiguration(60, 16, InstrumentHolder.SearchInstrument("piano"), 60);
		InstrumentHolder.getInstance().addConfiguration(piano);
		// voeg bas toe
		InstrumentGridConfiguration bass = new InstrumentGridConfiguration(36, 16, InstrumentHolder.SearchInstrument("bass"), 180);
		InstrumentHolder.getInstance().addConfiguration(bass);
		// voeg gitaar toe
		InstrumentGridConfiguration guitar = new InstrumentGridConfiguration(60, 16, InstrumentHolder.SearchInstrument("guitar"), 60);
		InstrumentHolder.getInstance().addConfiguration(guitar);


		ToneGrid bassGrid = new ToneGrid(bass);
		p.registerToneGrid(bassGrid);
		bassGrid.setIsActive(true);

		ToneGrid highthingy = new ToneGrid(piano);
		p.registerToneGrid(highthingy);
		highthingy.setIsActive(true);

		ToneGrid strangesound1 = new ToneGrid(guitar);
		p.registerToneGrid(strangesound1);
		strangesound1.setIsActive(true);

		ToneGrid drumGrid = new ToneGrid(drums);
		p.registerToneGrid(drumGrid);
		drumGrid.setIsActive(true);


		MainWindow mw = new MainWindow(p);

		mw.setParticlePanel(pp);

		p.start();
	}

}
