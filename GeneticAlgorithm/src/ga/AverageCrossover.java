package ga;

import java.util.Random;
import java.util.Vector;

public class AverageCrossover {
	Random rnd=new Random();
	Vector pop=new Vector();
	double probability;
	/*
	 * ���캯����ʼ��
	 */
	public AverageCrossover(Vector pop,double probability){
		this.pop=pop;
		this.probability=probability;
	}
	/*
	 * ִ�о��Ƚ������
	 */
	public Vector averagecrossover(){		
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
	/*
	 * �����ʷ�����
	 */
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
	/*
	 *����ʵʩ���Ƚ���ϸ��
	 */
	public String[] operate(String[] gene){
		//int start=0;
		String[] subpar=new String[2];
		String[] sons=new String[2];
		/*
		 * ȡ�ø���
		 */
		char[] p0=gene[0].toCharArray();//������1
		char[] p1=gene[1].toCharArray();//������2
		
		int genelength=p0.length;//ÿһ������ĳ���
		subpar[0]="";
		subpar[1]="";
		if(checkProbability(probability)){
			/*
			 * �ֱ������Ӧ������������������
			 */
			for(int i=0;i<genelength;i++){
				subpar[0]=subpar[0]+String.valueOf(rnd.nextInt(2));//�������1
				subpar[1]=subpar[1]+String.valueOf(rnd.nextInt(2));//�������2
			}
			char[] A=subpar[0].toCharArray();
			char[] B=subpar[1].toCharArray();
			/*
			 * ���������Ѹ������Ϊ�Ӹ���
			 */
			for(int j=0;j<A.length;j++){
				if(A[j]==0){
					p0[j]=p1[j];
				}
				if(B[j]==1){
					p1[j]=p0[j];
				}
			}
			sons[0]=new String(p0);
			sons[1]=new String(p1);
		}
		else{
			sons=gene;
		}
		
		return sons;
	}
}
