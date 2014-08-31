import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageManager 
{
	BufferedImage img;
	public ImageManager(String path)
	{
		try {
			img = ImageIO.read(getClass().getClassLoader().getResource(path));
		} catch (IOException e) {e.printStackTrace();}
		
	}
	
	/**
	 * gets subimage of current image in ImageManager at specified location, of standard game size
	 * @param col
	 * @param row
	 * @return sprite from current image at size standard game size
	 */
	public BufferedImage getSprite(int col, int row)
	{
		return img.getSubimage(col*Game.SIZE, row*Game.SIZE, Game.SIZE, Game.SIZE);
	}
	
	
	/**
	 * gets specified dimensions subimage of current image in ImageManager at specified location
	 * @param col x coordinate at main image
	 * @param row y coordinate at main image
	 * @param w width of image
	 * @param h height of image
	 * @return
	 */
	public BufferedImage getSprite(int col, int row, int w, int h)
	{
		return img.getSubimage(col*Game.SIZE, row*Game.SIZE, w, h);
	}
}
