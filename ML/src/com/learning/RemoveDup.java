package com.learning;
import java.util.*;


public class RemoveDup{


  public double calculate_Info_Gain(List<String> peArray_list,List<Integer> pe_freq_count,double mainEntropy, int listSize)
            {		
	  				
	  				seperatePE(peArray_list);
	  				this.dataCleaning(peArray_list,pe_freq_count);
                  
	  				
                  int num_dis_ele=peArray_list.size();
                  double ele_p=0,ele_e=0,total=0;
                  double first_term=0,second_term=0;
                  
                  // double info_gain_of_element=0;
                  for(int element_num=0;element_num<num_dis_ele;element_num+=2)
                  {
                    ele_p=pe_freq_count.get(element_num);
                    ele_e=pe_freq_count.get(element_num+1);
                    total=ele_p+ele_e;
                    first_term=-(ele_p/listSize)*(Math.log(ele_p/total))/Math.log(2);
                    second_term=-(ele_e/listSize)*(Math.log(ele_e/total))/Math.log(2);
                    
                                      
                    mainEntropy=mainEntropy-(first_term+second_term);
                    
                  }

                  return mainEntropy;
            }

  public void dataCleaning(List<String> pe_list_to_be_cleaned,List<Integer> corresponding_freq_count)
            {      int currentPosition=0;
                   while(currentPosition<pe_list_to_be_cleaned.size()){

                      if(Collections.frequency(pe_list_to_be_cleaned,pe_list_to_be_cleaned.get(currentPosition))==1)
                      {pe_list_to_be_cleaned.remove(currentPosition);
                      corresponding_freq_count.remove(currentPosition);}
                      else{currentPosition+=2;}            }

                      //seperatePE(pe_list_to_be_cleaned);
            }

  public void seperatePE(List<String> list_arr)
            { String getLetter;
              for(int i=0;i<list_arr.size();i++)
              { getLetter= Character.toString(list_arr.get(i).charAt(0));
            	list_arr.remove(i);
                list_arr.add(i, getLetter);}
            }
}
