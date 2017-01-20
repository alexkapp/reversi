package gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

import utils.GLOBALS;
import core.State;

/*
 * The view layout for the cells in the game board 
 */
@SuppressWarnings("serial")

public class GridSquare extends JPanel {
	
	private Color color = null;
	private int radius = 0;
	private boolean selected = false;
	
	public GridSquare() {
		setBackground(GLOBALS.GRID_BACKGROUND_COLOR);
	}
	
	public void setPieceColor(Color color) 
	{	this.color = color; }
	
	public Color getPieceColor() 
	{	return this.color;  }
	
	public int getRadius() 
	{	return this.radius;  }
	
	public void setRadius(int r) 
	{	this.radius = r;  }
	
	public boolean isSelected() 
	{	return this.selected;  }
	
	public void select()
	{	this.selected = true;  }
	
	public void unselect() 
	{	this.selected = false;  }
	
	public synchronized void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		if (color == State.EMPTY.toColor() )
			return;
		
		int width = getWidth();
		int height = getHeight();
		g.setColor(color);
		
		if (this.color == State.AVAIL.toColor())
			g.fillOval(width/2 - width/4, height/2- height/4, width/2, height/2);		
		else 
			g.fillOval(width/2-this.radius, 1, 2*this.radius, height-1);
		
		if (selected) {
			g.setColor(Color.RED);
			g.fillOval(width/2 - width/4, height/2- height/4, width/2, height/2);
		}
	}
	
}