package hrshiftschedule;

import ga.BaseGA;
import ga.IGenome;
import java.util.Arrays;

class SingleSched implements Comparable<SingleSched>, Cloneable {
	String sp;	// off block combination
	int w;		// Work block combination 0-2^8-1
	int rotate;	// Phase shift 0-5
	
	public int compareTo(SingleSched arg0) {
		return this.toLongString().compareTo(arg0.toLongString());
	}	

	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getRotate() {
		return rotate;
	}

	public void setRotate(int rotate) {
		this.rotate = rotate;
	}

	public void doMutation(double rate) {
		double s = BaseGA.rnd.nextDouble();
		if(s <= rate) {
			int p = BaseGA.rnd.nextInt(ShiftScheduleGA.NumberOfWorkingBlocks);
			w = w ^ ((int)Math.pow(2, p));
		}
		s = BaseGA.rnd.nextDouble();
		if(s <= rate) {
			int p;
			char c[] = sp.toCharArray();
			char c1;
			char c2;
			
			do {
				p = BaseGA.rnd.nextInt(sp.length());
				c1 = c[p];
				if(p == c.length-1) 
					c2 = c[0];
				else
					c2 = c[p+1];
			} while(c1 == c2);
			
			c[p] = c2;
			if(p == c.length-1) 
				c[0] = c1;
			else
				c[p+1] = c1;
			
			sp = new String(c);
		}
		s = BaseGA.rnd.nextDouble();
		if(s <= rate) {
			rotate = (rotate + 1) % 5;
		}
	}
	
	public void randgenSp(boolean roll) {
		if( ! roll )
			rotate = 0;
		else
			rotate = BaseGA.rnd.nextInt(5);
		
		w = BaseGA.rnd.nextInt((int)Math.pow(2, ShiftScheduleGA.NumberOfWorkingBlocks));

		char [] h = {'A','A','B','B','B','C','C','C'};
		sp = "";
		
		for(int ii = 0;ii<ShiftScheduleGA.NumberOfWorkingBlocks;ii++) {
			int r = BaseGA.rnd.nextInt(h.length);
			while(h[r] == '_') {
				r = BaseGA.rnd.nextInt(h.length);
			}
			
			sp += h[r];
			h[r] = '_';
		}
	}
	
	public String toString() {
		String ws = Integer.toBinaryString(w);
		while(ws.length() < ShiftScheduleGA.NumberOfWorkingBlocks) {
			ws = '0' + ws;
		}
		String s = "";
		for(int i=0; i<ShiftScheduleGA.NumberOfWorkingBlocks; i++)
			s += ws.charAt(i) + "" + sp.charAt(i) ;
		return s + "(" + rotate + ")";
	}

	public String toLongString() {
		String ws = Integer.toBinaryString(w);
		while(ws.length() < ShiftScheduleGA.NumberOfWorkingBlocks) {
			ws = '0' + ws;
		}
		String s = "";
		for(int i=0; i<ShiftScheduleGA.NumberOfWorkingBlocks; i++)
			s += ws.charAt(i) + "" + sp.charAt(i);
		String s1 = "";
		for(int j=0; j<s.length();j++) {
			switch (s.charAt(j)) {
			case '0':s1 += "ddddd"; break;
			case '1':s1 += "nnnnn"; break;
			case 'A':s1 += "0000"; break;
			case 'B':s1 += "000"; break;
			case 'C':s1 += "00"; break;
			}
		}
		for(int i=0; i<rotate; i++) 
			s1 = s1.charAt(s1.length()-1) + s1.substring(0,s1.length()-1);
		return s1;
	}

	public SingleSched clone() throws CloneNotSupportedException {         
		SingleSched cloned = (SingleSched) super.clone();      
		cloned.sp = sp;
		cloned.w = w;
		cloned.rotate = rotate;
		return cloned;        
	}
}

public class ShiftScheduleGenome implements IGenome, Cloneable {

	SingleSched Sp[] = new SingleSched[ShiftScheduleGA.NumberOfTeams];	
	
	double fitness;
	boolean buffered  = false;
	ShiftScheduleGA ga;
	
	public ShiftScheduleGenome(ShiftScheduleGA ga) {
		setGA(ga);
	}

	public void randGenerate() {
		do {
			int c = 0;
			while(c < ShiftScheduleGA.NumberOfTeams){
				SingleSched sp = new SingleSched();
				if(c == 0)
					sp.randgenSp(false);
				else
					sp.randgenSp(true);
					
				boolean regen = false;
				for(int i=0;i<c;i++) {
					if(sp.compareTo(Sp[i]) == 0) {
						regen = true;
						break;
					}
				}
				if(! regen) {
					Sp[c] = sp;
					c++;
				}
			}
			Arrays.sort(Sp);
			buffered = false;
			break; // TODO - Mask if want to find a solution greedy
		}while(getFitness() >= 0);
	}
		
