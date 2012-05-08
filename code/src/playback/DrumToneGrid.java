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
public class DrumToneGrid extends ToneGrid {
    
    private List<Integer> tracks;
    
    public DrumToneGrid(List<Integer> tracks, Instrument instrument) {
        super(tracks.size());
        this.tracks = tracks;
        this.instrument = instrument;
    }

    @Override
    public List<Integer> getColumnTones(int x) {
        List<Integer> tones = new ArrayList<Integer>();
        int i = 0;
        for(Boolean b : this.grid.get(x)) {
            if(b) {
                tones.add(this.tracks.get(i));
            }
            i++;
        }
        return tones;
    }
}
