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
package GUI;

import java.awt.Color;
import java.util.HashMap;
import java.util.Set;

public class Gradient {
    
    private HashMap<Integer, Color> knots;
    
    public Gradient() {
        this.knots = new HashMap<Integer, Color>();
    }
    
    public void setColorAt(int x, Color c) {
        knots.put(x, c);
    }
    
    public Color getColorAt(int x) {
        Set<Integer> keys = knots.keySet();
        int x1 = -1;
        int x2 = Integer.MAX_VALUE;
        for(int key : keys) {
            if(key == x) {
                return this.knots.get(key);
            }
            if(x1 < key && key < x) {
                x1 = key;
            }
            if(x2 > key && key > x) {
                x2 = key;
            }
        }
        Color c1 = this.knots.get(x1);
        Color c2 = this.knots.get(x2);
        int distance = x2 - x1;
        double fraction = (x - x1) / (double)distance;
        
        int r = c1.getRed() + (int)Math.round(((double)c2.getRed() - (double)c1.getRed())*fraction);
        int g = c1.getGreen() + (int)Math.round(((double)c2.getGreen() - (double)c1.getGreen())*fraction);
        int b = c1.getBlue() + (int)Math.round(((double)c2.getBlue() - (double)c1.getBlue())*fraction);
        return new Color(r,g,b);
    }
    
    public static void main(String[] args) {
        Gradient g = new Gradient();
        g.setColorAt(0, Color.red);
        g.setColorAt(255, Color.blue);
        Color c = g.getColorAt(128); // paars
        System.out.println("r:" + c.getRed() + ", g:" + c.getGreen() + ", b:" + c.getBlue());
    }
}
