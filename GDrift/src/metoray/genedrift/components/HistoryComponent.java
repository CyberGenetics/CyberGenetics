package metoray.genedrift.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;

import metoray.genedrift.Organism;
import metoray.genedrift.historyevent.Event;
import metoray.genedrift.historyevent.ICreate;
import metoray.genedrift.historyevent.IDestroy;
import metoray.genedrift.render.IGWrapper;

public class HistoryComponent extends Component {
	
	public static HistoryComponent instance;
	public List<Event> history;
	private int scrollx;
	public int scrolly;
	
	public HistoryComponent(){
		history = new ArrayList<Event>();
		scrolly = 0;
		this.setVisible(false);
		instance = this;
	}
	
	@Override
	public void render(GameContainer gc, IGWrapper g){
		if(EventViewComponent.instance.getVisible()){
			return;
		}
		g.setColor(10,10,10);
		g.fillRect(0, 20, gc.getWidth(), gc.getHeight()-20);
		g.setColor(5,5,5);
		for(int i=0; i<gc.getWidth();i+=64){
			g.fillRect(i, 20, 32, gc.getHeight()-20);
		}
		for(int i=0; i<this.getColumCount(); i++){
			Organism o = getActiveOrg(scrolly-1, i);
			if(o!=null){
				o.renderAt(posToX(i), 20+16, 20, gc, g);
				int deathy = this.getNextDeath(o,scrolly)-scrolly;
				g.drawLine(posToX(i), 36, posToX(i), 32+20+16+deathy*32);
			}
		}
		for(int i=0; i<16; i++){
			if((scrolly+i)<history.size()){
				history.get(scrolly+i).render(gc, g, scrollx, i, this);
			}
			if(scrolly+i==history.size()){
				int ry = 20+48+i*32;
				g.setColor(0,255,0);
				g.drawLine(posToX(0)-16, ry, gc.getWidth(), ry);
			}
		}
		g.setColor(0,255,0);
		g.drawLine(posToX(0)-16, 0, posToX(0)-16, gc.getHeight());
	}
	
	public void logic(GameContainer gc){

	}
	
	public void scrlUp(){
		scrolly--;
		if(scrolly<0){
			scrolly=0;
		}
	}
	
	public void scrlDn(){
		scrolly++;
	}

	public void addEvent(Event event) {
		history.add(event);
		if(history.size()==(scrolly+12)){
			scrolly++;
		}
	}
	
	@Deprecated
	public int getEmptyColum(int ignoreplaces) {
		return getColumCount();
		/**
		boolean[] emptyColums = new boolean[getColumCount()];
		for(int i=0; i<emptyColums.length; i++){
			emptyColums[i] = true;
		}
		for(int i = 0; i<history.size(); i++){
			Event e = history.get(i);
			if(e instanceof ICreate){
				for(int j=0; j<emptyColums.length; j++){
					if(e.isOnColum(j)){
						emptyColums[j] = false;
					}
				}
			}
			if(e instanceof IDestroy){
				for(int j=0; j<emptyColums.length; j++){
					if(e.isOnColum(j)){
						emptyColums[j] = true;
					}
				}
			}
		}
		for(int i=0; i<emptyColums.length; i++){
			if(emptyColums[i]){
				if(ignoreplaces==0){
					return i;
				}else{
					ignoreplaces--;
				}
			}
		}
		return getColumCount()+ignoreplaces;
		**/
		
	}
	
	public HashMap<Integer, Organism> getEntryFor(List<Organism> list){
		HashMap<Integer,Organism> entry = new HashMap<Integer,Organism>();
		int ignoredplaces = 0;
		for(Organism o: list){
			int[] index = getColumFor(o, ignoredplaces);
			if(index[1]==1){
				ignoredplaces++;
			}
			entry.put(index[0],o);
		}
		return entry;
	}
	
	public int[] getColumFor(Organism org, int ignore){
		int index = getColumFromOrg(org);
		System.out.println(index);
		if(index==-1){
			index=getEmptyColum(ignore);
			return new int[]{index,1};
		}
		return new int[]{index,0};
	}
	
