package metoray.genedrift.historyevent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import metoray.genedrift.Organism;
import metoray.genedrift.components.HistoryComponent;
import metoray.genedrift.render.IGWrapper;

public class BirthEvent extends Event implements ICreate {
	
	public Organism p1;
	public Organism p2;
	public Organism c;
	public int p1i;
	public int p2i;
	public int ci;
	
	public BirthEvent(Organism p1, Organism p2, Organism child, HistoryComponent history){
		//this.organisms = new HashMap<Integer, Organism>();
		ArrayList<Organism> list = new ArrayList<Organism>();
		list.add(child);
		list.add(p1);
		list.add(p2);
		this.organisms = history.getEntryFor(list);
		this.p1 = p1;
		this.p2 = p2;
		this.c = child;
		p1i = history.getColumFromOrg(p1);
		p2i = history.getColumFromOrg(p2);
		ci = -1;
		for(Entry<Integer,Organism> e: organisms.entrySet()){
			if(e.getValue().matches(child)){
				ci = e.getKey();
			}
		}
	}

	@Override
	@Deprecated
	public void render(GameContainer gc, Graphics g, int y) {
	}
	
	@Override
	public int[] getColums() {
		int[] colums = new int[organisms.size()];
		int i = 0;
		for(int colum: organisms.keySet()){
			colums[i] = colum;
			i++;
		}
		return super.getColums();
	}
	
	@Override
	public boolean isOnColum(int colum) {
		return organisms.keySet().contains(colum);
	}

	@Override
	public HashMap<Integer, Organism> getCreatedOrganisms() {
		HashMap<Integer, Organism> children = new HashMap<Integer, Organism>();
		for(Entry<Integer, Organism> e: organisms.entrySet()){
			if(!(e.getValue()==p1||e.getValue()==p2)){
				children.put(e.getKey(), e.getValue());
			}
		}
		return children;
	}

	@Override
	public void render(GameContainer gc, IGWrapper g, int x, int y, HistoryComponent history) {
		boolean blink = (System.nanoTime()%50000000)>45000000;
		int ry = HistoryComponent.posToY(y);
		int mincolum = Collections.min(organisms.keySet());
		int maxcolum = Collections.max(organisms.keySet());
		g.setColor(0,0,255);
		if(blink){
			g.setColor(127, 127, 255);
		}
		g.drawLine(HistoryComponent.posToX(mincolum), ry, HistoryComponent.posToX(maxcolum), ry);
		int rx = HistoryComponent.posToX(ci);
		
		int span = history.getNextDeath(c, y+history.scrolly)-(y+history.scrolly);
		c.renderAt(rx, ry, 20, gc, g);
		g.drawLine(rx, ry, rx, history.posToYScrl((y+history.scrolly)+span, history));
		
		int p1x = HistoryComponent.posToX(p1i);
		int p2x = HistoryComponent.posToX(p2i);
		g.setColor(0,255,255);
		g.drawString(span+"", rx, ry);
		if(blink){
			g.setColor(127, 255, 255);
		}
		g.fillOval(p1x-2, ry-2, 4, 4);
		g.fillOval(p2x-2, ry-2, 4, 4);
		g.drawImage("egg", HistoryComponent.posToX(-1)-16, ry-16);
	}
	
	public boolean matches(BirthEvent e){
		if(p1.matches(e.p1)&&p2.matches(e.p2)&&c.matches(e.c)){
			return true;
		}
		if(p1.matches(e.p2)&&p2.matches(e.p1)&&c.matches(e.c)){
			return true;
		}
		return false;
	}

}
