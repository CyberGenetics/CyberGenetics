package metoray.genedrift;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import metoray.genedrift.components.HistoryComponent;
import metoray.genedrift.render.IGWrapper;

import org.newdawn.slick.GameContainer;

public class Organism {
	
	public int x;
	public int y;
	public int ammount;
	public Map<String,AllelePair> genes;
	private int generation;
	private int age;
	private boolean isDead;
	public boolean syntetic;
	
	
	public Organism(int x, int y, int generation){
		this.syntetic = false;
		this.x = x;
		this.y = y;
		this.age = 0;
		this.generation = generation;
		this.ammount = 1;
		genes = new HashMap<String,AllelePair>();
		genes.put("size", new AllelePair(new Allele(false, 1f), new Allele(true, 0.4f)));
		genes.put("speed", new AllelePair(new Allele(false, 1), new Allele(true, 2)));
		genes.put("red", new AllelePair(new Allele(true, 1F), new Allele(false, 0F)));
		genes.put("green", new AllelePair(new Allele(true, 1F), new Allele(true, 0F)));
		genes.put("blue", new AllelePair(new Allele(true, 0F), new Allele(false, 0.5F)));
	}

	public void render(GameContainer container, IGWrapper g) {
		renderAt(x*32+16,y*32+36,container,g);
	}
	
	public Colour getColor(String name){
		Colour c;
		if(genes.containsKey(name)&&genes.get(name).getValue() instanceof Colour){
			if(genes.get(name).genesAreCodom()){
				Colour c1 = (Colour) genes.get(name).a1.value;
				Colour c2 = (Colour) genes.get(name).a2.value;
				c = c1.mix(c2);
			}else{
				c = (Colour) genes.get(name).getValue();
			}
		}else{
			c = new Colour(127,127,127);
		}
		return c;
	}
	
	public int maxTemp(){
		if(genes.containsKey("temp+")&&genes.get("temp+").getValue() instanceof Integer){
			return (Integer)genes.get("temp+").getValue();
		}
		return 50;
	}
	
	public int minTemp(){
		if(genes.containsKey("temp-")&&genes.get("temp-").getValue() instanceof Integer){
			return (Integer)genes.get("temp-").getValue();
		}
		return -10;
	}
	
	public void renderAt(int x, int y, GameContainer container, IGWrapper g){
		renderAt(x,y,32,container,g);
	}
	
	public void renderAt(int x, int y, int size, GameContainer container, IGWrapper g){
		Colour c = getColor("color");
		Colour c2 = getColor("icolor");
		int pixelsize = Math.abs(getSize(size))*2;
		g.drawImage("frogskin", x-pixelsize/2, y-pixelsize/2, pixelsize, pixelsize, c.r, c.g, c.b);
		g.drawImage("frogeye", x-pixelsize/2, y-pixelsize/2, pixelsize, pixelsize, c2.r, c2.g, c2.b);
		g.drawImage("froglines", x-pixelsize/2, y-pixelsize/2, pixelsize, pixelsize, 255,255,255);
		g.setColor(c);
		//g.setColor(getColor());
		//g.fillOval(x-getSize(size), y-getSize(size), getSize(size)*2, getSize(size)*2);	
	}
	
	public void logic(Random rand){
		age++;
		if(age%(100/getSpeed())==0){
			int direction = rand.nextInt(4);
			switch(direction){
			default: return;
			case 0: x++; break;
			case 1: x--; break;
			case 2: y++; break;
			case 3: y--; break;
			}
		}
	}
	
	public int getSize(float max){
		return (int)(findPropFloat("size")*max);
	}
	
	public int getSpeed(){
		return (int)(findPropInt("speed"));
	}
	
	public int findPropInt(String prop){
		if(genes.containsKey(prop)){
			Object value = genes.get(prop).getValue();
			if(value instanceof Integer){
				return (Integer) value;
			}
		}
		return -1;
	}
	
	public float findPropFloat(String prop){
		if(genes.containsKey(prop)){
			Object value = genes.get(prop).getValue();
			if(value instanceof Float){
				return (Float) value;
			}
		}
		return 1f;
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public boolean isDead() {
		return isDead;
	}

	public void die(String reason) {
		if(reason.equals("delete")){
			EventExecutionBus.delete(HistoryComponent.instance, this);
		}
		this.isDead = true;
	}
	
	public void delete(){
		
	}

	public void llogic(Random rand) {
		age++;
		
	}
	
	public boolean matches(Organism o){
		Map<String,AllelePair> ogenes = o.genes;
		if(o.genes.size()==genes.size()){
			for(Map.Entry<String,AllelePair> e :o.genes.entrySet()){
				if(!genes.containsKey(e.getKey())){
					return false;
				}
				if(!(e.getValue().matches(genes.get(e.getKey())))){
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
