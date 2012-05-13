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
 * @author Niels Kamp, Kasper Vaessen, Niels Visser
 */
public abstract class ToneGrid {

    protected List<List<Boolean>> grid;
    protected int width;
    protected int height;
    protected Instrument instrument;
    protected MidiChannel channel;
    protected boolean isActive;

    public ToneGrid(int height) {
        this.height = height;
    }

    public void toggleTone(int column, int note) {

        List<Boolean> col = this.grid.get(column);
        synchronized (col) {
            col.set(note, !col.get(note));
        }
    }
    
    public void activateTone(int column, int note) {
    	List<Boolean> col = this.grid.get(column);
        synchronized (col) {
            col.set(note, true);
        }
    }
    
    public void deactivateTone(int column, int note) {
    	List<Boolean> col = this.grid.get(column);
        synchronized (col) {
            col.set(note, false);
        }
    }

    public synchronized List<List<Boolean>> getAllTones() {
        List<List<Boolean>> result = new ArrayList<List<Boolean>>();
        for (int i = 0; i < this.width; i++) {
            List<Boolean> el = new ArrayList<Boolean>();
            result.add(el);
            for (int j = 0; j < this.height; j++) {
                el.add(this.grid.get(i).get(j));
            }
        }
        return result;
    }

    
    public boolean getTone(int column, int note) {
        synchronized (this.grid) {
            return this.grid.get(column).get(note);
        }
    }
    
    public abstract void playColumnTones(int column);
    
    public void playColumnTones(int column, int velocity) {
        List<Integer> tones = this.getColumnTones(column);
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
        this.width = w;
        this.grid = new ArrayList<List<Boolean>>();
        for (int i = 0; i < w; i++) {
            List<Boolean> el = new ArrayList<Boolean>();
            this.grid.add(el);
            for (int j = 0; j < height; j++) {
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
