package ga;

public abstract class BaseCrossover {
	protected BaseGA ga;
	
	public void setGA(BaseGA ga) {
		this.ga = ga;
	}
	
	/**
	 * Crossover operator
	 * @param g1
	 * @param g2
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public abstract IGenome doCrossover(IGenome g1, IGenome g2) throws InstantiationException, IllegalAccessException;
}
