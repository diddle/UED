/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

/**
 *
 * @author Niels Visser
 */
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
