package metoray.genedrift.historyevent;

import java.util.HashMap;
import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import metoray.genedrift.Organism;
import metoray.genedrift.components.HistoryComponent;
import metoray.genedrift.render.IGWrapper;

public class GenesisEvent extends Event implements ICreate {
	
	//private HashMap<Integer, Organism> organisms;
	
	public GenesisEvent(Organism[] organisms, HistoryComponent history){
		this.organisms = new HashMap<Integer, Organism>();
		for(int i=0; i<organisms.length; i++){
			this.organisms.put(history.getEmptyColum(i), organisms[i]);
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g, int y) {
	}
	
	@Override
	public boolean isOnColum(int colum) {
		return organisms.keySet().contains(colum);
	}

	@Override
	public HashMap<Integer, Organism> getCreatedOrganisms() {
		return organisms;
	}

	@Override
	public void render(GameContainer gc, IGWrapper g, int x, int y, HistoryComponent history) {
		int ry = HistoryComponent.posToY(y);
		for(Entry<Integer,Organism> e: organisms.entrySet()){
			int colum = e.getKey();
			Organism o = e.getValue();
			int rx = HistoryComponent.posToX(colum);
			
			int span = history.getNextDeath(o, y+history.scrolly)-(y+history.scrolly);
			o.renderAt(rx, ry, 20, gc, g);
			g.drawLine(rx, ry, rx, history.posToYScrl((y+history.scrolly)+span, history));
		}
		g.drawImage("spark", HistoryComponent.posToX(-1)-16, ry-16);
		
	}

}
