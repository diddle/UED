/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;

/**
 *
 * @author Niels Visser
 */
public class Player implements Runnable {
    
    private List<ToneGrid> grids;
    private int bpm;
    private long lastBeat;
    private int w;
    private int pos;
    private Synthesizer s;
    private Instrument[] il;
    private int lastChannelIndex = -1;
    private boolean stopped = false;
    
    public Player(int bpm, int w) {
        this.grids = new ArrayList<ToneGrid>();
        this.bpm = bpm;
        this.lastBeat = this.now();
        this.w = w;
        this.pos = 0;
        
        try {
            this.s = MidiSystem.getSynthesizer();
            this.s.open();
            this.il = s.getAvailableInstruments();
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private long now() {
        return new Date().getTime();
    }
    
    public void start() {
        Thread runner = new Thread(this, "SoundOverseer");
        runner.start();
    }
    
    @Override
    public void run() {
        while(!this.stopped) {
            if(this.lastBeat < this.now() - 15000d / (double)this.bpm) {
                synchronized(this.grids) {
                    for(ToneGrid g : this.grids) {
                        if(g.isIsActive())
                            g.playColumnTones(pos);
                    }
                }
                this.lastBeat = this.now();
                this.pos = (this.pos + 1) % this.w;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.s.close();
    }
    
    public void registerToneGrid(ToneGrid grid) {
        synchronized (this.grids) {
            if (!this.grids.contains(grid)) {
                this.grids.add(grid);
            }
        }
        this.s.loadInstrument(grid.getInstrument());
        MidiChannel[] channels = this.s.getChannels();
        Patch instrPatch = grid.getInstrument().getPatch();
        this.lastChannelIndex++;
        channels[this.lastChannelIndex].programChange(instrPatch.getBank(), instrPatch.getProgram());
        grid.setChannel(channels[this.lastChannelIndex]);
    }
    
    public void setTempo(int bpm) {
        this.bpm = bpm;
    }

    public Instrument[] getAllInstruments() {
        return this.il;
    }
    
    public void stop() {
        this.stopped = true;
    }
}
