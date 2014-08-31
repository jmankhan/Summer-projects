import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;


public class Level 
{
	private Player[] players = new Player[Game.getPlayers().length];
	private Rectangle hpBar1, hpBar2, icon1Bounds, icon2Bounds;
	private boolean hit1, hit2;
	
	public Level()
	{
		players = Game.getPlayers();
		players[0].setEnemy(players[1]);
		players[1].setEnemy(players[0]);
		
		hpBar1 = new Rectangle(Game.WIDTH/2-Game.SIZE*2-200, Game.HEIGHT/10, 200, 25);
		hpBar2 = new Rectangle(Game.WIDTH/2+Game.SIZE*3, Game.HEIGHT/10, 200, 25);
		
		icon1Bounds = new Rectangle(hpBar1.x+hpBar1.width+Game.SIZE, hpBar1.y, Game.SIZE, Game.SIZE);
		icon2Bounds = new Rectangle(icon1Bounds.x+Game.SIZE*2, icon1Bounds.y, Game.SIZE, Game.SIZE);
		
		//if player i attack has hit enemy
		hit1 = false;
		hit2 = false;
	}

	public void render(Graphics g)
	{
		Graphics2D gr = (Graphics2D) g;

		gr.setColor(Color.white);

		//draw hp bars
		gr.draw(hpBar1);
		gr.drawRect(Game.WIDTH/2+Game.SIZE*3, Game.HEIGHT/10, 200, 25);

		//draw player icons next to hp bars
		gr.drawImage(players[0].face, icon1Bounds.x, icon1Bounds.y, icon1Bounds.width, icon1Bounds.height, null);
		gr.drawImage(players[1].face, icon2Bounds.x, icon2Bounds.y, icon2Bounds.width, icon2Bounds.height, null);
		
		//fill player hp
		gr.setColor(Color.red);
		gr.fillRect(hpBar1.x, hpBar1.y, players[0].getHP()*2, hpBar1.height);
		gr.fill(hpBar2);
		
		//draw player names
		gr.setColor(Color.white);
		g.drawString(players[0].getName(), icon1Bounds.x, icon1Bounds.y+Game.SIZE*2);
		g.drawString(players[1].getName(), icon2Bounds.x, icon2Bounds.y+Game.SIZE*2);
	}

	
	public void update(double delta)
	{
		if(!players[0].attacking)
			hit1=false;
		if(!players[1].attacking)
			hit2=false;
		
		//creating enemy in player class didn't really work out, so im handling enemies in the level class
		if(players[0].checkCollision() && !hit1)
		{
			if(players[0].isSlime())
			{
				players[1].setSpeed(players[1].getSpeed()-1);
				Timer timer = new Timer();
				timer.schedule(new ResetSpeed(players[1]), 500);
			}
			
			players[1].setHP(-players[0].getDamage());
			hpBar2 = new Rectangle(hpBar2.x+players[0].getDamage()*2, hpBar2.y, hpBar2.width-players[0].getDamage()*2, hpBar2.height);
			hit1=true;
		}
		
		if(players[1].checkCollision() && !hit2)
		{
			if(players[1].isSlime())
			{
				players[0].setSpeed(players[0].getSpeed()-1);
				Timer timer = new Timer();
				timer.schedule(new ResetSpeed(players[0]), 500);
			}
			
			players[0].setHP(-players[1].getDamage());
			hit2=true;
		}
	}
	
	class ResetSpeed extends TimerTask
	{
		private Player p;
		public ResetSpeed(Player rp)
		{
			p=rp;
		}
		
		public void run()
		{
			p.setSpeed(Player.baseSpeed());
		}
	}
}
