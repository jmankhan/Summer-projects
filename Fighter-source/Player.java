import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * prototype class for Player
 * handles methods, keys, counters, animations, hp, and enemy player
 * each subclass must implement its own collision method since weapons will vary
 * @author Jalal
 *
 */
public class Player
{
	//id
	protected String name;
	
	//images
	protected BufferedImage img;
	protected BufferedImage[] imgLeft, imgRight, imgUp, imgDown;
	protected BufferedImage[] imgAttack;
	protected BufferedImage face;
	
	protected int x; //pos
	protected int y;
	protected int speed; //speed to move
	protected int num; //player number
	protected int left, right, up, down; //keyevent controls
	protected int attack; //attack key
	protected int attackCounter; //attack animation step counter
	protected int animCounter; //player animation step counter
	protected int hp; //hp counter
	protected int damage;
	
	protected boolean leftD, rightD, upD, downD; //keyevent checks
	protected boolean attacking;
	
	protected ImageManager im;

	protected Player enemy;
	
	/**
	 * create new player(num)
	 * @param num player 1,2,3,etc
	 */
	public Player(int num)
	{
		im = new ImageManager("res/player_sheet.png");
		this.num=num;
		name = "player";
		
		init();
	}
	
	public void init()
	{
		switch(num)
		{
		case 1: x = Game.WIDTH/3; y = Game.HEIGHT/2; break;
		case 2: x = Game.WIDTH*2/3; y= Game.HEIGHT/2; break;
		default: x = 0; y = 0;
		}
		setupKeys();
		
		speed=2;
		attackCounter=0;
		animCounter=0;
		hp=100;
	}
	
	/**
	 * sets the direction to face/move
	 * @param dir keycode integer
	 */
	public void setDir(int dir)
	{
		animCounter = 0; //reset animation counter for new movement
		if(dir==up)
			upD=true;
		else if(dir==right)
			rightD=true;
		else if(dir==down)
			downD=true;
		else if(dir==left)
			leftD=true;
		else if(dir==attack) //um.. try not to read this in english
			attacking=true;
	}
	
	/**
	 * stop facing/moving in a direction 
	 * @param dir 0 is north, rotate clockwise
	 */
	public void stopDir(int dir)
	{
		if(dir==up)
			upD=false;
		else if(dir==right)
			rightD=false;
		else if(dir==down)
			downD=false;
		else if(dir==left)
			leftD=false;
	}
	
	/**
	 * create controls depending on player num
	 */
	public void setupKeys()
	{
		if(num == 1)
		{
			left = KeyEvent.VK_LEFT;
			right = KeyEvent.VK_RIGHT;
			up = KeyEvent.VK_UP;
			down = KeyEvent.VK_DOWN;
			attack = KeyEvent.VK_SLASH;
		}
		
		else if(num == 2)
		{
			left = KeyEvent.VK_A;
			right = KeyEvent.VK_D;
			up = KeyEvent.VK_W;
			down = KeyEvent.VK_S;
			attack = KeyEvent.VK_SPACE;
		}
	}
	
	/**
	 * hp of player
	 * @return hp integer, 0-100
	 */
	public int getHP()
	{
		return hp;
	}

	/**
	 * set change in hp
	 * @param dx change in hp
	 */
	public void setHP(int dx)
	{
		hp+=dx;
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle(x, y, Game.SIZE, Game.SIZE);
	}
	
	public boolean checkCollision() 
	{
		return false;
	}

	public void setSpeed(int sspeed)
	{
		speed=sspeed;
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	public static int baseSpeed()
	{
		return 2;
	}
	public String getName()
	{
		return name;
	}
	
	public int getDamage()
	{
		return damage;
	}
	public Player getEnemy()
	{
		return null;
	}
	
	public void setEnemy(Player p)
	{
		enemy = p;
	}
	
	public boolean isDead()
	{
		if(hp<=0)
			return true;
		
		return false;
	}
	
	public BufferedImage getFace()
	{
		return face;
	}
	
	public void setNum(int snum)
	{
		num=snum;
	}
	
	public boolean isSlime()
	{
		return false;
	}
	
	/**
	 * each player subclass should return its respective weapon
	 * @return weapon
	 */
	public Weapon getWeapon()
	{
		return null;
	}
	public void render(Graphics g)
	{
		if(hp<=0)
		{
			String[] options = {"Redeem your honor", "Menu"};
			int select = JOptionPane.showOptionDialog(null, name + " died (This option dialog is not fully functional yet)", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, new ImageIcon(face), options, options[0]);
			if(select == JOptionPane.YES_OPTION)
			{
				for(Component c:Game.getFrame().getContentPane().getComponents())
					c.setVisible(false);
				Game.getFrame().getContentPane().add(new ChampSelect());
			}
			else if(select == JOptionPane.NO_OPTION)
			{
				for(Component c:Game.getFrame().getContentPane().getComponents())
					c.setVisible(false);
				Game.getFrame().getContentPane().add(new FighterMenu());
			}
			System.exit(0);
		}
	}
	
	public void update(double delta)
	{
	}
}