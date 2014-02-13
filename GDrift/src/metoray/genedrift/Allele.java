package metoray.genedrift;

public class Allele {
	
	private boolean dominant;
	public Object value;
	
	public Allele(boolean dominance, Object val){
		this.dominant = dominance;
		this.value = val;
	}
	
	public boolean isDominant(){
		return dominant;
	}

	public boolean matches(Allele a) {
		return (this.value == a.value)&&(this.dominant==a.dominant);
	}

}
