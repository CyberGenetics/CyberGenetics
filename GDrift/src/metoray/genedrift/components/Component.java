package metoray.genedrift.components;

import metoray.genedrift.render.IGWrapper;

import org.newdawn.slick.GameContainer;

public abstract class Component {
	
	private boolean visible;
	
	public Component(){
		visible = true;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}
	
	public boolean getVisible(){
		return visible;
	}
	
	final public void cRender(GameContainer gc, IGWrapper g){
		if(!getVisible()){
			return;
		}
		this.render(gc, g);
	}
	
	protected abstract void render(GameContainer gc, IGWrapper g);
	
	public abstract void clickat(int x, int y);

}
