/**
 * 
 */
package edgecover;

import java.util.Random;
import java.util.Stack;

import ga.BaseCrossover;
import ga.BaseGA;
import ga.BaseMutation;
import ga.GAResult;
import ga.IPopulation;
import ga.ISelection;
import ga.SetCoverGenome;

/**
 * @author jack
 *
 */
public class EdgeCoverGA extends BaseGA {
	int order; 		//the number of vertices
	int size; 		//the number of edges
	int diameter; 	//the diameter of the graph
	int degree;		//the total unsymmetric degrees
	int cookie;		//the number of cookie configurations, i.e., all the possible combination of cookies with difference values
					//for each vertex, all the incoming edges should be assigned with the same cookie configuration
	int hopRate; 		//a percentage that (edges changing cookie values)/size

	int[][][] graph;  	//graph[i][j][0] = x means there are x edges from vertex i to j
						//graph[i][j][1] = x means x edges from vertex i to j have been visited
						//graph[i][j][2] = x means cookie configuration No.
	int[][] distance; 	//the length of the shortest path from vertex i to j
	
	int recovery; //an index to back-trace which vertex is used to connect to unvisited edges
	int[] used; //record which vertex is used to connect to unvisited edges

	/**
	 * @param populationAPI
	 * @param selectionAPI
	 * @param crossoverAPI
	 * @param mutationAPI
	 */
	public EdgeCoverGA(int p, int q, int c, int r, IPopulation populationAPI, ISelection selectionAPI,
			BaseCrossover crossoverAPI, BaseMutation mutationAPI) {
		super(populationAPI, selectionAPI, crossoverAPI, mutationAPI);
		order = p;
		size = q;
		cookie = c;
		hopRate = r;
		graph = new int[order][order][3];
		distance = new int[order][order];
		used = new int[order];
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ga.BaseGA#Eval()
	 */
	@Override
	public GAResult Eval() throws CloneNotSupportedException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void graphGenerator(){

		Random random = new Random();
		int hopEdges = 0;

		if(cookie > 1){
			hopEdges = size * hopRate /100;
			if(hopEdges < cookie){	//No. of hopping edges should be enough to make G strongly connected
				hopEdges = cookie;
			}
		//	System.out.println("No. of hoping edges is " + hopEdges);
		}


		//randomly assign a cookie configuration on each vertex, i.e., all the incoming edges for a vertex will be associated with the same cookie configuration
		int configuration = 0;
		for(int i = 0; i < order; i++){
			//cookie = 1 means there is no cookie variables
			//cookie = 2, typically, login-in and login-out
			if(cookie > 1){
				configuration = random.nextInt(cookie);
			//	System.out.println("vertex " + i + " has cookie configuration NO. " + configuration);
				for(int j = 0; j < order; j++){
					graph[j][i][2] = configuration;
				}
			}
		}


		//make the graph strongly connected first
		//case 1: connect vertices of the same cookie configuration with a path
		//case 2: select a bridge edge to connect paths of different cookie configurations
		int start = 0, end = 0, tmpEnd = 0;
		int counter = 0; // no. of edges have already been generated
		boolean turn = true;

		if(cookie == 1){
			for(int i = 0; i < order; i++){
				graph[i][(i+1)%(order)][0]++;
				counter++;
			//	System.out.println(i + " to " + (i+1)%(order) + " has edge " + graph[i][(i+1)%(order)][0]);
			}
		}
		else{
			for(int k = 0; k < cookie; k++){
				turn = true;
				for(int i = 0; i < order; i++){
					if(graph[0][i][2] == k){
						end = i;
						if(k == 0) start = i;
						if((k > 0) && turn){ //case 2
							turn = false;
							graph[tmpEnd][end][0]++;
							counter++;
							hopEdges--;
					//		System.out.println(tmpEnd + " to " + end + " has edge " + graph[tmpEnd][end][0]);
						}
						for(int j = i + 1; j < order; j++){
							if(graph[0][j][2] == k){
								graph[end][j][0]++;
					//			System.out.println(end + " to " + j + " has edge " + graph[end][j][0]);
								counter++;
								end = j;
							}
						}
						tmpEnd = end;
						break;
					}
				}
			}
			//complete the tour
			graph[tmpEnd][start][0]++;
		//	System.out.println(tmpEnd + " to " + start + " has edge " + graph[tmpEnd][start][0]);
			counter++;
			hopEdges--;
		}

		//generate the rest of edges randomly
		while(counter < size){
			start = random.nextInt(order);
			end = random.nextInt(order);
			if((hopEdges > 0) && graph[0][start][2] != graph[0][end][2]){
				graph[start][end][0]++;
			//	System.out.println(start + " to " + end + " has edge " + graph[start][end][0]);
				hopEdges--;
				counter++;
			}
			if((hopEdges <= 0) && graph[0][start][2] == graph[0][end][2]){
				graph[start][end][0]++;
				counter++;
			//	System.out.println(start + " to " + end + " has edge " + graph[start][end][0]);
			}
		}

		while((hopEdges > 0 ) || !isStronglyConnected()){
			System.out.println("After: graph is generated again!");
			reset();
			graphGenerator();
		}
		//calculate the diameter
		diameter = calculateDiameter();
		//System.out.println("The diameter of the graph is " + diameter);

//		degree = calculateUnsymmetricDegrees();
//		System.out.println("The total unsymmetric degree of the graph is " + degree);
//		System.out.println(diameter + " - " + degree);


	}

	public boolean isStronglyConnected(){
		boolean connected = true;
		boolean[][] connectivity = new boolean[order][order];

		for(int i = 0; i < order; i++){
			for(int j = 0; j < order; j++){
				if((i == j) || (graph[i][j][0]>0)) connectivity[i][j] = true;
			}
		}

		boolean update = true;
		while(update){
		  update = false;
			for(int k = 0; k < order; k++ )
			  for(int i = 0; i < order; i++ )
				  if( connectivity[i][k] )
					  for(int j = 0; j < order; j++ )
						  if( connectivity[k][j] && !connectivity[i][j]){
							  connectivity[i][j] = true;
							  update = true;
						  }
		}

		for(int i = 0; (i < order) && connected; i++){
			for(int j = 0; (j < order) && connected; j++){
				if(!connectivity[i][j]){
					connected = false;
				}
			}
		}

		return connected;
	}

	public void reset(){ //reset the graph

		for(int i = 0; i < order; i++)
			for(int j = 0; j < order; j++)
				for(int k = 0; k < 3; k++)
					graph[i][j][k] = 0;
	}

	public int calculateDiameter(){
		//initialize distance[][]
		for(int i = 0; i < order; i++)
			for(int j = 0; j < order; j++)
				distance[i][j] = 0;

		for(int i = 0; i < order; i++){		//calculate the shortest paths from i to all the other vertices
			calculateShortestPath(i);
		}

		int diameter = 0;
		for(int i = 0; i < order; i++){
			for(int j = 0; j < order; j++){
			//	System.out.println(i + " to " + j + " distance " + distance[i][j]);
				if(distance[i][j] > diameter)
					diameter = distance[i][j];
			}
		}
		return diameter;
	}

	public void calculateShortestPath(int current){

		Stack<String> reached = new Stack<String>();			//use the breadth first search

		for(int i = 0; i < order; i++){
			if((graph[current][i][0] > 0) && (current != i)){
				distance[current][i] = 1;
				reached.push(String.valueOf(i));
			}
		}

		processStack(2, current, reached);
	}

	public void processStack(int countRounds, int current, Stack<String> reached){
		Stack<String> next = new Stack<String>();

		boolean flag = false;
		for(int i = 0; i < order; i++){
			if((distance[current][i] == 0) && (i != current))
				flag = true;
		}

		int tmp = 0;
		while(!reached.empty() && flag){
			tmp = Integer.parseInt(reached.pop());
			for(int i = 0; i < order; i++){
				if((graph[tmp][i][0] > 0) && (current != i) && (distance[current][i] == 0)){
					distance[current][i] = countRounds;
					next.push(String.valueOf(i));
				}
			}
		}

		if(flag){
			countRounds++;
			processStack(countRounds, current, next);
		}
	}

	public void PrintPopulation() {
		int i = 0;
		System.out.println("************** Population (t = "+ t +") ***************");
		for(Object p : getPopulation()) {
			i ++;
			//System.out.println(i + ":" + ((EdgeCoverGenome)p).toString());
		}
	}

	public void printInput() {
		System.out.println("Input matrix -->");
		System.out.print("\t");
		
		/*
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
		*/
		System.out.println();		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/*
		  
		 EdgeCoverGA eg = new EdgeCoverGA(100, 150, 4, 10);
		eg.graphGenerator();
*/
		// TODO Auto-generated method stub

	}

}
