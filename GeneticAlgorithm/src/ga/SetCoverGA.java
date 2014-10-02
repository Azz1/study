package ga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class SetCoverGA extends BaseGA {
	
	public SetCoverGA(IPopulation populationAPI, ISelection selectionAPI,
			BaseCrossover crossoverAPI, BaseMutation mutationAPI) {
		super(populationAPI, selectionAPI, crossoverAPI, mutationAPI);
		populationAPI.setGA(this);
		selectionAPI.setGA(this);
		crossoverAPI.setGA(this);
		mutationAPI.setGA(this);
	}

	// Input matrix IxJ, I: set of all rows, J: set of all columns 
	public int[][] input = {
			{0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0},
			{0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0},
			{0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1}, 
			{1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0}, 
			{0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0}, 
			{0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0}, 
			{0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 1}, 
			{0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1}, 
			{0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0},
			{1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1},
			{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}};
	public int row = 11, col = 12;
	
	// Cost vector for set cover problem
	public double[] cost = {1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0};
	
	public int [] off_target_list;
	
	// Input array without taking away offtarget genes
	public int [][] original_input;
	public int original_row, original_col;

	private int [] off_target_gene_flag;
	private int [] off_target_flag;
	private int [] col_map;		// idx: new column, val: original column
	private int [] row_map;		// idx: new row, val: original row
	
	public int getOriginalCol(int c) {
		return col_map[c];
	}
	
	public void LoadData(String fname, int orow, int ocol, int [] offtarget) throws FileNotFoundException  {
		// read data from file into original_input
		File f = new File(fname);
		FileReader fr = new FileReader(f);
		long filesize = f.length();
		BufferedReader reader = new BufferedReader(fr);
		try {
			String s = reader.readLine();
			original_row = Math.min(orow, (int)(filesize/s.length()));
			original_col = Math.min(ocol, s.length());
			original_input = new int[original_row][original_col];
			off_target_gene_flag = new int[original_col];
			off_target_flag = new int[original_row];
			col_map = new int[original_col];
			row_map = new int[original_row];
			
			for(int r=0; r<original_row;r++) {
				for(int c=0; c<original_col;c++)
					original_input[r][c] = s.charAt(c)-'0';
				s = reader.readLine();
			}
				
		} catch (IOException e) {
			// end of file
		}
		
		// Off target processing
		if(offtarget == null) {
			row = original_row;
			col = original_col;
			input = new int[row][col];
			cost = new double[col];
			for(int c = 0; c<col; c++) {
				cost[c] = 1.0;
				col_map[c] = c;
			}
			for(int r = 0;r<row;r++) {
				row_map[r] = r;
				for(int c = 0; c<col; c++) {
					input[r][c] = original_input[r][c];
				}
			}
		} else {
			row = 0;
			col = 0;
			
			for(int c = 0; c<original_col; c++) {
				off_target_gene_flag[c] = 0;
			}
			for(int r = 0; r<original_row; r++) {
				off_target_flag[r] = 0;
			}
			for(int i=0; i<offtarget.length;i++) {
				off_target_flag[offtarget[i]] = 1;
				for(int c=0;c<original_col;c++) {
					if(original_input[offtarget[i]][c] != 0) {
						off_target_gene_flag[c] = 1;
					}
				}
			}

			for(int r=0;r<original_row;r++) {
				if(off_target_flag[r] == 0) row ++;
			}
			for(int c=0;c<original_col;c++) {
				if(off_target_gene_flag[c] == 0) col ++;
			}
			input = new int[row][col];
			cost = new double[col];
			for(int c = 0; c<col; c++) {
				cost[c] = 1.0;
			}
			
			int r1=0;
			for(int r=0;r<original_row;r++) {
				if(off_target_flag[r] == 0) {
					row_map[r1] = r;
					
					int c1=0;
					for(int c=0;c<original_col;c++) {
						if(off_target_gene_flag[c] == 0) {
							col_map[c1] = c;
							input[r1][c1] = original_input[r][c];
							c1 ++;
						}
					}
					r1 ++;
				}
			}
		}
		
		// Generate Input array
		
		genelength = col;
		
		Maxknock = 200;
		Maxgen = 100;
		popsize= 30;
	}
	
	//static double[] fitnessy;
	
	public void PrintPopulation() {
		int i = 0;
		System.out.println("************** Population (t = "+ t +") ***************");
		for(Object p : population) {
			i ++;
			System.out.println(i + ":" + ((SetCoverGenome)p).toString());
		}
	}

	public void printInput() {
		System.out.println("Input matrix -->");
		System.out.print("\t");
		for(int c=0;c<original_col;c++) {
			if((c+1)%10==0)
				System.out.print(((c+1)/10)%10);
			else
				System.out.print(' ');
		}
		System.out.println();
		System.out.print("\t");
		for(int c=0;c<original_col;c++) {
			System.out.print((c+1)%10);
		}
		System.out.println();
		System.out.print("\t");
		for(int c=0;c<original_col;c++) {
			if(off_target_gene_flag[c] != 0)
			   System.out.print('X');
			else
			   System.out.print(' ');
		}

		for(int r=0;r<original_row;r++) {
			System.out.println();
			if(off_target_flag[r] != 0)
				System.out.print("X"+r+":\t");
			else
				System.out.print(" "+r+":\t");
			for(int c=0;c<original_col;c++) {
				System.out.print(original_input[r][c]);
			}
		}
		
		System.out.println();		
	}
	
	@Override
	public GAResult Eval() throws Exception {
		int knock = 0;
		t = 0;
		GAResult ret = new GAResult();

		ret.startTime = System.currentTimeMillis();
		
		// Generate population
		getPopulationAPI().generateInitialPopulation(popsize, genelength);

		PrintPopulation();

		while(t < Maxgen) {
			System.out.println("*************** (t = " + (t+1) + ") *********************");
			
			// Select parents
			Vector selected = getSelectionAPI().doSelect();
			System.out.println("Selected parents:");
			System.out.println("\t"+selected.get(0).toString());
			System.out.println("\t"+selected.get(1).toString());
			
			// Crossover
			SetCoverGenome c = (SetCoverGenome) getCrossoverAPI().doCrossover((IGenome)selected.get(0), (IGenome)selected.get(1));
			System.out.println("New generation after crossover:");
			System.out.println("\t"+c.toString());

			// Mutation
			getMutationAPI().doMutation(c);
			System.out.println("New generation after mutation:");
			System.out.println("\t"+c.toString());
			
			// Apply Heuristic Feasibility Operator
			c.HeuristicFeasiOp();
			System.out.println("New generation after feasibility convertion:");
			System.out.println("\t"+c.toString());
			
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
				do{
					replace = BaseGA.rnd.nextInt(population.size());
				}while(avg_fitness > ((SetCoverGenome)population.get(replace)).getFitness());
				population.set(replace, c);
				knock = 0;
			}
			
		}

		// Get the one with smallest fitness as result
		double min_fitness = getAvgFitness();
		for(Object o : population) {
			double f = ((SetCoverGenome)o).getBufferedFitness();
			if(f <= min_fitness) {
				min_fitness = f;
				ret.g = (SetCoverGenome)o;
			}
		}
		ret.endTime = System.currentTimeMillis();
		ret.generations = t;
		return ret;
	}

	private double getAvgFitness() {
		double ret = 0.0;
		int c = 0;
		for(Object o : population) {
			ret += ((SetCoverGenome)o).getBufferedFitness();
			c ++;
		}
		return ret/(1.0*c);
	}

	public static void main(String[] args) {
		GAResult result = null;
		int [] offtarget = new int[] {3};
		
		SetCoverGA ga=new SetCoverGA(
				new SetCoverGAPopulation(),
				new BinaryTournamentSelection(2),
				new FitnessCrossover(),
				new VariableMutation(2, 6, 0.5)
				);
		try {
			ga.LoadData("xp1_1.matrix.txt", 24, 80, offtarget);
		
		// Generate Initial Solutions
			result = ga.Eval();
			System.out.println("********************************************************");
			ga.printInput();
			System.out.println("Find optimal solution: " + result.g.toString());
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
