package ai.evaluation;

/*
 * Represents a game board divided into seperate regions of the stability 
 */
public class RiskRegions {
	
	public static final int [][] rrmap = new int [][]
		      { { 5, 4, 3, 3, 3, 3, 4, 5 },
		      { 4, 4, 2, 2, 2, 2, 4, 4 },
		      { 3, 2, 1, 1, 1, 1, 2, 3 },
		      { 3, 2, 1, 1, 1, 1, 2, 3 },
		      { 3, 2, 1, 1, 1, 1, 2, 3 },
		      { 3, 2, 1, 1, 1, 1, 2, 3 },
		      { 4, 4, 2, 2, 2, 2, 4, 4 },
		      { 5, 4, 3, 3, 3, 3, 4, 5 } };	
		      
    /* Stability rankings */
    public static final int [][] rankmap = new int [][]
	    { { 5, 1, 4, 4, 4, 4, 1, 5 },
	      { 1, 1, 2, 2, 2, 2, 1, 1 },
	      { 4, 2, 3, 3, 3, 3, 2, 4 },
	      { 4, 2, 3, 3, 3, 3, 2, 4 },
	      { 4, 2, 3, 3, 3, 3, 2, 4 },
	      { 4, 2, 3, 3, 3, 3, 2, 4 },
	      { 1, 1, 2, 2, 2, 2, 1, 1 },
	      { 5, 1, 4, 4, 4, 4, 1, 5 } };		      
}
