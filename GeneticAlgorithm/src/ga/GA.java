package ga;

import java.util.*;

public class GA {
	final static int popsize=600;
	final static int genelength=24;
	final static int maxgen=300;
	double bestFitness=0.0;
	String bestChromosome="";
	int generation=2;
	int bestGeneration=0;
	public long startTime=0;
	public long endTime=0;
	static double[] fitness;
	//static double[] fitnessy;
	public static void main(String[] args) {
		//GA ga=new GA();
		Vector pop=new Vector();
		Vector selectpop=new Vector();
		Vector crossoverpop=new Vector();
		Vector mutationpop=new Vector();
		Vector averagecrossoverpop=new Vector();
		
		Vector popy=new Vector();
		Vector selectpopy=new Vector();
		Vector crossoverpopy=new Vector();
		Vector mutationpopy=new Vector();
		Vector averagecrossoverpopy=new Vector();
		
		for(int j=0;j<20;j++){//做N次遗传算法程序
			System.out.println("算法第"+(j+1)+"次运行的结果：");
			int gen=1;
			//产生初始种群
			GaInitializer gainitializer=new GaInitializer(genelength,popsize);
			pop=gainitializer.generateInitialPopulation();
			
			GaInitializer gainitializery=new GaInitializer(genelength,popsize);
			popy=gainitializery.generateInitialPopulation();
			
			//评价当前种群，同时保留最优个体
			Decode decode=new Decode();
			//Evaluate evalute=new Evaluate();
			Evaluatexy evaluatexy=new Evaluatexy();
			fitness=new double[popsize];
			
			//fitnessy=new double[popsize];
			for(int i=0;i<popsize;i++){//初始化
				fitness[i]=0.0;
			}
			/*for(int i=0;i<popsize;i++){
				fitness[i]=evalute.evaluate(decode.operate((String)pop.get(i),genelength,-1,2));
				
			}*/
			for(int i=0;i<popsize;i++){
				double x=decode.operate((String)pop.get(i),genelength,-100,100);
				//System.out.println((String)pop.get(i));
				double y=decode.operate((String)popy.get(i),genelength,-100,100);
				//System.out.println("x="+x);
				//System.out.println("y="+y);
				fitness[i]=evaluatexy.evaluate(x,y);
				//System.out.println(fitness[i]);
				
			}
			//EvaluatexyNew evaluatexynew=new EvaluatexyNew();
			while(gen<maxgen){
				//fitnessbf=fitness;
				gen++;
				RouletteSelection select=new RouletteSelection(pop);
				RouletteSelection selecty=new RouletteSelection(popy);
				pop=select.select(fitness);
				popy=selecty.select(fitness);
				
				OnePointCrossover crossover=new OnePointCrossover(pop,0.45);
				OnePointCrossover crossovery=new OnePointCrossover(popy,0.45);
				pop=crossover.crossover();
				popy=crossovery.crossover();
				
				Mutation mutation=new Mutation(pop,0.13);
				Mutation mutationy=new Mutation(popy,0.13);
				pop=mutation.mutation();
				popy=mutationy.mutation();
				
				/*AverageCrossover averagecrossover=new AverageCrossover(pop,0.44);
				AverageCrossover averagecrossovery=new AverageCrossover(popy,0.44);
				pop=averagecrossover.averagecrossover();
				popy=averagecrossovery.averagecrossover();*/
				
				//pop=averagecrossoverpop;
				//popy=averagecrossoverpopy;
			
				
				
				for(int i=0;i<popsize;i++){
					double x=decode.operate((String)pop.get(i),genelength,-100,100);
					double y=decode.operate((String)popy.get(i),genelength,-100,100);
					//System.out.println("x="+x);
					//System.out.println("y="+y);
					fitness[i]=evaluatexy.evaluate(x,y);
				}
			}
			
			//MinFitness minfitness=new MinFitness();
			//double x=minfitness.calminfitness(fitness);
			MaxFitness maxfitness=new MaxFitness();
			double x=maxfitness.calmaxfitness(fitness);
			System.out.println("最优个体的适应度值为："+x);
			System.out.println("*****************************************");
		}
}
}
