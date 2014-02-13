package metoray.genedrift.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

import metoray.genedrift.Allele;
import metoray.genedrift.AllelePair;
import metoray.genedrift.Colour;
import metoray.genedrift.EventExecutionBus;
import metoray.genedrift.GeneHelper;
import metoray.genedrift.GeneRenderUtil;
import metoray.genedrift.Organism;
import metoray.genedrift.render.IGWrapper;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class WorldComponent extends Component {
	
	public WorldComponent instance;
	public List<Organism> organismList;
	public List<Organism> selectedOrganisms;
	protected Organism selected = null;
	protected Organism alsoselected = null;
	private Random rand;

	public WorldComponent(File file) {
		super();
		instance = this;
		this.organismList = new ArrayList<Organism>();
		rand = new Random();
		this.parseFile(file);
	}
	
	public WorldComponent() {
		super();
		instance = this;
	}

	public void logic(int speed, GameContainer gc){
		Iterator<Organism> it = organismList.iterator();
		while(it.hasNext()){
			Organism o = it.next();
			for(int i=0; i<speed; i++){
				o.logic(rand);
			}
			if(o.isDead()){
				if(this.getSelected()==o){
					this.select(null);
				}
				it.remove();
			}
		}
	}
	
	@Override
	public boolean getVisible(){
		if(HistoryComponent.instance.getVisible()){
			return false;
		}else{
			return super.getVisible();
		}
	}

	@Override
	protected void render(GameContainer gc, IGWrapper g) {
		g.setBackground(63,200,0);
		for(int i=0; i<organismList.size(); i++){
			organismList.get(i).render(gc, g);
		}
		if(selected!=null){
			int x = getSelected().x*32+16;
			int y = getSelected().y*32+36;
			int s = getSelected().getSize(32);
			g.setColor(0,0,255);
			g.drawRect(x-s-4, y-s-4, s*2+8, s*2+8);
			g.setColor(0,0,0);
			g.fillRect(gc.getWidth()-160, 20, 160, gc.getHeight()-20);
			g.setColor(255,255,255);
			g.drawString("Generation: "+selected.getGeneration(), gc.getWidth()-160+8, 28);
			GeneRenderUtil.renderGenes(selected, g, gc.getWidth()-160, 48);
		}
		if(alsoselected!=null){
			int x = getAlsoSelected().x*32+16;
			int y = getAlsoSelected().y*32+36;
			int s = getAlsoSelected().getSize(32);
			g.setColor(255,0,0);
			g.drawRect(x-s-4, y-s-4, s*2+8, s*2+8);
		}
	}
	
	public boolean bothSelected(){
		return (selected!=null)&&(alsoselected!=null);
	}

	public Organism getSelected() {
		return selected;
	}
	
	public Organism getAlsoSelected() {
		return alsoselected;
	}

	@Override
	public void clickat(int x, int y) {
		selectOnLoc(x,y);
	}
	
	public void rclickat(int x, int y) {
		alsoSelectOnLoc(x, y);
		//Organism o1 = getSelected();
		//selectOnLoc(x, y);
		//if(o1 != null && this.selected != null){
		//	this.combine(o1, this.selected, 0.5f);
		//}
	}
	
	public void combine(float mutachance){
		if(!bothSelected()){
			return;
		}
		Organism o1 = getSelected();
		Organism o2 = getAlsoSelected();
		int x = (o1.x + o2.x)/2;
		int y = (o1.y + o2.y)/2;
		Organism newOrg = new Organism(x,y,Math.max(o1.getGeneration(), o2.getGeneration()));
		newOrg.genes = GeneHelper.combine(o1.genes, o2.genes);
		newOrg.setGeneration(Math.max(o1.getGeneration(), o2.getGeneration())+1);
		organismList.add(newOrg);
		EventExecutionBus.birth(HistoryComponent.instance, true, o1, o2, newOrg);
	}
	
	private void selectOnLoc(int x, int y){
		for(Organism o: organismList){
			int a = (o.x*32+16) - x;
			int b = (o.y*32+36) - y;
			int c = o.getSize(32);
			if((Math.pow(a,2)+Math.pow(b,2))<(Math.pow(c, 2))){
				select(o);
				return;
			}
		}
		select(null);
		
	}
	
	private void alsoSelectOnLoc(int x, int y){
		for(Organism o: organismList){
			int a = (o.x*32+16) - x;
			int b = (o.y*32+36) - y;
			int c = o.getSize(32);
			if((Math.pow(a,2)+Math.pow(b,2))<(Math.pow(c, 2))){
				alsoSelect(o);
				return;
			}
		}
		alsoSelect(null);
		
	}
	
	public void select(Organism organism){
		this.selected = organism;
	}
	
	@Deprecated
	public void alsoSelect(Organism organism){
		this.alsoselected = organism;
	}
	
	public void delete(){
		if(getSelected()!=null){
			EventExecutionBus.delete(HistoryComponent.instance, "nothing", getSelected());
			getSelected().die("delete");
		}
	}
	
	public void parseFile(File f){
		if(f.exists()){
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(f));
				String line;
				Map<String,AllelePair> genes = null;
				String mode = "NONE";
				int x = -555;
				int y = -555;
				while ((line = br.readLine()) != null) {
					String[] args = line.split(" ");
					System.out.println(line);
					System.out.println(args[0].length());
					if(args[0].equals("ORGANISM")){
						mode = "ORGANISM";
						String[] coords = args[1].split(",");
						x = Integer.valueOf(coords[0]);
						y = Integer.valueOf(coords[1]);
						genes = new HashMap<String,AllelePair>();
						continue;
					}
					if(args[0].equals("DONE")){
						mode = "NONE";
						Organism o = new Organism(x, y, 0);
						organismList.add(o);
						EventExecutionBus.genesis(HistoryComponent.instance, o);
						o.genes = genes;
						continue;
					}
					if(mode.equals("ORGANISM")&&args.length==6) {
						if(genes == null){
							continue;
						}
						String type = args[0];
						String gname = args[1];
						boolean dominance1 = args[2].equals("d");
						String val1 = args[3];
						boolean dominance2 = args[4].equals("d");
						String val2 = args[5];
						if(type.equals("i")){
							genes.put(gname, new AllelePair(new Allele(dominance1, Integer.valueOf(val1)), new Allele(dominance2, Integer.valueOf(val2))));
						}
						if(type.equals("f")){
							genes.put(gname, new AllelePair(new Allele(dominance1, Float.valueOf(val1)), new Allele(dominance2, Float.valueOf(val2))));
						}
						if(type.equals("c")){
							String[] v1 = val1.split(",");
							String[] v2 = val2.split(",");
							Colour c1 = new Colour(v1[0],v1[1],v1[2]);
							Colour c2 = new Colour(v2[0],v2[1],v2[2]);
							genes.put(gname, new AllelePair(new Allele(dominance1, c1), new Allele(dominance2, c2)));
						}
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void scrlUp() {
		// NOTHGING
		
	}

	public void scrlDn() {
		// NOPE
		
	}

}
