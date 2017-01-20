package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import ai.Difficulty;
import core.*;
import logic.GameEngine;
import utils.GLOBALS;
import utils.UTILS;

/*
 * The main layout of the game GUI
 */
public class BoardGUI  implements MouseListener, MouseMotionListener {
	
	private JFrame mFrame = null;
	private JPanel statuspanel = null;
	private JPanel boardpanel = null;
	private JLabel score_black, score_white, moves_white, moves_black;
	
	private ButtonGroup bGroup = null;
	private ButtonModel curSelected = null;
	
	private GameEngine engine = null;
	private DifficultyOptsWindow optionWindow = null;
	private ArrayList<GridSquare> grid = new ArrayList<GridSquare>();
	private Point MousePosition = null;
	
	public BoardGUI() {}
	public BoardGUI(GameEngine engine) {
		this.engine = engine;
		initMain();
	}

	/* Inititalizes the Application's Window Frame */
	private void initMain() {
		
		mFrame = new JFrame();
		mFrame.getContentPane().setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		mFrame.setBounds(100, 100, GLOBALS.MIN_WINDOW_WIDTH, GLOBALS.MIN_WINDOW_HEIGHT);
		mFrame.setMinimumSize(new Dimension(GLOBALS.MIN_WINDOW_WIDTH, GLOBALS.MIN_WINDOW_HEIGHT));
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.getContentPane().setLayout(new BorderLayout());

		initBoardPanel();
		initStatusPanel();
		initMenu();
		
		boardpanel.setPreferredSize(new Dimension(480,480));
		mFrame.getContentPane().add(statuspanel, BorderLayout.NORTH);
		mFrame.getContentPane().add(boardpanel, BorderLayout.CENTER);
		
		mFrame.pack();
		grid.forEach(g-> g.setRadius(Math.min(boardpanel.getWidth(), boardpanel.getHeight()) /16 - 2));
		mFrame.setResizable(false);
		mFrame.setVisible(true);
	}
	
	/* Initializes the layout of the gameboard grid */
	private void initBoardPanel() {
		
		boardpanel = new JPanel();
		boardpanel.setLayout(new GridLayout(GLOBALS.BOARD_LENGTH, GLOBALS.BOARD_LENGTH,1,1));
		
		//Create all grid squares and add them to this panel
		for (int i = 0; i < GLOBALS.BOARD_SIZE; i++) {
			grid.add(new GridSquare());
			boardpanel.add(grid.get(i));
		}
		
		boardpanel.addMouseListener(this);
		boardpanel.addMouseMotionListener(this);
		
		grid.get(UTILS.pointToIndex(3, 3)).setPieceColor(State.HUMAN.toColor());
		grid.get(UTILS.pointToIndex(4, 4)).setPieceColor(State.HUMAN.toColor());
		grid.get(UTILS.pointToIndex(4, 3)).setPieceColor(State.COMPUTER.toColor());
		grid.get(UTILS.pointToIndex(3, 4)).setPieceColor(State.COMPUTER.toColor());
	}
	
	/* Inititalizes the top panel that displays the scores and number of avail. moves for human and computer players */
	private void initStatusPanel() {
		
		statuspanel = new JPanel();
		
		JLabel lblPlayerscore = new JLabel("PlayerScore: ");
		lblPlayerscore.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		statuspanel.add(lblPlayerscore);
		
		score_black = new JLabel("2");
		score_black.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		statuspanel.add(this.score_black);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setPreferredSize(new Dimension(11, 0));
		statuspanel.add(horizontalStrut);
		
		JLabel lblPlayerMoves = new JLabel("Player Moves: ");
		lblPlayerMoves.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		statuspanel.add(lblPlayerMoves);
		
		moves_black = new JLabel("4");
		moves_black.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		statuspanel.add(moves_black);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		horizontalStrut_1.setPreferredSize(new Dimension(11, 0));
		statuspanel.add(horizontalStrut_1);
		
		JLabel lblComputerScore = new JLabel("Computer Score: ");
		lblComputerScore.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		statuspanel.add(lblComputerScore);
		
		score_white = new JLabel("2");
		score_white.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		statuspanel.add(score_white);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		horizontalStrut_2.setPreferredSize(new Dimension(11, 0));
		statuspanel.add(horizontalStrut_2);
		
		JLabel lblComputerMoves = new JLabel("Computer Moves: ");
		lblComputerMoves.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		statuspanel.add(lblComputerMoves);
		
		moves_white = new JLabel("4");
		moves_white.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		statuspanel.add(moves_white);
	}
	
