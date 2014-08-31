import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;


public class FighterButton extends JButton
{
	public FighterButton(String name)
	{
		super(name);

		setFont(new Font("Arial", Font.PLAIN, 25));
		setBorder(null);
		setFocusPainted(false);
		setBackground(Color.gray);
		setForeground(Color.black);
		setPreferredSize(new Dimension(314, 100));
	}
}
