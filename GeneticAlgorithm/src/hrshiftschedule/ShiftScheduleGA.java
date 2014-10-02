package hrshiftschedule;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Vector;

import ga.BaseCrossover;
import ga.BaseGA;
import ga.BaseMutation;
import ga.BinaryTournamentSelection;
import ga.FitnessCrossover;
import ga.GAResult;
import ga.IGenome;
import ga.IPopulation;
import ga.ISelection;
import ga.SetCoverGA;
import ga.SetCoverGAPopulation;
import ga.SetCoverGenome;
import ga.VariableMutation;

public class ShiftScheduleGA extends BaseGA {
	
	//Global parameters
	
	public static int NumberOfTeams = 4;	// Number of teams
	public static int NumberOfWorkingBlocks = 8;	// 5 day working block, 0-day shift, 1-night shift

	public static int NumberOf4DayBlocks = 2;	// A block 
	public static int NumberOf3DayBlocks = 3;	// B block 
	public static int NumberOf2DayBlocks = 3;	// C block 	

	//public static int NumberOf4DayBlocks = 1;	// A block 
	//public static int NumberOf3DayBlocks = 5;	// B block 
	//public static int NumberOf2DayBlocks = 2;	// C block 	

	public ShiftScheduleGA(IPopulation populationAPI, ISelection selectionAPI,
			BaseCrossover crossoverAPI, BaseMutation mutationAPI) {
		super(populationAPI, selectionAPI, crossoverAPI, mutationAPI);
		populationAPI.setGA(this);
		selectionAPI.setGA(this);
		crossoverAPI.setGA(this);
		mutationAPI.setGA(this);
	
		Maxknock = 200;
		setMaxgen(40000);
		setPopsize( 200 );
	}

	//static double[] fitnessy;
	
	public void PrintPopulation() {
		int i = 0;
		System.out.println("************** Population (t = "+ t +") ***************");
		for(Object p : getPopulation()) {
			i ++;
			System.out.println(i + ":\n" );
			((ShiftScheduleGenome)p).print();
		}
	}

	private double getAvgFitness() {
		double ret = 0.0;
		int c = 0;
		for(Object o : getPopulation()) {
			ret += ((ShiftScheduleGenome)o).getBufferedFitness();
			c ++;
		}
		return ret/(1.0*c);
	}

	/**
	 * Check if g exists in population
	 * @param g
	 * @return
	 */
	@Override
	public boolean Contains(IGenome g) {
		for(Object p : getPopulation()) {
			//System.out.println("Hash:" + p.hashCode());
			if(((ShiftScheduleGenome)p).toString().equals(((ShiftScheduleGenome)g).toString()))
				return true;
		}
		return false;
	}

	@Override
	public GAResult Eval() throws CloneNotSupportedException, Exception {
		int knock = 0;
		t = 0;
		GAResult ret = new GAResult();

		ret.startTime = System.currentTimeMillis();
		
		// Generate population
		getPopulationAPI().generateInitialPopulation(getPopsize(), getGenelength());

		PrintPopulation();

		while(t < getMaxgen()) {
		//while(true) {			
			// Select parents
			Vector selected = getSelectionAPI().doSelect();
			//System.out.println("Selected parents:");
			//System.out.println(selected.get(0).toString());
			//System.out.println(selected.get(1).toString());
			
			// Crossover
			ShiftScheduleGenome c = (ShiftScheduleGenome) getCrossoverAPI().doCrossover((IGenome)selected.get(0), (IGenome)selected.get(1));
			//System.out.println("New generation after crossover:");
			//System.out.println(c.toString());

			// Mutation
			getMutationAPI().doMutation(c);
			//System.out.println("New generation after mutation:");
			//System.out.println(c.toString());
						
			if(Contains(c)) {
				knock ++;
				if(knock >= BaseGA.Maxknock) break;
				else continue;
			}
			else {
				t ++;
				
				// Replace one with new generation, steady-state replacement method
				double avg_fitness = getAvgFitness();
				int replace = 0;
				double replacefitness = 0.0;
				do{
					replace = BaseGA.rnd.nextInt(getPopulation().size());
					replacefitness = ((ShiftScheduleGenome)getPopulation().get(replace)).getBufferedFitness();
				}while(avg_fitness >= replacefitness);
				if(replacefitness > c.getBufferedFitness())
					getPopulation().set(replace, c);
				knock = 0;

				// Get the one with smallest fitness as result
				double min_fitness = getAvgFitness();
				int avg = (int)min_fitness;
				for(Object o : getPopulation()) {
					double f = ((ShiftScheduleGenome)o).getFitness();
					if(f <= min_fitness) {
						min_fitness = f;
						ret.g = (ShiftScheduleGenome)o;
					}
				}
				System.out.println("***** (t = " + t + "  Avg Fitness:" + avg + "  Min Fitness:" + min_fitness + "  Replace Fitness:" + replacefitness + "  New Fitness:" + c.getBufferedFitness() + ") ******");
				if(min_fitness < 0) break;
			}
		}

		ret.endTime = System.currentTimeMillis();
		ret.generations = t;
		return ret;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GAResult result = null;
		
		ShiftScheduleGA ga=new ShiftScheduleGA(
				new ShiftScheduleGAPopulation(),
				new BinaryTournamentSelection(2),
				new ShiftScheduleCrossover(),
				new ShiftScheduleMutation(0.1)
				);
		try {
		
		// Generate Initial Solutions
			result = ga.Eval();
			System.out.println("********************************************************");
			//ga.printInput();
			System.out.println("Find optimal solution: \n" );
			((ShiftScheduleGenome)result.g).print();
			System.out.println("  M: " + result.generations);
			System.out.println("  time eclipsed: " + (result.endTime-result.startTime) + " ms");
			
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
