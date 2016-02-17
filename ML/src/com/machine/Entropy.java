/**
 * @author KrishnaChaitanyaReddyBurri + JivanPatil
 *
 * CS 529 - Machine Learning Project 1 - Detection of poisonous mushrooms with Decision Trees 
 *
 * ****/
//import relevant packages and libraries
package com.machine;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import java.io.FileWriter;

import com.learning.*;

public class Entropy extends Attribute {
	// 'ha' hashmap maps attribute name to attribute number(attribute position in data) 
   static HashMap<String, String> ha = new HashMap<String, String>();
   // arraylist of type attribute to store list of all 22 attributes
   static ArrayList<Attribute> attr_all = new ArrayList<Attribute>();
   // store training data in lines
   static ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
   // store testing data in lines_test
   static ArrayList<ArrayList<String>> lines_test = new ArrayList<ArrayList<String>>();
// store validation data in lines_valid
   static ArrayList<ArrayList<String>> lines_valid = new ArrayList<ArrayList<String>>();
   // store chi_square_table data in lines_chi_table
   static ArrayList<ArrayList<String>> lines_chi_table = new ArrayList<ArrayList<String>>();
   // maintaining root status
   static boolean root_status = false;
   // hashmap to store chi_square_table values effectively. Example: <["10","alpha=0.05"],21.07>
   static HashMap<ArrayList<String>, String> chi_map =  new HashMap<ArrayList<String>, String>();
   // head node or root of the decision tree 
   static Attribute root = new Attribute();
    static ArrayList<ArrayList<String>> data_latest = new ArrayList<ArrayList<String>>();
	static ArrayList<ArrayList<ArrayList<String>>> data_split_latest= new ArrayList<ArrayList<ArrayList<String>>>();
	static Attribute attr_latest = new Attribute();
	static Attribute attr_next = new Attribute();
	static ArrayList<Attribute> attr_list_latest = null;
	static ArrayList<Attribute> attr_list_next = new ArrayList<Attribute>();
	static ArrayList<Attribute> attr_list_temp = new ArrayList<Attribute>();
	// hashmap to store popped values from stack. Example: <<[Sa],[q_list]>,<<Attribute_latest,Attribute_next>,<attribute_list_latest>>>
	static HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>> map_latest = 
			new HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>, HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>(); 
	// Stack which stores hashmaps of below declaration.  
	static Stack<HashMap<HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>>> stack = 
			new Stack<HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>,HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>>();
	static ArrayList<String> unique_latest = new ArrayList<String>();
	static ArrayList<Attribute> attr_all_minus_class = new ArrayList<Attribute>();
	
	// arraylist for storing testing data
	static ArrayList<String> testing_main = new ArrayList<String>();
	
	// return of build_decision_tree stored in main
	static HashMap<ArrayList<String>, String> main_miserr = new HashMap<ArrayList<String>, String>();
	static HashMap<ArrayList<String>, String> main_infogain = new HashMap<ArrayList<String>, String>();
	static ArrayList<String> key_final =new ArrayList<String>();
	static HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> temp = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();

	static CalculateEntropy cal_entro_obj = new CalculateEntropy();
	static MaxInfoGainIndex max_info_gain_obj= new MaxInfoGainIndex();
	static double ENTROPY;
	static int attributeIndex;

