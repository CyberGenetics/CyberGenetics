package metoray.genedrift;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import metoray.genedrift.components.Component;
import metoray.genedrift.components.EventViewComponent;
import metoray.genedrift.components.HistoryComponent;
import metoray.genedrift.components.ListComponent;
import metoray.genedrift.components.TaskbarComponent;
import metoray.genedrift.components.WorldComponent;
import metoray.genedrift.render.IGWrapper;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

public class MainState implements IState {
	
	public boolean clicked;
	private HistoryComponent history;
	private TaskbarComponent taskbar;
	private WorldComponent world;
	private EventViewComponent evc;
	public static int scroll;
	Image historyImg;
	
	public MainState(File file){
		history = new HistoryComponent();
		evc = new EventViewComponent();
		if(file.getName().endsWith(".lst")){
			world = new ListComponent(file);
		}else{
			world = new WorldComponent(file);
		}
		taskbar = new TaskbarComponent(history,world);
	}

	@Override
	public void render(GameContainer gc, IGWrapper g) {
		world.cRender(gc, g);
		history.cRender(gc,g);
		evc.cRender(gc, g);
		taskbar.cRender(gc, g);
	}

	@Override
	public void logic(GameContainer gc) {
		history.logic(gc);
		Input in = gc.getInput();
		if(in.isKeyPressed(Input.KEY_ESCAPE)){
			System.exit(0);
		}
		int mx = in.getMouseX();
		int my = in.getMouseY();
		if(in.isMousePressed(0)){
			if(my>20){
				if(history.getVisible()){
					history.clickat(mx, my);
				}else if(world.getVisible()){
					world.clickat(mx, my);
				}
			}else{
				taskbar.clickat(mx, my);
			}
		}
		if(in.isMousePressed(1)){
			world.rclickat(mx, my);
		}
		if(in.isKeyPressed(Input.KEY_DELETE)){
			world.delete();
		}
		world.logic(taskbar.runspeed, gc);
		
	}

}
