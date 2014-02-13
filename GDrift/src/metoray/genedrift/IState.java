package metoray.genedrift;

import metoray.genedrift.render.IGWrapper;

import org.newdawn.slick.GameContainer;

public interface IState {
	
	public void render(GameContainer container, IGWrapper g);
	
	public void logic(GameContainer gc);

}
