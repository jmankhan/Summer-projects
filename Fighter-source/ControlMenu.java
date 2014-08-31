import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class ControlMenu extends JPanel implements ActionListener, KeyListener
{
	private String path = "res/controls.txt";
	private String controls;
	private String nextKey;
	private JLabel[] p1c = new JLabel[6], p2c = new JLabel[6];
	private JButton[] p1f = new JButton[5], p2f = new JButton[5];
	private FighterButton ok, cancel;
	
	public ControlMenu()
	{
		setLayout(new GridBagLayout());
		
		init();
		addToMenu();
		readControls();
		formatControls();
		
	}
	
	/**
	 * instantiate fields
	 */
	public void init()
	{
		p1c[0] = new JLabel("Player 1 Controls");
		p2c[0] = new JLabel("Player 2 Controls");
		
		p1c[1] = new JLabel("Left");
		p2c[1] = new JLabel("Left");
		
		p1c[2] = new JLabel("Right");
		p2c[2] = new JLabel("Right");
		
		p1c[3] = new JLabel("Up");
		p2c[3] = new JLabel("Up");
		
		p1c[4] = new JLabel("Down");
		p2c[4] = new JLabel("Down");
		
		p1c[5] = new JLabel("Attack");
		p2c[5] = new JLabel("Attack");
		
		p1f[0] = new JButton("A");
		p2f[0] = new JButton("left");
		
		p1f[1] = new JButton("D");
		p2f[1] = new JButton("right");
		
		p1f[2] = new JButton("W");
		p2f[2] = new JButton("north");
		
		p1f[3] = new JButton("S");
		p2f[3] = new JButton("south");
		
		p1f[4] = new JButton("SPACE");
		p2f[4] = new JButton("SLASH");
		
		for(JButton b:p1f)
		{
			b.addActionListener(this);
		}
		for(JButton b:p2f)
		{
			b.addActionListener(this);
		}
		
		ok = new FighterButton("OK");
		cancel = new FighterButton("Cancel");
		
	}

	/**
	 * add fields to screen after formatting
	 */
	public void addToMenu()
	{
		GridBagConstraints gr = new GridBagConstraints();

		gr.insets = new Insets(0,0,25,0);
		gr.anchor = GridBagConstraints.WEST;
		for(int i=0;i<p1c.length;i++)
		{
			//player 1 labels
			gr.gridx=0;
			gr.gridy=i;
			p1c[i].setPreferredSize(new Dimension(157, 50));
			add(p1c[i], gr);
			
			//player 2 labels
			gr.gridx=2;
			p2c[i].setPreferredSize(new Dimension(157,50));
			add(p2c[i], gr);
		}

		gr.anchor = GridBagConstraints.EAST;
		for(int i=0;i<p1f.length;i++)
		{
			gr.gridx = 1;
			gr.gridy=i+1;
			p1f[i].setMinimumSize(new Dimension(157,25));
			add(p1f[i],gr);

			gr.gridx=3;
			gr.gridy=i+1;
			p2f[i].setMinimumSize(new Dimension(157,25));
			add(p2f[i],gr);
		}

		gr.gridx=1;
		gr.gridy = p1c.length+p2c.length+1;
		ok.setMinimumSize(new Dimension(157,25));
		add(ok, gr);
		
		gr.gridy=p1c.length+p2c.length+2;
		cancel.setMinimumSize(new Dimension(157,25));
		add(cancel,gr);
		
		addKeyListener(this);
		requestFocusInWindow();
	}
	
	/**
	 * read saved controls from text
	 */
	public void readControls()
	{
		InputStream is = getClass().getClassLoader().getResourceAsStream(path);
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		controls="";
		
		try
		{
			String line = in.readLine();
			while(line!=null)
			{
				controls+=line;
				line = in.readLine();
			}
		} catch(IOException ioe) {ioe.printStackTrace();}
	}
	
	/**
	 * format controls into individual strings, add them to corresponding fields
	 */
	public void formatControls()
	{
		String[] p1 = new String[p1f.length];
		String[] p2 = new String[p1f.length];
		
		p1[0] = controls.substring(0,controls.indexOf('`'));
		cutString('`');
		p1[1] = controls.substring(0,controls.indexOf('`'));
		cutString('`');
		p1[2] = controls.substring(0,controls.indexOf('`'));
		cutString('`');
		p1[3] = controls.substring(0,controls.indexOf('`'));
		cutString('`');
		p1[4] = controls.substring(0,controls.indexOf('`'));
		cutString('`');
		
		p2[0] = controls.substring(0,controls.indexOf('`'));
		cutString('`');
		p2[1] = controls.substring(0,controls.indexOf('`'));
		cutString('`');
		p2[2] = controls.substring(0,controls.indexOf('`'));
		cutString('`');
		p2[3] = controls.substring(0,controls.indexOf('`'));
		cutString('`');
		p2[4] = controls.substring(0,controls.indexOf('`'));
		cutString('`');
		
		for(int i=0;i<p1f.length;i++)
		{
			p1f[i].setText(p1[i].toUpperCase());
			p2f[i].setText(p2[i].toUpperCase());
		}
	}

	/**
	 * helper method to format string
	 * @param toCutAt 'escape' char to signal end of control
	 */
	public void cutString(char toCutAt)
	{
		controls = controls.substring(controls.indexOf(toCutAt)+1);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		for(JButton b:p1f)
		{
			if(e.getSource()==b)
			{
				if(nextKey!=null)
				{
					b.setText(nextKey);
					b.revalidate();
					nextKey=null;
					requestFocusInWindow();
				}
			}
		}
		
		if(e.getSource()==cancel)
		{
			JFrame f = (JFrame) SwingUtilities.getRoot(cancel);
			for(Component c:f.getContentPane().getComponents())
				c.setVisible(false);
			
			f.add(new FighterMenu());
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		nextKey = KeyEvent.getKeyText(e.getKeyCode());
		System.out.println(nextKey);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
