package metoray.genedrift.render;

import java.util.HashMap;

import metoray.genedrift.Colour;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GWrapper implements IGWrapper { //slick-graphic wrapper
	
	public Graphics g;
	
	private HashMap<String,Image> images;
	
	public GWrapper(){
		images = new HashMap<String,Image>();
	}

	/* (non-Javadoc)
	 * @see metoray.genedrift.render.IGWrapper#setBackground(org.newdawn.slick.Color)
	 */
	@Override
	public void setBackground(int r, int g, int b) {
		this.g.setBackground(new Color(r,g,b));
	}
	
	@Deprecated
	public void setColor(Color color) {
		g.setColor(color);
	}
	
	/* (non-Javadoc)
	 * @see metoray.genedrift.render.IGWrapper#setColor(int, int, int)
	 */
	@Override
	public void setColor(int r, int g, int b){
		this.g.setColor(new Color(r,g,b));
	}

	/* (non-Javadoc)
	 * @see metoray.genedrift.render.IGWrapper#drawRect(int, int, int, int)
	 */
	@Override
	public void drawRect(int x, int y, int w, int h) {
		g.drawRect(x, y, w, h);
	}

	/* (non-Javadoc)
	 * @see metoray.genedrift.render.IGWrapper#fillRect(int, int, int, int)
	 */
	@Override
	public void fillRect(int x, int y, int w, int h) {
		g.fillRect(x, y, w, h);
	}

	/* (non-Javadoc)
	 * @see metoray.genedrift.render.IGWrapper#drawString(java.lang.String, int, int)
	 */
	@Override
	public void drawString(String string, int x, int y) {
		g.drawString(string, x, y);
	}

	/* (non-Javadoc)
	 * @see metoray.genedrift.render.IGWrapper#drawLine(int, int, int, int)
	 */
	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
	}

	/* (non-Javadoc)
	 * @see metoray.genedrift.render.IGWrapper#fillOval(int, int, int, int)
	 */
	@Override
	public void fillOval(int x, int y, int w, int h) {
		g.fillOval(x, y, w, h);
	}
	
	/* (non-Javadoc)
	 * @see metoray.genedrift.render.IGWrapper#drawImage(java.lang.String, int, int)
	 */
	
	@Override
	public void drawImage(String name, int x, int y, int w, int h, int r, int g, int b){
		if(name==""){return;};
		if(images.containsKey(name)){
			images.get(name).draw(x, y, w, h, new Color(r,g,b));
		}else{
			try {
				images.put(name, new Image(name + ".png"));
				drawImage(name, x, y);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void drawImage(String name, int x, int y){
		if(name==""){return;};
		if(images.containsKey(name)){
			images.get(name).draw(x, y);
		}else{
			try {
				images.put(name, new Image(name + ".png"));
				drawImage(name, x, y);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see metoray.genedrift.render.IGWrapper#setColor(metoray.genedrift.Colour)
	 */
	@Override
	public void setColor(Colour c) {
		this.setColor(c.r, c.g,c.b);
	}

	@Override
	public void setGraphics(Object o) {
		if(o instanceof Graphics){
			g = (Graphics)o;
		}
	}

	@Override
	public void setColor(int r, int g, int b, int a) {
		this.g.setColor(new Color(r,g,b,a));
		
	}
	

}
