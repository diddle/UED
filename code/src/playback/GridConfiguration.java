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
public abstract class GridConfiguration {
    
    private int velocity;
    protected Instrument instrument;

    public GridConfiguration(Instrument instrument, int velocity) {
        this.instrument = instrument;
        this.velocity = velocity;
    }

    public int getVelocity() {
        return velocity;
    }
    
    public Instrument getInstrument() {
        return this.instrument;
    }
    
    
}
