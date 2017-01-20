package ai.evaluation;

import ai.Difficulty;
import core.Board;
import core.State;

/*
 * Evaluates the board for a given player state using multiple heuristics where each 
 * heuristic can be weighted diffirently in how much it affects the outputted score.
 * 
 * Heuristics Used: coin parity, number of corners owned, Mobility (number of available moves),
 * 				    and stabilty of owned cells
 */
public class WeightedEval implements Heuristic {
	
	private Difficulty level;
	
	public WeightedEval(Difficulty level) {
		this.level = level;
	}
	@Override
	public int heuristic(final Board board, final State maxPlayer) {
		
		double score = 0;
		State minPlayer = maxPlayer.oppenent();
		
		/* Coin parity */
		if (board.getCount(maxPlayer) + board.getCount(minPlayer) != 0)
			score += 100 * level.coinParityWeight()
						 * (board.getCount(maxPlayer) - board.getCount(minPlayer)
						 / (board.getCount(maxPlayer) + board.getCount(minPlayer)));
		
		/* Mobility */
		if (board.getNumMoves(maxPlayer) + board.getNumMoves(minPlayer) != 0)
			score += 100 * level.mobilityWeight()
						 * (board.getNumMoves(maxPlayer) - board.getNumMoves(minPlayer)) 
						 / (board.getNumMoves(maxPlayer) + board.getNumMoves(minPlayer));
		
		int maxStability = 0;
		int minStability = 0;
		int maxCorners = 0;
		int minCorners = 0;
		
		for (int x = 0; x < 8; x++)
		for (int y = 0; y < 8; y++) {
			State curState;
			if ((curState = board.getStateAt(x, y)) != State.EMPTY) {
				
				if (curState == maxPlayer) {
					if (RiskRegions.rrmap[x][y] == 5) {
						maxCorners ++;
						maxStability += 5;
					}
					else if (RiskRegions.rrmap[x][y] == 4) maxStability -= 5;
					else if (RiskRegions.rrmap[x][y] == 3) maxStability += 1;
					else if (RiskRegions.rrmap[x][y] == 2) maxStability -= 1;
					else 	    /* region 1 is neutral*/			   ;
				}
				else if (curState == minPlayer) {
					if 	(RiskRegions.rrmap[x][y] == 5) {
						minStability+=5;
						minCorners++;
					}
					else if (RiskRegions.rrmap[x][y] == 4) minStability -= 5;
					else if (RiskRegions.rrmap[x][y] == 3) minStability++;
					else if (RiskRegions.rrmap[x][y] == 2) minStability--;
					else 	    /* region 1 is neutral */			   ;
				}
			}
		}
		
		/* Corner */
		if (maxCorners + minCorners != 0)
			score += 100 * level.cornerWeight() * (maxCorners - minCorners) / (maxCorners + minCorners);

		/* Stability */
		if (maxStability + minStability != 0)
			score += 100 * level.stabilityWeight() * (maxStability - minStability) / (maxStability + minStability);
				
		return (int) score;
	}
	
}
