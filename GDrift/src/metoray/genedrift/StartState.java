package metoray.genedrift;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import metoray.genedrift.render.IGWrapper;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class StartState implements IState {
	
	List<File> fileList;
	
	public StartState(){
		fileList = new ArrayList<File>();
		File directory = new File("data");
		for(File f: directory.listFiles()){
			if(f.getName().endsWith("wrld")){
				fileList.add(f);
			}
			if(f.getName().endsWith("lst")){
				fileList.add(f);
			}
		}
	}

	@Override
	public void render(GameContainer gc, IGWrapper g) {
		g.setBackground(20, 20, 20);
		for(int i=0;i<fileList.size();i++){
			if(i%2==0){
				g.setColor(40, 40, 40);
			}else{
				g.setColor(20, 20, 20);
			}
			g.fillRect(0, i*32, gc.getWidth(), 32);
			g.setColor(0,255,0);
			g.drawString(fileList.get(i).getPath(), 8, i*32+8);
		}
	}

	@Override
	public void logic(GameContainer gc) {
		Input in = gc.getInput();
		if(in.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			int index = in.getMouseY()/32;
			if(index<fileList.size()){
				GDrift.instance.setState(new MainState(fileList.get(index)));
			}
		}
	}

}
