package metoray.genedrift;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

public class GeneHelper {
	
	public static HashMap<String,AllelePair> combine(Map<String, AllelePair> genes, Map<String, AllelePair> genes2){
		Random r = new Random();
		HashMap<String,AllelePair> newGenes = new HashMap<String,AllelePair>();
		for(Entry<String,AllelePair> e: genes.entrySet()){
			if(genes2.containsKey(e.getKey())){
				Allele a1 = e.getValue().getAlleleFromBool(r.nextBoolean());
				Allele a2 = genes2.get(e.getKey()).getAlleleFromBool(r.nextBoolean());
				newGenes.put(e.getKey(), new AllelePair(a1,a2));
			}else{ //TODO implement actual genetics
				newGenes.put(e.getKey(), e.getValue());
			}
		}
		for(Entry<String,AllelePair> e: genes2.entrySet()){
			if(genes.containsKey(e.getKey())){
				continue;
			}
			newGenes.put(e.getKey(), e.getValue());
		}
		return newGenes;
	}

}
