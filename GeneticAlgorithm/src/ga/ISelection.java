package ga;

import java.util.Vector;

public interface ISelection {
	public void setGA(BaseGA ga);
	
	/**
	 * Select two genomes from population
	 * @param ga
	 * @return
	 * @throws Exception 
	 */
	public Vector doSelect() throws Exception;
}
