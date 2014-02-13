package metoray.genedrift;

import java.util.List;
import java.util.Random;

public class CombinationHelper {
	
	public static Organism[] chooseWeighted(Random rand, List<Organism> organismList){
		int total = 0;
		for(Organism o: organismList){
			total += o.ammount;
			System.out.println(organismList.indexOf(o)+": "+o.ammount);
		}
		if(total<2){
			return null;
		}
		int value = rand.nextInt(total);
		System.out.println("Value: "+value);
		Organism first = null;
		for(Organism o: organismList){
			value -= o.ammount;
			System.out.print(organismList.indexOf(o)+": "+value+" "+(value<0)+" ");
			if(value<0){
				first = o;
				break;
			}
		}
		System.out.println();
		value = rand.nextInt(total-1);
		Organism second = null;
		for(Organism o: organismList){
			value -= o.ammount;
			if(o==first){
				value++;
			}
			if(value<0){
				second = o;
			}
		}
		Organism[] results = new Organism[]{first, second};
		return results;
	}
	
	public static Organism[] choose(Random rand, List<Organism> organismList, boolean weighted){
		if(weighted){
			return chooseWeighted(rand, organismList);
		}
		return chooseNormal(rand, organismList);
	}
	
	public static Organism[] chooseNormal(Random rand, List<Organism> organismList){
		int total = organismList.size();
		if(total==0){
			return null;
		}
		int index = rand.nextInt(total);
		if(total<=1){
			return new Organism[]{organismList.get(index),organismList.get(index)};
		}
		int secondindex = rand.nextInt(total-1);
		if(secondindex>=index){
			secondindex++;
		}
		return new Organism[]{organismList.get(index),organismList.get(secondindex)};
	}

}
