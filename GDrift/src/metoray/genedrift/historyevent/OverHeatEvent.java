package metoray.genedrift.historyevent;

import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;

import metoray.genedrift.Organism;
import metoray.genedrift.components.HistoryComponent;
import metoray.genedrift.render.IGWrapper;

public class OverHeatEvent extends DeleteEvent {

	public OverHeatEvent(HistoryComponent history, Organism[] organisms) {
		super(history, organisms);
	}
	
	@Override
	public void render(GameContainer gc, IGWrapper g, int x, int y, HistoryComponent history) {
		int ry = HistoryComponent.posToY(y);
		for(Entry<Integer,Organism> e: organisms.entrySet()){
			int colum = e.getKey();
			int rx = HistoryComponent.posToX(colum);
			g.setColor(255,0,0);
			g.drawLine(rx-12, ry-12, rx+12, ry+12);
			g.drawLine(rx-12, ry+12, rx+12, ry-12);
		}
		g.drawImage("freeze", HistoryComponent.posToX(-1)-16, ry-16);
		
	}

}
