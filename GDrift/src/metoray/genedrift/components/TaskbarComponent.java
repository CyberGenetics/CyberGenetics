package metoray.genedrift.components;

import java.util.Iterator;

import metoray.genedrift.GDrift;
import metoray.genedrift.Organism;
import metoray.genedrift.StartState;
import metoray.genedrift.TBB;
import metoray.genedrift.TBBVal;
import metoray.genedrift.TTBB;
import metoray.genedrift.render.IGWrapper;

import org.newdawn.slick.GameContainer;

public class TaskbarComponent extends Component {
	
	private TBB[] left;
	
	private TBB stop;
	private TBBVal pause;
	public TBBVal play;
	private TBBVal forward;
	private TBB plus;
	private TBB plus10;
	public TTBB plusplus;
	private TBB book;
	public TTBB scale;
	public TBB trash;
	public TBB trash10;
	public TBB trashall;
	public TTBB die;
	public TBBVal hot;
	public TBBVal cold;
	
	public Component history;
	public WorldComponent world;
	public int timer;
	
	public static TaskbarComponent instance; 
	
	
	
	public TaskbarComponent(Component history, WorldComponent world){
		super();
		timer = 1;
		instance = this;
		this.history = history;
		this.world = world;
		stop = new TBB("stop","Gaat terug naar het hoofdmenu");
		play = new TBBVal("play","Zet snelheid op 1   snelheid: ",1, 0);
		pause = new TBBVal("pause","Zet automatische mode stil   snelheid: ", play, 0);
		forward = new TBBVal("forward","Versnelt automatische mode   snelheid: ", play, 1);
		plus = new TBB("plus","Combineert willekeurig 2 organismen");
		plus10 = new TBB("plus10","Combineert willekeurig 2 organismen, 10 keer");
		plusplus = new TTBB("plusplus","Combineert automatisch willekeurig organismen",false);
		book = new TBB("book","Bekijk de geschiedenis van de organismen");
		scale = new TTBB("scale","Gebruikt het aantal organismen van 1 type bij het kiezen van organismen",false);
		trash = new TBB("trash","Verwijderd een organisme per groep");
		trash10 = new TBB("trash10","Verwijderd 10 organismen per groep");
		trashall = new TBB("trashall","Verwijderd alle geselecteerde organismen");
		die = new TTBB("die","Sta toe organismen dood te laten gaan",false);
		hot = new TBBVal("hot","Temperatuur: ",10,1);
		cold = new TBBVal("cold","Temperatuur: ",hot,-1);
		left = new TBB[]{stop,pause,play,forward,null,plus,plus10,plusplus,null,scale,null,book,null,trash,trash10,trashall,null,die,null,hot,cold};
	}

	@Override
	protected void render(GameContainer gc, IGWrapper g) {
		int index = gc.getInput().getMouseX()/20;
		boolean tooltip = gc.getInput().getMouseY()<20;
		g.setColor(0, 0, 0);
		g.fillRect(0, 0, gc.getWidth(), 20);
		for(int i=0; i<left.length; i++){
			if(left[i]!=null){
				left[i].render(g, i,(i==index&&tooltip));
			}
		}
		g.setColor(0,255,0);
	}

	public void clickat(int x, int y) {
		if(y<20){
			int lid = x/20;
			System.out.println(lid);
			if(lid>=0&&lid<left.length){
				actionLeft(lid);
				return;
			}
		}
	}
	
	public void actionLeft(int i){
		TBB a = left[i];
		if(a instanceof TTBB){
			((TTBB) a).toggle();
		}
		if(a==play){
			play.value = 1;
		}
		if(a==pause){
			play.value = 0;
		}
		if(a instanceof TBBVal){
			((TBBVal) a).increase(((TBBVal) a).modifier);
		}
		if(a==stop){
			GDrift.instance.setState(new StartState());
		}
		if(a==book){
			history.setVisible(!history.getVisible());
			if(history.getVisible()){
				EventViewComponent.instance.e = null;
			}
		}
		if(a==plus){
			world.combine(0);
		}
		if(a==plus10){
			for(int j=0; j<10; j++){
				world.combine(0);
			}
		}
		if(a==trash){
			Iterator<Organism> it = world.selectedOrganisms.iterator();
			while(it.hasNext()){
				Organism o = it.next();
				o.ammount--;
				if(o.ammount<=0){
					o.die("delete");
					it.remove();
				}
			}
		}
		if(a==trash10){
			Iterator<Organism> it = world.selectedOrganisms.iterator();
			while(it.hasNext()){
				Organism o = it.next();
				o.ammount-=10;
				if(o.ammount<=0){
					o.die("delete");
					it.remove();
				}
			}
		}
		if(a==trashall){
			Iterator<Organism> it = world.selectedOrganisms.iterator();
			while(it.hasNext()){
				Organism o = it.next();
				o.ammount=0;
				if(o.ammount<=0){
					o.die("delete");
					it.remove();
				}
			}
		}
	}
	
	public void tick(){
		timer++;
	}

}
