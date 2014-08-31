import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Weapon implements Runnable
{
	private BufferedImage img;
	private Rectangle bounds;
	private int damage, x, y, currD, delay;
	private Thread thread;
	private boolean running;
	
	public Weapon(BufferedImage weaponImg)
	{
		img = weaponImg;
		bounds = new Rectangle(-100,-100,Game.SIZE,Game.SIZE);
		damage = 1;
		delay = 20;
		
		x=-100;
		y=-100;
	}
	
	public void start()
	{
		thread = new Thread(this);
		thread.start();
		running=true;
	}
	
	public void stop()
	{
		if(thread==null)
			return;
		
		try 
		{
			running=false;
			x=-100;
			y=-100;
			thread.join();
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	public void setDir(int dir)
	{
		currD=dir;
	}
	public void setDamage(int x)
	{
		damage = x;
	}
	
	public BufferedImage getImage()
	{
		return img;
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}
	
	public Rectangle getLargeBounds()
	{
		return new Rectangle(x, y, Game.SIZE*3, Game.SIZE*3);
	}
	
	public void setBounds(Rectangle r)
	{
		bounds=r;
	}
	public int getDamage()
	{
		return damage;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int sx)
	{
		x=sx;
	}
	
	public void setY(int sy)
	{
		y=sy;
	}
	
	public void setLocation(int sx, int sy)
	{
		bounds.setLocation(sx,sy);
		x=sx;
		y=sy;
	}

	public void setImage(BufferedImage bimg)
	{
		img = bimg;
	}
	
	public void setDelay(int sdelay)
	{
		delay=sdelay;
	}
	
	public void move()
	{
		switch(currD)
		{
		case 0: y-=3; break;
		case 1: x+=3; break;
		case 2: y+=3; break;
		case 3: x-=3; break;
		default: x=-100;y=-100; stop();
		}
	}
	
	public void update()
	{
		setLocation(x,y);
	}
	
	@Override
	public void run() 
	{
		while(running)
		{
			move();
			update();
			
			if(x<0||y<0||x>Game.WIDTH||y>Game.HEIGHT)
				running=false;
				
			try {
			    Thread.sleep(delay);              
			} catch(InterruptedException ex) {Thread.currentThread().interrupt();}
		}
		stop();
	}
}
