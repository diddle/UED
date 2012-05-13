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
import GUI.GridPanel;

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
    private Synthesizer synthesizer;
    private Instrument[] instrumentList;
    private int lastChannelIndex = -1;
    private boolean stopped = false;
    private GridPanel gridPanel;
    
    public Player(int bpm, int w) {
        this.grids = new ArrayList<ToneGrid>();
        this.bpm = bpm;
        this.lastBeat = this.now();
        this.w = w;
        this.pos = 0;
        
        try {
            this.synthesizer = MidiSystem.getSynthesizer();
            this.synthesizer.open();
            this.instrumentList = synthesizer.getAvailableInstruments();
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
                this.lastBeat = this.now();
                this.pos = (this.pos + 1) % this.w;
                
                if(this.gridPanel != null)
                	gridPanel.repaint();
                
                synchronized(this.grids) {
                    for(ToneGrid g : this.grids) {
                        if(g.isIsActive())
                            g.playColumnTones(pos);
                    }
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.synthesizer.close();
    }
    
    public void registerToneGrid(ToneGrid grid) {
        grid.registerCallBack(this.w);
        synchronized (this.grids) {
            if (!this.grids.contains(grid)) {
                this.grids.add(grid);
            }
        }
        if(grid instanceof DrumToneGrid) {
            MidiChannel[] channels = this.synthesizer.getChannels();
            grid.setChannel(channels[9]);
        }
        else {
            this.synthesizer.loadInstrument(grid.getInstrument());
            MidiChannel[] channels = this.synthesizer.getChannels();
            Patch instrPatch = grid.getInstrument().getPatch();
            this.lastChannelIndex++;
            channels[this.lastChannelIndex].programChange(instrPatch.getBank(), instrPatch.getProgram());
            grid.setChannel(channels[this.lastChannelIndex]);
        }
    }
    
    public void setGridPanel(GridPanel gridPanel) {
    	this.gridPanel = gridPanel;
    }
    
    public void setTempo(int bpm) {
        this.bpm = bpm;
    }

    public Instrument[] getAllInstruments() {
        return this.instrumentList;
    }
    
    public void stop() {
        this.stopped = true;
    }
    
    public int getPosition() {
        return this.pos;
    }
    
    public List<ToneGrid> getActiveGrids() {
        List<ToneGrid> result = new ArrayList<ToneGrid>();
        for(ToneGrid tg : this.grids) {
            if(tg.isIsActive()) {
                result.add(tg);
            }
        }
        return result;
    }
    
    /**
     * MOET OPGESLAGEN WORDEN IN DE PLAYER!
     * @return 
     */
    public int getHeight() {
        return 10;
    }
    
    public int getWidth() {
        return this.w;
    }
}
