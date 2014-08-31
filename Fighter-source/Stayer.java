import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;


public class Stayer extends Player
{
	private final int STAYER = 0;

	private BufferedImage[] attackL, attackR, attackU, attackD;
	private int currD; //current facing direction
	private boolean attackingAnim;
	private Weapon weapon;

	public Stayer(int num)
	{
		super(num);
		name = "MStayer";
	}

	public void init()
	{
		super.init();

		currD=2; //starts off facing down
		damage = 25;
		attackingAnim = false;

		setupImages();
		setupAnimation();
	}

	public void setupImages()
	{
		img = im.getSprite(0, STAYER);
		weapon = new Weapon(im.getSprite(9, STAYER+1));
		face = im.getSprite(10, STAYER);
	}

	public void setupAnimation()
	{
		imgDown = new BufferedImage[2];
		imgDown[0] = im.getSprite(0, STAYER);
		imgDown[1] = im.getSprite(1, STAYER);

		imgUp = new BufferedImage[3];
		imgUp[0] = im.getSprite(2, STAYER);
		imgUp[1] = im.getSprite(3, STAYER);
		imgUp[2] = im.getSprite(4, STAYER);

		imgLeft = new BufferedImage[2];
		imgLeft[0] = im.getSprite(5, STAYER);
		imgLeft[1] = im.getSprite(6, STAYER);

		imgRight = new BufferedImage[2];
		imgRight[0] = im.getSprite(7, STAYER);
		imgRight[1] = im.getSprite(8, STAYER);

		attackL = new BufferedImage[2];
		attackL[0] = im.getSprite(4, STAYER+1);
		attackL[1] = im.getSprite(5, STAYER+1);

		attackR = new BufferedImage[2];
		attackR[0] = im.getSprite(6, STAYER+1);
		attackR[1] = im.getSprite(7, STAYER+1);

		attackU = new BufferedImage[2];
		attackU[0] = im.getSprite(2, STAYER+1);
		attackU[1] = im.getSprite(3, STAYER+1);

		attackD = new BufferedImage[2];
		attackD[0] = im.getSprite(0, STAYER+1);
		attackD[1] = im.getSprite(1, STAYER+1);
	}

	public boolean checkCollision()
	{
		if(weapon.getBounds().intersects(enemy.getBounds()))
		{
			weapon.setLocation(-100, -100);
			attacking=false;
			return true;
		}

		return false;
	}

	public void attack()
	{
		Timer timer = new Timer();

		if(currD == 0)
		{
			weapon.setLocation(x, y-Game.SIZE);
			weapon.setDir(currD);
			weapon.start();
			img = attackU[0];
			timer.schedule(new AttackAnim(currD), 400);
		}
		else if(currD == 1)
		{
			weapon.setLocation(x+Game.SIZE, y);
			weapon.setDir(currD);
			weapon.start();
			img = attackR[0];
			timer.schedule(new AttackAnim(currD), 400);
		}
		else if(currD == 2)
		{
			weapon.setLocation(x, y+Game.SIZE);
			weapon.setDir(currD);
			weapon.start();
			img = attackD[0];
			timer.schedule(new AttackAnim(currD), 400);
		}
		else if(currD == 3)
		{
			weapon.setLocation(x-Game.SIZE, y);
			weapon.setDir(currD);
			weapon.start();
			img = attackL[0];
			timer.schedule(new AttackAnim(currD), 400);
		}
		if(weapon.getX() < 0 || weapon.getY() < 0 || weapon.getX()>Game.WIDTH || weapon.getY()> Game.HEIGHT)
		{
			attacking=false;
			weapon.setLocation(-100, -100);
		}
		
		attacking = false;
	}

	/**
	 * modified setDir method that keeps track of last facing direction
	 */
	public void setDir(int dir)
	{
		animCounter = 0; //reset animation counter for new movement
		if(dir==up)
		{
			upD=true;
			currD=0;
		}
		else if(dir==right)
		{
			rightD=true;
			currD=1;
		}
		else if(dir==down)
		{
			downD=true;
			currD=2;
		}
		else if(dir==left)
		{
			leftD=true;
			currD=3;
		}

		else if(dir==attack) //um.. it makes sense if you dont think about it
			attacking=true;
	}

	public Weapon getWeapon()
	{
		return weapon;
	}

	public void render(Graphics g)
	{
		super.render(g);

		g.setColor(Color.yellow);
		g.drawImage(img, x, y, null);
		g.drawImage(weapon.getImage(), weapon.getX(), weapon.getY(), null);
	}

	public void update(double delta)
	{
		super.update(delta);
		if(leftD)
		{
			if(x>0)
				x-=speed;

			img = imgLeft[animCounter++];
			animCounter = animCounter==2 ? 0 : animCounter;
		}
		else if(rightD)
		{
			if(x<Game.WIDTH-Game.SIZE*1.5)
				x+=speed;

			img = imgRight[animCounter++];
			animCounter = animCounter==2 ? 0 : animCounter;
		}
		else if(upD)
		{
			if(y>0)
				y-=speed;

			img = imgUp[animCounter++];
			animCounter = animCounter==3 ? 0 : animCounter;
		}
		else if(downD)
		{
			if(y<Game.HEIGHT-Game.SIZE*2)
				y+=speed;

			img = imgDown[animCounter++];
			animCounter = animCounter==2 ? 0 : animCounter;
		}

		if(attacking)
			attack();
	}

	class AttackAnim extends TimerTask
	{
		private int dir;

		/**
		 * tells method which direction first animation was, so it can complete the next frame accurately
		 * @param dir 0=n, 1=e, 2=s, 3=w
		 */
		public AttackAnim(int adir)
		{
			dir=adir;
		}

		@Override
		public void run() 
		{
			switch(dir)
			{
			case 0:img=attackU[1]; break;
			case 1:img=attackR[1]; break;
			case 2:img=attackD[1]; break;
			case 3:img=attackL[1]; break;
			default:System.out.println("error animating stayer attack");
			}

			Timer timer = new Timer();
			timer.schedule(new ResetPlayer(currD), 500);
			return;
		}

	}
	
	class ResetPlayer extends TimerTask
	{
		private int currD;
		
		public ResetPlayer(int dir)
		{
			currD=dir;
		}
		
		public void run()
		{
			switch(currD)
			{
			case 0: img = imgUp[0]; break;
			case 1: img = imgRight[0]; break;
			case 2: img = imgDown[0]; break;
			case 3: img = imgLeft[0]; break;
			default: System.out.println("error resetting stayer");
			}
		}
	}
}


