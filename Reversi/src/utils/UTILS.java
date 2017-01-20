package utils;


import java.awt.Point;

public class UTILS {
	
	public static boolean inBounds(int x, int y) { 
		return (x >= 0 && x < GLOBALS.BOARD_LENGTH && y >= 0 && y < GLOBALS.BOARD_LENGTH);  
	}
	
	public static boolean inBounds(Point p) {  
		return (p.x >= 0 && p.x < GLOBALS.BOARD_LENGTH && p.y >= 0 && p.y < GLOBALS.BOARD_LENGTH);  
	}
	
	public static int pointToIndex(Point p) {
		return p.x + (p.y * GLOBALS.BOARD_LENGTH); 
	}
	
	public static int pointToIndex(int x, int y) {  
		return x + (y * GLOBALS.BOARD_LENGTH);
	}
	
	public static Point indexToPoint(int i) {  
		return new Point (i % GLOBALS.BOARD_LENGTH, i / GLOBALS.BOARD_LENGTH);
	}
}
