package metoray.genedrift;

import metoray.genedrift.render.IGWrapper;

public class TBB { //taskbarbutton, short because of frequent use
	
	protected String imagename;
	protected String description;
	
	public TBB(String image, String desc){
		imagename = image;
		description = desc;
	}
	
	public void render(IGWrapper g, int i, boolean dotooltip){
		g.drawImage(imagename, i*20+2, 2);
		if(dotooltip){
			g.setColor(0, 0, 0, 127);
			g.fillRect(0, 20, 640, 20);
			g.setColor(255, 255, 255);
			g.drawString(description, 2, 22);
		}
	}

}
