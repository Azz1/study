package ga;

public interface IGenome {
	public void setGA(BaseGA ga);
	
	public int getLength();

	public int getAt(int i);
	public void setAt(int i, int v);
	
	public int hashCode();

	public boolean isEqual(IGenome g);
	
	public double getFitness();
	public double getBufferedFitness();
}
