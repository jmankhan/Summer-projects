import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Wagner extends Player
{
	private final int WAGNER = 2; //row in spritesheet
	private Weapon weapon;
	
	public Wagner(int num)
	{
		super(num);
		name = "MWagner";
	}

	public void init()
	{
		super.init();

		damage = 20;
		setupImages();
		setupAnimation();
	}

	public void setupImages()
	{
		img = im.getSprite(0,WAGNER);
		weapon = new Weapon(im.getSprite(9, WAGNER));
		face = im.getSprite(10, WAGNER);
	}

	public void setupAnimation()
	{
		imgDown = new BufferedImage[3];
		imgDown[0] = im.getSprite(0, WAGNER);
		imgDown[1] = im.getSprite(1, WAGNER);
		imgDown[2] = im.getSprite(2, WAGNER);

		imgUp = new BufferedImage[3];
		imgUp[0] = im.getSprite(3, WAGNER);
		imgUp[1] = im.getSprite(4, WAGNER);
		imgUp[2] = im.getSprite(5, WAGNER);

		imgLeft = new BufferedImage[2];
		imgLeft[0] = im.getSprite(6, WAGNER);
		imgLeft[1] = im.getSprite(6, WAGNER);

		imgRight = new BufferedImage[2];
		imgRight[0] = im.getSprite(8, WAGNER);
		imgRight[1] = im.getSprite(8, WAGNER);
	}

	public boolean checkCollision()
	{
		if(weapon.getBounds().intersects(enemy.getBounds()))
			return true;

		return false;
	}
	public void playAttackAnimation(BufferedImage img, int xpos, int ypos, int degrees, Graphics g)
	{
		// rotate image
		double rotationRequired = Math.toRadians(-degrees);
		double locationX = img.getWidth() / 2;
		double locationY = img.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		//translate image
		int dx = (int) (xpos - Math.sin(degrees)*32);
		int dy = (int) (ypos - Math.cos(degrees)*32);

		// Draw rotations at translated points
		g.drawImage(op.filter(img, null), dx, dy, null);
		weapon.getBounds().setLocation(dx, dy);
	}
	
	public void render(Graphics g)
	{
		super.render(g);
		g.drawImage(img, x, y, null);
		if(attacking)
			playAttackAnimation(weapon.getImage(), x, y, attackCounter++, g);
		
		if(attackCounter >= 30)
		{
			attackCounter = 0;
			attacking = false;
			weapon.getBounds().setLocation(-100,-100);
		}
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
			animCounter = animCounter==3 ? 0 : animCounter;
		}
	}
}
