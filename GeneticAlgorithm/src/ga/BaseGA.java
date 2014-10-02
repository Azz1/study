package ga;

import java.util.Random;
import java.util.Vector;

public abstract class BaseGA {

	public static int Maxknock = 10;
	int Maxgen = 3;
	int popsize= 2;
	int genelength=5;
	
	static public Random rnd = new Random(System.currentTimeMillis());
	
	// Generation
	public int t = 0;

	// Population
	Vector population;
	IPopulation populationAPI;

	// Selection
	ISelection selectionAPI;

	// Crossover
	BaseCrossover crossoverAPI;

	// Mutation
	BaseMutation mutationAPI;

	/**
	 *  Default constructor
	 */
	public BaseGA (IPopulation populationAPI, ISelection selectionAPI, BaseCrossover crossoverAPI,
			BaseMutation mutationAPI) {
		population = new Vector();
		this.populationAPI = populationAPI;
		this.selectionAPI = selectionAPI;
		this.crossoverAPI = crossoverAPI;
		this.mutationAPI = mutationAPI;
	}

	/**
	 * Check if g exists in population
	 * @param g
	 * @return
	 */
	public boolean Contains(IGenome g) {
		for(Object p : population) {
			//System.out.println("Hash:" + p.hashCode());
			if(((IGenome)p).hashCode() == g.hashCode())
				return true;
		}
		return false;
	}
	
	/**
	 * Evolving
	 * @return
	 * @throws CloneNotSupportedException 
	 * @throws Exception 
	 */
	public abstract GAResult Eval() throws CloneNotSupportedException, Exception;
	
	public int getPopsize() {
		return popsize;
	}

	public void setPopsize(int popsize) {
		this.popsize = popsize;
	}

	public int getGenelength() {
		return genelength;
	}

	public void setGenelength(int genelength) {
		this.genelength = genelength;
	}

	public Vector getPopulation() {
		return population;
	}

	public void setPopulation(Vector population) {
		this.population = population;
	}

	protected IPopulation getPopulationAPI() {
		return populationAPI;
	}

	protected void setPopulationAPI(IPopulation populationAPI) {
		this.populationAPI = populationAPI;
	}

	protected ISelection getSelectionAPI() {
		return selectionAPI;
	}

	protected void setSelectionAPI(ISelection selectionAPI) {
		this.selectionAPI = selectionAPI;
	}

	protected BaseCrossover getCrossoverAPI() {
		return crossoverAPI;
	}

	protected void setCrossoverAPI(BaseCrossover crossoverAPI) {
		this.crossoverAPI = crossoverAPI;
	}

	protected BaseMutation getMutationAPI() {
		return mutationAPI;
	}

	protected void setMutationAPI(BaseMutation mutationAPI) {
		this.mutationAPI = mutationAPI;
	}

	public int getMaxgen() {
		return Maxgen;
	}

	public void setMaxgen(int maxgen) {
		Maxgen = maxgen;
	}
		
}
