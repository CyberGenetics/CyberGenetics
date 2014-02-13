package metoray.genedrift;

import java.io.File;

import javax.swing.JFrame;

import metoray.genedrift.render.GWrapper;
import metoray.genedrift.render.IGWrapper;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class GDrift implements Game {
	
	public static GDrift instance;
	
	private static int w = 640;
	private static int h = 480;
	private IState state;
	private IGWrapper gwrap;
	
	public static void main(String[] args){
		System.setProperty("org.lwjgl.librarypath",new File("natives").getAbsolutePath());
		instance = new GDrift();
		AppGameContainer container;//AppGameContainer
		try {
			container = new AppGameContainer(instance);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void startRendering(GameContainer container, Graphics g){
		gwrap.setGraphics(g);
		state.render(container, gwrap);
	}
	
	public static int getW(){
		return w;
	}
	
	public static int getH(){
		return h;
	}

	@Override
	public boolean closeRequested() {
		return Display.isCloseRequested();
	}

	@Override
	public String getTitle() {
		return "Cyber Genetics";
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		gwrap = new GWrapper(); 
		gc.setShowFPS(false);
		state = new StartState();
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		startRendering(container,g);
	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		state.logic(gc);
		if(closeRequested()){
			gc.exit();
		}
	}
	
	public void setState(IState state){
		this.state = state;
	}

}
