package ai.search;

import java.awt.Point;
import java.util.ArrayList;

import ai.evaluation.Heuristic;
import core.*;
import logic.Traverse;

/* 
 * Searches for the best move within the maximum depth using a 
 * MiniMax search with alpha-beta pruning
 */
public class AlphaBetaMinMax implements Search{

	private int max_depth;
	private State Maximizing;
	
	public Move findBestMove(final Board board, final State maxPlayer, int max_depth, Heuristic evalfunct) {
		
		this.Maximizing = maxPlayer;
		this.max_depth = max_depth;
		int bestscore = Integer.MIN_VALUE;
		Move bestmove = new Move(-1, -1, bestscore);
		
		ArrayList<Move> rootchildren = board.findAvailableMovesSorted(maxPlayer);

		for (Move root : rootchildren) {
			
			Board tmpBoard = board.clone();
			Traverse traversal = new Traverse(root, maxPlayer, maxPlayer.oppenent(), tmpBoard);
			tmpBoard.set(root.X(), root.Y(), maxPlayer);
			traversal.getFlips().forEach(flip -> tmpBoard.set(flip, maxPlayer));
			
			int score = search(tmpBoard, maxPlayer.oppenent(), 1, evalfunct, Integer.MIN_VALUE, Integer.MAX_VALUE);
			root.setScore(score);
			if (score > bestscore) {
				bestscore = score;
				bestmove.X(root.X());  bestmove.Y(root.Y());
				bestmove.setScore(score);
			}
		}		
		rootchildren.forEach(root -> System.out.println(root.toString()));
		return bestmove;
	}

	@Override
	public int search(final Board board, final State state, int depth, Heuristic evalfunct, int alpha, int beta) {
		
		if (depth > max_depth)
			return evalfunct.heuristic(board, state);
		
		ArrayList<Move> childmoves = board.findAvailableMovesSorted(state);
		
		if (childmoves.isEmpty())
			return evalfunct.heuristic(board, state);

		if (state == Maximizing) {
			
			for (Move childmove : childmoves) {
			
				Board tmpBoard = board.clone();
				ArrayList<Point> flips = new Traverse(childmove, state, state.oppenent(), tmpBoard).getFlips();
			
				flips.forEach(flip -> tmpBoard.set(flip, state));
				tmpBoard.set(childmove.X(), childmove.Y(), state);
			
				int score = search(tmpBoard, state.oppenent(), depth+1, evalfunct, alpha, beta);
				if (score > alpha) alpha = score;
				if (beta <= alpha) break;
			}
			return alpha;
		}
		else { /* Minimizing Player */
			for (Move childmove : childmoves) {
				
				Board tmpBoard = board.clone();
				ArrayList<Point> flips = new Traverse(childmove, state, state.oppenent(), tmpBoard).getFlips();
				
				flips.forEach(flip -> tmpBoard.set(flip, state));					
				tmpBoard.set(childmove.X(),childmove.Y(), state);
				
				int score = search(tmpBoard, state.oppenent(), depth+1, evalfunct, alpha, beta);
				
				if (score < beta)  beta = score;
				if (beta <= alpha) break;		
			}
			return beta;
		}
	}
}	