	public int getColumFromOrg(Organism organism){
		for(Event e: history){
			if(e instanceof ICreate){
				HashMap<Integer,Organism> map = ((ICreate)e).getCreatedOrganisms();
				for(Entry<Integer,Organism> f: map.entrySet()){
					if(f.getValue().matches(organism)){
						System.out.println("::"+f.getKey());
						return f.getKey();
					}
				}
			}
		}
		return -1;
	}
	
	public int getBirthIndex(Organism organism, int y){
		for(int i=y;i<history.size();i++){
			Event e = history.get(i);
			if(e instanceof ICreate){
				if(((ICreate) e).getCreatedOrganisms().containsValue(organism)){
					return i;
				}
			}
		}
		return -1;
	}
	
	public int getLastBirth(Organism organism, int y){
		for(int i=y;i>0;i++){
			Event e = history.get(i);
			if(e instanceof ICreate){
				if(((ICreate) e).getCreatedOrganisms().containsValue(organism)){
					return i;
				}
			}
		}
		return -1;
	}
	
	public int getDeathIndex(Organism organism, int y){
		for(int i=y;i<history.size();i++){
			Event e = history.get(i);
			if(e instanceof IDestroy){
				if(((IDestroy) e).getDestroyedOrganisms().containsValue(organism)){
					return i;
				}
			}
		}
		return history.size();
	}
	
	public int getNextDeath(Organism organism, int y){
		for(int i=y;i<history.size();i++){
			Event e = history.get(i);
			if(e instanceof IDestroy){
				Collection<Organism> olist = ((IDestroy) e).getDestroyedOrganisms().values();
				for(Organism o: olist){
					if(o.matches(organism)){
						return i;
					}
				}
			}
		}
		return history.size();
	}
	
	public int getNextDeathOrBirth(Organism organism, int y){
		for(int i=y;i<history.size();i++){
			Event e = history.get(i);
			if(e instanceof IDestroy){
				if(((IDestroy) e).getDestroyedOrganisms().containsValue(organism)){
					return i;
				}
			}
			if(e instanceof ICreate){
				if(((ICreate) e).getCreatedOrganisms().containsValue(organism)){
					return i;
				}
			}
		}
		return history.size();
	}
	
	public int getLifeSpan(Organism organism, int y){
		if(!organism.isDead()){
			return history.size()-this.getBirthIndex(organism,y);
		}
		return this.getDeathIndex(organism,y)-this.getBirthIndex(organism,y);
	}
	
	public Organism getActiveOrg(int index, int colum){
		if(index<0){
			return null;
		}
		index++;
		Organism organism = null;
		if(index>history.size()){index=history.size();}
		for(int i=0; i<index; i++){
			Event e = history.get(i);
			if(e instanceof ICreate){
				if(((ICreate) e).getCreatedOrganisms().get(colum)!=null){
					organism = ((ICreate) e).getCreatedOrganisms().get(colum);
				}
			}
			if(e instanceof IDestroy){
				if(((IDestroy)e).getDestroyedOrganisms().containsValue(organism)){
					organism = null;
				}
				
			}
		}
		return organism;
	}

	private int getColumCount() {
		int columCount = 0;
		for(Event e: history){
			for(int colum: e.getColums()){
				if((colum+1)>columCount){
					columCount = colum+1;
				}
			}
		}
		return columCount;
	}
	
	private int visibleColums(GameContainer gc){
		return gc.getWidth()/32-1;
	}
	
	public static int posToX(int xpos){
		return (xpos+1)*32+16;
	}
	
	public static int xToPos(int x){
		return x/32-1;
	}
	
	public static int posToY(int ypos){
		return ypos*32+68;
	}
	
	public int posToYScrl(int ypos, HistoryComponent hist){
		return (ypos-scrolly)*32+68;
	}
	
	public static int yToPos(int y){
		return (y-20-32)/32;
	}

	@Override
	public void clickat(int x, int y) {
		if(y>(20+32)){
			int posx = xToPos(x);
			int posy = scrolly+yToPos(y);
			if(posx==-1){
				if(posy<history.size()){
					EventViewComponent.instance.e = history.get(posy);
				}
			}
		}
		
	}

}
