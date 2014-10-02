package ga;

import java.util.Random;

public class Probability {
	//private double probability=0.0;
	protected Random random=new Random();
	public boolean checkProbability(double probability){
		if(random.nextDouble()<probability) return true;
		return false;
	}
}
