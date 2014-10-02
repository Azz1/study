package ga;

import java.util.Random;
import java.util.Vector;

public class OnePointCrossover {
	Random rnd=new Random();
	double probability;
	Vector pop=new Vector();
	public OnePointCrossover(Vector pop,double probability){
		this.pop=pop;
		this.probability=probability;
	}
	public Vector crossover(){		
		int size=pop.size();
		String[] parents=new String[2];
		Vector population=new Vector();
		for(int i=0;i<size;i+=2){
			parents[0]=(String)pop.get(i);
			parents[1]=(String)pop.get(i+1);
			String[] sons=operate(parents);
			population.add(sons[0]);
			population.add(sons[1]);
		}
		return population;
	}
	public boolean checkProbability(double probability){
		double p;
		p=(double)rnd.nextInt(200001)/200000;
		//System.out.println("p="+p);
		//System.out.println("p-probability="+(p-probability));
		if((p-probability)<=0.000001){
			return true;
		}
		else{
			return false;
		}
	}
	public String[] operate(String[] gene){
		int start=0;
		String[] sons=new String[2];
		/*
		 * 取得父本
		 */
		char[] p0=gene[0].toCharArray();
		char[] p1=gene[1].toCharArray();
		char[] s0=gene[0].toCharArray();
		char[] s1=gene[1].toCharArray();
		int size=p0.length;
		if(checkProbability(probability)){
			start=rnd.nextInt(size-1)+1;
			//System.out.println("交叉位置："+start);
		}
		else{
			start=size;
		}
		if(start!=size){
			for(int i=start;i<size;i++){
				s0[i]=p1[i];
				s1[i]=p0[i];
			}
			sons[0]=new String(s0);
			sons[1]=new String(s1);
		}
		else{
			sons=gene;
		}
		return sons;
	}
}
