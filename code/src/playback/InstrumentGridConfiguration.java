/*	Copyright Niels Visser 2012
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
package playback;

import GUI.ParticlePanel;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.Instrument;

public class InstrumentGridConfiguration extends GridConfiguration {
    
    private int basenote;
    private int numnotes;

    public InstrumentGridConfiguration(int basenote, int numnotes, Instrument instrument, int velocity) {
        super(instrument, velocity);
        this.basenote = basenote;
        this.numnotes = numnotes;
    }

    public int getBasenote() {
        return basenote;
    }

    public int getNumnotes() {
        return numnotes;
    }
    
    /**
     * Zorgt ervoor dat de noten altijd goed bij elkaar klinken. Gaat ervan uit 
     * dat alle noten mogelijk zijn in de lijst die meegegeven wordt, retourneert
     * een lijst met deze noten op een dergelijke manier getransponeerd dat ze in
     * een toonladder zitten die altijd goed klinkt.
     * @param tone
     * @return 
     */
    private int toneOffset(int tone) {
        int rem = tone % 5;
        int oct = (int)Math.floor(tone / 5);
        int remNew = 0;
        switch(rem) {
            case 0: remNew = 0; break;
            case 1: remNew = 2; break;
            case 2: remNew = 5; break;
            case 3: remNew = 7; break;
            case 4: remNew = 9; break;
        }
        return oct * 12 + remNew;
    }

    @Override
    public void playTones(List<Integer> tones, ParticlePanel vp) {
        // converteer van relatieve naar absolute lijst
        List<Integer> toPlay = new ArrayList<Integer>();
        for(Integer tone : tones) {
            toPlay.add(this.basenote + this.toneOffset(tone));
        }
        this.playNotesOnChannel(toPlay, velocity, vp);
    }
    
}
