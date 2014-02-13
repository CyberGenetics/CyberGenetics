package metoray.genedrift.render;

import metoray.genedrift.Colour;

import org.newdawn.slick.Color;

public interface IGWrapper {
	
	public void setGraphics(Object o);

	public void setBackground(int r, int g, int b);

	public void setColor(int r, int g, int b);

	public void drawRect(int x, int y, int w, int h);

	public void fillRect(int x, int y, int w, int h);

	public void drawString(String string, int x, int y);

	public void drawLine(int x1, int y1, int x2, int y2);

	public void fillOval(int x, int y, int w, int h);

	public void drawImage(String name, int x, int y);

	public void setColor(Colour c);

	public void setColor(int r, int g, int b, int a);

	public void drawImage(String name, int x, int y, int w, int h, int r, int g, int b);

}