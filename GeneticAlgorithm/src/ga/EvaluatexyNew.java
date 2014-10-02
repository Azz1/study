package ga;

public class EvaluatexyNew {
	/*
	 * 评价个体的适应度函数值
	 */
	public double evaluate(double x,double y,double max,double min){
		double sum=0.0;
		//y=x*Math.sin(10*Math.PI*x)+2.0;
		//System.out.println("y="+y);
		//y=Math.sin(x)*Math.sin(x);
		/*y=Math.PI*x;
		y=Math.sin(2.0*y);
		y=y*y;*/
		//y=x*x;
		//sum=(4-2.1*x*x+Math.pow(x,4))*x*x+x*y+(-4+4*y*y)*y*y;
		//sum=0.5-((Math.sin((Math.sqrt(x*x+y*y)))*Math.sin((Math.sqrt(x*x+y*y))))-0.5)/((1+0.001*(x*x+y*y))*(1+0.001*(x*x+y*y)));
		/*for(int i=1;i<=5;i++){
			sum=sum+(i*Math.cos((i+1)*x+1))*(i*Math.cos((i+1)*y+1))+0.5*((Math.pow(x+1.42513,2)+(Math.pow(y+0.80032,2))));
		}*/
		sum=max-(4-2.1*x*x+Math.pow(x,4))*x*x+x*y+(-4+4*y*y)*y*y+(max-min);
		return sum;
}
}
