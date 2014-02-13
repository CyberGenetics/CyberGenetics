package metoray.genedrift.components;

import java.util.Map.Entry;

import metoray.genedrift.GeneRenderUtil;
import metoray.genedrift.Organism;
import metoray.genedrift.historyevent.BirthEvent;
import metoray.genedrift.historyevent.Event;
import metoray.genedrift.render.IGWrapper;

import org.newdawn.slick.GameContainer;

public class EventViewComponent extends Component {
	
	public static EventViewComponent instance;
	
	public Event e;
	
	public EventViewComponent(){
		instance = this;
	}

	@Override
	protected void render(GameContainer gc, IGWrapper g) {
		g.setBackground(20, 20, 20);
		g.setColor(30, 30, 30);
		for(int i=0; i<gc.getWidth()/32;i++){
			for(int j=0; j<gc.getHeight()/32; j++){
				if((i+j)%2==0){
					g.fillRect(i*32, j*32+20, 32, 32);
				}
			}
		}
		if(e instanceof BirthEvent){
			BirthEvent be = (BirthEvent) e;
			Organism p1 = be.p1;
			Organism p2 = be.p2;
			Organism c = be.c;
			if(c==null){
				System.out.println("Fuck this shit.");
				System.exit(666);
			}
			
			g.setColor(0, 0, 0);
			g.fillRect(16+48, 36, 64, 64);
			p1.renderAt(16+48+32, 36+32, 32, gc, g);
			
			g.setColor(0, 0, 0);
			g.fillRect(16, 36+80, 160, 428-80);
			g.setColor(255, 255, 255);
			g.drawString("Parent 1:", 16+8, 16+28+80);
			GeneRenderUtil.renderGenes(p1, g, 16, 68+80);
			
			g.setColor(0, 0, 0);
			g.fillRect(192+48, 36, 64, 64);
			p2.renderAt(192+48+32, 36+32, 32, gc, g);
			
			g.setColor(0, 0, 0);
			g.fillRect(192, 36+80, 160, 428-80);
			g.setColor(255, 255, 255);
			g.drawString("Parent 2:", 16*12+8, 16+28+80);
			GeneRenderUtil.renderGenes(p2, g, 192, 68+80);
			
			g.setColor(0, 0, 0);
			g.fillRect(gc.getWidth()-16*11+48, 36, 64, 64);
			c.renderAt(gc.getWidth()-16*11+48+32, 36+32, 32, gc, g);
			
			g.setColor(0, 0, 0);
			g.fillRect(gc.getWidth()-16*11, 36+80, 160, 428-80);
			g.setColor(255, 255, 255);
			g.drawString("Child:", gc.getWidth()-16*11+8, 16+28+80);
			GeneRenderUtil.renderGenes(c, g, gc.getWidth()-16*11, 68+80, true);
		}
		
	}

	@Override
	public void clickat(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean getVisible(){
		if(HistoryComponent.instance!=null){
			return (e!=null)&&HistoryComponent.instance.getVisible();
		}
		return e!=null;
	}

}
