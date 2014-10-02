package ga;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class BinaryTournamentSelection implements ISelection {
	BaseGA ga;
	int T = 2;
	
	public BinaryTournamentSelection( int t ){
		T = t;
	}

	/**
	 * Should return a Vector that contains only two genomes for mating
	 * @throws Exception 
	 */
	public Vector doSelect() throws Exception {
		Map m1 = new HashMap(), m2 = new HashMap();
		Vector pop = ga.getPopulation();
		if(pop.size() < 2*T) 
			T = pop.size()/2;
		if(T == 0) // only contains 1 genome
			throw(new Exception("Population only contains less than two genomes !"));
		
		IGenome f1=null, f2=null;	// selected genomes with the lowest fitness
		
		// Select T gnomes from population randomly, put into set 1
		for(int i=0; i<T; i++) {
			Integer idx;
			do {
				idx = new Integer(BaseGA.rnd.nextInt(pop.size()));		
			}while(m1.containsKey(idx));

			IGenome g = (IGenome) pop.get(idx.intValue());
			// Check the lowest fitness
			if(m1.isEmpty()) {
				f1 = g;
			} else {
				if(f1.getBufferedFitness() > g.getBufferedFitness())
					f1 = g;
			}
			m1.put(idx, g);			
		}

		// Select T gnomes from population randomly, put into set 2
		for(int i=0; i<T; i++) {
			Integer idx;
			do {
				idx = new Integer(BaseGA.rnd.nextInt(pop.size()));		
			}while(m1.containsKey(idx) || m2.containsKey(idx));

			IGenome g = (IGenome) pop.get(idx.intValue());
			// Check the lowest fitness
			if(m2.isEmpty()) {
				f2 = g;
			} else {
				if(f2.getBufferedFitness() > g.getBufferedFitness())
					f2 = g;
			}
			m2.put(idx, g);			
		}
		
		Vector ret = new Vector();
		ret.add(f1);
		ret.add(f2);
		return ret;
	}

	public void setGA(BaseGA ga) {
		this.ga = ga;
	}

}