	// No use
	public int getAt(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public SingleSched getSpAt(int i) {
		return Sp[i];
	}

	public double getBufferedFitness() {
		//buffered = false;
		if(! buffered ) return getFitness();
		return fitness;
	}

	public double getFitness() {
		double ret = 0;
		String [] ss = new String[Sp.length];
		for(int i=0; i<Sp.length; i++)
			ss[i] = Sp[i].toLongString();
		
		for(int i=0; i<ss[0].length(); i++){
			int s = 0;
			for(int j=0; j<ss.length; j++) {
				switch(ss[j].charAt(i)) {
				case 'd': s += 1; break;	// day shift
				case 'n': s += 10; break;	// night shift
				default: s += 0; break;
				}
			}
			if(s == 0 || s % 10 == 0 || s/10 == 0)	{ // no shift in the day or only day/night shift exists 
				ret += -10000;
				//break;		// TODO - Unmask if want to find a solution greedy
			} else
				ret += ((s % 10) * daynight_weights[0] + (s/10) * daynight_weights[1]) * weekday_weights[i%7];
		}			
		buffered = true;
		fitness = -ret;
		
		//print();
		//System.out.println("Fitness:"+fitness);
		return fitness;
	}

	public int getLength() {
		return Sp.length;
	}

	public boolean isEqual(IGenome g) {
		return this.toString().equals(g.toString());
	}

	// No use
	public void setAt(int i, int v) {
		// TODO Auto-generated method stub
		buffered = false;
	}

	public void setSpAt(int i, SingleSched v) {
		Sp[i] = v;
		buffered = false;
	}

	public void setGA(BaseGA ga) {
		this.ga = (ShiftScheduleGA)ga;
	}
	
	public String toShortString() {
		String s = "";
		for(int i=0; i<Sp.length; i++) {
			s += Sp[i].toString() + " ";
		}
		return s;
	}

	public void sort() {
		Arrays.sort(Sp);
		buffered = false;
	}
	
	public String toString() {
		String s = "";
		for(int i=0; i<Sp.length; i++) {
			String s1 = Sp[i].toLongString();
			s += s1 + "\n";
		}
		return s;
	}
	
	public void print() {
		int m = Sp[0].toLongString().length();
		
		System.out.print("DAY:");
		for(int i=0; i<m;i++) {
			System.out.print((i+1)%7);
		}
		System.out.println();

		System.out.print("----");
		for(int i=0; i<m;i++) {
			if((i+1) % 7 == 0)
				System.out.print("+");
			else
				System.out.print("-");
		}
		System.out.println();

		for(int i=0; i<Sp.length;i++) {
			System.out.print("T"+(i+1)+": ");
			System.out.println(Sp[i].toLongString());			
		}

		System.out.println("Fitness: " + getFitness());
	}

	public Object clone() throws CloneNotSupportedException {         
		ShiftScheduleGenome cloned = (ShiftScheduleGenome) super.clone();      
		cloned.Sp = new SingleSched[Sp.length];
		for (int i = 0; i < Sp.length; i++)
			cloned.Sp[i] = Sp[i].clone();
		cloned.buffered = false;
		return cloned;        
	}

	private static double [] weekday_weights = {1.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0};
	private static double [] daynight_weights = {2.0, 1.0};
	
	public double getSTD() {
		double ret = 0.0;
		double N = 0.0;
		double c = 0;
		String [] ss = new String[Sp.length];
		for(int i=0; i<Sp.length; i++)
			ss[i] = Sp[i].toLongString();
		
		double [] ds = new double[ss[0].length()];
		for(int i=0; i<ss[0].length(); i++){
			int s = 0;
			ds[i] = 0.0;
			for(int j=0; j<ss.length; j++) {
				switch(ss[j].charAt(i)) {
				//case 'd': s += 1; ds[i] += 1; break;	// day shift
				//case 'n': s += 10; ds[i] += 1; break;	// night shift

				case 'd': s += 1; ds[i] += daynight_weights[0]; break;	// day shift
				case 'n': s += 10; ds[i] += daynight_weights[1]; break;	// night shift
				}
			}
			ds[i] *= weekday_weights[i%7];
			N += ds[i];
			if(s == 0 || s % 10 == 0 || s/10 == 0)	{ // no shift in the day or only day/night shift exists 
				// ret = -10000;
			} else {
				c +=weekday_weights[i%7];
			}
		}			
		N /= c;
		for(int i=0; i<ss[0].length(); i++){
			if(ds[i] > 0)
				ret += (ds[i]-N) * (ds[i]-N);
		}
		ret = Math.sqrt(ret / c);
			
		System.out.println("N:"+N + " d:" + ret);
		return ret;
	}
	
	public static void main(String[] args) {
		ShiftScheduleGenome g = new ShiftScheduleGenome(null);
		g.randGenerate();
		System.out.println(g.toShortString()+"\n");
		g.print();
		System.out.println("--------------");
		g.getSTD();
	}

}
