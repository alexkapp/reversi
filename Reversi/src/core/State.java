package core;
import java.awt.Color;

/* Represents individual cells of the game board */
public enum State { 
	
	WHITE, 
	BLACK,
	AVAIL,
	EMPTY;
	
	public static final State COMPUTER = WHITE;
	public static final State HUMAN = BLACK;
	private static final Color AVAIL_COLOR = new Color(0,0,0,75);
	
	public State oppenent() {
		return (this == BLACK) ? WHITE : BLACK;
	}
	
	public Color toColor() {
		if (this == EMPTY) return null;
		else if (this == AVAIL) return AVAIL_COLOR;
		else return (this == BLACK) ? Color.BLACK : Color.WHITE;
	}
	
	public String toString() {
		if (this == EMPTY) return " ";
		else if (this == AVAIL) return "AVAIL";
		else return this == BLACK ? "BLACK" : "WHITE";
	}
}