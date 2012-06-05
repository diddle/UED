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
import GUI.VisualizationPanel;
import java.util.*;

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
    private HashMap<Integer, List<ToneGrid>> channelUses;
    private ParticlePanel vp;
    
    public Player(int bpm, int w, ParticlePanel vp) {
        this.grids = new ArrayList<ToneGrid>();
        this.channelUses = new HashMap<Integer, List<ToneGrid>>();
        this.bpm = bpm;
        this.lastBeat = this.now();
        this.w = w;
        this.pos = 0;
        this.vp = vp;
        
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
                            g.playColumnTones(pos, this.vp);
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
    
    private void useChannel(int channel, ToneGrid tg) {
        if(!this.channelUses.containsKey(new Integer(9))) {
            this.channelUses.put(channel, new ArrayList<ToneGrid>());
        }
        this.channelUses.get(channel).add(tg);
    }
    
    private void releaseChannel(int channel, ToneGrid tg) {
        this.channelUses.get(channel).remove(tg);
    }
    
    private int getFreeChannel() {
        // 16 kanalen
        for(int i=0; i<16; i++) {
            if(i != 9 && (!this.channelUses.containsKey(i) || this.channelUses.get(i).isEmpty())) {
                return i;
            }
        }
        return -1;
    }
    
    private int gridIndex(ToneGrid g) {
        for(int i = 0; i< this.grids.size(); i++) {
            if(this.grids.get(i) == g) {
                return i;
            }
        }
        return -1;
    }
    
    public void changeInstrument(ToneGrid tg, Instrument newInstrument) {
        GridConfiguration gc = null;
        List<List<Boolean>> gridBackup = tg.getGrid();
        ToneGrid newToneGrid = null;
        if(newInstrument.toString().toLowerCase().contains("drum")) {
            //... drums
            Instrument drums = InstrumentHolder.GetDrums();
            gc = InstrumentHolder.getInstance().GetGridConfigurationByInstrument(drums);
            if(gc == null) {
                throw new RuntimeException("Could not find configuration for drum kit!");
            }
            newToneGrid = new DrumToneGrid((DrumGridConfiguration)gc);
        }
        else {
            gc = InstrumentHolder.getInstance().GetGridConfigurationByInstrument(newInstrument);
            if(gc == null) {
                throw new RuntimeException("Could not find configuration for " + newInstrument.toString());
            }
            newToneGrid = new InstrumentToneGrid((InstrumentGridConfiguration)gc);
        }
        newToneGrid.setGrid(gridBackup);
        // maak het kanaal klaar
        int currentGridIndex = this.gridIndex(tg);
        MidiChannel[] channels = this.synthesizer.getChannels();
        int currentChannel = -1;
        for(Integer key : this.channelUses.keySet()) {
            List<ToneGrid> tgl = this.channelUses.get(key);
            if(tgl.contains(tg)) {
                currentChannel = key;
            }
        }
        if(currentChannel == -1) {
            throw new RuntimeException("Channel could not be found");
        }
        if(currentChannel != 9) {
            this.releaseChannel(currentChannel, tg);
        }
        int newChannel = this.getFreeChannel();
        Patch instrPatch = newInstrument.getPatch();
        channels[newChannel].programChange(instrPatch.getBank(), instrPatch.getProgram());
        tg.setIsActive(false);
        newToneGrid.channel = channels[newChannel];
        newToneGrid.setIsActive(true);
        this.grids.set(currentGridIndex, newToneGrid);
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
            useChannel(9, grid);
            grid.setChannel(channels[9]);
        }
        else {
            this.synthesizer.loadInstrument(grid.getInstrument());
            MidiChannel[] channels = this.synthesizer.getChannels();
            Patch instrPatch = grid.getInstrument().getPatch();
            this.lastChannelIndex++;
            channels[this.lastChannelIndex].programChange(instrPatch.getBank(), instrPatch.getProgram());
            grid.setChannel(channels[this.lastChannelIndex]);
            useChannel(this.lastChannelIndex, grid);
        }
    }
    
    /**
     * 
     * @param grid
     * @param instrument Drums are equal to null for the purposes of this method
     * @param velocity
     */
    public void setInstrument(ToneGrid grid,Instrument instrument,int velocity){
    	if(grid instanceof DrumToneGrid){
    		//check if 
    		if(instrument == null){
    			//doNothing
    		}
    		else{
    			grid.setIsActive(false);
    			grid.getChannel().allNotesOff();
    			ToneGrid newToneGrid = new InstrumentToneGrid(60, 16, instrument, velocity);
    	        this.registerToneGrid(newToneGrid);
    	        newToneGrid.setAllTones(grid.getAllTones());
    	        newToneGrid.setIsActive(true);
    		}
    	} else {
    		if(instrument==null){
    			boolean drums = false;
    			for(int i=0; i < getActiveGrids().size(); i++){
    				if(getActiveGrids().get(i) instanceof DrumToneGrid){
    					drums=true;
    				}
    			}
    			if(!drums){
    				
        			boolean stop=false;
        			for(int i=0;i<getActiveGrids().size()&&!stop;i++){
        				if(getActiveGrids().get(i).getChannel().equals(synthesizer.getChannels()[lastChannelIndex])){
        					getActiveGrids().get(i).setChannel(grid.getChannel());
        					lastChannelIndex--;
        					stop=true;
        					
        				}
        			}
        			grid.setIsActive(false);
        			grid.getChannel().allNotesOff();
        			List<Integer> drumset = new ArrayList<Integer>();
        	        drumset.add(DrumToneGrid.d35_Acoustic_Bass_Drum);
        	        drumset.add(DrumToneGrid.d36_Bass_Drum_1);
        	        drumset.add(DrumToneGrid.d37_Side_Stick);
        	        drumset.add(DrumToneGrid.d38_Acoustic_Snare);
        	        drumset.add(DrumToneGrid.d41_Low_Floor_Tom);
        	        drumset.add(DrumToneGrid.d42_Closed_Hi_Hat);
        	        drumset.add(DrumToneGrid.d44_Pedal_Hi_Hat);
        	        drumset.add(DrumToneGrid.d49_Crash_Cymbal_1);
        	        drumset.add(DrumToneGrid.d56_Cowbell);
        	        drumset.add(DrumToneGrid.d54_Tambourine);
        			ToneGrid newToneGrid = new DrumToneGrid(drumset);
        	        this.registerToneGrid(newToneGrid);
        	        newToneGrid.setAllTones(grid.getAllTones());
        	        newToneGrid.setIsActive(true);
    			//kanaal vrijmaken, nieuwe drumgrid maken, alle tonen inzetten van de grid, registeren.
    			//drum laden
    			}
    		} else{
    			grid.setInstrument(instrument);
    			((InstrumentToneGrid) grid).setVelocity(velocity);
    			this.synthesizer.loadInstrument(grid.getInstrument());
    			Patch instrPatch = grid.getInstrument().getPatch();
    			grid.getChannel().programChange(instrPatch.getBank(), instrPatch.getProgram());
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
