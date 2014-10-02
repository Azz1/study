package ga;

import java.util.Vector;

public class Population {
	private Vector chroms=new Vector();

	/*
	 * 添加染色体
	 */
	public void add(String chromosome){
		chroms.add(chromosome);
	}
	/*
	 *取得第i个染色体 
	 */
	public String get(int i){
		return (String)chroms.elementAt(i);
	}
	/*
	 * 返回种群大小
	 */
	public int size(){
		return chroms.size();
	}
	
}
