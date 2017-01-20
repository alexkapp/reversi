package ai;

import java.util.ArrayList;

/*
 * Holds the AI search depth and evaluation weights
 */
public enum Difficulty {

	EASY(3, 1, 1, 2, 1),
	MEDIUM(5, 1, 1, 2, 1),
	HARD(5, 5, 1, 7, 5),
	CUSTOM();
	
	private int movesAhead;
	private int coinParWeight;
	private int mobilityWeight;
	private int cornerWeight;
	private int stabilityWeight;

	
	Difficulty(){}
	
	Difficulty(int movesAhead, int coinParWeight, int mobilityWeight,  
			   int cornerWeight, int stabilityWeight)
	{
		this.movesAhead = movesAhead;
		this.coinParWeight = coinParWeight;
		this.mobilityWeight = mobilityWeight;
		this.cornerWeight = cornerWeight;
		this.stabilityWeight = stabilityWeight;
	}

	public void setCustomValues(ArrayList<Integer> opts) {
			
		this.movesAhead = (opts.get(0) >= 3 && opts.get(0) < 10) ? opts.get(0) : this.movesAhead;
		this.coinParWeight = (opts.get(1) >= 1 && opts.get(1) <= 10) ? opts.get(1) : this.coinParWeight;
		this.mobilityWeight = (opts.get(2) >= 0 && opts.get(2) <= 10) ? opts.get(2) : this.mobilityWeight;
		this.cornerWeight = (opts.get(3) >= 0 && opts.get(3) <= 10) ? opts.get(3) : this.cornerWeight;	
		this.stabilityWeight =  (opts.get(4) >= 0 && opts.get(4) <= 10) ? opts.get(4) : this.stabilityWeight;
	}
		
	public int movesAhead() {
		return this.movesAhead;
	}
	
	public int coinParityWeight() {
		return this.coinParWeight;
	}
	public int mobilityWeight() {
		return this.mobilityWeight;
	}
	
	public int cornerWeight() {
		return this.cornerWeight;
	}
	
	public int stabilityWeight() {
		return this.stabilityWeight;
	}
	
}
