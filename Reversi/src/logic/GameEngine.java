package logic;

import java.awt.Point;
import java.util.ArrayList;

import ai.Difficulty;
import ai.evaluation.Heuristic;
import ai.evaluation.WeightedEval;
import ai.search.AlphaBetaMinMax;
import core.*;
import gui.BoardGUI;
import utils.GridPieceFlipper;

/*
 * Game Controller 
 */
public class GameEngine {

	private Board board = null;
	private BoardGUI bGUI = null;
	private Difficulty level = null;
	private Point selectedMove = null;
	private Thread AI_thread = null;
	private Move AI_bestMove = null;
	
	private boolean inputEnabled;

	public GameEngine() {
		selectedMove = null;
		level = Difficulty.EASY;
		board = new Board();
		bGUI = new BoardGUI(this);
		bGUI.setAvailableMoves(board.findAvailableMoves(State.HUMAN));
		enableInput();
	}
	
	public void clear() {
		selectedMove = null;
		board.clear();
		bGUI.clearGrid();
		updateGUILabels();
		bGUI.setAvailableMoves(board.findAvailableMoves(State.HUMAN));
		enableInput();
	}
	
	public Difficulty getDifficultyLevel() {	
		return this.level;  
	}
	
	public void setDifficultyLevel(Difficulty difficulty) {
		level = difficulty;
	}
	
	public void setCustomDifficultyLevel(ArrayList<Integer> diffVars) {
		level = Difficulty.CUSTOM;
		level.setCustomValues(diffVars);
	}

	public boolean isInputEnabled() {
		return inputEnabled;
	}
	
	public void enableInput() {
		inputEnabled = true;
	}
	
	public void disableInput() {
		inputEnabled = false;
	}
	
	public void performHumanMove(int x, int y) {		
		performMove(x, y, State.HUMAN);	
	}
	
	/* Used when running move search on a new thread */
	public void getComputerMove() {	
		
		AlphaBetaMinMax alphabeta = new AlphaBetaMinMax();
		Heuristic evalfunc = new WeightedEval(this.level);
		AI_bestMove = alphabeta.findBestMove(this.board, State.COMPUTER, level.movesAhead(), evalfunc);
	}
	
	/* Used when running move search on current thread */
	public void performComputerMove() {
		
		AlphaBetaMinMax alphabeta = new AlphaBetaMinMax();
		Heuristic evalfunc = new WeightedEval(this.level);
		Move best = alphabeta.findBestMove(this.board, State.COMPUTER, level.movesAhead(), evalfunc);
		performMove(best.X(), best.Y(), State.COMPUTER);
	}
	
	public void performMove(final int x, final int y, final State player){ 
		
		Traverse traversal = new Traverse(new Move(x, y), player, player.oppenent(), board);
		if (traversal.isValid()) {
			
			bGUI.clearAvailableMoves();
			
			//Unselect previouse Move (removes red marker)
			if (selectedMove != null) 
				bGUI.unselectGridPiece(selectedMove.x, selectedMove.y);
			
			selectedMove = new Point (x, y);
			bGUI.selectGridPiece(x, y, player.toColor());
			board.set(x, y, player);
			traversal.getFlips().forEach(flip -> board.set(flip, player));
			traversal.getFlips().forEach(flip -> bGUI.setGridPiece(flip.x, flip.y, player.toColor()));
			
			if (player == State.HUMAN) {
				/* runs the AI move search during flip animation */
				AI_thread = new Thread(() -> getComputerMove());
				AI_thread.start();
			}
			new GridPieceFlipper(this, bGUI, traversal.getFlips(), player);
		}
		else bGUI.showErrorMessage();	
	}
		
	public void postMoveEval(State curPlayer) {
		
		updateGUILabels();
		if (board.isEndState()) {
			bGUI.displayGameOver(board.getWinner());
			return;
		}
		
		if (curPlayer == State.HUMAN) {				
			if (board.canMove(State.COMPUTER)) {
				/* creates slight delay before placing computer move
				 * when the AI search finishes before the animation */
				if (AI_bestMove != null) {
				 	try { Thread.sleep(150); } 
				 	catch (InterruptedException e) {}
				 	performMove(AI_bestMove.X(), AI_bestMove.Y(), State.COMPUTER);
				 	AI_bestMove = null;
				}
				else { /* wait for search to finish */
					try { AI_thread.join(); }
					catch (InterruptedException e) { e.printStackTrace(); }
					performMove(AI_bestMove.X(), AI_bestMove.Y(), State.COMPUTER);
					AI_bestMove = null;
				}
			}
			else { /* Computer can't move */
				bGUI.setAvailableMoves(board.findAvailableMoves(State.HUMAN));
				enableInput();
			}
		}
		else  if (curPlayer == State.COMPUTER) {
			
			if (board.canMove(State.HUMAN)) {
				bGUI.setAvailableMoves(board.findAvailableMoves(State.HUMAN));
				enableInput();
			}
			else
				performComputerMove();
		}		
	}

	private synchronized void updateGUILabels() {
		try {
			this.bGUI.setWhiteScoreLb(String.valueOf(board.getCount(State.WHITE)));
			this.bGUI.setWhiteMovesLb(String.valueOf(board.getNumMoves(State.WHITE)));
			this.bGUI.setBlackScoreLb(String.valueOf(board.getCount(State.BLACK)));
			this.bGUI.setBlackMovesLb(String.valueOf(board.getNumMoves(State.BLACK)));
		} catch (NullPointerException e) {}
	}
	
	public static void main(String[] args) {
		new GameEngine();
	}
}
