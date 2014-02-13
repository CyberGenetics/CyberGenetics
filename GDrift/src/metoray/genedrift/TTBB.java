package metoray.genedrift;

import metoray.genedrift.render.IGWrapper;

public class TTBB extends TBB {
	
	public boolean state;
	
	public TTBB(String image, String desc, boolean startstate) {
		super(image, desc);
		state = startstate;
	}

	public void toggle(){
		state = !state;
	}
	
	public void render(IGWrapper g, int i, boolean dotooltip){
		g.setColor(0, 0, 255);
		if(state){
			g.fillRect(i*20+1, 1, 18, 18);
		}
		super.render(g, i, dotooltip);
	}
	
	

}
