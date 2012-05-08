/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musictable;

import GUI.GridPanel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;
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
        ToneGrid piano = new InstrumentToneGrid(60, 16, il[0]);
        p.registerToneGrid(piano);
        piano.setIsActive(true);
        piano.toggleTone(0, 0);
        piano.toggleTone(0, 4);
        piano.toggleTone(0, 7);
        piano.toggleTone(4, 0);
        piano.toggleTone(8, 0);
        piano.toggleTone(12, 0);
        
        ToneGrid guitar = new InstrumentToneGrid(60, 16, il[10]);
        p.registerToneGrid(guitar);
        guitar.setIsActive(true);
        guitar.toggleTone(0, 0);
        guitar.toggleTone(0, 4);
        guitar.toggleTone(0, 7);
        guitar.toggleTone(4, 0);
        guitar.toggleTone(8, 0);
        guitar.toggleTone(12, 0);
        
        p.start();
        GridPanel gf = new GridPanel(p);
        //gf.setVisible(true);
        gf.display();
        
        try {
            Thread.sleep(30000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        p.stop();
        
        
        
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
//            System.exit(0);
//            
//            
//            
//            Soundbank bank = s.getDefaultSoundbank();
//            s.loadAllInstruments(bank);
//            Instrument instrs[] = s.getLoadedInstruments();
//            for (int i = 0; i < instrs.length; i++) {
//                System.out.println(instrs[i].getName());
//            }
//            MidiChannel[] channels = s.getChannels();
//            Patch seashorePatch = instrs[20].getPatch();
//            channels[1].programChange(seashorePatch.getBank(), seashorePatch.getProgram());
//            
//            System.out.print("Druk op enter om de toon af te spelen...");
//            System.in.read();
//            channels[1].noteOn(60, 60);
//            //channels[1].noteOn(64, 60);
//            channels[1].noteOn(67, 60);
//
//            System.in.read();
//            s.close();
//        } catch (MidiUnavailableException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

}
