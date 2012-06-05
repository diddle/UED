/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playback;

import java.util.List;

/**
 *
 * @author Niels Visser
 */
public class DrumGridConfiguration extends GridConfiguration{
    
    private List<Integer> drumkit;

    public DrumGridConfiguration(List<Integer> drumkit, int velocity) {
        super(InstrumentHolder.GetDrums(), velocity);
        this.drumkit = drumkit;
    }
    
    public List<Integer> getDrumkit() {
        return this.drumkit;
    }
    
}
