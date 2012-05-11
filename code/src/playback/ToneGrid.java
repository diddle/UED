/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playback;

import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;

/**
 *
 * @author Niels Visser
 */
public abstract class ToneGrid {

    protected List<List<Boolean>> grid;
    protected int w;
    protected int h;
    protected Instrument instrument;
    protected MidiChannel channel;
    protected boolean isActive;

    public ToneGrid(int h) {
        this.h = h;
    }

    public void toggleTone(int x, int y) {

        List<Boolean> col = this.grid.get(x);
        synchronized (col) {
            col.set(y, !col.get(y));
        }
    }
    
    public void activateTone(int x, int y) {
    	List<Boolean> col = this.grid.get(x);
        synchronized (col) {
            col.set(y, true);
        }
    }
    
    public void deactivateTone(int x, int y) {
    	List<Boolean> col = this.grid.get(x);
        synchronized (col) {
            col.set(y, false);
        }
    }

    public synchronized List<List<Boolean>> getAllTones() {
        List<List<Boolean>> result = new ArrayList<List<Boolean>>();
        for (int i = 0; i < this.w; i++) {
            List<Boolean> el = new ArrayList<Boolean>();
            result.add(el);
            for (int j = 0; j < this.h; j++) {
                el.add(this.grid.get(i).get(j));
            }
        }
        return result;
    }

    
    public boolean getTone(int x, int y) {
        synchronized (this.grid) {
            return this.grid.get(x).get(y);
        }
    }
    
    public abstract void playColumnTones(int x);
    
    public void playColumnTones(int x, int velocity) {
        List<Integer> tones = this.getColumnTones(x);
        this.channel.allNotesOff();
        for(int tone : tones) {
            this.channel.noteOn(tone, velocity);
        }
    }
    
    public abstract List<Integer> getColumnTones(int x);
    
    public Instrument getInstrument() {
        return this.instrument;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public MidiChannel getChannel() {
        return channel;
    }

    public void setChannel(MidiChannel channel) {
        this.channel = channel;
    }
    
    public void registerCallBack(int w) {
        this.w = w;
        this.grid = new ArrayList<List<Boolean>>();
        for (int i = 0; i < w; i++) {
            List<Boolean> el = new ArrayList<Boolean>();
            this.grid.add(el);
            for (int j = 0; j < h; j++) {
                el.add(false);
            }
        }
        this.isActive = false;
    }
    
    public void clear() {
        for(List<Boolean> l1 : this.grid) {
            for(int i = 0; i < l1.size(); i++) {
                l1.set(i, false);
            }
        }
    }
    
}
