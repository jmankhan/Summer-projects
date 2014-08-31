import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Khan extends Player
{
	private final int KHAN = 3, AMOUNT = 3;
	private Weapon[] weapon;
	private BufferedImage[] attackAnim;
	private boolean[] rotating;
	private int rotateCounter;

	public Khan(int num)
	{
		super(num);
		name = "Khan";
	}

	public void init()
	{
		super.init();

		setupImages();
		setupAnimations();

		damage = 25;
		attacking=false;

		rotateCounter=0;
		rotating = new boolean[weapon.length];
		rotating[0]=false;
		rotating[1]=false;
		rotating[2]=false;
	}

	public void setupImages()
	{
		img = im.getSprite(0, KHAN);
		weapon = new Weapon[AMOUNT];

		weapon[0] = new Weapon(im.getSprite(9, KHAN));
		weapon[1] = new Weapon(im.getSprite(9, KHAN));
		weapon[2] = new Weapon(im.getSprite(9, KHAN));

		face = im.getSprite(10, KHAN);
	}

	public void setupAnimations()
	{
		imgDown = new BufferedImage[2];
		imgDown[0] = im.getSprite(0, KHAN);
		imgDown[1] = im.getSprite(1, KHAN);

		imgUp = new BufferedImage[2];
		imgUp[0] = im.getSprite(2, KHAN);
		imgUp[1] = im.getSprite(3, KHAN);

		imgLeft = new BufferedImage[2];
		imgLeft[0] = im.getSprite(4, KHAN);
		imgLeft[1] = im.getSprite(5, KHAN);

		imgRight = new BufferedImage[2];
		imgRight[0] = im.getSprite(6, KHAN);
		imgRight[1] = im.getSprite(7, KHAN);

		attackAnim = new BufferedImage[2];
		attackAnim[0] = im.getSprite(0, KHAN+1);
		attackAnim[1] = im.getSprite(0, KHAN+2, Game.SIZE*3, Game.SIZE*2);
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
		else if(dir==attack)
		{
			rotateCounter++;
			attacking=false;
		}
	}

	public void detonateAllBombs()
	{
		for(Weapon w:weapon)
		{
			w.setImage(attackAnim[0]);
			Timer timer = new Timer();
			timer.schedule(new AttackAnim(), 500);
		}

		attacking=false;
		rotateCounter=0;
	}

	/**
	 * rotates bombs around khan
	 * @param i bomb num to rotate
	 */
	public void rotateWeapon(int i, double angle)
	{
		double angleDeg = angle + attackCounter++;

		double angleRad = (angleDeg/180)*Math.PI;

		double cosAngle = Math.cos(angleRad);
		double sinAngle = Math.sin(angleRad);

		//components of radius
		double dx = (weapon[i].getX()-x+16);
		double dy = (weapon[i].getY()-y+9);

		weapon[i].setX(x + (int) (dx*cosAngle-dy*sinAngle));
		weapon[i].setY(y + (int) (dx*sinAngle+dy*cosAngle));

		weapon[i].setLocation(weapon[i].getX(), weapon[i].getY());

		//in case someone leaves this rotating for many hours
		if(attackCounter==360)
			attackCounter=0;
	}

	public boolean checkCollision()
	{
		for(Weapon w:weapon)
		{
			if(w.getBounds().intersects(enemy.getBounds()))
			{
				w.setImage(attackAnim[1]);
				Timer timer = new Timer();
				timer.schedule(new ResetWeapon(), 200);
				return true;
			}
		}
		return false;
	}

	public void render(Graphics g)
	{
		super.render(g);
		g.drawImage(img, x, y, null);
		for(Weapon w:weapon)
			g.drawImage(w.getImage(), w.getX(), w.getY(), null);
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
			animCounter = animCounter==2 ? 0 : animCounter;
		}
		else if(downD)
		{
			if(y<Game.HEIGHT-Game.SIZE*2)
				y+=speed;

			img = imgDown[animCounter++];
			animCounter = animCounter==2 ? 0 : animCounter;
		}

		if(rotateCounter==1)
		{
			rotating[0]=true;
			rotating[1]=true;
			rotating[2]=true;
			attacking=true;
		}

		if(rotateCounter==2)
		{
			rotating[0]=false;
			weapon[0].setLocation(x, y);
			rotateCounter++;
			attacking=true;
		}

		if(rotateCounter==4)
		{
			rotating[1]=false;
			weapon[1].setLocation(x,y);
			rotateCounter++;
			attacking=true;
		}

		if(rotateCounter==6)
		{
			rotating[2]=false;
			weapon[2].setLocation(x, y);
			rotateCounter++;
		}
		if(rotateCounter==8)
		{
			detonateAllBombs();
		}
		for(int i=0;i<weapon.length;i++)
		{
			if(rotating[i])
			{
				weapon[i].setLocation(x, y-Game.SIZE);
				rotateWeapon(i, 120*i);
			}
		}
	}

	class AttackAnim extends TimerTask 
	{
		@Override
		public void run()
		{
			for(Weapon w:weapon)
				w.setImage(attackAnim[1]);

			Timer timer = new Timer();
			timer.schedule(new ResetWeapon(), 500);
		}
	}

	class ResetWeapon extends TimerTask
	{
		int i=-1;
		public ResetWeapon()
		{

		}
		public ResetWeapon(int index)
		{
			i=index;
		}
		public void run()
		{
			if(i!=-1)
			{
				weapon[i].setImage(im.getSprite(9, KHAN));
				weapon[i].setLocation(-100, -100);
			}
			else
			{
				for(Weapon w:weapon)
				{
					w.setImage(im.getSprite(9, KHAN));
					w.setLocation(-100,-100);
				}
			}
		}
	}
}