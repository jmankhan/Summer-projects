import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

//just started learning about hashsets, decided to use a real simple one in this
public class FighterKeyListener implements KeyListener 
{
    // Set of currently pressed keys
    private static final Set<Integer> pressed = new HashSet<Integer>();

    /**
     * get most recent keypress and add it to hashset
     */
    @Override
    public synchronized void keyPressed(KeyEvent e) 
    {
        pressed.add(e.getKeyCode());
        for(Player p:Game.getPlayers())
        	p.setDir(e.getKeyCode());
    }

    /**
     * get most recent key release and remove it from hashset
     */
    @Override
    public synchronized void keyReleased(KeyEvent e) 
    {
        pressed.remove(e.getKeyCode());
        for(Player p:Game.getPlayers())
        	p.stopDir(e.getKeyCode());
    }

    /**
     * unused
     */
    @Override
    public void keyTyped(KeyEvent e) 
    {
    }
}