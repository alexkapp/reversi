package gui;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import gui.SpringUtilities;

/* 
 * The layout for inputting custome AI search and evaluation values
 */ 

public class DifficultyOptsWindow {

	private JLabel[] labels =  { new JLabel("Moves Ahead:", JLabel.TRAILING), 
				     new JLabel("Coin Parity Weight:", JLabel.TRAILING),
				     new JLabel("Mobilty Weight:", JLabel.TRAILING),  
				     new JLabel("Corner Weight (5):", JLabel.TRAILING),
				     new JLabel("Stability Weight:", JLabel.TRAILING)
				    };

	private JTextField[] textfields;
	private JFrame optFrame = null;
	private JPanel basepanel = null;
	
	private BoardGUI bGUI = null;
	ArrayList<Integer> options = null;
	
	public DifficultyOptsWindow(BoardGUI bGUI, ArrayList<Integer> options) {
		this.bGUI = bGUI;
		this.options = options;
		initWindow();		
	}
	
	private void initWindow() {
		
		this.optFrame = new JFrame("Difficulty Options");	
		
		basepanel = new JPanel(new SpringLayout());
		textfields = new JTextField[labels.length];

		for (int i = 0; i < labels.length; i++) {
			basepanel.add(labels[i]);
			textfields[i] = new JTextField(5);
			textfields[i].setText(String.valueOf(options.get(i)));
			textfields[i].setToolTipText("0-10");
			labels[i].setLabelFor(textfields[i]);
			basepanel.add(textfields[i]);
		}
		
		JButton submitbutton = new JButton("Submit");
		submitbutton.addActionListener(e -> { 
			options.stream().forEach(opt -> 		
				options.set(options.indexOf(opt),Integer.parseInt(textfields[options.indexOf(opt)].getText())));
			bGUI.saveOptions(options);
		});
		basepanel.add(submitbutton);
		
		JButton cancelbutton = new JButton("Cancel");
		cancelbutton.addActionListener(e -> bGUI.optionsCancelled());
		basepanel.add(cancelbutton);
			
		SpringUtilities.makeCompactGrid( basepanel,
				                 labels.length + 1, 2,
				                  8, 8,
				                  4, 4);

		this.optFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		optFrame.getContentPane().add(basepanel);
		optFrame.setLocation(bGUI.getWidth() /2, bGUI.getHeight() / 2);
		optFrame.setVisible(true);
		optFrame.pack();
	}
	
	public JFrame getOptionFrame() {
		return this.optFrame;
	}
}
