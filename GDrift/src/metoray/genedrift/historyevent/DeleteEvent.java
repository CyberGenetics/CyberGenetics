package metoray.genedrift.historyevent;

import java.util.HashMap;
import java.util.Map.Entry;

import metoray.genedrift.Organism;
import metoray.genedrift.components.HistoryComponent;
import metoray.genedrift.render.IGWrapper;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class DeleteEvent extends Event implements IDestroy {
	
	public DeleteEvent(HistoryComponent history, Organism... organisms){
		for(int i=0; i<organisms.length;i++){
			this.organisms.put(history.getColumFromOrg(organisms[i]), organisms[i]);
		}
	}

	@Override
	public HashMap<Integer, Organism> getDestroyedOrganisms() {
		return organisms;
	}

	@Override
	public void render(GameContainer gc, Graphics g, int i) {
		
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
		g.drawImage("delete", HistoryComponent.posToX(-1)-16, ry-16);
		
	}
	
	

}
