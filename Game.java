import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;


public class Game extends Canvas implements Runnable
{
	public static int FPS = 30, WIDTH = 640, HEIGHT = 640, SIZE = 32;
	
	private static Thread gameThread;
	private static boolean running;
	private static Player[] players = new Player[2];
	
	private Level level;
	private static JFrame frame;
	
	/**
	 * constructor
	 */
	public Game()
	{
		init();
		addKeyListener(new FighterKeyListener());
	}
	
	/**
	 * initalize fields
	 */
	public void init()
	{
		players[0] = ChampSelect.getPlayer1();
		players[1] = ChampSelect.getPlayer2();
		
		level = new Level();
	}
	
	/**
	 * starts thread unless already started
	 */
	public synchronized void start()
	{
		if(running)
			return;
		
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	/**
	 * stops thread unless already stopped
	 */
	public synchronized void stop()
	{
		if(!running)
			return;
		
		running=false;
		try {
			gameThread.join();
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 * game loop
	 * @param delta time since last update
	 */
	public void update(double delta)
	{
		level.update(delta);
		for(Player p:players)
			p.update(delta);
	}
	
	/**
	 * render loop
	 */
	public void render()
	{
		BufferStrategy bs = getBufferStrategy();
		if(bs==null)
		{
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, WIDTH, HEIGHT);
		//start render
		level.render(g);
		for(Player p:players)
			p.render(g);
		//end render
		
		g.dispose();
		bs.show();
	}

	/**
	 * runs game thread, updating game loop and render loop
	 * also, math is cool
	 * but sometimes hard
	 * usually hard
	 */
	@Override
	public void run()
	{
		long lastTime = System.nanoTime();
		final double amountOfUpdates = FPS;
		double ns = 1000000000/amountOfUpdates;
		double delta = 0;

		requestFocusInWindow();
		
		while(running)
		{
			long now = System.nanoTime();
			delta += (now-lastTime)/ns;
			
			if(delta>=1)
			{
				update(delta--);
				lastTime = System.nanoTime();
			}
			render();
		}
		stop();
	}
	
	public static Player[] getPlayers()
	{
		return players;
	}
	
	public void setFrame(JFrame f)
	{
		frame = f;
	}
	
	public static JFrame getFrame()
	{
		return frame;
	}
	
	public static void reset()
	{
		running = false;
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
