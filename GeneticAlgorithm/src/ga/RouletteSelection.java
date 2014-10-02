/*
 * Created on 2007-5-22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ga;

import java.util.Random;
import java.util.Vector;

public class RouletteSelection {
	
	double sumfitness=0.0;
	Random rnd=new Random();
	Vector pop=new Vector();
	//double x=0;
	double x=1E20;
	int site=0;
	//MaxFitness maxfitness=new MaxFitness();
	/*
	 * 初始化
	 */
	public RouletteSelection(Vector pop){
		this.pop=pop;
	}
	/*
	 * 实际操作
	 */
	public int[] operate(double[] fitness){
		
		int size=pop.size();
		int[] index=new int[size];
		double sum=0.0;
		//double[] probabilities = new double[size+1];
		double[] probabilities = new double[size];
		double s = 0.0;
		for(int i=0;i<size;i++){//求解最大适应度值对应的个体编号
			if(fitness[i]>x){
				x=fitness[i];
				site=i;
			}
			/*if(fitness[i]<x){
				x=fitness[i];
				site=i;
			}*/
		}
		//System.out.println("index[0]="+site);
		index[0]=site;
		
		for(int i=0;i<size;i++){
			sum+=fitness[i];
		}
		
		for (int i=0; i<size; i++){
			s += fitness[i];
			probabilities[i] = s/sum; 
			//s += fitness[i];
			//System.out.println("probabilities["+i+"]="+probabilities[i]);
		}
		//probabilities[size] = 1.0;
		// 选择个体
		//int[] index = new int[size];
		/*for (int i=1; i<size; i++){// 第一个位置留给最优个体
			double p = Math.random();
			int j;
			for (j=0; j<size; j++){
				if (p<probabilities[j+1]) 
					break;
			}			
			if(j==size){
				//System.out.println("j="+(j-1));
				index[i]=j-1;
			}
			else{
				//System.out.println("j="+j);
				index[i] = j;
			}			
		}	*/
		for (int i=1; i<size; i++){// 第一个位置留给最优个体
			double p = Math.random();
			int j;
			for (j=0; j<size; j++){
				if (p<probabilities[j]) 
					break;
			}			
			index[i] = j;		
		}		
		return index;
	}
	/*
	 * 选择操作
	 */
	public Vector select(double[] fitness){
		int[] index=operate(fitness);
		//Vector pop=new Vector();
		int size=index.length;
		Vector population=new Vector();
		for(int i=0;i<size;i++){
			population.add(pop.elementAt(index[i]));
		}
		return population;
	}
}
