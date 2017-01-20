package core;

import java.awt.Point;
import java.util.*;
import java.util.stream.Collectors;

import ai.evaluation.RiskRegions;
import logic.*;
import utils.GLOBALS;
import utils.UTILS;

/*
 * A model representation of the game noard
 */
public final class Board implements Cloneable{
	
	private State [][] board;
	
	public Board() {	
		this.board = new State [GLOBALS.BOARD_LENGTH][GLOBALS.BOARD_LENGTH];
		this.clear();
	}
	
	private Board (final Board board) {
		
		this.board = new State[GLOBALS.BOARD_LENGTH][GLOBALS.BOARD_LENGTH];
		for (int x = 0; x < GLOBALS.BOARD_LENGTH; x++)
		for (int y = 0; y < GLOBALS.BOARD_LENGTH; y++)
			this.board[x][y] = board.getStateAt(x, y);
	}
	
	public void clear() {
		
		for (int x = 0; x < GLOBALS.BOARD_LENGTH; x++)
		for (int y = 0; y < GLOBALS.BOARD_LENGTH; y++)
			board[x][y] = State.EMPTY;
	
		board[3][3] = board[4][4] = State.HUMAN;
		board[3][4] = board[4][3] = State.COMPUTER;
	}

	public State getStateAt(Point p) 	
	{	return board[p.x][p.y];  }
	
	public State getStateAt(int x, int y) 
	{	return this.board[x][y];  }
	
	public void set(Point p, State ps) {
		if (UTILS.inBounds(p))
			this.board[p.x][p.y] = ps;
	}
	
	public void set(int x, int y, State ps) {	
		if (UTILS.inBounds(x, y))
			this.board[x][y] = ps;		
	}
	
	public boolean isFull() {
		
		for (int x = 0; x < GLOBALS.BOARD_LENGTH; x++)
		for (int y = 0; y < GLOBALS.BOARD_LENGTH; y++)
			if (board[x][y] ==  State.EMPTY)
				return false;
		return true;
	}
	
	public int getCount (State p) {
		
		int count = 0;	
		for (int x = 0; x < GLOBALS.BOARD_LENGTH; x++)
		for (int y = 0; y < GLOBALS.BOARD_LENGTH; y++)
			if (this.board[x][y] == p)
				count++;		
		return count;
	}
	
	public int getNumMoves(State p) 
	{	return findAvailableMoves(p).size();  }
	
	public boolean canMove(State state) 
	{	return getNumMoves(state) > 0;  }
	
	public boolean isEndState() {
		return isFull() || 
			   isZeroScore() ||
			   (!canMove(State.BLACK) && !canMove(State.WHITE));
	}
	
	public boolean isDraw() {
		return getCount(State.BLACK) == getCount(State.WHITE);
	}
	
	public State getWinner() {
		if (isDraw()) return State.EMPTY;
		else return (getCount(State.WHITE) > getCount(State.BLACK)) ? State.WHITE : State.BLACK;
	}
	
	public boolean isZeroScore() {
		return getCount(State.BLACK) == 0 || getCount(State.WHITE) == 0;
	}
	
	public ArrayList<Move> findAvailableMoves (final State player) {

		ArrayList<Move> availmoves = new ArrayList<Move>();
		for (int x = 0; x < GLOBALS.BOARD_LENGTH; x++)
		for (int y = 0; y < GLOBALS.BOARD_LENGTH; y++) 
			if (this.board[x][y] == player) {
				Traverse traversal = new Traverse(new Move(x, y), player.oppenent(), this);
				
				//adds available moves found in the traversal that are not already in the list
				traversal.getMoves().stream().filter(newMove-> !availmoves.contains(newMove))
			          		  				 .forEach(m-> availmoves.add(m));
			}
		return availmoves;
	}
	
	/* returns a list of moves sorted by the stability of moves' location */
	public ArrayList<Move> findAvailableMovesSorted (final State player) {
		
		ArrayList<Move> availmoves = new ArrayList<Move>();
		for (int x = 0; x < GLOBALS.BOARD_LENGTH; x++)
		for (int y = 0; y < GLOBALS.BOARD_LENGTH; y++) 
			if (this.board[x][y] == player) {
				Traverse traversal = new Traverse(new Move(x, y), player.oppenent(), this);
				
				//adds available moves found in the traversal that are not already in the list
				List<Move> newMoves = 
				traversal.getMoves().stream().filter(newMove-> !availmoves.contains(newMove))
						  .collect(Collectors.toList());
				
				//Adds the the new moves to the sorted list	
				newMoves.forEach(newmove -> {
					if (availmoves.isEmpty()) 
						availmoves.add(newmove);
					else {
						int val = RiskRegions.rankmap[newmove.X()][newmove.Y()];
						if (RiskRegions.rankmap[availmoves.get(0).X()][availmoves.get(0).Y()] < val)
							availmoves.add(0, newmove);
						else {
							int i;
							for(i = 0; i < availmoves.size(); i++) 
								if (val >= RiskRegions.rankmap[availmoves.get(i).X()][availmoves.get(i).Y()])
									break;
							availmoves.add(i, newmove);	
						}
					 }
				 });
			 }
		return availmoves;
	}
	
	@Override
	public Board clone() {
		return new Board(this); 
	}
}
