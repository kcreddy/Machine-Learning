package com.jivan;

import java.awt.List;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DataSplit{
public ArrayList<ArrayList<ArrayList<String>>> split_data(ArrayList<ArrayList<String>> S, int att_num_for_split) throws FileNotFoundException{
  int unique = 0;
  ArrayList<String> un = (ArrayList<String>) (new AttributeValues()).getDistinctValues(att_num_for_split);
  ArrayList<ArrayList<String>> sub;
  
  unique = un.size();
  
  ArrayList<ArrayList<ArrayList<String>>> S_sets = new ArrayList<ArrayList<ArrayList<String>>>(unique);
  ArrayList<ArrayList<String>> empty = new ArrayList<ArrayList<String>>();
  empty.add(S.get(0));
/*		for (int j = 0; j<unique;j++)
  {
    S_sets.add(j,empty);
  }*/
  for (int j = 0; j<unique;j++)
  {
    sub = new ArrayList<ArrayList<String>>();
    for (int i = 0; i<S.size(); i++)
    {
      //System.out.println(un.get(j));
      if (S.get(i).get(att_num_for_split).equals(un.get(j)))
      {
        sub.add(S.get(i));
        //System.out.println(i);
        //S_sets.get(j).add(S.get(i));
      }
    }
    S_sets.add(sub);
    //System.out.println(S_sets);
  }
  return S_sets;
}
}
