package metoray.genedrift;

public class AllelePair {
	
	public Allele a1;
	public Allele a2;
	
	public AllelePair(Allele a1, Allele a2){
		this.a1 = a1;
		this.a2 = a2;
	}
	
	public Allele getDominantAllele(){
		boolean d1 = a1.isDominant();
		boolean d2 = a2.isDominant();
		if(d1 && !d2){
			return a1;
		}
		if(d2 && !d1){
			return a2;
		}
		return a1; //not sure what goes here
	}
	
	public Allele getNonDomAllele(){
		boolean d1 = a1.isDominant();
		boolean d2 = a2.isDominant();
		if(d1 && !d2){
			return a2;
		}
		if(d2 && !d1){
			return a1;
		}
		return a2; //not sure what goes here
	}
	
	public Allele getAlleleFromBool(boolean b){
		if(b){
			return a1;
		}else{
			return a2;
		}
	}
	
	public boolean genesAreCodom(){
		return a1.isDominant()==a2.isDominant();
	}
	
	public Object getValue(){
		if(genesAreCodom()){
			Object o1 = a1.value;
			Object o2 = a2.value;
			if(o1.getClass().equals(o2.getClass())){
				if(o1 instanceof Integer){
					return ((Integer)o1+(Integer)o2)/2;
				}
				if(o1 instanceof Float){
					return ((Float)o1+(Float)o2)/2;
				}
				if(o1 instanceof Double){
					return ((Float)o1+(Float)o2)/2;
				}
			}
		}
		return getDominantAllele().value;
	}
	
	public boolean matches(AllelePair otherpair){
		if(a1.matches(otherpair.a1)&&a2.matches(otherpair.a2)){
			return true;
		}
		if(a1.matches(otherpair.a2)&&a2.matches(otherpair.a1)){
			return true;
		}
		return false;
	}

}
