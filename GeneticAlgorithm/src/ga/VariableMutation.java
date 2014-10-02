package ga;

public class VariableMutation extends BaseMutation {

	double mf;	// stable mutation rate
	double mc;  // ga.t at whitch mf/2 is reached
	double mg;  // gradient at t=mc
	
	public VariableMutation(double mf, double mc, double mg) {
		this.mf = mf;
		this.mc = mc;
		this.mg = mg;
		
		rate = 0.0;
	}

	@Override
	public void doMutation(IGenome g) {
		rate = mf/(1.0+Math.exp(-4.0*mg*(ga.t-mc)/mf));
		System.out.println("Mutation Rate:" + rate);
	
		double p = rate/g.getLength();

		for(int i=0; i<g.getLength(); i++) {
			double s = BaseGA.rnd.nextDouble();
			if(s <= p) {
				g.setAt(i, 1-g.getAt(i));
			}
		}
	}	
}
