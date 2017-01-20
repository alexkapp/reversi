package utils;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import core.State;
import gui.BoardGUI;
import logic.GameEngine;

/* Animates the flipping of game pieces */
public class GridPieceFlipper {

    	private BoardGUI bgui = null;
	private GameEngine engine = null;
	private ArrayList<Point> flips = null;
	private State state = null;
	private Timer timer = null;
	private FlipTask fTask = null;
	
	private int count = 0;
	
	public GridPieceFlipper() {}	
	public GridPieceFlipper(GameEngine eng, BoardGUI bgui, ArrayList<Point> flips, State state) {

		this.engine = eng;
		this.bgui = bgui;
		this.flips = flips;	
		this.state = state;
		
		eng.disableInput();
		fTask = new FlipTask();
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(fTask, 0, GLOBALS.FLIPPER_DELAY);
	}
	
	
	private class FlipTask extends TimerTask {
		
		int dx = -1;
		Color curColor = state.toColor();
		int radius = bgui.getGrid().get(0).getRadius();
		int tmpRadius = radius;
		
		public void run() {
			
			tmpRadius += dx;
			flips.forEach(fp -> bgui.setGridPiece(fp, tmpRadius));
			
			if (count == 4) {		
				this.cancel();
				flips.forEach(fp-> bgui.setGridPiece(fp, radius));
				flips.forEach(fp -> bgui.setGridPiece(fp.x, fp.y, state.toColor()));
				engine.postMoveEval(state);
		
				return;
			}

			if (tmpRadius >= radius) {
				dx *= -1;
				count++;
			}	
			else if (tmpRadius <= 0) {
				dx *= -1;
				curColor = (curColor == State.HUMAN.toColor()) ? State.COMPUTER.toColor() : State.HUMAN.toColor();
				flips.forEach(fp -> bgui.setGridPiece(fp.x, fp.y, curColor));
				count++;
			}
		}
	}
}
