/*	Copyright Niels Visser 2012
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

import GUI.ParticlePanel;
import java.util.ArrayList;
import java.util.List;

public class DrumGridConfiguration extends GridConfiguration{
    
    private List<Integer> drumkit;

    public DrumGridConfiguration(List<Integer> drumkit, int velocity) {
        super(InstrumentHolder.GetDrums(), velocity);
        this.drumkit = drumkit;
    }
    
    public List<Integer> getDrumkit() {
        return this.drumkit;
    }
    
    @Override
    public void playTones(List<Integer> x, ParticlePanel vp) {
        // converteer van relatieve lijst naar absolute lijst...
        List<Integer> tones = new ArrayList<Integer>();
        for(Integer i : x ) {
            tones.add(this.drumkit.get(i));
        }
        this.playNotesOnChannel(tones, this.velocity, vp);
    }
    
    public static int d35_Acoustic_Bass_Drum = 35;
    public static int d36_Bass_Drum_1 = 36;
    public static int d37_Side_Stick = 37;
    public static int d38_Acoustic_Snare = 38;
    public static int d39_Hand_Clap = 39;
    public static int d40_Electric_Snare = 40;
    public static int d41_Low_Floor_Tom = 41;
    public static int d42_Closed_Hi_Hat = 42;
    public static int d43_High_Floor_Tom = 43;
    public static int d44_Pedal_Hi_Hat = 44;
    public static int d45_Low_Tom = 45;
    public static int d46_Open_Hi_Hat = 46;
    public static int d47_Low_Mid_Tom = 47;
    public static int d48_Hi_Mid_Tom = 48;
    public static int d49_Crash_Cymbal_1 = 49;
    public static int d50_High_Tom = 50;
    public static int d51_Ride_Cymbal_1 = 51;
    public static int d52_Chinese_Cymbal = 52;
    public static int d53_Ride_Bell = 53;
    public static int d54_Tambourine = 54;
    public static int d55_Splash_Cymbal = 55;
    public static int d56_Cowbell = 56;
    public static int d57_Crash_Cymbal_2 = 57;
    public static int d58_Vibraslap = 58;
    public static int d59_Ride_Cymbal_2 = 59;
    public static int d60_Hi_Bongo = 60;
    public static int d61_Low_Bongo = 61;
    public static int d62_Mute_Hi_Conga = 62;
    public static int d63_Open_Hi_Conga = 63;
    public static int d64_Low_Conga = 64;
    public static int d65_High_Timbale = 65;
    public static int d66_Low_Timbale = 66;
    public static int d67_High_Agogo = 67;
    public static int d68_Low_Agogo = 68;
    public static int d69_Cabasa = 69;
    public static int d70_Maracas = 70;
    public static int d71_Short_Whistle = 71;
    public static int d72_Long_Whistle = 72;
    public static int d73_Short_Guiro = 73;
    public static int d74_Long_Guiro = 74;
    public static int d75_Claves = 75;
    public static int d76_Hi_Wood_Block = 76;
    public static int d77_Low_Wood_Block = 77;
    public static int d78_Mute_Cuica = 78;
    public static int d79_Open_Cuica = 79;
    public static int d80_Mute_Triangle = 80;
    public static int d81_Open_Triangle = 81;
    
}
