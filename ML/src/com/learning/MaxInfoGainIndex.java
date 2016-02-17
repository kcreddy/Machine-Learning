package com.learning;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MaxInfoGainIndex{
  public int max_Information_Att_number(ArrayList<ArrayList<String>> lines,double ENTROPY){
  
	  double info_gain=0;	  
	  ArrayList infoList= new ArrayList<Double>();
	  
	  for(int i=0; i < lines.get(0).size();i++)
	  {
		  ArrayList<String> currentColumn = new ArrayList<String>();
		  ArrayList<String> mergeList = new ArrayList<String>();
		  for(int j=0; j<lines.size(); j++)
		  
		  {
          currentColumn.add(lines.get(j).get(i));
          mergeList.add(lines.get(j).get(i)+lines.get(j).get(0));
		  }

		  Set<String> uniqueST = new HashSet<String>(currentColumn);
		  Set<String> peCombinationST = new HashSet<String>(mergeList);

		  List<String> unique= new ArrayList<String>(uniqueST);
		  List<String> peCombination= new ArrayList<String>(peCombinationST);
		  List<Integer> frequencyCount= new ArrayList<Integer>();

		  Collections.sort(unique);
		  Collections.sort(peCombination);

		  for (String key : unique) {
			  //System.out.println("This is for column "+ i);
			  //System.out.print(key + ": " + Collections.frequency(currentColumn, key));
		  		}

		  for (String combinationKey:peCombination) {
			  
			  //System.out.println(combinationKey + ": " + Collections.frequency(mergeList, combinationKey));
			  frequencyCount.add(Collections.frequency(mergeList, combinationKey));
		  		}

		  RemoveDup rem_dup_obj=new RemoveDup();
		  info_gain= rem_dup_obj.calculate_Info_Gain(peCombination,frequencyCount,ENTROPY,lines.size());
		  //System.out.println(info_gain+"for att number "+i);
		  infoList.add(info_gain);
    
    }
	  infoList.remove(0);
	  //System.out.println("The attribute which gives the max info has number .."+(1+infoList.indexOf(Collections.max(infoList))));
	  return infoList.indexOf(Collections.max(infoList))+1;
}
}
