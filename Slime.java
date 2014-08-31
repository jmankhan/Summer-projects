import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;


public class Slime extends Player
{
	private final int SLIME = 8;
	private final int CASTDELAY = 500;
	private final int ATTDELAY = 1500;

	private Weapon weapon;
	private BufferedImage attackL, attackR, attackU, attackD;
	private BufferedImage attackAnim[];
	private int currD;
	private boolean canMove, firstAttack;

	private Graphics g;

	public Slime(int num) 
	{
		super(num);
		name = "Slime";
	}

	public void init()
	{
		super.init();

		setupImages();
		setupAnimations();
		damage = 25;
		currD = 2;
		canMove=true;
		firstAttack=true;
	}

	public void setupImages()
	{
		img = im.getSprite(0, SLIME);
		weapon = new Weapon(im.getSprite(4, SLIME+1, Game.SIZE*3, Game.SIZE));
		face = im.getSprite(10, SLIME);
	}

	public void setupAnimations()
	{
		imgDown = new BufferedImage[2];
		imgDown[0] = im.getSprite(0, SLIME);
		imgDown[1] = im.getSprite(1, SLIME);

		imgUp = new BufferedImage[2];
		imgUp[0] = im.getSprite(2, SLIME);
		imgUp[1] = im.getSprite(3, SLIME);

		imgLeft = new BufferedImage[2];
		imgLeft[0] = im.getSprite(4, SLIME);
		imgLeft[1] = im.getSprite(5, SLIME);

		imgRight = new BufferedImage[2];
		imgRight[0] = im.getSprite(6, SLIME);
		imgRight[1] = im.getSprite(7, SLIME);

		attackD = im.getSprite(0, SLIME+1);
		attackU = im.getSprite(1, SLIME+1);
		attackL = im.getSprite(2, SLIME+1);
		attackR = im.getSprite(3, SLIME+1);

		attackAnim = new BufferedImage[2];
		attackAnim[0] = im.getSprite(4, SLIME+1, Game.SIZE*3, Game.SIZE);
		attackAnim[1] = im.getSprite(7, SLIME+1, Game.SIZE*3, Game.SIZE);
	}

	/**
	 * modified setDir method that keeps track of last facing direction
	 */
	public void setDir(int dir)
	{
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

	/**
	 * rotates attack by 90 degrees clockwise
	 * @param img image to rotate
	 * @return
	 */
	public BufferedImage rotateAttack(BufferedImage img)
	{
		// rotate image
		double rotationRequired = Math.toRadians(90);
		double locationX = img.getWidth() / 2;
		double locationY = img.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		return op.filter(img, null);
	}
	public void attack()
	{
		if(firstAttack)
		{
			canMove=false;
			weapon.setDir(currD);
			firstAttack = false;
			
			if(currD == 0)
			{
				weapon.setImage(rotateAttack(attackAnim[0]));
				weapon.setLocation(x-Game.SIZE, y-Game.SIZE*2);
				weapon.setBounds(new Rectangle(weapon.getX(), weapon.getY(), Game.SIZE, Game.SIZE*3));
				img = attackU;

				Timer timer = new Timer();
				timer.schedule(new AttackAnim(currD), CASTDELAY);
				timer.schedule(new ResetWeapon(), CASTDELAY+ATTDELAY);

			}
			else if(currD == 1)
			{
				weapon.setImage(attackAnim[0]);
				weapon.setLocation(x+Game.SIZE, y);
				weapon.setBounds(new Rectangle(weapon.getX(), weapon.getY(), Game.SIZE*3, Game.SIZE));
				img = attackR;

				weapon.setBounds(new Rectangle(weapon.getX(), weapon.getY(), Game.SIZE, Game.SIZE*3));
				Timer timer = new Timer();
				timer.schedule(new AttackAnim(currD), CASTDELAY);
				timer.schedule(new ResetWeapon(), CASTDELAY+ATTDELAY);
			}
			else if(currD == 2)
			{
				weapon.setImage(rotateAttack(attackAnim[0]));
				weapon.setLocation(x-Game.SIZE, y+Game.SIZE);
				weapon.setBounds(new Rectangle(weapon.getX(), weapon.getY(), Game.SIZE, Game.SIZE*3));
				img = attackD;

				Timer timer = new Timer();
				timer.schedule(new AttackAnim(currD), CASTDELAY);
				timer.schedule(new ResetWeapon(), CASTDELAY+ATTDELAY);
			}
			else if(currD == 3)
			{
				weapon.setImage(attackAnim[0]);
				weapon.setLocation(x-Game.SIZE*3, y);
				weapon.setBounds(new Rectangle(weapon.getX(), weapon.getY(), Game.SIZE*3, Game.SIZE));
				img = attackL;

				Timer timer = new Timer();
				timer.schedule(new AttackAnim(currD), CASTDELAY);
				timer.schedule(new ResetWeapon(), CASTDELAY+ATTDELAY);
			}
		}
	}

	public boolean checkCollision()
	{
		if(weapon.getBounds().intersects(enemy.getBounds()))
			return true;

		return false;
	}

	public boolean isSlime()
	{
		return true;
	}
	public Weapon getWeapon()
	{
		return weapon;
	}
	public void render(Graphics g)
	{
		super.render(g);
		this.g = g;

		g.drawImage(weapon.getImage(), weapon.getX(), weapon.getY(), null);
		g.drawImage(img, x, y, null);
	}

	public void update(double delta)
	{
		super.update(delta);

		if(canMove)
		{
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
		if(attacking)
			attack();
	}

	class AttackAnim extends TimerTask
	{
		private int currD;

		public AttackAnim(int dir)
		{
			currD=dir;
		}
		public void run()
		{
			switch(currD)
			{
			case 0: weapon.setImage(rotateAttack(attackAnim[1])); img = imgUp   [0]; break;
			case 1: weapon.setImage(attackAnim[1]); 			  img = imgRight[0]; break;
			case 2: weapon.setImage(rotateAttack(attackAnim[1])); img = imgDown [0]; break;
			case 3: weapon.setImage(attackAnim[1]); 			  img = imgLeft [0]; break;
			default: System.out.println("error playing slime animation");
			}

			canMove=true;
		}
	}

	class ResetWeapon extends TimerTask
	{
		public void run()
		{
			weapon.setLocation(-100,-100);
			attacking=false;
			firstAttack=true;
		}
	}

}
