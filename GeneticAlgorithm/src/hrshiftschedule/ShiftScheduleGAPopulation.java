package hrshiftschedule;

import ga.BaseGA;
import ga.IGenome;
import ga.IPopulation;
import java.util.Vector;

public class ShiftScheduleGAPopulation implements IPopulation {

	ShiftScheduleGA sga = null;
	int[] w;
	
	/**
	 * Generate Initial Population
	 * @throws CloneNotSupportedException 
	 */
	public void generateInitialPopulation(int size, int genelength) throws CloneNotSupportedException {
		Vector pop = sga.getPopulation();
		pop.clear();

		ShiftScheduleGenome Sp = null;
		int knock = 0;
		for(int i=0; i<size; i++) {
			//System.out.println("Generate Population ..." + i);
			do {
				Sp = generateOne();
				knock ++;
				if(knock > BaseGA.Maxknock) break;
			}while(sga.Contains(Sp));
			if(! sga.Contains(Sp)) pop.add(Sp);
			knock = 0;
		}		
	}

	/**
	 * Generate One Solution
	 * @throws CloneNotSupportedException 
	 */
	private ShiftScheduleGenome generateOne() throws CloneNotSupportedException {		
		ShiftScheduleGenome Sp = new ShiftScheduleGenome(sga);
		Sp.randGenerate();
		return Sp;
	}

	/**
	 * Must be called before use
	 */
	public void setGA(BaseGA ga) {
		sga = (ShiftScheduleGA)ga;
	}

}
