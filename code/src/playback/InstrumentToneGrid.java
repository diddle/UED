/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playback;

import GUI.NoteIndex;
import GUI.ParticlePanel;
import GUI.VisualizationPanel;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.Instrument;

/**
 *
 * @author Niels Visser
 */
public class InstrumentToneGrid extends ToneGrid{
    
    private int baseNote;
    private int velocity;
    
    public InstrumentToneGrid(InstrumentGridConfiguration config) {
        super(config.getNumnotes());
        this.instrument = config.getInstrument();
        this.baseNote = config.getBasenote();
        this.velocity = config.getVelocity();
    }

    InstrumentToneGrid(int i, int i0, Instrument instrument, int velocity) {
        this(null);
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int getBaseNote() {
        return this.baseNote;
    }
    
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
    public List<Integer> getColumnTones(int x) {
        List<Integer> tones = new ArrayList<Integer>();
        int i = 0;
        for(Boolean b : this.grid.get(x)) {
            if(b) {
                tones.add(this.baseNote + this.toneOffset(i));
            }
            i++;
        }
        return tones;
    }

    @Override
    public void playColumnTones(int x, ParticlePanel vp) {
        super.playColumnTones(x, velocity, vp);
    }

    @Override
	public void setInstrument(Instrument instrument) {
		this.instrument=instrument;
	}
	
	public void setVelocity(int velocity){
		this.velocity = velocity ;
	}
}
