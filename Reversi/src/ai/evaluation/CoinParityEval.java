package ai.evaluation;

import core.State;
import core.Board;

/*
 * Evaluates the board for a given state using the coin parity evaluation method
 */
public class CoinParityEval implements Heuristic {
	
	@Override
	public int heuristic(final Board board, final State state) {
		
		if (board.getCount(state) + board.getCount(state.oppenent()) != 0)
			return 100 * (board.getCount(state) - board.getCount(state.oppenent())
					   / (board.getCount(state) + board.getCount(state.oppenent())));
		else return 0;
	}
}