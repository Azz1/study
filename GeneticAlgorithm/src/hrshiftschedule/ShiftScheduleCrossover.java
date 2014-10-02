package hrshiftschedule;

import ga.BaseCrossover;
import ga.BaseGA;
import ga.IGenome;

public class ShiftScheduleCrossover extends BaseCrossover {

	@Override
	public IGenome doCrossover(IGenome g1, IGenome g2) throws InstantiationException, IllegalAccessException {
		ShiftScheduleGenome gg1 = (ShiftScheduleGenome) g1;
		ShiftScheduleGenome gg2 = (ShiftScheduleGenome) g2;
		double p = Math.abs(gg2.getBufferedFitness()) / (Math.abs(gg1.getBufferedFitness()) + Math.abs(gg2.getBufferedFitness()));

		//System.out.println("Prob:" + p);
		
		ShiftScheduleGenome g = new ShiftScheduleGenome((ShiftScheduleGA)ga);
		try {
			for(int i=0; i<gg1.getLength(); i++) {
				if(gg1.getSpAt(i).compareTo(gg2.getSpAt(i)) == 0)
						g.setSpAt(i,gg1.getSpAt(i).clone());
				else {
					double s = BaseGA.rnd.nextDouble();
					if(s <= p) g.setSpAt(i, gg1.getSpAt(i).clone());
					else g.setSpAt(i, gg2.getSpAt(i).clone());
				}
			}
			g.sort();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return g;
	}
}
