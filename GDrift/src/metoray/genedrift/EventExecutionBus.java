package metoray.genedrift;

import java.util.Map.Entry;

import metoray.genedrift.components.HistoryComponent;
import metoray.genedrift.historyevent.*;

public class EventExecutionBus {
	
	public static void genesis(HistoryComponent history, Organism...organisms){
		history.addEvent(new GenesisEvent(organisms, history));
	}
	
	public static void birth(HistoryComponent history, boolean overwrite,Organism p1, Organism p2, Organism child){
		BirthEvent event = new BirthEvent(p1, p2, child, history);
		if(!overwrite){
			boolean hasHappened = false;
			for(Event e: history.history){
				if(e instanceof BirthEvent){
					if(((BirthEvent) e).matches(event)){
						hasHappened = true;
					}
				}
				if(e instanceof IDestroy){
					for(Entry<Integer,Organism> entry :e.organisms.entrySet()){
						if(entry.getValue().matches(event.c)){
							hasHappened = false;
						}
					}
				}
			}
			if(hasHappened){
				return;
			}
		}
		history.addEvent(event);
	}
	
	public static void delete(HistoryComponent history, String reason, Organism... organisms){
		if(reason=="delete"){
			history.addEvent(new DeleteEvent(history, organisms));
		}
		if(reason=="heat"){
			history.addEvent(new OverHeatEvent(history, organisms));
		}
		if(reason=="freeze"){
			history.addEvent(new FreezeEvent(history, organisms));
		}
	}

}
