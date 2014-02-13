package metoray.genedrift.historyevent;

import java.util.HashMap;

import metoray.genedrift.Organism;
import metoray.genedrift.components.HistoryComponent;
import metoray.genedrift.render.IGWrapper;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public abstract class Event {
	
	public HashMap<Integer, Organism> organisms;
	
	public Event(){
		organisms = new HashMap<Integer, Organism>();
	}
	
	public boolean isOnColum(int colum) {
		return organisms.keySet().contains(colum);
	}

	public abstract void render(GameContainer gc, Graphics g, int i);
	
	
	public int[] getColums() {
		int[] colums = new int[organisms.size()];
		int i = 0;
		for(int colum: organisms.keySet()){
			colums[i] = colum;
			i++;
		}
		return colums;
	}

	public abstract void render(GameContainer gc, IGWrapper g, int x, int y, HistoryComponent history);

}
