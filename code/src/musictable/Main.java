/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musictable;

import GUI.MainWindow;
import GUI.ParticlePanel;
//import GUI.VisualizationPanel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;
import playback.*;

/**
 *
 * @author Niels Visser
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InvalidMidiDataException {
//        Synthesizer s = null;
//        try {
//            s = MidiSystem.getSynthesizer();
//            s.open();
//        } catch (MidiUnavailableException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//                Soundbank bank = s.getDefaultSoundbank();
//                s.loadAllInstruments(bank);
//                Instrument instrs[] = s.getAvailableInstruments();
//                for (int i = 0; i < instrs.length; i++) {
//                    System.out.println(instrs[i].getName());
//                }
//                MidiChannel[] channels = s.getChannels();
//                Patch seashorePatch = instrs[10].getPatch();
//                channels[1].programChange(seashorePatch.getBank(), seashorePatch.getProgram());
//        
//        
//        MidiChannel c = null;
//        c = s.getChannels()[1];
//
//        c.noteOn(60, 60);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        c.allNotesOff();
//        System.exit(0);
        
        ParticlePanel pp = new ParticlePanel();
        Player p = Player.getInstance();
        p.init(120, 16, pp);
        
        // voeg drums toe
        List<Integer> drumkit = new ArrayList<Integer>();
        drumkit.add(DrumGridConfiguration.d35_Acoustic_Bass_Drum);
        drumkit.add(DrumGridConfiguration.d36_Bass_Drum_1);
        drumkit.add(DrumGridConfiguration.d37_Side_Stick);
        drumkit.add(DrumGridConfiguration.d38_Acoustic_Snare);
        drumkit.add(DrumGridConfiguration.d41_Low_Floor_Tom);
        drumkit.add(DrumGridConfiguration.d42_Closed_Hi_Hat);
        drumkit.add(DrumGridConfiguration.d44_Pedal_Hi_Hat);
        drumkit.add(DrumGridConfiguration.d49_Crash_Cymbal_1);
        drumkit.add(DrumGridConfiguration.d56_Cowbell);
        drumkit.add(DrumGridConfiguration.d54_Tambourine);
        DrumGridConfiguration drums = new DrumGridConfiguration(drumkit, 120);
        InstrumentHolder.getInstance().addConfiguration(drums);
        // voeg piano toe
        InstrumentGridConfiguration piano = new InstrumentGridConfiguration(60, 16, InstrumentHolder.SearchInstrument("piano"), 60);
        InstrumentHolder.getInstance().addConfiguration(piano);
        // voeg bas toe
        InstrumentGridConfiguration bass = new InstrumentGridConfiguration(36, 16, InstrumentHolder.SearchInstrument("bass"), 180);
        InstrumentHolder.getInstance().addConfiguration(bass);
        // voeg gitaar toe
        InstrumentGridConfiguration guitar = new InstrumentGridConfiguration(60, 16, InstrumentHolder.SearchInstrument("guitar"), 60);
        InstrumentHolder.getInstance().addConfiguration(guitar);
        
        
        ToneGrid bassGrid = new ToneGrid(bass);
        p.registerToneGrid(bassGrid);
        bassGrid.setIsActive(true);
        
        ToneGrid highthingy = new ToneGrid(piano);
        p.registerToneGrid(highthingy);
        highthingy.setIsActive(true);
        
        ToneGrid strangesound1 = new ToneGrid(guitar);
        p.registerToneGrid(strangesound1);
        strangesound1.setIsActive(true);
        
        ToneGrid drumGrid = new ToneGrid(drums);
        p.registerToneGrid(drumGrid);
        drumGrid.setIsActive(true);
        
        
        MainWindow mw = new MainWindow(p);
        
        mw.setParticlePanel(pp);
        
        p.start();
        //try {
         //   Thread.sleep(5000);
         //   p.changeInstrument(drumGrid, guitar);
            
            
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
    //    } catch (InterruptedException ex) {
    //        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    //    }
    }

}
