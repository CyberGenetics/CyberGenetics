package metoray.genedrift;

public class Colour {
	
	public int r;
	public int g;
	public int b;
	
	public Colour(int r, int g, int b){
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public Colour(String sr, String sg, String sb) {
		this.r =  Integer.valueOf(sr);
		this.g =  Integer.valueOf(sg);
		this.b =  Integer.valueOf(sb);
	}
	
	public Colour mix(Colour c){
		return new Colour(avg(this.r,c.r),avg(this.g,c.g),avg(this.b,c.b));
	}
	
	private int avg(int p, int s){
		return (p+s)/2;
	}
	
	private int logAvg(int p, int s){
		double logP = Math.log10(p);
		double logS = Math.log10(p);
		double avgLog = (logP+logS)/2;
		int newVal = (int) Math.pow(10, avgLog);
		return newVal;
	}
	
	public Colour inverted(){
		return new Colour(invertChannel(r),invertChannel(g),invertChannel(b));
	}
	
	private int invertChannel(int i){
		return 255-i;
	}

}
