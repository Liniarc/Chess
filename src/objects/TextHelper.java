package objects;

import java.awt.Graphics2D;

public class TextHelper
{

	public static int textWidth(String s, Graphics2D g)
	{
		return g.getFontMetrics().stringWidth(s);
	}
	
	public static int textHeight(Graphics2D g)
	{
		return g.getFontMetrics().getHeight()/2;
	}
}
