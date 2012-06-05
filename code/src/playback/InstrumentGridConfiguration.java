/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playback;

import javax.sound.midi.Instrument;

/**
 *
 * @author Niels Visser
 */
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
    
    
    
}
