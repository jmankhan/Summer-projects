import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;


public class DeerHunter extends Player
{
	private final int HUNTER = 10;
	private final int HUNTER_WEP_VX = 3;
	private final int HUNTER_WEP_VY = 4;
	
	private Weapon weapon;
	private int currD;
	private BufferedImage baseWeaponH, baseWeaponV;
	
	public DeerHunter(int num) 
	{
		super(num);
		name = "Deer Hunter";
	}

	public void init()
	{
		super.init();
		
		setupImages();
		setupAnimations();
		
		currD=2;
		attacking=false;
		damage=25;
	}

	public void setupImages()
	{
		img = im.getSprite(0, HUNTER);
		baseWeaponH = im.getSprite(0, HUNTER+2, Game.SIZE*4, Game.SIZE);
		baseWeaponV = im.getSprite(HUNTER_WEP_VX, HUNTER_WEP_VY, Game.SIZE, Game.SIZE*4);
		weapon = new Weapon(baseWeaponH);
		
		face = im.getSprite(10, HUNTER);
	}

	public void setupAnimations()
	{
		imgDown = new BufferedImage[2];
		imgDown[0] = im.getSprite(0, HUNTER);
		imgDown[1] = im.getSprite(1, HUNTER);

		imgUp = new BufferedImage[2];
		imgUp[0] = im.getSprite(2, HUNTER);
		imgUp[1] = im.getSprite(3, HUNTER);

		imgLeft = new BufferedImage[2];
		imgLeft[0] = im.getSprite(4, HUNTER);
		imgLeft[1] = im.getSprite(5, HUNTER);

		imgRight = new BufferedImage[2];
		imgRight[0] = im.getSprite(6, HUNTER);
		imgRight[1] = im.getSprite(7, HUNTER);
		
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

	public BufferedImage rotateImage(BufferedImage img, int xpos, int ypos, int degrees)
	{
		// rotate image
		double rotationRequired = Math.toRadians(-degrees);
		double locationX = img.getWidth() / 2;
		double locationY = img.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		return op.filter(img, null);
	}
	
	public void attack()
	{
		weapon.setImage(baseWeaponH);
		if(currD==0)
		{
			weapon.setImage(baseWeaponV);
			weapon.setLocation(x, y-Game.SIZE*4);
			weapon.setBounds(new Rectangle(x, y-Game.SIZE*4, Game.SIZE, Game.SIZE*4));
			img = imgUp[1];
		}
		else if(currD==1)
		{
			weapon.setImage(rotateImage(baseWeaponH, x, y, 180));
			weapon.setLocation(x+Game.SIZE, y);
			weapon.setBounds(new Rectangle(x+Game.SIZE, y, Game.SIZE*4, Game.SIZE));
			img = imgRight[1];
		}
		else if(currD==2)
		{
			weapon.setImage(rotateImage(baseWeaponV, x, y, 180));
			weapon.setLocation(x-15, y+Game.SIZE); //adjust for rotation rounding error
			weapon.setBounds(new Rectangle(x, y+Game.SIZE, Game.SIZE, Game.SIZE*4));
			img = imgDown[1];
		}
		else if(currD==3)
		{
			weapon.setLocation(x-Game.SIZE*4, y);
			weapon.setBounds(new Rectangle(x-Game.SIZE*4, y, Game.SIZE*4, Game.SIZE));
			img = imgLeft[1];
		}

		Timer timer = new Timer();
		timer.schedule(new ResetWeapon(), 250);
	}
	
	public boolean checkCollision()
	{
		if(weapon.getBounds().intersects(enemy.getBounds()))
			return true;
		
		return false;
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
	}
	
	public void render(Graphics g)
	{
		super.render(g);
		g.drawImage(img, x, y, null);
		g.drawImage(weapon.getImage(), weapon.getX(), weapon.getY(), null);
		
		if(attacking)
			attack();
	}

	class ResetWeapon extends TimerTask
	{
		
		public void run()
		{
			weapon.setLocation(-100, -100);
			weapon.setBounds(new Rectangle(-100,-100,0,0));
			attacking=false;
		}
	}
}

