package logic;

import java.awt.Point;
import java.util.ArrayList;

import core.*;
import utils.UTILS;

/*
 * Traverses the game board from a given board location to validate a move,
 * find the pieces that will be flipped by a move, or find available moves
 */
public class Traverse {
	
	private Board trvBoard = null;
	private State trvPlayer = null;
	private State trvOpp = null;
	private int trvX = 0;
	private int trvY = 0;
	private ArrayList<Point> trvFlips = null; // pieces intended to be flipped.
	private ArrayList<Move> trvMoves = null; // all possible moves from point (trvX, trvY)
	private boolean trvIsValid = false;
		
	public Traverse(){ }
	
	public Traverse(final Move mv, final State opponent, final Board board) {
		this.trvX = mv.X();
		this.trvY = mv.Y();
		this.trvOpp = opponent;
		this.trvBoard = board;
		this.traverseBoard();
	}
	
	public Traverse(final Move mv,final State state,final State opponent,final Board board) {
		this.trvX = mv.X();
		this.trvY = mv.Y();
		this.trvOpp = opponent;
		this.trvBoard = board;
		this.trvPlayer = state;		
		this.traverseBoard();
	}
	
	private void traverseBoard() {
		
		trvMoves = new ArrayList<Move>();
		trvFlips = new ArrayList<Point>();
	
		int oppPieces = traverseDirection(1, 0);
		oppPieces += traverseDirection(-1, 0);
		oppPieces += traverseDirection(0, 1);
		oppPieces += traverseDirection(0, -1);
		oppPieces += traverseDirection(1, 1);
		oppPieces += traverseDirection(-1,-1);
		oppPieces += traverseDirection(1, -1);
		oppPieces += traverseDirection(-1, 1);

		if (oppPieces > 0 && trvBoard.getStateAt(trvX, trvY) == State.EMPTY) {
			trvIsValid = true;
		}	
	}
	
	private int traverseDirection(final int dirX, final int dirY) {
		
		    State target; //Goal State of the traversal
		    /* target is set to Player.EMPTY to search for available moves 
		     * target is set to trvPlayer, when trying to place a move */
		    target= (this.trvPlayer == null) ? State.EMPTY : this.trvPlayer;
			
		    int x = trvX; 
			int y = trvY;
			int n = 0;	
			
			x += dirX; y += dirY;
			while (UTILS.inBounds(x,  y) && this.trvBoard.getStateAt(x, y) == trvOpp) {
				x += dirX;
				y += dirY;
				n++;
			}
			
			if (n > 0 && UTILS.inBounds(x, y) && trvBoard.getStateAt(x, y) == target) {
			//Move is Valid in current direction
				trvMoves.add(new Move(x, y, n));

				//Traverse back n times to store the pieces that would be flipped
				for (int i = 0; i < n; i++) {
					x -= dirX; y -= dirY;
					trvFlips.add(new Point(x, y));
				}
				return n;
			}
			return 0;
	}
	
	public ArrayList<Move> getMoves() {
		return this.trvMoves;
	}
	
	public ArrayList<Point> getFlips() {
		return this.trvFlips;
	}
	
	public boolean isValid() {
		return this.trvIsValid;
	}	
}
