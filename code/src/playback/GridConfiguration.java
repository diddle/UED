/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playback;

import GUI.ParticlePanel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;

/**
 *
 * @author Niels Visser
 */
public abstract class GridConfiguration {
    
    protected int velocity;
    protected MidiChannel channel;
    protected Instrument instrument;
    
    public GridConfiguration(Instrument instrument, int velocity) {
        this.velocity = velocity;
        this.instrument = instrument;
        // configureer kanaal...
        this.channel = null;
        int channelIndex = GetChannelFor(instrument);
        this.channel = Player.getInstance().getSynthesizer().getChannels()[channelIndex];
        UseChannel(channelIndex, instrument);
    }

    public int getVelocity() {
        return velocity;
    }
    
    public MidiChannel getChannel() {
        return this.channel;
    }
    
    /**
     * Speelt alle (relatieve) noten af
     * @param tones Noten, relatief aan het grid (dus 0 is laagste, 9 is hoogste)
     * @param vp 
     */
    public abstract void playTones(List<Integer> tones, ParticlePanel vp);
    
    /**
     * Speelt alle meegegeven (absolute) noten af op het eigen kanaal.
     * @param tones Lijst van absolute noten (dus 0 is de laagste, 127 is de hoogste)
     * @param velocity
     * @param vp 
     */
    protected void playNotesOnChannel(List<Integer> tones, int velocity, ParticlePanel vp) {
        for(int tone : tones) {
            this.channel.noteOn(tone, velocity);
            vp.notePlayed(tone, velocity, null);
        }
    }
    
    protected static HashMap<Integer, List<Instrument>> channelMap = new HashMap<Integer, List<Instrument>>();
    
    /**
     * Registreert channel voor gebruik van opgegeven instrument. Channels die
     * in gebruik zijn kunnen niet gepatcht worden. Meerdere registraties per channel
     * zijn toegestaan.
     * @param channel
     * @param instrument 
     */
    public static void UseChannel(int channel, Instrument instrument) {
        if(channelMap.containsKey(channel)) {
            List<Instrument> instruments = channelMap.get(channel);
            instruments.add(instrument);
        }
        else {
            List<Instrument> instruments = new ArrayList<Instrument>();
            instruments.add(instrument);
            channelMap.put(channel, instruments);
        }
    }
    
    /**
     * Geeft channel vrij van gebruik. Vrije channels kunnen gepatcht worden om
     * zo andere geluiden af te kunnen spelen
     * @param channel
     * @param instrument 
     */
    public static void FreeChannel(int channel, Instrument instrument) {
        int targetChannel = -1;
        for(Integer i : channelMap.keySet()) {
            List<Instrument> instruments = channelMap.get(i);
            if(instruments.contains(instrument)) {
                targetChannel = i;
            }
        }
        if(targetChannel != -1) {
            channelMap.get(targetChannel).remove(instrument);
        }
    }
    
    /**
     * Geeft het eerstvolgende vrije channel terug. Hierop kan een patch uitgevoerd
     * worden met PatchChannel
     * @return channel index, -1 indien er geen channel beschikbaar is
     */
    public static int GetFirstFreeChannel() {
        for(int i=0; i<9; i++) {
            if(channelMap.containsKey(i)) {
                if(channelMap.get(i).isEmpty()) {
                    return i;
                }
            }
            else {
                List<Instrument> instruments = new ArrayList<Instrument>();
                channelMap.put(i, instruments);
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Retourneert channel voor het aangegeven instrument. Indien er een channel
     * in gebruik is met hetzelfde instrument (dus als een andere speler het instrument
     * ook heeft) wordt dat channel gereturnd. Indien dat niet het geval is, wordt
     * een vrije channel gereturnd (die gepatcht is naar het meegegeven instrument).
     * Indien er geen channel vrij is en er geen channels met gelijke instrumenten
     * zijn, wordt -1 teruggegeven.
     * @param instrument
     * @return 
     */
    public static int GetChannelFor(Instrument instrument) {
        if(instrument.getName().toLowerCase().contains("drum")) {
            // 9 is het standaardkanaal voor drums
            return 9;
        }
        int targetChannel = -1;
        for(Integer i : channelMap.keySet()) {
            List<Instrument> instruments = channelMap.get(i);
            if(instruments.contains(instrument)) {
                targetChannel = i;
            }
        }
        if(targetChannel != -1) {
            return targetChannel;
        }
        else {
            // channel needs patching
            int freeChannel = GetFirstFreeChannel();
            if(freeChannel > -1) {
                PatchChannel(freeChannel, instrument);
                return freeChannel;
            }
        }
        return -1;
    }
    
    /**
     * Patcht een kanaal met een nieuw instrument.
     * @param channel
     * @param newInstrument 
     */
    protected static void PatchChannel(int channel, Instrument newInstrument) {
        MidiChannel[] channels = null;
        channels = Player.getInstance().getSynthesizer().getChannels();
        Patch instrPatch = newInstrument.getPatch();
        channels[channel].programChange(instrPatch.getBank(), instrPatch.getProgram());
    }
    
    public Instrument getInstrument() {
    	return instrument;
    }
    
    /**
     * Geeft de naam van het instrument dat tot deze configuratie behoort
     * @return 
     */
    public String toString() {
        return this.instrument.getName();
    }

    void muteActiveTones() {
        this.channel.allNotesOff();
    }
}
