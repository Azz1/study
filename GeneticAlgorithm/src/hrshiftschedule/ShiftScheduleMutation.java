package hrshiftschedule;

import ga.BaseGA;
import ga.BaseMutation;
import ga.IGenome;

public class ShiftScheduleMutation extends BaseMutation {

	public ShiftScheduleMutation(double rate) {
		
		setRate ( rate );
	}

	@Override
	public void doMutation(IGenome g) {
		ShiftScheduleGenome gg = (ShiftScheduleGenome) g;
		double p = getRate()/g.getLength();

		for(int i=0; i<gg.getLength(); i++) {
			double s = BaseGA.rnd.nextDouble();
			if(s <= p) {
				gg.getSpAt(i).doMutation(p);
			}
		}
		gg.sort();
	}

}
