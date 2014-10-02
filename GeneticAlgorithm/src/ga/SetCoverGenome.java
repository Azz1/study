package ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SetCoverGenome implements IGenome, Cloneable {

	public int[] Sp;
	double fitness;
	boolean buffered  = false;
	SetCoverGA ga;

	public SetCoverGenome() {
	}
	
	public void setGA(BaseGA ga) {
		this.ga = (SetCoverGA) ga;
		Sp = new int[this.ga.col];
		for (int i = 0; i < this.ga.col; i++)
			Sp[i] = 0;
	}

	public SetCoverGenome(SetCoverGA ga) {
		setGA(ga);
	}

	public String toString() {
		String ret = "{";
		for (int i = 0; i < Sp.length; i++) {
			if (Sp[i] > 0) {
				if (ret.length() == 1) // first
					ret += (ga.getOriginalCol(i)+1);
				else
					ret += ", " + (ga.getOriginalCol(i)+1);
			}
		}
		ret += "}";
		ret += "\tfitness: " + getBufferedFitness(); 
		return ret;
	}

	public int hashCode() {
		int ret = 0;
		for (int i = 0; i < Sp.length; i++) {
			if (Sp[i] > 0) {
				ret = ret*2 + 1;
			} else
				ret *= 2;
		}
		return ret;
	}

	public boolean isEqual(IGenome g) {
		return hashCode() == g.hashCode();
	}

	public int getAt(int i) {
		return Sp[i];
	}

	public void setAt(int i, int v) {
		Sp[i] = v;
		buffered = false;
	}

	public int getLength() {
		return Sp.length;
	}

	public Object clone() throws CloneNotSupportedException {         
		SetCoverGenome cloned = (SetCoverGenome) super.clone();      
		cloned.Sp = new int[Sp.length];
		for (int i = 0; i < Sp.length; i++)
			cloned.Sp[i] = Sp[i];
		return cloned;        
	}

	public double getFitness() {
		double f = 0.0;
		for(int j=0; j<Sp.length; j++) {
			f += ga.cost[j]*Sp[j];
		}
		fitness = f;
		buffered = true;
		return f;
	}

	public double getBufferedFitness() {
		if(! buffered ) return getFitness();
		return fitness;
	}
	
	/**
	 * Heuristic Feasibility Operator
	 */
	public void HeuristicFeasiOp() {
		int[] w = new int[ga.row];
		
		for(int i=0;i<ga.row;i++) {
			w[i] = 0;
			for(int j = 0; j < Sp.length; j ++)
				if(Sp[j] != 0 && ga.input[i][j] != 0)
					w[i] ++;
		}
		
		List<Integer> U = new ArrayList<Integer>();
		for(int i=0; i<ga.row; i++){
			if(w[i] == 0) 
				U.add(new Integer(i));
		}	

		while(! U.isEmpty()) {
			int i = U.get(0).intValue();
			boolean allzero = true;
			for(int j=0; j<ga.col; j++)
				if(ga.input[i][j] > 0) {
					allzero = false;
					break;
				}
			if(! allzero) {
				int j=0;
				double minc = ga.cost[j]+100;
				int minj = 0;
				for(j=0;j<getLength();j++) {
					if(ga.input[i][j] != 0) {
						int c = 0;
						for(Integer r : U) {
							if(ga.input[r.intValue()][j] != 0)
								c++;
						}
						double f = ga.cost[j]/(1.0*c);
						if(minc > f) {
							minc = f;
							minj = j;
						}
					}
				}
				Sp[minj] ++;
				for(int r=0; r<ga.row; r++) {
					if(ga.input[r][minj] != 0) {
						w[r] ++;
						Stack<Integer> stk = new Stack<Integer>();
						for(Integer u : U) {
							if(ga.input[u.intValue()][minj] != 0)
								stk.push(u);
						}
						while(!stk.isEmpty()) {
							Integer u = stk.pop();
							U.remove(u);							
						}
					}
				}
			} else {
				U.remove(0);	
			}
		}
		
		for(int j=Sp.length-1; j >= 0; j--) {
			if(Sp[j] != 0) {
				boolean clear = true;
				for(int i=0; i<ga.row;i++) {
					if(ga.input[i][j] != 0 && w[i] < 2) {
						clear = false;
						break;
					}
				}
				if(clear) {
					//System.out.println("Clear: "+ (j+1));
					Sp[j] --;
					for(int i=0; i<ga.row;i++) {
						if(ga.input[i][j] != 0) {
							w[i] --;
						}
					}
				}	
			}
		}
		
/*			
			System.out.print("\nSp: ");
			for(int k=0; k<Sp.length; k++)
				System.out.print(" " + Sp[k]);
			System.out.println("");
*/			
		
		for(int i=0; i<Sp.length; i++) {
			if(Sp[i] > 0) Sp[i] = 1;
		}
		buffered = false;
	}
}
