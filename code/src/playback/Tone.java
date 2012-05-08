package playback;
import javax.sound.midi.Instrument;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Niels Visser
 */
public class Tone {
    
    private Instrument instrument;
    private int index;

    public Tone(Instrument instrument, int index) {
        this.instrument = instrument;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Instrument getInstrument() {
        return instrument;
    }
}
