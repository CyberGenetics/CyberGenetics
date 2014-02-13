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
import metoray.genedrift.CombinationHelper;
import metoray.genedrift.EventExecutionBus;
import metoray.genedrift.GeneHelper;
import metoray.genedrift.GeneRenderUtil;
import metoray.genedrift.Organism;
import metoray.genedrift.render.IGWrapper;
import metoray.util.Out;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class ListComponent extends WorldComponent {
	
	public List<Organism> organismList;
	private Random rand;
	private int scroll;
	private int sindex;
	private int sindex2;
	public int timer = 0;
	public int hoverIndex = -1;
	private int lastmousex = 0;
	private int lastmousey = 0;

	public ListComponent(File file) {
		this();
		organismList = new ArrayList<Organism>();
		selectedOrganisms = new ArrayList<Organism>();
		rand = new Random();
		this.parseFile2(file);
		sindex = -1;
		sindex2 = -1;
	}
	
	public ListComponent() {
		super();
	}

	public void logic(int speed, GameContainer gc){
		Iterator<Organism> it = organismList.iterator();
		while(it.hasNext()){
			Organism o = it.next();
			for(int i=0; i<speed; i++){
				o.llogic(rand);
			}
			if(o.isDead()){
				if(this.getSelected()==o){
					this.select(null);
				}
				it.remove();
			}
		}
		for(int i=0; i<speed; i++){
			tickRandom();
		}
		Input in = gc.getInput();
		if(in.getMouseX()!=lastmousex||in.getMouseY()!=lastmousey){
			lastmousex = in.getMouseX();
			lastmousey = in.getMouseY();
			if(lastmousey>20&&lastmousex<gc.getWidth()-160){
				int newindex = (in.getMouseY()-20+scroll)/40;
				if(newindex<organismList.size()&&newindex>=0){
					hoverIndex = newindex;
				}else{
					hoverIndex = -1;
				}
			}
		}
	}
	
	public void tickRandom(){
		timer++;
		if(timer>=20){
			timer=0;
			if(TaskbarComponent.instance.plusplus.state){
				this.combine(0f, organismList);
			}
			if(TaskbarComponent.instance.die.state){
				for(Organism o: organismList){
					double chance = 0.000005d;
					double ammount = o.ammount;
					double tempDmg = 0.0d;
					double temp = TaskbarComponent.instance.hot.getValue();
					double maxTemp = o.maxTemp();
					double minTemp = o.minTemp();
					double overHeat = Math.max(0, (temp-maxTemp)*0.075d);
					chance += overHeat;
					double freeze = Math.max(0, -(temp-minTemp)*0.075d);
					chance += freeze;
					double am1 = Math.floor(ammount*chance);
					double am2 = Math.ceil(ammount*chance);
					o.ammount-=am1;
					if(am2>am1){
						if(rand.nextDouble()<chance){
							o.ammount--;
						}
					}
					if(o.ammount<=0){
						if(overHeat>freeze){
							o.die("heat");
						}else{
							o.die("freeze");
						}
					}
				}
			}
		}
	}

	@Override
	protected void render(GameContainer gc, IGWrapper g) {
		g.setBackground(200,210,210);
		g.setColor(160, 160, 160);
		for(int i=-20-scroll%80; i<gc.getHeight(); i+=80){
			g.fillRect(0, i, gc.getWidth()-20-160, 40);
		}
		int startindex = scroll/40;
		for(int i=startindex;i<organismList.size();i++){
			//if(i==sindex&&i==sindex2){
			//	g.setColor(100, 100, 200);
			//	g.fillRect(0, 20+i*40-scroll, 640-20-160, 20);
			//	g.setColor(200, 100, 100);
			//	g.fillRect(0, 40+i*40-scroll, 640-20-160, 20);
			//	
			//}
			//if(i==sindex&&i!=sindex2){
			//	g.setColor(100, 100, 200, 127);
			//	g.fillRect(0, 20+i*40-scroll, 640-20-160, 40);
			//}
			//if(i==sindex2&&i!=sindex){
			//	g.setColor(200, 100, 100);
			//	g.fillRect(0, 20+i*40-scroll, 640-20-160, 40);
			//}
			if(selectedOrganisms.contains(organismList.get(i))){
				g.setColor(127,127,255,127);
				g.fillRect(0, 20+i*40-scroll, 640-20-160, 40);
			}
			if(i==hoverIndex){
				g.setColor(255,255,255,31);
				g.fillRect(0, 20+i*40-scroll, 640-20-160, 40);
			}
			Organism o = organismList.get(i);
			o.renderAt(64, 40+i*40-scroll, 20, gc, g);
			g.drawString(o.ammount+"x", 4, 40+i*40-scroll);
		}
		g.setColor(127, 127, 127);
		g.fillRect(gc.getWidth()-20-160, 20, 20, gc.getHeight()-20);
		g.drawImage("up", gc.getWidth()-20-160, 20);
		g.drawImage("down", gc.getWidth()-20-160, gc.getHeight()-20);
		g.setColor(0,0,0);
		g.fillRect(gc.getWidth()-160, 20, 160, gc.getHeight()-20);
		if(hoverIndex>=0&&hoverIndex<organismList.size()){
			g.setColor(100,100,200);
			g.drawString("Genes:", gc.getWidth()-160+8, 28);
			GeneRenderUtil.renderGenes(organismList.get(hoverIndex), g, gc.getWidth()-160, 48, true);
		}
	}
		
	public Colour getGeneColor(Allele a){
		if(a.isDominant()){
			return new Colour(200,0,0);
		}
		return new Colour(0,0,200);
	}

	public Organism getSelected() {
		return selected;
	}

	@Override
	public void clickat(int x, int y) {
		if(x>460&&x<480){
			if(y<40){
				scroll-=40;
				if(scroll<0){
					scroll=0;
				}
			}
			if(y>460){
				scroll+=40;
			}
		}else{
			int newy = (y-20+scroll)/40;
			if(newy<organismList.size()){
				if(selectedOrganisms.contains(organismList.get(newy))){
					selectedOrganisms.remove(organismList.get(newy));
				}else{
					selectedOrganisms.add(organismList.get(newy));
				}
			}
		}
		selectOnLoc(x,y);
	}
	
	public void rclickat(int x, int y) {
		if(!(x>620)){
			int newy = (y-20+scroll)/40;
			if(sindex2==newy){
				sindex2 = -1;
			}else{
				sindex2 = newy;
			}
		}
	}
	
	public void combine(float mutachance){
		this.combine(mutachance, selectedOrganisms);
	}
	
	public void combine(float mutachance, List<Organism> organisms){
		Organism[] o = CombinationHelper.choose(rand, organisms, TaskbarComponent.instance.scale.state);
		if(o == null){
			return;
		}
		int x = (o[0].x + o[1].x)/2;
		int y = (o[0].y + o[1].y)/2;
		Organism newOrg = new Organism(x,y,Math.max(o[0].getGeneration(), o[1].getGeneration()));
		newOrg.genes = GeneHelper.combine(o[0].genes, o[1].genes);
		Iterator<Organism> it = organismList.iterator();
		EventExecutionBus.birth(HistoryComponent.instance, false, o[0], o[1], newOrg);
		while(it.hasNext()){
			Organism or = it.next();
			if(newOrg.matches(or)){
				or.ammount++;
				or.syntetic = false;
				return;
			}
		}
		organismList.add(newOrg);
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
	
	public void select(Organism organism){
		this.selected = organism;
	}
	
	@Deprecated
	public void delete(){
		if(getSelected()!=null){
			EventExecutionBus.delete(HistoryComponent.instance, getSelected());
			getSelected().die("delete");
		}
	}
	
	public void parseFile2(File f){
		HashMap<String,Allele> genemap = new HashMap<String,Allele>();;
		List<Organism> newOrgs = new ArrayList<Organism>();
		if(f.exists()){
			BufferedReader br;
			try{
				br = new BufferedReader(new FileReader(f));
				String line;
				while ((line = br.readLine()) != null) {
					if(line.equals("GENES")){
						parseGeneMap(br,genemap);
					}
					if(line.equals("ORGANISMS")){
						parseOrganisms(br,newOrgs,genemap);
					}
				}
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void parseOrganisms(BufferedReader br, List<Organism> newOrgs, HashMap<String, Allele> genemap) throws IOException {
		String line;
		boolean c = true;
		while (((line = br.readLine()) != null)&&c) {
			if(line.equals("END")){
				c = false;
			}else{
				String[] parts = line.split(":");
				int ammount = Integer.valueOf(parts[0]);
				HashMap<String,AllelePair> genes = new HashMap<String,AllelePair>();
				for(int i=1; i<parts.length; i++){
					String[] geneval = parts[i].split(",");
					String name = geneval[0];
					Allele a1 = genemap.get(geneval[1]);
					Allele a2 = genemap.get(geneval[2]);
					genes.put(name, new AllelePair(a1, a2));
				}
				Organism o = new Organism(0, 0, 0);
				o.genes = genes;
				o.ammount = ammount;
				o.syntetic = true;
				organismList.add(o);
				EventExecutionBus.genesis(HistoryComponent.instance, o);
			}
		}
	}

	private void parseGeneMap(BufferedReader br, HashMap<String,Allele> genemap) throws IOException{
		String line;
		boolean c = true;
		while (((line = br.readLine()) != null)&&c) {
			if(line.equals("END")){
				c = false;
			}else{
				String[] parts = line.split(":");
				String key = parts[0];
				String type = parts[1];
				String val = parts[2];
				boolean dom = parts[3].equals("d");
				Allele a = null;
				if(type.equals("i")){
					a = new Allele(dom, Integer.valueOf(val));
				}
				if(type.equals("f")){
					a = new Allele(dom, Float.valueOf(val));
				}
				if(type.equals("c")){
					String[] rgb = val.split(",");
					int r = Integer.valueOf(rgb[0]);
					int g = Integer.valueOf(rgb[1]);
					int b = Integer.valueOf(rgb[2]);
					a = new Allele(dom, new Colour(r,g,b));
				}
				genemap.put(key, a);
			}
		}
	}
	
	public void parseFile(File f){
		List<Organism> newOrgs;
		if(f.exists()){
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(f));
				String line;
				Map<String,AllelePair> genes = null;
				String mode = "NONE";
				int x = -555;
				int y = -555;
				int ammount = 1;
				while ((line = br.readLine()) != null) {
					String[] args = line.split(" ");
					if(args[0].equals("ORGANISM")){
						mode = "ORGANISM";
						if(args[1].contains(",")){
							String[] coords = args[1].split(",");
							x = Integer.valueOf(coords[0]);
							y = Integer.valueOf(coords[1]);
						}else{
							ammount = Integer.valueOf(args[1]);
						}
						genes = new HashMap<String,AllelePair>();
						continue;
					}
					if(args[0].equals("DONE")){
						mode = "NONE";
						Organism o = new Organism(x, y, 0);
						organismList.add(o);
						EventExecutionBus.genesis(HistoryComponent.instance, o);
						o.genes = genes;
						o.ammount = ammount;
						o.syntetic = true;
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
							Colour c1 = new Colour(v1[0],v1[1],v1[2]);
							Colour c2;
							if(val2.equals("dup")){
								Allele color = new Allele(dominance1, c1);
								genes.put(gname, new AllelePair(color, color));
							}else{
								String[] v2 = val2.split(",");
								c2 = new Colour(v2[0],v2[1],v2[2]);
								genes.put(gname, new AllelePair(new Allele(dominance1, c1), new Allele(dominance2, c2)));
							}
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

}
