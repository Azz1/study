/*
 * Created on 2007-5-22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ga;

import java.util.Random;
import java.util.Vector;

/**
 * @author DS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Mutation {
	Random rnd=new Random();
	Vector pop=new Vector();
	double probability=0.0;
	/*
	 * 初始化
	 */
	public Mutation(Vector pop,double probability){
		this.pop=pop;
		this.probability=probability;
	}
	/*
	 * 检查概率
	 */
	public boolean checkProbability(double probability){
		double p;
		p=(double)rnd.nextInt(200001)/200000;
		if((p-probability)<=0.00000001){
			return true;
		}
		else{
			return false;
		}
	}
	/*
	 * 变异操作
	 */
	public Vector mutation(){
		int size=pop.size();
		Vector population=new Vector();
		for(int i=0;i<size;i++){
			String str=(String)pop.elementAt(i);
			str=operate(str);
			population.add(str);
		}
		return population;
	}
	/*
	 * 实际变异操作
	 */
	public String operate(String str){
		char[] s=str.toCharArray();
		int size=s.length;
		int index=0;
		for(int i=0;i<size;i++,index++){
			if(!checkProbability(probability)) continue;
			if(s[i]==0){
				s[index]=1;
			}
			else if(s[i]==1){
				s[index]=0;
			}
		}
		//System.out.println(new String(s));
		return new String(s);
	}
}
