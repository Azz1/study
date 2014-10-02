/*
 * Created on 2007-5-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ga;

/**
 * @author DS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MaxFitness {
	public double calmaxfitness(double[] fitness){
		int size=fitness.length;
		double x=0.0;
		for(int i=0;i<size;i++){
			if(fitness[i]>x){
				x=fitness[i];
			}
		}
		return x;
	}
}
