package metoray.genedrift;

import metoray.genedrift.render.IGWrapper;

public class TBBVal extends TBB { //taskbarbutton, short because of frequent use
	
	public Object value;
	public int modifier;
	
	public TBBVal(String image, String desc, Object value, int modifier) {
		super(image, desc);
		this.value = value;
		this.modifier = modifier;
	}

	public void render(IGWrapper g, int i, boolean dotooltip){
		g.drawImage(imagename, i*20+2, 2);
		if(dotooltip){
			g.setColor(0, 0, 0, 127);
			g.fillRect(0, 20, 640, 20);
			g.setColor(255, 255, 255);
			g.drawString(description + getValue(), 2, 22);
		}
	}
	
	public void increase(int i){
		if(value instanceof TBBVal){
			((TBBVal) value).increase(i);
		}
		if(value instanceof Integer){
			value = getValue()+i;
		}
	}
	
	public int getValue(){
		if(value instanceof TBBVal){
			return ((TBBVal) value).getValue();
		}
		if(value instanceof Integer){
			return (Integer) value;
		}
		return -1;
	}

}
