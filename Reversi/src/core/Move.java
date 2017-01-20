package core;

import java.awt.Point;

import utils.UTILS;

/*
 * Associates a given game move with the moves resulting score.
 * Mainly utilized in AI search
 */
public class Move {
	
	private int x = 0;
	private int y = 0;
	private int score = 0;
	
	public Move() {}

	public Move(int x, int y, int score) {
		this.x = x;
		this.y = y;
		this.score = score;
	}
	
	public Move(Point p, int score) {
		this(p.x, p.y, score);
	}
	
	public Move(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int X()
	{  return this.x;  }

	public int Y()
	{  return this.y;  }

	public void X(int x)
	{  this.x = x;  }

	public void Y(int y)
	{  this.y = y;  }

	public int getScore()
	{  return this.score;  }

	public void setScore(int score)
	{  this.score = score;  }
	
	@Override
	public String toString() {	
		return "x: "+String.valueOf(this.x)+",  y: "+String.valueOf(y)+",  score: "+String.valueOf(score);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) return true;
		if (obj == null)  return false;
		if (getClass() != obj.getClass()) return false;
		
		Move other = (Move) obj;
		if (this.x != other.x || this.y != other.y) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 29;
		return prime * 1 + UTILS.pointToIndex(x, y);
	}
}