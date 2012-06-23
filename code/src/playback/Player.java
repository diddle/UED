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
import GUI.ParticlePanel;
//import GUI.VisualizationPanel;
import java.util.*;

/**
 *
 * @author Niels Visser
 */
public class Player implements Runnable {
    
    private List<ToneGrid> grids;
    private int bpm;
    private long lastBeat;
    private int width;
    private int pos;
    private Synthesizer synthesizer;
    private Instrument[] instrumentList;
    private boolean stopped = false;
    private GridPanel gridPanel;
    private HashMap<Integer, List<ToneGrid>> channelUses;
    private ParticlePanel pp;
    private static Player instance = null;
    
    private Player() {}
    
    public static Player getInstance() {
        if(instance == null) {
            instance = new Player();
        }
        return instance;
    }
    
    public void init(int bpm, int width, ParticlePanel pp) {
        this.grids = new ArrayList<ToneGrid>();
        this.channelUses = new HashMap<Integer, List<ToneGrid>>();
        this.bpm = bpm;
        this.lastBeat = this.now();
        this.width = width;
        this.pos = 0;
        this.pp = pp;
        
        try {
            this.synthesizer = MidiSystem.getSynthesizer();
            Instrument[] instrArr = this.synthesizer.getAvailableInstruments();
            for (Instrument i : instrArr) {
                this.synthesizer.loadInstrument(i);
            }
            this.synthesizer.open();
            this.instrumentList = synthesizer.getAvailableInstruments();
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Synthesizer getSynthesizer() {
        return this.synthesizer;
    }
    
    /**
     * Geeft de huidige tijd
     * @return 
     */
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
                this.pos = (this.pos + 1) % this.width;
                
                if(this.gridPanel != null)
                    gridPanel.repaint();
                
                synchronized(this.grids) {
                    for(GridConfiguration gc : InstrumentHolder.getInstance().getAvailableConfigurations()) {
                        gc.muteActiveTones();
                    }
                    for(ToneGrid g : this.grids) {
                        if(g.isIsActive())
                            g.playColumnTones(pos, this.pp);
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
    
    /**
     * Registreer een ToneGrid in deze Player.
     * @param grid
     */
    public void registerToneGrid(ToneGrid grid) {
        grid.registerCallBack(this.width);
        synchronized (this.grids) {
            if (!this.grids.contains(grid)) {
                this.grids.add(grid);
            }
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
     * Wissel een ToneGrid van GridConfiguration en dus van instrument.
     * @param grid welk grid van instrument moet veranderen
     * @param config de configuratie van het instrument
     */
    public void changeInstrument(ToneGrid grid, GridConfiguration config) {
        grid.setConfiguration(config);
    }
    
    /**
     * MOET OPGESLAGEN WORDEN IN DE PLAYER!
     * @return 
     */
    public int getHeight() {
        return 10;
    }
    
    public int getWidth() {
        return this.width;
    }
}
