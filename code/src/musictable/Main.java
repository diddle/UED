/*	Copyright Alexander Drechsel 2012
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
package musictable;

import GUI.MainWindow;
import GUI.ParticlePanel;
//import GUI.VisualizationPanel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.*;
import playback.*;

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
		InstrumentGridConfiguration guitar = new InstrumentGridConfiguration(60, 16, InstrumentHolder.SearchInstrument("gt."), 60);
		InstrumentHolder.getInstance().addConfiguration(guitar);
		// voeg misc instrument toe
		InstrumentGridConfiguration misc = new InstrumentGridConfiguration(60, 16, InstrumentHolder.SearchInstrument("bell"), 60);
		InstrumentHolder.getInstance().addConfiguration(misc);

		ToneGrid bassGrid = new ToneGrid(bass);
		p.registerToneGrid(bassGrid);
		bassGrid.setIsActive(true);

		ToneGrid pianoGrid = new ToneGrid(piano);
		p.registerToneGrid(pianoGrid);
		pianoGrid.setIsActive(true);

		ToneGrid guitarGrid = new ToneGrid(guitar);
		p.registerToneGrid(guitarGrid);
		guitarGrid.setIsActive(true);

		ToneGrid drumGrid = new ToneGrid(drums);
		p.registerToneGrid(drumGrid);
		drumGrid.setIsActive(true);
		
		MainWindow mw = new MainWindow(p);

		mw.setParticlePanel(pp);

		p.start();
	}

}
