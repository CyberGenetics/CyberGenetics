package metoray.genedrift;

import java.util.TreeMap;
import java.util.Map.Entry;

import metoray.genedrift.render.IGWrapper;

public class GeneRenderUtil {
	
	public static void renderGenes(Organism selected, IGWrapper g, int x, int y, boolean legend){
		int j = 0;
		TreeMap<String,AllelePair> genes = new TreeMap<String,AllelePair>(selected.genes);
		
		for(Entry<String,AllelePair> e : genes.entrySet()){
			g.setColor(255,255,255);
			g.drawString(e.getKey(), x+8, y+j*24);
			
			if(e.getValue().a1.value instanceof Colour){
				Allele aactive = e.getValue().getDominantAllele();
				Allele adormant = e.getValue().getNonDomAllele();
				Colour c1 = (Colour) aactive.value;
				Colour c2 = (Colour) adormant.value;
				
				g.setColor(getGeneColor(aactive));
				g.fillOval(x+64, y+j*24, 20, 20);
				g.setColor(c1);
				g.fillOval(x+66, y+2+j*24, 16, 16);
				
				g.setColor(getGeneColor(adormant));
				g.fillOval(x+128, y+j*24, 20, 20);
				g.setColor(c2);
				g.fillOval(x+130, y+2+j*24, 16, 16);
			}else{
				Allele aactive = e.getValue().getDominantAllele();
				String s1 = String.valueOf(aactive.value);
				g.setColor(getGeneColor(aactive));
				g.drawString(s1, x+64, 48+j*24);
				
				Allele adormant = e.getValue().getNonDomAllele();
				String s2 = String.valueOf(adormant.value);
				g.setColor(getGeneColor(adormant));
				g.drawString(s2, x+128, 48+j*24);
			}
			
			j++;
		}
		
		if(legend){
			j++;
			g.setColor(200, 0, 0);
			g.drawString("Dominant (A)", x+8, y+j*24);
			j++;
			g.setColor(0, 0, 200);
			g.drawString("Recessief (a)", x+8, y+j*24);
		}
		
	}
	
	public static Colour getGeneColor(Allele a){
		if(a.isDominant()){
			return new Colour(200,0,0);
		}
		return new Colour(0,0,200);
	}

	public static void renderGenes(Organism organism, IGWrapper g, int x, int y) {
		renderGenes(organism, g, x, y, false);
		
	}

}
