import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class ChampSelect extends JPanel implements ActionListener
{
	private JButton[] options;
	private Player[] players;
	private static Player player1;
	private static Player player2;
	private boolean firstSelect=true; //true if first player is selecting
	
	public ChampSelect()
	{
		players = new Player[5];
		players[0] = new Wagner(1);
		players[1] = new Stayer(1);
		players[2] = new Khan(1);
		players[3] = new Slime(1);
		players[4] = new DeerHunter(1);
		
		options = new JButton[players.length];
		
		//pro as fk
		for(int i=0;i<players.length;i++)
		{
			options[i] = new JButton(new ImageIcon(players[i].getFace()));
			options[i].addActionListener(this);
		}
		
		setLayout(new GridBagLayout());
		GridBagConstraints gr = new GridBagConstraints();
		
		gr.gridy = 0;
		for(int i=0;i<options.length;i++)
		{
			gr.gridx = i;
			add(options[i], gr);
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		for(int i=0;i<options.length;i++)
		{
			if(e.getSource()==options[i])
			{
				if(firstSelect)
				{
					player1 = players[i];
					
					players[0] = new Wagner(2);
					players[1] = new Stayer(2);
					players[2] = new Khan(2);
					players[3] = new Slime(2);
					players[4] = new DeerHunter(2);
					
					firstSelect=false;
					repaint();
				}
				else
				{
					player2 = players[i];
					
					JFrame f = (JFrame) SwingUtilities.getRoot(options[i]);
					for(Component c:f.getContentPane().getComponents())
						c.setVisible(false);
					
					Game g = new Game();
					f.add(g);
					g.setFrame(f);
					g.start();
				}
			}
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(firstSelect)
			g.drawString("Select player 1", 740/2 - 50, 740/10);
		else
			g.drawString("Select player 2", 740/2 - 50, 740/10);
	}
	public static Player getPlayer1()
	{
		return player1;
	}
	
	public static Player getPlayer2()
	{
		return player2;
	}
}
