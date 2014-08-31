import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class FighterMenu extends JPanel implements ActionListener 
{
	FighterButton start, controls, exit;
	
	public FighterMenu()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints gr = new GridBagConstraints();
		
		start = new FighterButton("Start");
		controls = new FighterButton("Controls");
		exit = new FighterButton("Exit");
		
		start.addActionListener(this);
		controls.addActionListener(this);
		exit.addActionListener(this);

		gr.gridx = 0;
		gr.gridy = 0;
		gr.insets = new Insets(0,0,20,0);
		add(start, gr);
		
		gr.gridy = 1;
		//add(controls, gr);
		
		gr.gridy = 2;
		add(exit, gr);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == start)
		{
			JFrame f = (JFrame) SwingUtilities.getRoot(start);
			for(Component c:f.getContentPane().getComponents())
				c.setVisible(false);
			
			f.getContentPane().add(new ChampSelect());
		}
		
		if(e.getSource() == controls)
		{
			JFrame f = (JFrame) SwingUtilities.getRoot(controls);
			for(Component c:f.getContentPane().getComponents())
				c.setVisible(false);
			f.getContentPane().add(new ControlMenu());
		}
		if(e.getSource() == exit)
		{
			System.exit(0);
		}
	}
}
