package ga;

public class Decode {
	
	//Population population=new Population();
	public double operate(String gene,int genelength,double a,double b){
		double sum=0.0;		
		double accum=0.0;
		int k=0;
		for(int j=genelength-1;j>=0;j--){			
			sum=sum+Integer.parseInt((String.valueOf(gene.charAt(j))))*Math.pow(2,k);
			k++;
		}	
		double p=(Math.pow(2,genelength)-1);
		//System.out.println("sum="+sum);
		accum=a+(sum*(b-a))/p;
		//System.out.println("accum="+accum);
		//accum=sum/(Math.pow(2,genelength)-1);
		//accum=sum;
		return accum;
	}
}
