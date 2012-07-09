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

public class NoteIndex {
    private int person, column, note;
    	
    	public NoteIndex(int person, int column, int note) {
    		this.person = person;
    		this.column = column;
    		this.note = note;
    	}
    	
    	public int getPerson() {
    		return person;
    	}
    	public int getColumn() {
    		return column;
    	}
    	public int getNote() {
    		return note;
    	}
    	
    	public boolean equals(Object o) {
    		if(o instanceof NoteIndex) {
    			NoteIndex noteIndex = (NoteIndex)o;
    			if(noteIndex.getPerson() == this.getPerson() &&
    					noteIndex.getColumn() == this.getColumn() &&
    					noteIndex.getNote() == this.getNote())
    				return true;
    		}
    		return false;
    	}
}