	public static void main(String[] args) {
		// store the unique values of class. add class attribute to all attributes list
		ArrayList<String> cla = new ArrayList<String>( Arrays.asList( new String[]{"p", "e"}));
		Attribute prime = new Attribute();
		prime.h.put("class",cla);                		
		attr_all.add(prime);			
		
		// Reading from training data and storing into lines
		try{
            BufferedReader buf = new BufferedReader(new FileReader("C:/KC/UNM/Spring 2016/Machine Learning/Project1/data/training.txt"));
            ArrayList<String> words;
            String lineJustFetched = null;
            String[] charsArray;
         
            while(true){
                lineJustFetched = buf.readLine();
                if(lineJustFetched == null){  
                    break; 
                }
                else{	charsArray = lineJustFetched.split(",");
	                	words=new ArrayList<String>();
	                	for(String eachchar : charsArray)
	                	{	                		
	                		words.add(eachchar);
	                	}
	                	lines.add(words);                        	               	
                    }
                    }
            buf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        // Reading from attributes data and storing attributes in attr_all. 
        // Create a mapping 'ha' from "attribute_name" to "attribute_number"
        
        try{
            BufferedReader buf2 = new BufferedReader(new FileReader("C:/KC/UNM/Spring 2016/Machine Learning/Project1/data/attributes3.txt"));
            ArrayList<String> words2 = null;
            String line1 = null;
            String line2 = null;
            String[] ch = null;
            String[] c;
            int i = 0;
            while(true){
                line1 = buf2.readLine();
                line2 = buf2.readLine();        
                if(line1 == null)
                {  
                    break; 
                }
                else{  
                		ch = line2.split(",");
                		Attribute attr = new Attribute();
                		words2=new ArrayList<String>();
	                	for(String eachchar : ch){
	                		c = eachchar.split("=");
	                		words2.add(c[1]);
	                	}
                		attr.h.put(line1, words2);
                		attr_all.add(attr);
                		ha.put("class", String.valueOf(0));
                		ha.put(line1,String.valueOf(i+1));
                		i++;
                	}
            }
            attr_all_minus_class = attr_all;
    		attr_all_minus_class.remove(attr_all_minus_class.get(0));
            buf2.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        // Reading from testing.txt file and storing in lines_test
        try{
            BufferedReader buf = new BufferedReader(new FileReader("C:/KC/UNM/Spring 2016/Machine Learning/Project1/data/testing.txt"));
            ArrayList<String> words;
            String lineJustFetched = null;
            String[] charsArray;
         
            while(true){
            	int i= 0;
                lineJustFetched = buf.readLine();
                if(lineJustFetched == null){  
                    break; 
                }

                else{	charsArray = lineJustFetched.split(",");
	                	words=new ArrayList<String>();
	                	for(String eachchar : charsArray){
	                		if (i==0){testing_main.add(eachchar);}
	                		i++;
	                		words.add(eachchar);
	                	}
	                	lines_test.add(words);                        	               	
                    }
                    }
            buf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
     // Reading from validation.txt file and storing in lines_valid
        try{
            BufferedReader buf = new BufferedReader(new FileReader("C:/KC/UNM/Spring 2016/Machine Learning/Project1/data/validation.txt"));
            ArrayList<String> words;
            String lineJustFetched = null;
            String[] charsArray;
         
            while(true){
                lineJustFetched = buf.readLine();
                if(lineJustFetched == null){  
                    break; 
                }

                else{	charsArray = lineJustFetched.split(",");
	                	words=new ArrayList<String>();
	                	for(String eachchar : charsArray){
	                		words.add(eachchar);
	                	}
	                	lines_valid.add(words);                        	               	
                    }
                    }
            buf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        // reading from chi_square_table and storing into lines_chi_table
        try{
            BufferedReader buf = new BufferedReader(new FileReader("C:/KC/UNM/Spring 2016/Machine Learning/Project1/data/chi_square_table.txt"));
            ArrayList<String> words;
            String lineJustFetched = null;
            String[] charsArray;
         
            while(true){
                lineJustFetched = buf.readLine();
                if(lineJustFetched == null){  
                    break; 
                }
                else{	charsArray = lineJustFetched.split(",");
	                	words=new ArrayList<String>();
	                	for(String eachchar : charsArray){
	                		words.add(eachchar);
	                	}
	                	lines_chi_table.add(words);                        	               	
                    }
                    }

            buf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        // Mapping to get chi_map from chi_square_table.txt
        chi_map = get_chi_map(lines_chi_table);

        // Calling function build_decision_tree_miserr to build decision tree based on mis-classification error
        System.out.println("*****************");
        System.out.println("********* Building tree based on Mis-classification error *********");
        build_decision_tree_miserr(lines);
        
        //Now, testing this tree with testing data
        ArrayList<String> cal_decision_value = new ArrayList<String>();
        cal_decision_value = cal_decision_miserr(lines_test);
        System.out.println("Predicted Testing data: "+cal_decision_value);
        System.out.println("---Actual Values in testing.txt-----: "+ testing_main);
        double count=0;
        for (int t = 0; t<cal_decision_value.size();t++)
        {
        	if (cal_decision_value.get(t).equals(testing_main.get(t)))
        	{
        		count++;
        	}
        }
        System.out.println("******* Percentage of matches with testing set: "+ (double)((count/cal_decision_value.size())*100));
            
        // store the result of validation into validation_output
        cal_decision_value = new ArrayList<String>();
        cal_decision_value = cal_decision_miserr(lines_valid);
        System.out.println("Predicted Validation data: "+cal_decision_value);
        
        System.out.println(" --- Storing validation set predictions in validation_miserr.txt file");
        try {
		        PrintWriter pw = new PrintWriter(new FileOutputStream("C:/KC/UNM/Spring 2016/Machine Learning/Project1/data/validation_miserr.txt"));
		        for (String str: cal_decision_value)
		            pw.println(str);
		        pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		// Setting root = null again between 2 builds
		root_status = false;
		
		// Calling function build_decision_tree_gain to build decision tree based on mis-classification error
		System.out.println("*****************");
		System.out.println("********* Building tree based on Information gain of Entropy *********");
        build_decision_tree_gain(lines);
        
        //Now, testing this tree with testing data
        cal_decision_value = new ArrayList<String>();
        cal_decision_value = cal_decision_gain(lines_test);
        System.out.println("Predicted Testing data: "+cal_decision_value);
        System.out.println("---Actual Values in testing.txt-----: "+ testing_main);
        count=0;
        for (int t = 0; t<cal_decision_value.size();t++)
        {
        	if (cal_decision_value.get(t).equals(testing_main.get(t)))
        	{
        		count++;
        	}
        }
        System.out.println("******  Percentage of matches with testing set: "+ (double)((count/cal_decision_value.size())*100));
            
        // store the result of validation into validation_output
        cal_decision_value = new ArrayList<String>();
        cal_decision_value = cal_decision_gain(lines_valid);
        System.out.println("Predicted Validation data: "+cal_decision_value);
        
        System.out.println(" --- Storing validation set predictions in validation_gain.txt file");
        try {
		        PrintWriter pw = new PrintWriter(new FileOutputStream("C:/KC/UNM/Spring 2016/Machine Learning/Project1/data/validation_gain.txt"));
		        for (String str: cal_decision_value)
		            pw.println(str);
		        pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// function to calculate chi_map from chi_square_table.txt
	public static HashMap<ArrayList<String>, String> get_chi_map(ArrayList<ArrayList<String>> S)
	{
		HashMap<ArrayList<String>, String> chi_temp_map = new HashMap<ArrayList<String>, String>();
		ArrayList<String> A = new ArrayList<String>();
		for (int i=0; i<S.size();i++)
		{
			for (int j=0;j<3;j++)
			{
				A = new ArrayList<String>();
				A.add(S.get(i).get(0));
				if (j==0)
				{
					A.add("alpha=0.5");
				}
				else if(j==1)
				{
					A.add("alpha=0.05");
				}
				else A.add("alpha=0.01");
				
				chi_temp_map.put(A,S.get(i).get(j+1));
			}
		}
		return chi_temp_map;
		
	}

	// function to calculate entropy for a dataset
	public static double cal_entropy(ArrayList<ArrayList<String>> S){
		double ent=0;
		double p=0,e=0;
		double k1,k2;
		String str = "";
		// looping through each row in data set
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
		if (p==S.size()) {k2 = 1;}
		if (e==S.size()) {k1 = 1;}
		
		//calcualting entropy (ent)
		ent = ((k1)*(Math.log(k1)/Math.log(2))) + ((k2)*(Math.log(k2)/Math.log(2))); 
		if (ent==0) {return 0;}
		return -ent;
	};
	
	//function to split data based on unique values of Attribute A, given a data set S : and return arraylist of all these datasets
	public static ArrayList<ArrayList<ArrayList<String>>> split_data(ArrayList<ArrayList<String>> S, Attribute A){
		int unique = 0;
		ArrayList<String> un = null;
		ArrayList<ArrayList<String>> sub;		 
		un = A.h.get(A.h.keySet().toArray(new String[A.h.size()])[0]);
		//'un' contains all unique values for Attribute A
		unique = un.size();
		//declaring resultant dataset S_sets
		ArrayList<ArrayList<ArrayList<String>>> S_sets = new ArrayList<ArrayList<ArrayList<String>>>(unique);
		ArrayList<ArrayList<String>> empty = new ArrayList<ArrayList<String>>(); 
		empty.add(S.get(0));
		//looping through each unique value of A
		for (int j = 0; j<unique;j++)
		{
			sub = new ArrayList<ArrayList<String>>();
			//looping each row
			for (int i = 0; i<S.size(); i++)
			{
				if (S.get(i).get(Integer.parseInt(ha.get(A.h.keySet().toArray(new String[A.h.size()])[0]))).equals(un.get(j)))
				{
					sub.add(S.get(i));
				}
			}
			S_sets.add(sub);
		}
		return S_sets;
	}

	//get unique value of attribute A for given dataset S
	public static String get_unique_value(ArrayList<ArrayList<String>> S, Attribute A)
	{
		int unique = 0;
		ArrayList<String> un = null;
		un = A.h.get(A.h.keySet().toArray(new String[A.h.size()])[0]);
		unique = un.size();
		int count=  0;
		String result = null;
		for (int j = 0; j<unique;j++)
		{
			result = un.get(j);
			for (int i = 0; i<S.size(); i++)
			{
				if (S.get(i).get(Integer.parseInt(ha.get(A.h.keySet().toArray(new String[A.h.size()])[0]))).equals(un.get(j)))
				{
					count++;
				}
			}
			if (count==S.size())
			{
				break;
			}
		}
		return result;
	}

	//calculate gain for attribute A based on dataset S
	public static double cal_gain(ArrayList<ArrayList<String>> S, Attribute A)
	{
		double sum = 0;
		double p = 0, e = 0, p_prime =0 , e_prime = 0;;
		ArrayList<ArrayList<ArrayList<String>>> data_after_split= split_data(S,A);
		for (int i=0; i<data_after_split.size();i++)
		{
			ArrayList<ArrayList<String>> ar = data_after_split.get(i);			
			sum = sum+((ar.size()/S.size())*cal_entropy(ar));
		}
		return (cal_entropy(S)-sum);
	}

	//calculate mis-classification error for entire dataset
	public static double cal_miserr_data(ArrayList<ArrayList<String>> data)
	{
		double result = 0;
		int p=0,e=0;
		double maxi =0;
		for (int i = 0; i<data.size();i++)
		{
			if(data.get(i).get(0).equals("p"))
			{
				p++;
			}
			else e++;
		}
		maxi = Math.max(p, e);
		result = (maxi/data.size());
		return result;
	}

	// calculate mis-classification error for given dataset and attribute A
	public static double cal_miserr(ArrayList<ArrayList<String>> data, Attribute A)
	{
		double err = 0;
		double err_data = 0;
		err_data = cal_miserr_data(data);
		double p = 0, e = 0, p_prime =0 , e_prime = 0;;
		ArrayList<ArrayList<ArrayList<String>>> data_after_split= split_data(data,A);
		for (int i=0; i<data_after_split.size();i++)
		{
			ArrayList<ArrayList<String>> ar = data_after_split.get(i);
			p= 0;
			e= 0;
			if (!(ar.size()==0))
			{
				for (int k=0;k<ar.size();k++)
				{
					if(ar.get(k).get(0).equals("p")){p++;}
					if(ar.get(k).get(0).equals("e")){e++;}
				}
				p_prime = p/ar.size();
				e_prime = e/ar.size();
			}
			else 
			{
				p_prime = 1;
				e_prime = 1;
			}
			err = err + ((1-Math.max(p_prime,e_prime))* (double)ar.size()/data.size());
		}
		return (err_data - err);
	}
	
	public static int get_prime(ArrayList<ArrayList<String>> data, Attribute A)
	{
		double err = 0;
		double p = 0, e = 0, p_prime =0 , e_prime = 0;
		
		p= 0;
		e= 0;
		for (int k=0;k<data.size();k++)
		{
			if(data.get(k).get(0).equals("p")){p++;}
			if(data.get(k).get(0).equals("e")){e++;}
		}
		p_prime = p/data.size();
		e_prime = e/data.size();
		if (p_prime==1){return 1;}
		else {return 0;}
			
	}

	// returns the attribute with max misclassification error among List of attributes for a given dataset
 	public static Attribute get_attr_maxerr(ArrayList<ArrayList<String>> S, ArrayList<Attribute> attr_current)

	{
		Attribute A = new Attribute();
		double e = -1,max_err = e;
		for (int p = 0; p<attr_current.size();p++)
		{	
			e = cal_miserr(S, attr_current.get(p));
			if (e>max_err){
				max_err = e;
				A = attr_current.get(p);
			}
		}
		return A;
	}

 	//get key from hashmap for a given value
 	public static Object getKeyFromValue(HashMap hm, Object dat) {
 	    for (Object o : hm.keySet()) {
 	      if (hm.get(o).equals(dat)) {
 	        return o;
 	      }
 	    }
 	    return null;
 	  }
 	
 	//get key from hashmap without any value. That hashmap should only contain one key
 	public static Object getOnlyKeyFromHashMap(HashMap hm) {
 	    for (Object o : hm.keySet()) {
 	        return o;
 	    }
 	    return null;
 	  }

 	// calculate parent attibute for a given attribute and dataset
 	public static Attribute get_parent_Attribute(ArrayList<ArrayList<String>> S, Attribute A)
 	{
 		for (Attribute atr : attr_all )
 		{
 			String k = new String();
 			k = get_unique_value(S, atr);
 			if (atr.getChild_map().get(k) == A)
 			{
 				return atr;
 			}
 		}
		return null;
 	}
 
 	//returns attribute of type object given the attribute's positionin dataset 
 	public static Attribute get_Attribute_from_index(int index)
 	{
 		Integer in = new Integer(index);
 		String v = (String) getKeyFromValue(ha, in.toString());
 		for (Attribute atr: attr_all)
 		{
 			if (atr.h.keySet().toArray(new String[atr.h.size()])[0].equals(v))
 			{
 				return atr;
 			}
 		}
 		return null;
 	}
 	
 	//calcualte number of "p" in dataset
 	public static double get_p_from_data(ArrayList<ArrayList<String>> S)
 	{
 		double res=0;
		double p=0,e=0;
		String str = "";
		for (int i= 0; i<S.size();i++)
		{
			str = S.get(i).get(0);
			if(str.equals("p"))	
			{
				res++;
			}
		}
		return res;
 	}

 	//calcualte number of "e" in dataset
 	public static double get_e_from_data(ArrayList<ArrayList<String>> S)
 	{
 		double res=0;
		double p=0,e=0;
		String str = "";
		for (int i= 0; i<S.size();i++)
		{
			str = S.get(i).get(0);
			if(str.equals("e"))	
			{
				res++;
			}
		}
		return res;
 	}
 	 	
 	// Build tree based on misclassification error for a given data
 	public static void build_decision_tree_miserr(ArrayList<ArrayList<String>> data)
	{
		//declaring variables for function use
 		double def = 0;
		HashMap<ArrayList<ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>> map_copy = 
			new HashMap<ArrayList<ArrayList<String>>, HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>(); 
		HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>> map_inner_copy = 
			new HashMap<HashMap<Attribute,Attribute>, HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>();
		HashMap<ArrayList<ArrayList<String>>, Attribute> child_parent = null;
		ArrayList<ArrayList<String>> data_lat_temp = new ArrayList<ArrayList<String>>();
		//declaring variables for chi_square 
		double ch_p=0, ch_e=0,ch_pi_prime = 0, ch_ei_prime = 0, ch_pi = 0, ch_ei = 0, chi_square = 0;

	try{
		if (!root_status){
			// +++++   Building root    +++++ ;
			data_latest = data;
			attr_list_latest = new ArrayList<Attribute>(attr_all_minus_class);
			//getting root as max_misclassification error
			root = get_attr_maxerr(data_latest,attr_list_latest);
			// assigning attribute_latest = root
			attr_latest = root;
			root_status = true;	
			}
		// unique values that attribute_latest has
		unique_latest = attr_latest.h.get(attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
		//Splitting data using function defined
		data_split_latest = split_data(data_latest,attr_latest);
		Attribute latest_temp = new Attribute();
		ArrayList<String> key_temp = new ArrayList<String>();
		data_lat_temp = data_latest;
		// calculating ch_p, ch_e values for chi_square
		ch_p = get_p_from_data(data_latest);
		ch_e = get_e_from_data(data_latest);
		chi_square = 0;
		
		for (int f = 0; f<unique_latest.size();f++)
		{
			latest_temp = attr_latest;
			//assigning each subset as data_latest
			data_latest = data_split_latest.get(f);
			//chi-square calculations
			ch_pi = get_p_from_data(data_latest);
			ch_ei = get_e_from_data(data_latest);
			ch_pi_prime = ch_p*((ch_pi+ch_ei)/(ch_p+ch_e));
			ch_ei_prime = ch_e*((ch_pi+ch_ei)/(ch_p+ch_e));
			if (ch_pi_prime==0 && ch_ei_prime==0)
			{
				chi_square = chi_square;
			}
			else if (ch_pi_prime==0)
			{
				chi_square = chi_square + (Math.pow((ch_ei-ch_ei_prime),2)/ch_ei_prime);
			}
			else if(ch_ei_prime==0)
			{
				chi_square = chi_square + (Math.pow((ch_pi-ch_pi_prime),2)/ch_pi_prime);
			}
			else
			{
				chi_square = chi_square + (Math.pow((ch_pi-ch_pi_prime),2)/ch_pi_prime) + (Math.pow((ch_ei-ch_ei_prime),2)/ch_ei_prime);	
			}
			
			if (!(data_latest.size()==0) )
			{
				// getting next attribute with max_error
				attr_next = get_attr_maxerr(data_latest,attr_list_latest);
				HashMap<Attribute, Attribute> map_inner_left = new HashMap<Attribute, Attribute>();
				HashMap<ArrayList<Attribute>, ArrayList<Attribute>> map_inner_right = new HashMap<ArrayList<Attribute>, ArrayList<Attribute>>();
				HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>> map_inner = 
						new HashMap<HashMap<Attribute,Attribute>, HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>();
				HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>> map = 
						new HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>, HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>(); 
				HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> map_left_part = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
				
				// adding values into smaller maps below so that bigger map "map" will be pushed into stack 
				// this "map" is of type: <<data_latest,key_final>,map_inner>
				map_inner_left.put(attr_latest,attr_next);
				attr_list_temp = new ArrayList<Attribute>(attr_list_latest);
				attr_list_temp.remove(attr_next);
				attr_list_next = new ArrayList<Attribute>(attr_list_temp);
				map_inner_right.put(attr_list_latest,attr_list_next);
				map_inner.put(map_inner_left, map_inner_right);
				
				// key_final stores the question list until this point. i.e., the question asked at each stage of the branch
				// key_temp is a helper variable to get previous question list from parent and concatenate to get key_final
				while(latest_temp.getParent_map()!=null)
				{
					if(latest_temp.getParent_map()!=null)
					{
						latest_temp = get_parent_Attribute(data_lat_temp, latest_temp);
						key_temp.add(0,get_unique_value(data_lat_temp, latest_temp));
					}
					else {
						break;
					}
				}
				key_final = key_temp;
				key_temp = new ArrayList<String>();
				
				//if no parent i.e., for root-> key_final = null because no question was asked for root
				if (attr_latest.getParent_map()==null)
				{
					key_final = new ArrayList<String>();
				}
				// add latest question-> based on data_latest,attr_latest to key_final
				key_final.add(get_unique_value(data_latest,attr_latest));
				
				//defining final variables to be added into "map"
				map_left_part.put(data_latest,key_final);
				map.put(map_left_part,map_inner);
				//pushing "map" onto stack
				stack.push(map);
				map_inner_copy = map_inner;
			}

		}
		
		System.out.println("chi_square value for attr_latest "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]+ 
				" is:  "+ chi_square);
		Integer dof = new Integer(0);
		dof = (attr_latest.h.get(attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]).size())-1;
		System.out.println("DOF: "+dof);
		System.out.println("unique values: "+attr_latest.h.get(attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]));
		for (int i=0; i<3;i++)
		{
			ArrayList<String> chi_temp = new ArrayList<String>();
			
			chi_temp.add(dof.toString());
			if (i==0)
			{
				chi_temp.add("alpha=0.5");
				System.out.println("chi_square value from chi_map for alpha=0.5 : "+chi_map.get(chi_temp));
				if (Double.parseDouble(chi_map.get(chi_temp))<chi_square)
				{
					System.out.println("No pruning needed for attribute: "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				}
			}
			else if(i==1)
			{
				chi_temp.add("alpha=0.05");
				System.out.println("chi_square value from chi_map for alpha=0.05 : "+chi_map.get(chi_temp));
				if (Double.parseDouble(chi_map.get(chi_temp))<chi_square)
				{
					System.out.println("No pruning needed for attribute: "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				}
			}
			else if(i==2)
			{
				chi_temp.add("alpha=0.01");
				System.out.println("chi_square value from chi_map for alpha=0.01 : "+chi_map.get(chi_temp));
				if (Double.parseDouble(chi_map.get(chi_temp))<chi_square)
				{
					System.out.println("No pruning needed for attribute: "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				}
			}
			
		}
		chi_square = 0;

		//Once all the maps containing subsets of a bigger dataset has been added into stack, pop one by one
		// In other words, implementing a Depth First Search kind of tree 
		while (!stack.empty())
		{
			//popping from stack. map_latest stores the latest map that is popped
			// So, map_latest is of same type as "map" : <<data_latest,key_final>,map_inner>
			// here map_inner is of type: HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>
			map_latest = stack.pop();
			
			//getting the data out of map_latest
			temp = (HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>) getOnlyKeyFromHashMap(map_latest);
			data_latest = (ArrayList<ArrayList<String>>) getOnlyKeyFromHashMap(temp);
			key_final = temp.get(data_latest);
			HashMap<Attribute, Attribute> hash_temp = new HashMap<Attribute, Attribute>();
			hash_temp = (HashMap<Attribute, Attribute>) getOnlyKeyFromHashMap(map_latest.get(temp));
			attr_latest = (Attribute) getOnlyKeyFromHashMap(hash_temp);
			attr_next = hash_temp.get(attr_latest);
			attr_list_latest = (ArrayList<Attribute>) getOnlyKeyFromHashMap(map_latest.get(temp).get(hash_temp));
			attr_list_next = map_latest.get(temp).get(hash_temp).get(attr_list_latest);

			//calculate mis_classification error gain
			def =  cal_miserr(data_latest, attr_next) - cal_miserr_data(data_latest);
			//If gain from mis_err =0: either "p" or "e" is reached
			if(def==0)
			{
				if(get_prime(data_latest, attr_latest)==1)
				{
					// assigning child as containing "p" so that we can use later.
					attr_latest.setLeaf_value("p");
					String child_key =  get_unique_value(data_latest, attr_latest);
					Attribute leaf_main = new Attribute();
					leaf_main.h.put("p",null);
					attr_latest.addChild_map(child_key, leaf_main);
					HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> hash_temp1 = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
					ArrayList<String> arr_temp1 = new ArrayList<String>();
					hash_temp1 = (HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>) getOnlyKeyFromHashMap(map_latest);
					arr_temp1= hash_temp1.get(data_latest);
					
					//updating the main_miserr which stores the hashmap for testing the data
					// Example it is of the form: <["n","w","y"],"p">
					main_miserr.put(arr_temp1, "p");

				}
				else 
				{
					// assigning child as containing "e" so that we can use later.
					attr_latest.setLeaf_value("e");
					String child_key =  get_unique_value(data_latest, attr_latest);
					Attribute leaf_main = new Attribute();
					leaf_main.h.put("e",null);
					attr_latest.addChild_map(child_key, leaf_main);
					HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> hash_temp1 = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
					ArrayList<String> arr_temp1 = new ArrayList<String>();
					hash_temp1 = (HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>) getOnlyKeyFromHashMap(map_latest);
					arr_temp1= hash_temp1.get(data_latest);
					main_miserr.put(arr_temp1, "e");
				}
			}
			else 
			{
				// If "p" or "e" is not reached, then find next attribute with max mis_error gain
				// update parent child relationships
				String child_key =  get_unique_value(data_latest, attr_latest);

				attr_latest.addChild_map(child_key, attr_next);
				child_parent = new HashMap<ArrayList<ArrayList<String>>, Attribute>();
				child_parent.put(data_latest, attr_latest);
				attr_next.setParent_map(child_parent);
				Attribute j2 =  attr_next.getParent_map().get(data_latest);
				key_final =temp.get(data_latest);

				//udpate attribute_latest as next attribute with max error gain
				attr_latest = attr_next;
				attr_list_next = map_latest.get(temp).get(hash_temp).get(attr_list_latest);
				attr_list_latest = new ArrayList<Attribute>(attr_list_next);
				
				build_decision_tree_miserr(data_latest);
			}
		}
	}catch(Exception e){
        e.printStackTrace();
    }
	}
	 	
 	//All the steps are similar to above tree except for choosing the attribute_next
 	//It is chosen as attribute with max info. gain based on entropy
 	public static void build_decision_tree_gain(ArrayList<ArrayList<String>> data)
	{
		double def = 0;
		HashMap<ArrayList<ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>> map_copy = 
			new HashMap<ArrayList<ArrayList<String>>, HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>(); 
		HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>> map_inner_copy = 
			new HashMap<HashMap<Attribute,Attribute>, HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>();
		HashMap<String, Attribute> child = new HashMap<String, Attribute>();
		HashMap<ArrayList<ArrayList<String>>, Attribute> child_parent = null;
		Attribute p_temp = new Attribute();
		ArrayList<ArrayList<String>> data_lat_temp = new ArrayList<ArrayList<String>>();
		double ch_p=0, ch_e=0,ch_pi_prime = 0, ch_ei_prime = 0, ch_pi = 0, ch_ei = 0, chi_square = 0;

	try{
		if (!root_status){
			data_latest = data;
			attr_list_latest = new ArrayList<Attribute>(attr_all_minus_class);
		
			double ENTROPY = cal_entropy(data_latest);
			int attributeIndex=max_info_gain_obj.max_Information_Att_number(data_latest,ENTROPY);
			root = get_Attribute_from_index(attributeIndex);			
			attr_latest = root;
			Attribute rem = attr_list_latest.remove(attr_list_latest.indexOf(attr_latest));
			root_status = true;	
			}
		unique_latest = attr_latest.h.get(attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
		data_split_latest = split_data(data_latest,attr_latest);
		Attribute latest_temp = new Attribute();
		ArrayList<String> key_temp = new ArrayList<String>();
		data_lat_temp = data_latest;
		ch_p = get_p_from_data(data_latest);
		ch_e = get_e_from_data(data_latest);
		chi_square = 0;
		for (int f = 0; f<unique_latest.size();f++)
		{
			latest_temp = attr_latest;
			data_latest = data_split_latest.get(f);
			ch_pi = get_p_from_data(data_latest);
			ch_ei = get_e_from_data(data_latest);
			ch_pi_prime = ch_p*((ch_pi+ch_ei)/(ch_p+ch_e));
			ch_ei_prime = ch_e*((ch_pi+ch_ei)/(ch_p+ch_e));
			if (ch_pi_prime==0 && ch_ei_prime==0)
			{
				chi_square = chi_square;
			}
			else if (ch_pi_prime==0)
			{
				chi_square = chi_square + (Math.pow((ch_ei-ch_ei_prime),2)/ch_ei_prime);
			}
			else if(ch_ei_prime==0)
			{
				chi_square = chi_square + (Math.pow((ch_pi-ch_pi_prime),2)/ch_pi_prime);
			}
			else
			{
				chi_square = chi_square + (Math.pow((ch_pi-ch_pi_prime),2)/ch_pi_prime) + (Math.pow((ch_ei-ch_ei_prime),2)/ch_ei_prime);	
			}
			if (!(data_latest.size()==0) )
			{
				
				ENTROPY = cal_entropy(data_latest);
				attributeIndex=max_info_gain_obj.max_Information_Att_number(data_latest,ENTROPY);
				attr_next = get_Attribute_from_index(attributeIndex);
				HashMap<Attribute, Attribute> map_inner_left = new HashMap<Attribute, Attribute>();
				HashMap<ArrayList<Attribute>, ArrayList<Attribute>> map_inner_right = new HashMap<ArrayList<Attribute>, ArrayList<Attribute>>();
				HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>> map_inner = 
						new HashMap<HashMap<Attribute,Attribute>, HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>();
				HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>> map = 
						new HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>, HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>(); 
				HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> map_left_part = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
				
				map_inner_left.put(attr_latest,attr_next);
				attr_list_temp = new ArrayList<Attribute>(attr_list_latest);
				attr_list_temp.remove(attr_next);
				attr_list_next = new ArrayList<Attribute>(attr_list_temp);
				map_inner_right.put(attr_list_latest,attr_list_next);
				map_inner.put(map_inner_left, map_inner_right);
				
				while(latest_temp.getParent_map()!=null)
				{
					if(latest_temp.getParent_map()!=null)
					{
						latest_temp = get_parent_Attribute(data_lat_temp, latest_temp);
						key_temp.add(0,get_unique_value(data_lat_temp, latest_temp));
					}
					else {
						break;
					}
				}
				key_final = key_temp;
				key_temp = new ArrayList<String>();
				if (attr_latest.getParent_map()==null)
				{
					key_final = new ArrayList<String>();
				}

				key_final.add(get_unique_value(data_latest,attr_latest));
				map_left_part.put(data_latest,key_final);
				map.put(map_left_part,map_inner);
				stack.push(map);
				map_inner_copy = map_inner;
			}
		}
		
		System.out.println("chi_square value for attr_latest "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]+ 
				" is:  "+ chi_square);
		Integer dof = new Integer(0);
		dof = (attr_latest.h.get(attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]).size())-1;
		System.out.println("DOF: "+dof);
		System.out.println("unique values: "+attr_latest.h.get(attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]));
		for (int i=0; i<3;i++)
		{
			ArrayList<String> chi_temp = new ArrayList<String>();
			
			chi_temp.add(dof.toString());
			if (i==0)
			{
				chi_temp.add("alpha=0.5");
				System.out.println("chi_square value from chi_map for alpha=0.5 : "+chi_map.get(chi_temp));
				if (Double.parseDouble(chi_map.get(chi_temp))<chi_square)
				{
					System.out.println("No pruning needed for attribute: "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				}
			}
			else if(i==1)
			{
				chi_temp.add("alpha=0.05");
				System.out.println("chi_square value from chi_map for alpha=0.05 : "+chi_map.get(chi_temp));
				if (Double.parseDouble(chi_map.get(chi_temp))<chi_square)
				{
					System.out.println("No pruning needed for attribute: "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				}
			}
			else if(i==2)
			{
				chi_temp.add("alpha=0.01");
				System.out.println("chi_square value from chi_map for alpha=0.01 : "+chi_map.get(chi_temp));
				if (Double.parseDouble(chi_map.get(chi_temp))<chi_square)
				{
					System.out.println("No pruning needed for attribute: "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				}
			}
			
		}
		chi_square = 0;
		
		while (!stack.empty())
		{
			map_latest = stack.pop();
			temp = (HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>) getOnlyKeyFromHashMap(map_latest);
			data_latest = (ArrayList<ArrayList<String>>) getOnlyKeyFromHashMap(temp);
		
			key_final = temp.get(data_latest);
		
			HashMap<Attribute, Attribute> hash_temp = new HashMap<Attribute, Attribute>();
			hash_temp = (HashMap<Attribute, Attribute>) getOnlyKeyFromHashMap(map_latest.get(temp));
			attr_latest = (Attribute) getOnlyKeyFromHashMap(hash_temp);
			attr_next = hash_temp.get(attr_latest);
			attr_list_latest = (ArrayList<Attribute>) getOnlyKeyFromHashMap(map_latest.get(temp).get(hash_temp));
			attr_list_next = map_latest.get(temp).get(hash_temp).get(attr_list_latest);
			def =  cal_gain(data_latest, attr_next);
		
			if(def==0)
			{
				if(get_prime(data_latest, attr_latest)==1)
				{
					attr_latest.setLeaf_value("p");
					String child_key =  get_unique_value(data_latest, attr_latest);
					Attribute leaf_main = new Attribute();
					leaf_main.h.put("p",null);
					attr_latest.addChild_map(child_key, leaf_main);
					HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> hash_temp1 = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
					ArrayList<String> arr_temp1 = new ArrayList<String>();
					hash_temp1 = (HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>) getOnlyKeyFromHashMap(map_latest);
					arr_temp1= hash_temp1.get(data_latest);
					main_infogain.put(arr_temp1, "p");
				}
				else 
				{

					attr_latest.setLeaf_value("e");
					String child_key =  get_unique_value(data_latest, attr_latest);
					Attribute leaf_main = new Attribute();
					leaf_main.h.put("e",null);
					attr_latest.addChild_map(child_key, leaf_main);
					HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> hash_temp1 = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
					ArrayList<String> arr_temp1 = new ArrayList<String>();
					hash_temp1 = (HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>) getOnlyKeyFromHashMap(map_latest);
					arr_temp1= hash_temp1.get(data_latest);
					main_infogain.put(arr_temp1, "e");
				}
			}
			else 
			{
				String child_key =  get_unique_value(data_latest, attr_latest);
				attr_latest.addChild_map(child_key, attr_next);
				child_parent = new HashMap<ArrayList<ArrayList<String>>, Attribute>();
				child_parent.put(data_latest, attr_latest);
				attr_next.setParent_map(child_parent);
				Attribute j2 =  attr_next.getParent_map().get(data_latest);
				key_final =temp.get(data_latest);
				attr_latest = attr_next;
				attr_list_next = map_latest.get(temp).get(hash_temp).get(attr_list_latest);
				
				attr_list_latest = new ArrayList<Attribute>(attr_list_next);
				build_decision_tree_gain(data_latest);
			}

		}
	}catch(Exception e){
        e.printStackTrace();
    }
	}

 	public static ArrayList<String> cal_decision_miserr(ArrayList<ArrayList<String>> data)
 	{
 		String value = null;
 		ArrayList<String> result = new ArrayList<String>();
		Attribute leaf_p = new Attribute();
		leaf_p.h.put("p", null);
		Attribute leaf_e = new Attribute();
		leaf_e.h.put("e", null);
		ArrayList<String> test = new ArrayList<String>();
 		for (int i=0; i<data.size();i++)
 		{
 			test = new ArrayList<String>();
 			Attribute r = root;
 			while(true)
 			{ 
 				value = data.get(i).get(Integer.parseInt(ha.get(r.h.keySet().toArray(new String[attr_latest.h.size()])[0])));
 				test.add(value);
 				if (main_miserr.containsKey(test))
 				{
 					result.add(main_miserr.get(test));
 					break;
 				}
 				Attribute k = r.getChild_map().get(value);
 				r = r.getChild_map().get(value);
 			}
 		}
 		System.out.println("----Main ----: "+main_miserr);
	return result;
 	}
 	
 	public static ArrayList<String> cal_decision_gain(ArrayList<ArrayList<String>> data)
 	{
 		String value = null;
 		ArrayList<String> result = new ArrayList<String>();
		Attribute leaf_p = new Attribute();
		leaf_p.h.put("p", null);
		Attribute leaf_e = new Attribute();
		leaf_e.h.put("e", null);
		ArrayList<String> test = new ArrayList<String>();
 		for (int i=0; i<data.size();i++)
 		{
 			test = new ArrayList<String>();
 			Attribute r = root;
 			while(true)
 			{ 
 				value = data.get(i).get(Integer.parseInt(ha.get(r.h.keySet().toArray(new String[attr_latest.h.size()])[0])));
 				test.add(value);
 				if (main_infogain.containsKey(test))
 				{
 					result.add(main_infogain.get(test));
 					break;
 				}
 				Attribute k = r.getChild_map().get(value);
 				r = r.getChild_map().get(value);
 			}
 		}
 		System.out.println("----Main ----: "+main_infogain);
	return result;
 	}

}
