import javax.swing.JFrame;


public class Runner extends JFrame
{
	/**
	 * bug fixes
	 */
	public Runner()
	{
		add(new FighterMenu());
		
		setVisible(true);
		setSize(Game.WIDTH, Game.HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("LumberFighter v0.7");
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	public static void main(String args[])
	{
		new Runner();
	}
}