	/* Initializes menu at the top of the frame */
	private void initMenu() {
		
		JMenuBar menuBar = new JMenuBar();
		this.mFrame.setJMenuBar(menuBar);
		
		JMenu mMenu = new JMenu("Main");
		menuBar.add(mMenu);
		
		JMenuItem mtmNewGame = new JMenuItem("New Game");
		mtmNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				engine.clear();
				boardpanel.repaint();
				mFrame.revalidate();
			}
		});
		mMenu.add(mtmNewGame);
		
		JMenu smDifficulty = new JMenu("Difficulty");
		bGroup = new ButtonGroup();
		JRadioButtonMenuItem smtmEasy = new JRadioButtonMenuItem("Easy");
		bGroup.add(smtmEasy);
		smtmEasy.addActionListener(e -> {
			engine.setDifficultyLevel(Difficulty.EASY);
			curSelected = bGroup.getSelection();
		});
		smtmEasy.setSelected(true);
		curSelected = bGroup.getSelection();
		smDifficulty.add(smtmEasy);
	
		JRadioButtonMenuItem smtmMedium = new JRadioButtonMenuItem("Medium");
		bGroup.add(smtmMedium);
		smtmMedium.addActionListener(e -> {
			engine.setDifficultyLevel(Difficulty.MEDIUM);
			curSelected = bGroup.getSelection();
		});
		smDifficulty.add(smtmMedium);
		
		JRadioButtonMenuItem smtmHard = new JRadioButtonMenuItem("Hard");
		bGroup.add(smtmHard);
		smtmHard.addActionListener(e -> {
			engine.setDifficultyLevel(Difficulty.HARD);
			curSelected = bGroup.getSelection();
		});
		smDifficulty.add(smtmHard);
		
		JRadioButtonMenuItem smtmCustom = new JRadioButtonMenuItem("(Custom)");
		bGroup.add(smtmCustom);
	    smtmCustom.addActionListener(e -> optionWindow = createOptWindow());

		smDifficulty.add(smtmCustom);	
	    
	    mMenu.add(smDifficulty);
	    
		JMenuItem mtmQuit = new JMenuItem("Quit");
		mtmQuit.addActionListener(e -> { mFrame.dispose(); 
										 System.exit(0); 
		});
		mMenu.add(mtmQuit);
	}
	
	public int getWidth() {
		return mFrame.getWidth();
	}
	
	public int getHeight() {
		return mFrame.getHeight();
	}
	
	private DifficultyOptsWindow createOptWindow() {
		
		return new DifficultyOptsWindow(this, new ArrayList<Integer>() {{
			add(engine.getDifficultyLevel().movesAhead());
			add(engine.getDifficultyLevel().coinParityWeight());
			add(engine.getDifficultyLevel().mobilityWeight());
			add(engine.getDifficultyLevel().cornerWeight());
			add(engine.getDifficultyLevel().stabilityWeight());
		}});
	}
		
	public void saveOptions(ArrayList<Integer> level) {
		
		engine.setCustomDifficultyLevel(level);
		curSelected = bGroup.getSelection();
		optionWindow.getOptionFrame().dispose();
	}

	/* Sets the Difficulty menu radio button back to the previously selected */
	public void optionsCancelled() {
		bGroup.setSelected(curSelected, true);
		optionWindow.getOptionFrame().dispose();
	}
	
	public void displayGameOver(State winner) {
		
		if (winner == State.EMPTY) {
			JOptionPane.showMessageDialog(mFrame, "DRAW", "Game Over",JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			String message = winner.toString() +" is the Winner";
			JOptionPane.showMessageDialog(mFrame, message, "Game Over",JOptionPane.INFORMATION_MESSAGE);	
		}
	}
	
	public synchronized void setWhiteScoreLb(String score) {
		score_white.setText(score);
	}
	
	public synchronized void setBlackScoreLb(String score) {
		score_black.setText(score);
	}
	public synchronized void setWhiteMovesLb(String score) {
		moves_white.setText(score);
	}
	
	public synchronized void setBlackMovesLb(String score) {
		moves_black.setText(score);
	}
	
	public synchronized void clearGrid() {
		
		grid.forEach(g -> { 
			g.setPieceColor(State.EMPTY.toColor());
			g.unselect();
		});
				
		grid.get(UTILS.pointToIndex(3, 3)).setPieceColor(State.HUMAN.toColor());
		grid.get(UTILS.pointToIndex(4, 4)).setPieceColor(State.HUMAN.toColor());
		grid.get(UTILS.pointToIndex(4, 3)).setPieceColor(State.COMPUTER.toColor());
		grid.get(UTILS.pointToIndex(3, 4)).setPieceColor(State.COMPUTER.toColor());		
	}
	
	public synchronized void setGridPiece(int x, int y, Color c) {
		grid.get(UTILS.pointToIndex(x, y)).setPieceColor(c);
		grid.get(UTILS.pointToIndex(x, y)).repaint();
	}
	
	/* Used by the GripPieceFlipper class to shrink/expand a piece's radius */  
	public synchronized void setGridPiece(Point p, int radius) {
		grid.get(UTILS.pointToIndex(p)).setRadius(radius);
		grid.get(UTILS.pointToIndex(p)).repaint();
	}
	
	public synchronized void selectGridPiece(int x, int y, Color c) {
		grid.get(UTILS.pointToIndex(x, y)).setPieceColor(c);
		grid.get(UTILS.pointToIndex(x, y)).select();
		grid.get(UTILS.pointToIndex(x, y)).repaint();
	}
	
	public synchronized void unselectGridPiece(int x, int y) {
		grid.get(UTILS.pointToIndex(x, y)).unselect();
		grid.get(UTILS.pointToIndex(x, y)).repaint();
	}
	
	public synchronized void setAvailableMoves(ArrayList<Move> avail) {
		clearAvailableMoves();
		avail.forEach(m -> {
			grid.get(UTILS.pointToIndex(m.X(), m.Y())).setPieceColor(State.AVAIL.toColor());
			grid.get(UTILS.pointToIndex(m.X(), m.Y())).repaint();
		});
	}
	
	public synchronized void clearAvailableMoves() {	
		grid.stream()
			.filter(g -> g.getPieceColor() == State.AVAIL.toColor())
			.forEach(g -> { g.setPieceColor(State.EMPTY.toColor());
							g.repaint(); });	
	}
	
	public ArrayList<GridSquare> getGrid() {
		return this.grid;
	}

	public void showErrorMessage() {
		JOptionPane.showMessageDialog(this.mFrame, "Illegal move","Reversi",JOptionPane.ERROR_MESSAGE);
	}
	
	public void mousePressed (MouseEvent e) {
		
		if (e.getSource() == this.boardpanel && e.getPoint() != null && engine.isInputEnabled()) {
		
			this.MousePosition = e.getPoint();
			
			int squareWidth = boardpanel.getWidth() / GLOBALS.BOARD_LENGTH;
			int squareHeight = boardpanel.getHeight() / GLOBALS.BOARD_LENGTH;
			int x = this.MousePosition.x / squareWidth;
			int y = this.MousePosition.y / squareHeight;

			if ( UTILS.inBounds(x, y) )
				engine.performHumanMove(x, y);	
			else showErrorMessage();
		}
	}
	
	public void mouseReleased(MouseEvent e)			{ return; }
	public void mouseDragged(MouseEvent e)			{ return; }
	public void mouseClicked(MouseEvent e) 			{ return; }
	public void componentHidden(ComponentEvent e)	{ return; }
	public void componentShown(ComponentEvent e)	{ return; }
	public void componentMoved(ComponentEvent e)	{ return; }
	public void mouseMoved(MouseEvent e) 			{ return; }
	public void mouseEntered(MouseEvent e) 			{ return; }
	public void mouseExited(MouseEvent e) 			{ return; }
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}