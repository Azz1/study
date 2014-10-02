package ga;

import java.util.*;

public class GaInitializer {
	//public GA ga;
	private int size;//��Ⱥ��ģ
	private int genelength;//Ⱦɫ�峤��
	Random rnd=new Random();
	String gene;
	public GaInitializer(int genelength,int size){
		this.genelength=genelength;
		this.size=size;
	}
	public Vector generateInitialPopulation(){
		Vector population=new Vector();
		
		for(int i=0;i<size;i++){
			gene="";
			for(int j=0;j<genelength;j++){
				gene=gene+String.valueOf(rnd.nextInt(2));
			}	
			population.add(gene);
		}
		return population;
	}
}
