package ai.search;

import ai.evaluation.Heuristic;
import core.*;

public interface Search {
	
	int search(Board board, State state, int depth, final Heuristic function, int alpha, int beta);

}
