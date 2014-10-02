package ga;

import java.util.Vector;

public class SetCoverGAPopulation implements IPopulation {

	SetCoverGA sga = null;
	int[] w;
	
	/**
	 * Generate Initial Population
	 * @throws CloneNotSupportedException 
	 */
	public void generateInitialPopulation(int size, int genelength) throws CloneNotSupportedException {
		Vector pop = sga.getPopulation();
		pop.clear();

		SetCoverGenome Sp = null;
		int knock = 0;
		for(int i=0; i<size; i++) {
			//System.out.println("Generate Population ..." + i);
			do {
				Sp = generateOne();
				knock ++;
				if(knock > BaseGA.Maxknock) break;
			}while(sga.Contains(Sp));
			if(! sga.Contains(Sp)) pop.add(Sp);
			knock = 0;
		}		
	}

	/**
	 * Generate One Solution
	 * @throws CloneNotSupportedException 
	 */
	private SetCoverGenome generateOne() throws CloneNotSupportedException {
		w = new int[sga.row]; 
		for(int i=0;i<sga.row;i++) w[i] = 0;
		
		SetCoverGenome Sp = new SetCoverGenome(sga);
		for(int i=0; i<sga.row; i++) {
			boolean allzero = true;
			for(int j=0; j<sga.col; j++)
				if(sga.input[i][j] > 0) {
					allzero = false;
					break;
				}
			if(! allzero) {
				int j;
				do {
					j = BaseGA.rnd.nextInt(sga.col);
				} while(sga.input[i][j] == 0);
				Sp.setAt(j,Sp.getAt(j)+1);
				for(int i1=0; i1<sga.row; i1++)
					if(sga.input[i1][j] != 0)
						w[i1] ++;
			}
		}
		
		/*
		System.out.println("----------------------");
		System.out.print("Sp: ");
		for(int k=0; k<Sp.getLength(); k++)
			System.out.print(" " + Sp.getAt(k));
		System.out.println("");
		for(int ii=0; ii<sga.row; ii++) {
			System.out.print("w("+ii+"):" + w[ii] + " ");
		}
		System.out.println("\n----------------------");
		*/
		
		SetCoverGenome T = (SetCoverGenome) Sp.clone();
		do {
			int j;
			do {
				j = BaseGA.rnd.nextInt(sga.col);
			} while(T.getAt(j) == 0);
			T.setAt(j, T.getAt(j)-1);
		
			boolean clear = true;
			for(int i=0; i<sga.row;i++) {
				if(sga.input[i][j] != 0 && w[i] < 2) {
					clear = false;
					break;
				}
			}
			if(clear) {
				//System.out.println("Clear: "+ (j+1));
				Sp.setAt(j, Sp.getAt(j)-1);
				for(int i=0; i<sga.row;i++) {
					if(sga.input[i][j] != 0) {
						w[i] --;
					}
				}
			}
		
/*			
			System.out.print("T: ");
			for(int k=0; k<T.getLength(); k++)
				System.out.print(" " + T.getAt(k));
			System.out.print("\nSp: ");
			for(int k=0; k<Sp.getLength(); k++)
				System.out.print(" " + Sp.getAt(k));
			System.out.println("");
*/			
		}while(T.hashCode() > 0);
		
		for(int i=0; i<Sp.getLength(); i++) {
			if(Sp.getAt(i) > 0) Sp.setAt(i,1);
		}
		return Sp;
	}

	/**
	 * Must be called before use
	 */
	public void setGA(BaseGA ga) {
		sga = (SetCoverGA)ga;
	}

}
