/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musictable;

import GUI.GridPanel;
import GUI.MainWindow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.*;
import playback.DrumToneGrid;
import playback.InstrumentToneGrid;
import playback.Player;
import playback.ToneGrid;

/**
 *
 * @author Niels Visser
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InvalidMidiDataException {
        
        Player p = new Player(120, 16);
        Instrument[] il = p.getAllInstruments();
        
        ToneGrid bass = new InstrumentToneGrid(36, 10, il[1%il.length], 60);
        p.registerToneGrid(bass);
        bass.setIsActive(true);
        
        ToneGrid highthingy = new InstrumentToneGrid(60, 16, il[25%il.length], 60);
        p.registerToneGrid(highthingy);
        highthingy.setIsActive(true);
        
        ToneGrid strangesound1 = new InstrumentToneGrid(60, 16, il[33%il.length], 60);
        p.registerToneGrid(strangesound1);
        strangesound1.setIsActive(true);
        
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
        ToneGrid drums = new DrumToneGrid(drumset);
        p.registerToneGrid(drums);
        drums.setIsActive(true);
        
        
        
        ////gf.setVisible(true);
        //gf.display();
        new MainWindow(p);
        
        p.start();
        
        
        
        //Main m = new Main();
//        try {
//            Synthesizer s = MidiSystem.getSynthesizer();
//            s.open();
//            Instrument[] is = s.getAvailableInstruments();
//
//            int j = 0;
//            for(Instrument i : is ) {
//                
//                System.out.println("" + j++ + ": " + i.toString());
//            }
//            s.close();
//            //System.exit(0);
//            
//            
//            
//            Soundbank bank = s.getDefaultSoundbank();
//            s.loadAllInstruments(bank);
//            Instrument instrs[] = s.getAvailableInstruments();//s.getLoadedInstruments();
//            //s.loadInstrument(instrs[10]);
//            for (int i = 0; i < instrs.length; i++) {
//                System.out.println(instrs[i].getName());
//            }
//            MidiChannel[] channels = s.getChannels();
//            Patch seashorePatch = instrs[10].getPatch();
//            channels[1].programChange(seashorePatch.getBank(), seashorePatch.getProgram());
//            
//            System.out.print("Druk op enter om de toon af te spelen...");
//            System.in.read();
//            //channels[1].noteOn(60, 60);
//            channels[1].noteOn(1, 127);
//            channels[1].noteOn(2, 127);
//            channels[1].noteOn(3, 127);
//            channels[1].noteOn(4, 127);
//            channels[1].noteOn(5, 127);
//            channels[1].noteOn(6, 127);
//            channels[1].noteOn(7, 127);
//            channels[1].noteOn(8, 127);
//            channels[1].noteOn(10, 127);
//            channels[1].noteOn(15, 127);
//            channels[1].noteOn(20, 127);
//            channels[1].noteOn(25, 127);
//            channels[1].noteOn(50, 127);
//            channels[1].noteOn(75, 127);
//            channels[1].noteOn(100, 127);
//            
//            //channels[1].noteOn(64, 60);
//            //channels[1].noteOn(67, 60);
//
//            System.in.read();
//            channels[1].allNotesOff();
//            s.close();
//        } catch (MidiUnavailableException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

}
