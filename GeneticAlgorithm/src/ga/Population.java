package ga;

import java.util.Vector;

public class Population {
	private Vector chroms=new Vector();

	/*
	 * ���Ⱦɫ��
	 */
	public void add(String chromosome){
		chroms.add(chromosome);
	}
	/*
	 *ȡ�õ�i��Ⱦɫ�� 
	 */
	public String get(int i){
		return (String)chroms.elementAt(i);
	}
	/*
	 * ������Ⱥ��С
	 */
	public int size(){
		return chroms.size();
	}
	
}
