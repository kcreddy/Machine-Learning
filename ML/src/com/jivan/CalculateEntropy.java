package com.jivan;

import java.util.ArrayList;

public class CalculateEntropy {
	
	public static double cal_entropy(ArrayList<ArrayList<String>> S){
		double ent=0;
		double p=0,e=0;
		double k1,k2;
		String str = "";
		for (int i= 0; i<S.size();i++)
		{
				str = S.get(i).get(0);
				if(str.equals("p"))
				{
					p = p+1;
				}

				else {
					e = e+1;
							}
		}

		k1 = p/S.size();

		k2 = e/S.size();
		ent = ((k1)*(Math.log(k1)/Math.log(2))) + ((k2)*(Math.log(k2)/Math.log(2)));
		
		return -ent;
	};
}
