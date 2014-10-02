package ga;

import java.util.Vector;

public interface IPopulation {
	
	public void setGA(BaseGA ga);
	
	/**
	 *  Generate Initial Population
	 *  @param size - population size
	 *  @param genelength - genome length
	 * @throws CloneNotSupportedException 
	 */
	public void generateInitialPopulation(int size, int genelength) throws CloneNotSupportedException;

}
