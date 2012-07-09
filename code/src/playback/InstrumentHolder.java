/*	Copyright Alexander Drechsel 2012
 *	Copyright Kasper Vaessen 2012
 *	Copyright Niels Visser 2012
 *	
 *	This file is part of MusicTable.
 *	
 *	MusicTable is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	MusicTable is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with MusicTable.  If not, see <http://www.gnu.org/licenses/>.
 */
package playback;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

public class InstrumentHolder {
    
    private static InstrumentHolder instance = null;
    private List<GridConfiguration> configurations;
    
    private InstrumentHolder() {
        this.configurations = new ArrayList<GridConfiguration>();
    }
    
    public static InstrumentHolder getInstance() {
        if(instance == null) {
            instance = new InstrumentHolder();
        }
        return instance;
    }
    
    public static Instrument[] InstrumentList() {
        return Player.getInstance().getSynthesizer().getAvailableInstruments();
    }
    
    public static Instrument GetDrums() {
        return SearchInstrument("drum");
    }
    /**
     * Doorzoekt de Synthesizer voor een instrument op basis van naam.
     * @param partialName
     * @return het gevonden Instrument of null
     */
    public static Instrument SearchInstrument(String partialName) {
        Instrument[] il = InstrumentList();
        for(Instrument i : il) {
            if(i.getName().toLowerCase().contains(partialName)) {
                return i;
            }
        }
        return null;
    }
    
    public void addConfiguration(GridConfiguration gc) {
        this.configurations.add(gc);
    }
    
    /**
     * Doorzoekt de InstrumentHolder voor een GridConfiguration op basis van Instrument.
     * @param i het instrument
     * @return de GridConfiguration van het instrument of null als deze niet bestaat.
     */
    public GridConfiguration getGridConfigurationByInstrument(Instrument i) {
        for(GridConfiguration gc : this.configurations) {
            if(gc.instrument == i) {
                return gc;
            }
        }
        return null;
    }
    
    public List<GridConfiguration> getAvailableConfigurations() {
        return this.configurations;
    }
}
