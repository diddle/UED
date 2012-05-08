/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playback;

import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.Instrument;

/**
 *
 * @author Niels Visser
 */
public class InstrumentToneGrid extends ToneGrid{
    
    private int baseNote;
    
    public InstrumentToneGrid(int baseNote, int numNotes, Instrument instrument) {
        super(numNotes);
        this.instrument = instrument;
        this.baseNote = baseNote;
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
    public void playColumnTones(int x) {
        super.playColumnTones(x, 60);
    }
}
