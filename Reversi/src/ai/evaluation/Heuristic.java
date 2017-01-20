package ai.evaluation;

import core.*;

public interface Heuristic {
	
	public int heuristic(final Board board, final State state);

}
