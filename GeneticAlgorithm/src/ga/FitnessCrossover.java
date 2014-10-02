package ga;

public class FitnessCrossover extends BaseCrossover {

	@Override
	public IGenome doCrossover(IGenome g1, IGenome g2) throws InstantiationException, IllegalAccessException {
		double p = g2.getBufferedFitness() / (g1.getBufferedFitness() + g2.getBufferedFitness());

		//System.out.println("Prob:" + p);
		
		IGenome g = g1.getClass().newInstance();
		g.setGA(ga);
		for(int i=0; i<g1.getLength(); i++) {
			if(g1.getAt(i) == g2.getAt(i))
				g.setAt(i,g1.getAt(i));
			else {
				double s = BaseGA.rnd.nextDouble();
				if(s <= p) g.setAt(i, g1.getAt(i));
				else g.setAt(i, g2.getAt(i));
			}
		}
		return g;
	}
}
