package com.machine;
/**
 * @author KrishnaChaitanyaReddyBurri + JivanPatil
 *
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import com.jivan.*;


public class Entropy3 extends Attribute {
   static HashMap<String, String> ha = new HashMap<String, String>();
   static ArrayList<Attribute> attr_all = new ArrayList<Attribute>();
   static ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
   static ArrayList<ArrayList<String>> lines_test = new ArrayList<ArrayList<String>>();
   static ArrayList<ArrayList<String>> lines_chi_table = new ArrayList<ArrayList<String>>();
   static boolean root_status = false;
   static HashMap<ArrayList<String>, String> chi_map =  new HashMap<ArrayList<String>, String>();
    static Attribute root = new Attribute();
    static ArrayList<ArrayList<String>> data_latest = new ArrayList<ArrayList<String>>();
	static ArrayList<ArrayList<ArrayList<String>>> data_split_latest= new ArrayList<ArrayList<ArrayList<String>>>();
	static Attribute attr_latest = new Attribute();
	static Attribute attr_next = new Attribute();
	static ArrayList<Attribute> attr_list_latest = null;
	static ArrayList<Attribute> attr_list_next = new ArrayList<Attribute>();
	static ArrayList<Attribute> attr_list_temp = new ArrayList<Attribute>();
	static HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>> map_latest = 
			new HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>, HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>(); 
	static Stack<HashMap<HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>>> stack = 
			new Stack<HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>,HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>>();
	static HashMap<HashMap<HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>>, String > hash_map_all = 
		new HashMap<HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>,HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>, String>();
	static ArrayList<String> unique_latest = new ArrayList<String>();
	//static ArrayList<Attribute> attr_all_minus_class = new ArrayList<Attribute>(attr_all);
	static ArrayList<Attribute> attr_all_minus_class = new ArrayList<Attribute>();
	static ArrayList<String> testing_main = new ArrayList<String>();
	static HashMap<ArrayList<String>, String> main = new HashMap<ArrayList<String>, String>();
	static ArrayList<String> key_final =new ArrayList<String>();
	static HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> temp = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();

	static CalculateEntropy cal_entro_obj = new CalculateEntropy();
	static MaxInfoGainIndex max_info_gain_obj= new MaxInfoGainIndex();
	static double ENTROPY;
	static int attributeIndex;
	static int stop = 0;
	static int stop_mis = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> cla = new ArrayList<String>( Arrays.asList( new String[]{"p", "e"}));
		Attribute prime = new Attribute();
		prime.h.put("class",cla);                		
		attr_all.add(prime);
		
		
		try{
            BufferedReader buf = new BufferedReader(new FileReader("C:/KC/UNM/Spring 2016/Machine Learning/Project1/data/training3.txt"));
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
	                	lines.add(words);                        	               	
                    }
                    }
           // System.out.println(lines.size());
            //System.out.println(cal_entropy(lines)); 
            buf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
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
                
                if(line1 == null){  
                    break; 
                }
                else{  
                		ch = line2.split(",");
                		Attribute attr = new Attribute();
                		words2=new ArrayList<String>();
	                	for(String eachchar : ch){
	                		c = eachchar.split("=");
	                		//System.out.println(c[1]);
	                		words2.add(c[1]);
	                	}
	                //	System.out.println(line1);
	                //	System.out.println(words2);
                		attr.h.put(line1, words2);

                		attr_all.add(attr);
                		ha.put("class", String.valueOf(0));
                		ha.put(line1,String.valueOf(i+1));
                		i++;
                	}
            }
            attr_all_minus_class = attr_all;
    		attr_all_minus_class.remove(attr_all_minus_class.get(0));
			//    System.out.println(words2.size());
            Attribute a =  attr_all.get(0);
            Set<String> st = a.h.keySet();
            String[] strings = a.h.keySet().toArray(new String[a.h.size()]);
           // System.out.println(st);
            //System.out.println(a.h.get(strings[0]));
            ArrayList<ArrayList<ArrayList<String>>> man = new ArrayList<ArrayList<ArrayList<String>>>();
            	man = split_data(lines, a);
            	int sum=0;
            	for (int z = 0; z<man.size();z++)
            	{
         //   System.out.println(man.get(z).size());
            sum = sum+ man.get(z).size();
            
            	}
            buf2.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
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
           // System.out.println(lines.size());
            //System.out.println(cal_entropy(lines)); 
            buf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
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
           // System.out.println(lines.size());
            //System.out.println(cal_entropy(lines)); 
            buf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        // Mapping to get chi-map from chi_square_table.txt
        chi_map = get_chi_map(lines_chi_table);
        
		// TODO Auto-generated method stub
		ArrayList<ArrayList<String>> lines1 = new ArrayList<ArrayList<String>>();
		
		// Read the data from text file
		//lines1=(new ReadDataFile()).readDataFile("training.txt");
		
		double info_gain=0;
		int attributeIndex=0;
		
		// Calculate the entropy of the dataset
		double ENTROPY = cal_entro_obj.cal_entropy(lines);
		//System.out.println("Size of lines: "+lines.size());
		attributeIndex=max_info_gain_obj.max_Information_Att_number(lines,ENTROPY);
        System.out.println("attribute Index: "+attributeIndex);
        
        Attribute A = get_Attribute_from_index(attributeIndex);
        System.out.println("A from index: "+ A.h.keySet().toArray(new String[A.h.size()])[0]);
        
        /*System.out.println("get_p_from_data: "+get_p_from_data(lines));
        System.out.println("get e from data: "+ get_e_from_data(lines));*/
        
        /*System.out.println("chi_map developed: "+chi_map);
        System.out.println("A's (or) "+getOnlyKeyFromHashMap(A.h)+" 's "+ "unique values: "+A.h.get(getOnlyKeyFromHashMap(A.h)) );
        */
        
        String result = build_decision_tree(lines);
        System.out.println("End of decision tree: "+ result);
        ArrayList<String> cal_decision_value = new ArrayList<String>();
        cal_decision_value = cal_decision_value(lines_test);
        System.out.println("Decision value for testing1: "+cal_decision_value);
        System.out.println("--Testing sheet values-----: "+ testing_main);
        double count=0;
        for (int t = 0; t<cal_decision_value.size();t++)
        {
        	
        	if (cal_decision_value.get(t).equals(testing_main.get(t)))
        	{
        		count++;
        	}
        }
        System.out.println("% of matches with testing set "+ (double)((count/cal_decision_value.size())*100));
    
        
	}
	
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
/*		System.out.println("p value: "+p);
		System.out.println("e value: "+e);*/
		
		k1 = p/S.size();
		k2 = e/S.size();
		if (p==S.size()) {k2 = 1;}
		if (e==S.size()) {k1 = 1;}
		ent = ((k1)*(Math.log(k1)/Math.log(2))) + ((k2)*(Math.log(k2)/Math.log(2))); 
		//System.out.println("ent value: "+ent);
		if (ent==0) {return 0;}
		return -ent;
	};
	
	public static ArrayList<ArrayList<ArrayList<String>>> split_data(ArrayList<ArrayList<String>> S, Attribute A){
		int unique = 0;
		ArrayList<String> un = null;
		ArrayList<ArrayList<String>> sub;
		un = A.h.get(A.h.keySet().toArray(new String[A.h.size()])[0]);
		unique = un.size();
		//System.out.println("split_data()  %%%%%%%  Unique values for attribute "+A.h.keySet().toArray(new String[A.h.size()])[0]+" is "+unique);
		//System.out.println("split_data()  %%%%%%%  Unique values are "+A.h.get(A.h.keySet().toArray(new String[A.h.size()])[0]) );
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
				if (S.get(i).get(Integer.parseInt(ha.get(A.h.keySet().toArray(new String[A.h.size()])[0]))).equals(un.get(j)))
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
	
	public static double cal_gain(ArrayList<ArrayList<String>> S, Attribute A)
	{
		double sum = 0;
		double p = 0, e = 0, p_prime =0 , e_prime = 0;;
		ArrayList<ArrayList<ArrayList<String>>> data_after_split= split_data(S,A);
		//System.out.println("cal_miserr()  %%%%%%%  Data Split size: "+data_after_split.size());
		for (int i=0; i<data_after_split.size();i++)
		{
			ArrayList<ArrayList<String>> ar = data_after_split.get(i);
			//System.out.println("cal_miserr()  %%%%%%%  Current data: "+ar);
			
			sum = sum+((ar.size()/S.size())*cal_entropy(ar));
		}
		return (cal_entropy(S)-sum);
	}
	
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
	
	public static double cal_miserr(ArrayList<ArrayList<String>> data, Attribute A)
	{
		double err = 0;
		double err_data = 0;
		err_data = cal_miserr_data(data);
		double p = 0, e = 0, p_prime =0 , e_prime = 0;;
		ArrayList<ArrayList<ArrayList<String>>> data_after_split= split_data(data,A);
		//System.out.println("cal_miserr()  %%%%%%%  Data Split size: "+data_after_split.size());
		for (int i=0; i<data_after_split.size();i++)
		{
			ArrayList<ArrayList<String>> ar = data_after_split.get(i);
			//System.out.println("cal_miserr()  %%%%%%%  Current data: "+ar);
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
			//System.out.println("err after "+i+" th iteration is "+err);
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
		
 	public static Attribute get_attr_maxerr(ArrayList<ArrayList<String>> S, ArrayList<Attribute> attr_current)

	{
		Attribute A = new Attribute();
		double e = -1,max_err = e;
		//System.out.println("mis_err_data value: "+cal_miserr_data(S));
		//System.out.println("get_attr_maxerr() %%%%%%% Data under consideration: "+S);
		for (int p = 0; p<attr_current.size();p++)
		{	
			e = cal_miserr(S, attr_current.get(p));
			//System.out.println("get_attr_maxerr() %%%%%%% value of e for Attribute "+attr_current.get(p).h.keySet().toArray(new String[attr_current.get(p).h.size()])[0]+ " is: "+e);
			if (e>max_err){
				max_err = e;
				A = attr_current.get(p);
			}
		}
		//System.out.println("get_attr_maxerr() %%%%%%% Maximum error: " +String.format("%.5g%n", max_err)+" for attribute : "+A.h.keySet().toArray(new String[A.h.size()])[0]);
		return A;
	}
	
 	public static Object getKeyFromValue(HashMap hm, Object dat) {
 	    for (Object o : hm.keySet()) {
 	      if (hm.get(o).equals(dat)) {
 	        return o;
 	      }
 	    }
 	    return null;
 	  }
 	
 	public static Object getOnlyKeyFromHashMap(HashMap hm) {
 	    for (Object o : hm.keySet()) {
 	        return o;
 	    }
 	    return null;
 	  }

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
 	 	
 	public static String build_decision_tree(ArrayList<ArrayList<String>> data)
	{
		//System.out.println("Size of attr_all: "+ attr_all.size());
		//System.out.println("Size of attr_all_minus_class: "+attr_all_minus_class.size());
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
			System.out.println("+++++   Building root    +++++ ");
			data_latest = data;
			attr_list_latest = new ArrayList<Attribute>(attr_all_minus_class);
			//attr_list_latest = attr_all;
			//System.out.println("Before removing root, list of attrs: ");
			for (int y=0;y<attr_list_latest.size();y++)
			{
				System.out.println(attr_list_latest.get(y).h.keySet().toArray(new String[attr_latest.h.size()])[0]);
			}
			root = get_attr_maxerr(data_latest,attr_list_latest);
			attr_latest = root;
			System.out.println("Attr with max error: "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
			Attribute rem = attr_list_latest.remove(attr_list_latest.indexOf(attr_latest));
			/*System.out.println("remove: "+rem.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
			System.out.println("Size of attr_list_latest after removing root: "+attr_list_latest.size());
			System.out.println("After removing root, attrs list: ");
			for (int y=0;y<attr_list_latest.size();y++)
			{
				System.out.println(attr_list_latest.get(y).h.keySet().toArray(new String[attr_latest.h.size()])[0]);
			}*/
			root_status = true;	
			}
		/*else { System.out.println("------   Root built    ----- moving on   ----");}
		
		System.out.println("attr_latest at beginning: "+ (attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]));
		System.out.println("key_final at beginning of loop: "+key_final);*/
		unique_latest = attr_latest.h.get(attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
		System.out.println("Unique elements: "+ unique_latest);
		//System.out.println("Size of unique_latest: "+ unique_latest.size());
		
		data_split_latest = split_data(data_latest,attr_latest);
		//System.out.println("Split data latest: "+ data_split_latest);
		Attribute latest_temp = new Attribute();
		//latest_temp = attr_latest;
		ArrayList<String> key_temp = new ArrayList<String>();
		data_lat_temp = data_latest;
		ch_p = get_p_from_data(data_latest);
		ch_e = get_e_from_data(data_latest);
		chi_square = 0;
		
		
		for (int f = 0; f<unique_latest.size();f++)
		{
			stop_mis++;
			latest_temp = attr_latest;
			data_latest = data_split_latest.get(f);
			
			ch_pi = get_p_from_data(data_latest);
			ch_ei = get_e_from_data(data_latest);
			ch_pi_prime = ch_p*((ch_pi+ch_ei)/(ch_p+ch_e));
			ch_ei_prime = ch_e*((ch_pi+ch_ei)/(ch_p+ch_e));
			System.out.println("value ch_pi: "+ch_pi +"---"+ "value ch_ei: "+ch_ei +"---"+
					"value ch_pi_prime: "+ch_pi_prime +"---"+"value ch_ei_prime: "+ch_ei_prime +"---");
			if (ch_pi_prime==0 && ch_ei_prime==0)
			{
				chi_square = chi_square;
				System.out.println("if clause: "+ chi_square);
			}
			else if (ch_pi_prime==0)
			{
				chi_square = chi_square + (Math.pow((ch_ei-ch_ei_prime),2)/ch_ei_prime);
				System.out.println("else if1 clause: "+ chi_square);
			}
			else if(ch_ei_prime==0)
			{
				chi_square = chi_square + (Math.pow((ch_pi-ch_pi_prime),2)/ch_pi_prime);
				System.out.println("else if2 clause: "+ chi_square);
			}
			else
			{
				chi_square = chi_square + (Math.pow((ch_pi-ch_pi_prime),2)/ch_pi_prime) + (Math.pow((ch_ei-ch_ei_prime),2)/ch_ei_prime);	
				System.out.println("else clause: "+ chi_square);
			}
			
			if (!(data_latest.size()==0) )
			{
				//System.out.println("Latest Attribute: "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				attr_next = get_attr_maxerr(data_latest,attr_list_latest);
				System.out.println("Next, Attr with max error: "+attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				System.out.println("Data to be put into stack "+data_latest);
				/*System.out.println("After choosing root, attrs list: ");
				for (int y=0;y<attr_list_latest.size();y++)
				{
					
					System.out.println(attr_list_latest.get(y).h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				}*/
				//if (!stack.isEmpty()){System.out.println("Peek into stack before declaration:  "+stack.peek());}

				HashMap<Attribute, Attribute> map_inner_left = new HashMap<Attribute, Attribute>();
				HashMap<ArrayList<Attribute>, ArrayList<Attribute>> map_inner_right = new HashMap<ArrayList<Attribute>, ArrayList<Attribute>>();
				HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>> map_inner = 
						new HashMap<HashMap<Attribute,Attribute>, HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>();
				HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>> map = 
						new HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>, HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>(); 
				HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> map_left_part = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
				
				
				//if (!stack.isEmpty()){System.out.println("Peek into stack after declaration:  "+stack.peek());}

				map_inner_left.put(attr_latest,attr_next);
				attr_list_temp = new ArrayList<Attribute>(attr_list_latest);
				attr_list_temp.remove(attr_next);
				attr_list_next = new ArrayList<Attribute>(attr_list_temp);
				map_inner_right.put(attr_list_latest,attr_list_next);
				map_inner.put(map_inner_left, map_inner_right);
				

				/*****
				 * Getting key_final working
				 * *****/
				
				while(latest_temp.getParent_map()!=null)
				{
					/*System.out.println("|||||| In while loop |||||");
					System.out.println("latest_temp: "+latest_temp.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
					System.out.println("printing parent_map: "+attr_latest.getParent_map());
					*/
					/*p_temp = attr_latest.getParent_map().get(data_lat_temp);
					System.out.println("atribute p_temp: "+p_temp.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
					System.out.println("data_lat_temp size: "+data_lat_temp.size());
					System.out.println("unique_value: "+ get_unique_value(data_lat_temp, p_temp));
					key_temp.add(0,get_unique_value(data_lat_temp, p_temp));
					System.out.println("key_temp after add: "+key_temp);*/
					if(latest_temp.getParent_map()!=null)
					{
						//System.out.println("Inside while if");
						//latest_temp = p_temp.getParent_map().get(data_lat_temp);
						latest_temp = get_parent_Attribute(data_lat_temp, latest_temp);
						//System.out.println("Latest_temp after assignment: "+latest_temp.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
						key_temp.add(0,get_unique_value(data_lat_temp, latest_temp));
						//System.out.println("key_temp after add: "+key_temp);

					}
					else {
						break;
					}
				}
				//System.out.println("key_temp after loop: "+key_temp);
				key_final = key_temp;
				key_temp = new ArrayList<String>();
				//System.out.println("key_final after key_temp assigning: "+key_final);
				
				/****
				 * 
				 * ******/
				
				if (attr_latest.getParent_map()==null)
				{
					//System.out.println("++++++Inside null for parentmap+++++++");
					key_final = new ArrayList<String>();
				}
				/*else{
					key_final =temp.get(data_latest);
					System.out.println("key_final in else: "+key_final);
				}*/
				/*System.out.println("key_final value before adding into map,stack: "+key_final);
				System.out.println(get_unique_value(data_latest,attr_latest));*/
				key_final.add(get_unique_value(data_latest,attr_latest));
				System.out.println("key_final value after adding into map,stack: "+key_final);
				map_left_part.put(data_latest,key_final);
				map.put(map_left_part,map_inner);
/*				if ( map.containsKey(data_latest))
				{
					map_copy.put(data_latest,map_inner);
				}*/
				stack.push(map);
				hash_map_all.put(map, "");
				System.out.println("Peek into stack after push:  "+stack.peek());
				map_inner_copy = map_inner;
				


			}
		//	attr_list_latest = (ArrayList<Attribute>) getKeyFromValue(map_inner_right,attr_list_next);
			
			if (stop_mis>30)
			{
				System.out.println("+++++++++++ Breaking from for loop ++++++++++");
				break;
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
		System.out.println("Now, chi_square is: "+ chi_square);
		
		while (!stack.empty())
		{
			System.out.println("-------Into the stack-------");
			System.out.println("Peek into stack before pop:  "+stack.peek());
			map_latest = stack.pop();
			//System.out.println("Testing empty stack after pop:   "+ stack.empty());
			//System.out.println("testing value of map_latest: "+map_latest);
			//data_latest = (ArrayList<ArrayList<String>>) map_latest.keySet();
			//System.out.println("Map_inner_copy: "+map_inner_copy);
			//temp= new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
			temp = (HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>) getOnlyKeyFromHashMap(map_latest);
			data_latest = (ArrayList<ArrayList<String>>) getOnlyKeyFromHashMap(temp);
			/*System.out.println("Data_latest before calculation: "+data_latest);*/
			key_final = temp.get(data_latest);
			System.out.println("key_final at this position: "+ key_final);
			HashMap<Attribute, Attribute> hash_temp = new HashMap<Attribute, Attribute>();
			/*System.out.println("hash_temp before assigning: "+hash_temp);
			System.out.println("map_latest.get(data_latest) value before assigning: "+map_latest.get(data_latest) );
			*/
			hash_temp = (HashMap<Attribute, Attribute>) getOnlyKeyFromHashMap(map_latest.get(temp));
			attr_latest = (Attribute) getOnlyKeyFromHashMap(hash_temp);
			attr_next = hash_temp.get(attr_latest);
			attr_list_latest = (ArrayList<Attribute>) getOnlyKeyFromHashMap(map_latest.get(temp).get(hash_temp));
			attr_list_next = map_latest.get(temp).get(hash_temp).get(attr_list_latest);
			System.out.println("Attribute next before calculation: "+attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0] );
			//def =  cal_miserr(data_latest, attr_latest);
			def =  cal_miserr(data_latest, attr_next) - cal_miserr_data(data_latest);
			System.out.println("Calculation: "+def);
			if(def==0)
			{
				System.out.println("****Into 0 calculation loop***");
				//System.out.println(get_prime(data_latest, attr_latest));
				if(get_prime(data_latest, attr_latest)==1)
				{
					System.out.println("into 'p' loop ");
					attr_latest.setLeaf_value("p");
					String child_key =  get_unique_value(data_latest, attr_latest);
					Attribute leaf_main = new Attribute();
					leaf_main.h.put("p",null);
					attr_latest.addChild_map(child_key, leaf_main);
					HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> hash_temp1 = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
					ArrayList<String> arr_temp1 = new ArrayList<String>();
					hash_temp1 = (HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>) getOnlyKeyFromHashMap(map_latest);
					arr_temp1= hash_temp1.get(data_latest);
					main.put(arr_temp1, "p");
					System.out.println("main: "+ main);
					
					/*					
					HashMap<String, Attribute> hmc =  new HashMap<String, Attribute>();
					attr_latest.getLeaf().h.put("p", unique_latest);
					hmc.put(child_key, attr_latest.getLeaf());
					attr_latest.setChild_map(hmc);*/
					//return "p";
				}
				else 
				{
					System.out.println("into 'e' loop ");
					attr_latest.setLeaf_value("e");
					String child_key =  get_unique_value(data_latest, attr_latest);
					Attribute leaf_main = new Attribute();
					leaf_main.h.put("e",null);
					attr_latest.addChild_map(child_key, leaf_main);
					HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> hash_temp1 = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
					ArrayList<String> arr_temp1 = new ArrayList<String>();
					hash_temp1 = (HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>) getOnlyKeyFromHashMap(map_latest);
					arr_temp1= hash_temp1.get(data_latest);
					main.put(arr_temp1, "e");
					System.out.println("main: "+ main);
					
					
					/*HashMap<String, Attribute> hmc =  new HashMap<String, Attribute>();
					attr_latest.setLeaf(leaf_main);
					hmc.put(child_key, attr_latest.getLeaf());
					attr_latest.setChild_map(hmc);
					*/
					//return "e";
				}
			}
			else 
			{
				//System.out.println("!!! Into !=0 calculation loop !!!!");
				
				//System.out.println("attr_next: "+attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0]);		
				String child_key =  get_unique_value(data_latest, attr_latest);
/*				HashMap<String, Attribute> hmc =  new HashMap<String, Attribute>();
				hmc.put(child_key, attr_next);*/
				attr_latest.addChild_map(child_key, attr_next);
;				/*System.out.println("Child of attribute_latest "+ attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]+
								" is "+attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0]+ " or "+
								attr_latest.getChild_map().get(attr_latest.getChild_map().keySet().toArray(new String[attr_latest.h.size()])[0]));
				*/
				//attr_next.setParent(attr_latest);
				child_parent = new HashMap<ArrayList<ArrayList<String>>, Attribute>();
				child_parent.put(data_latest, attr_latest);
				attr_next.setParent_map(child_parent);
				Attribute j2 =  attr_next.getParent_map().get(data_latest);
				/*System.out.println("=============== Parent of attribute_next "+ attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0]+
									" is "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]+ " or "+
									j2.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				*/
				key_final =temp.get(data_latest);
				/*System.out.println("---Printing key_final value: "+ key_final);
				System.out.println("attr_latest before: "+ attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				*/
				attr_latest = attr_next;
				//System.out.println("attr_latest after: "+ attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				attr_list_next = map_latest.get(temp).get(hash_temp).get(attr_list_latest);
				//System.out.println("attr_list_latest before: "+attr_list_latest);
				attr_list_latest = new ArrayList<Attribute>(attr_list_next);
				/*System.out.println("attr_list_latest after: "+attr_list_latest);
				System.out.println("data_latest at end: "+data_latest);
				System.out.println("attr_latest at end: "+attr_latest+"  "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				*/
				if (stop_mis>30)
				{
					System.out.println("--------- Breaking from while loop --------");
					break;
				}
				
				build_decision_tree(data_latest);
			}
		}
	}catch(Exception e){
        e.printStackTrace();
    }
	return null;	
	}
	 	
 	public static String build_decision_tree_gain(ArrayList<ArrayList<String>> data)
	{
		//System.out.println("Size of attr_all: "+ attr_all.size());
		//System.out.println("Size of attr_all_minus_class: "+attr_all_minus_class.size());
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
			System.out.println("+++++   Building root    +++++ ");
			data_latest = data;
			attr_list_latest = new ArrayList<Attribute>(attr_all_minus_class);
			//attr_list_latest = attr_all;
			//System.out.println("Before removing root, list of attrs: ");
			for (int y=0;y<attr_list_latest.size();y++)
			{
				System.out.println(attr_list_latest.get(y).h.keySet().toArray(new String[attr_latest.h.size()])[0]);
			}
			
			double ENTROPY = cal_entropy(data_latest);
			//double ENTROPY = cal_entro_obj.cal_entropy(data_latest);
			int attributeIndex=max_info_gain_obj.max_Information_Att_number(data_latest,ENTROPY);
			root = get_Attribute_from_index(attributeIndex);
			
		/***	root = get_attr_maxerr(data_latest,attr_list_latest);  ***/
			
			attr_latest = root;
			System.out.println("Attr with max gain: "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
			Attribute rem = attr_list_latest.remove(attr_list_latest.indexOf(attr_latest));
			/*System.out.println("remove: "+rem.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
			System.out.println("Size of attr_list_latest after removing root: "+attr_list_latest.size());
			System.out.println("After removing root, attrs list: ");
			for (int y=0;y<attr_list_latest.size();y++)
			{
				System.out.println(attr_list_latest.get(y).h.keySet().toArray(new String[attr_latest.h.size()])[0]);
			}*/
			root_status = true;	
			}
		/*else { System.out.println("------   Root built    ----- moving on   ----");}
		
		System.out.println("attr_latest at beginning: "+ (attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]));
		System.out.println("key_final at beginning of loop: "+key_final);*/
		unique_latest = attr_latest.h.get(attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
		System.out.println("Unique elements: "+ unique_latest);
		//System.out.println("Size of unique_latest: "+ unique_latest.size());
		
		data_split_latest = split_data(data_latest,attr_latest);
		//System.out.println("Split data latest: "+ data_split_latest);
		Attribute latest_temp = new Attribute();
		//latest_temp = attr_latest;
		ArrayList<String> key_temp = new ArrayList<String>();
		data_lat_temp = data_latest;
		//System.out.println("data_latest size before for loop: "+data_latest.size());
		ch_p = get_p_from_data(data_latest);
		ch_e = get_e_from_data(data_latest);
		chi_square = 0;
		
		for (int f = 0; f<unique_latest.size();f++)
		{
			stop++;
			latest_temp = attr_latest;
			data_latest = data_split_latest.get(f);
			ch_pi = get_p_from_data(data_latest);
			ch_ei = get_e_from_data(data_latest);
			ch_pi_prime = ch_p*((ch_pi+ch_ei)/(ch_p+ch_e));
			ch_ei_prime = ch_e*((ch_pi+ch_ei)/(ch_p+ch_e));
			System.out.println("value ch_pi: "+ch_pi +"---"+ "value ch_ei: "+ch_ei +"---"+
					"value ch_pi_prime: "+ch_pi_prime +"---"+"value ch_ei_prime: "+ch_ei_prime +"---");
			if (ch_pi_prime==0 && ch_ei_prime==0)
			{
				chi_square = chi_square;
				System.out.println("if clause: "+ chi_square);
			}
			else if (ch_pi_prime==0)
			{
				chi_square = chi_square + (Math.pow((ch_ei-ch_ei_prime),2)/ch_ei_prime);
				System.out.println("else if1 clause: "+ chi_square);
			}
			else if(ch_ei_prime==0)
			{
				chi_square = chi_square + (Math.pow((ch_pi-ch_pi_prime),2)/ch_pi_prime);
				System.out.println("else if2 clause: "+ chi_square);
			}
			else
			{
				chi_square = chi_square + (Math.pow((ch_pi-ch_pi_prime),2)/ch_pi_prime) + (Math.pow((ch_ei-ch_ei_prime),2)/ch_ei_prime);	
				System.out.println("else clause: "+ chi_square);
			}
			//System.out.println("Data_latest size inside for loop: "+data_latest.size());
			if (!(data_latest.size()==0) )
			{
				System.out.println("Latest Attribute: "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				
				ENTROPY = cal_entropy(data_latest);
				System.out.println("latest entropy: "+ENTROPY);
				attributeIndex=max_info_gain_obj.max_Information_Att_number(data_latest,ENTROPY);
				System.out.println("attr_index: "+attributeIndex);
				attr_next = get_Attribute_from_index(attributeIndex);
					
				
	/*****	attr_next = get_attr_maxerr(data_latest,attr_list_latest);    ****/
				
				System.out.println("Next, Attr with max error: "+attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				//System.out.println("Data to be put into stack "+data_latest);
				/*System.out.println("After choosing root, attrs list: ");
				for (int y=0;y<attr_list_latest.size();y++)
				{
					
					System.out.println(attr_list_latest.get(y).h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				}*/
				//if (!stack.isEmpty()){System.out.println("Peek into stack before declaration:  "+stack.peek());}

				HashMap<Attribute, Attribute> map_inner_left = new HashMap<Attribute, Attribute>();
				HashMap<ArrayList<Attribute>, ArrayList<Attribute>> map_inner_right = new HashMap<ArrayList<Attribute>, ArrayList<Attribute>>();
				HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>> map_inner = 
						new HashMap<HashMap<Attribute,Attribute>, HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>();
				HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>> map = 
						new HashMap<HashMap<ArrayList<ArrayList<String>>,ArrayList<String>>, HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>(); 
				HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> map_left_part = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
				
				
				//if (!stack.isEmpty()){System.out.println("Peek into stack after declaration:  "+stack.peek());}

				map_inner_left.put(attr_latest,attr_next);
				attr_list_temp = new ArrayList<Attribute>(attr_list_latest);
				attr_list_temp.remove(attr_next);
				attr_list_next = new ArrayList<Attribute>(attr_list_temp);
				map_inner_right.put(attr_list_latest,attr_list_next);
				map_inner.put(map_inner_left, map_inner_right);
				

				/*****
				 * Getting key_final working
				 * *****/
				
				while(latest_temp.getParent_map()!=null)
				{
					/*System.out.println("|||||| In while loop |||||");
					System.out.println("latest_temp: "+latest_temp.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
					System.out.println("printing parent_map: "+attr_latest.getParent_map());
					*/
					/*p_temp = attr_latest.getParent_map().get(data_lat_temp);
					System.out.println("atribute p_temp: "+p_temp.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
					System.out.println("data_lat_temp size: "+data_lat_temp.size());
					System.out.println("unique_value: "+ get_unique_value(data_lat_temp, p_temp));
					key_temp.add(0,get_unique_value(data_lat_temp, p_temp));
					System.out.println("key_temp after add: "+key_temp);*/
					if(latest_temp.getParent_map()!=null)
					{
						//System.out.println("Inside while if");
						//latest_temp = p_temp.getParent_map().get(data_lat_temp);
						latest_temp = get_parent_Attribute(data_lat_temp, latest_temp);
						//System.out.println("Latest_temp after assignment: "+latest_temp.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
						key_temp.add(0,get_unique_value(data_lat_temp, latest_temp));
						//System.out.println("key_temp after add: "+key_temp);

					}
					else {
						break;
					}
				}
				//System.out.println("key_temp after loop: "+key_temp);
				key_final = key_temp;
				key_temp = new ArrayList<String>();
				//System.out.println("key_final after key_temp assigning: "+key_final);
				
				/****
				 * 
				 * ******/
				
				if (attr_latest.getParent_map()==null)
				{
					//System.out.println("++++++Inside null for parentmap+++++++");
					key_final = new ArrayList<String>();
				}
				/*else{
					key_final =temp.get(data_latest);
					System.out.println("key_final in else: "+key_final);
				}*/
				/*System.out.println("key_final value before adding into map,stack: "+key_final);
				System.out.println(get_unique_value(data_latest,attr_latest));*/
				key_final.add(get_unique_value(data_latest,attr_latest));
				System.out.println("key_final value after adding into map,stack: "+key_final);
				map_left_part.put(data_latest,key_final);
				map.put(map_left_part,map_inner);
/*				if ( map.containsKey(data_latest))
				{
					map_copy.put(data_latest,map_inner);
				}*/
				stack.push(map);
				hash_map_all.put(map, "");
				System.out.println("Peek into stack after push:  "+stack.peek());
				map_inner_copy = map_inner;
				
				if (stop>50)
				{
					System.out.println("+++++++++ Breaking from for loop ++++++++");
					break;
				}

			}
		//	attr_list_latest = (ArrayList<Attribute>) getKeyFromValue(map_inner_right,attr_list_next);
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
		System.out.println("Now, chi_square is: "+ chi_square);
		
		
		while (!stack.empty())
		{
			System.out.println("-------Into the stack-------");
			System.out.println("Peek into stack before pop:  "+stack.peek());
			map_latest = stack.pop();
			//System.out.println("Testing empty stack after pop:   "+ stack.empty());
			//System.out.println("testing value of map_latest: "+map_latest);
			//data_latest = (ArrayList<ArrayList<String>>) map_latest.keySet();
			//System.out.println("Map_inner_copy: "+map_inner_copy);
			//temp= new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
			temp = (HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>) getOnlyKeyFromHashMap(map_latest);
			data_latest = (ArrayList<ArrayList<String>>) getOnlyKeyFromHashMap(temp);
			/*System.out.println("Data_latest before calculation: "+data_latest);*/
			key_final = temp.get(data_latest);
			System.out.println("key_final at this position: "+ key_final);
			HashMap<Attribute, Attribute> hash_temp = new HashMap<Attribute, Attribute>();
			/*System.out.println("hash_temp before assigning: "+hash_temp);
			System.out.println("map_latest.get(data_latest) value before assigning: "+map_latest.get(data_latest) );
			*/
			hash_temp = (HashMap<Attribute, Attribute>) getOnlyKeyFromHashMap(map_latest.get(temp));
			attr_latest = (Attribute) getOnlyKeyFromHashMap(hash_temp);
			attr_next = hash_temp.get(attr_latest);
			attr_list_latest = (ArrayList<Attribute>) getOnlyKeyFromHashMap(map_latest.get(temp).get(hash_temp));
			attr_list_next = map_latest.get(temp).get(hash_temp).get(attr_list_latest);
			System.out.println("Attribute latest before calculation: "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0] );
			System.out.println("data latest size before calculation: "+data_latest.size());
			System.out.println("Next attribute is : "+ attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
			def =  cal_gain(data_latest, attr_next);
			System.out.println("Calculation: "+def);
			if(def==0)
			{
				System.out.println("****Into 0 calculation loop***");
				//System.out.println(get_prime(data_latest, attr_latest));
				if(get_prime(data_latest, attr_latest)==1)
				{
					System.out.println("into 'p' loop ");
					attr_latest.setLeaf_value("p");
					String child_key =  get_unique_value(data_latest, attr_latest);
					Attribute leaf_main = new Attribute();
					leaf_main.h.put("p",null);
					attr_latest.addChild_map(child_key, leaf_main);
					HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> hash_temp1 = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
					ArrayList<String> arr_temp1 = new ArrayList<String>();
					hash_temp1 = (HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>) getOnlyKeyFromHashMap(map_latest);
					arr_temp1= hash_temp1.get(data_latest);
					main.put(arr_temp1, "p");
					System.out.println("main: "+ main);
					
					/*					
					HashMap<String, Attribute> hmc =  new HashMap<String, Attribute>();
					attr_latest.getLeaf().h.put("p", unique_latest);
					hmc.put(child_key, attr_latest.getLeaf());
					attr_latest.setChild_map(hmc);*/
					//return "p";
				}
				else 
				{
					System.out.println("into 'e' loop ");
					attr_latest.setLeaf_value("e");
					String child_key =  get_unique_value(data_latest, attr_latest);
					Attribute leaf_main = new Attribute();
					leaf_main.h.put("e",null);
					attr_latest.addChild_map(child_key, leaf_main);
					HashMap<ArrayList<ArrayList<String>>, ArrayList<String>> hash_temp1 = new HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>();
					ArrayList<String> arr_temp1 = new ArrayList<String>();
					hash_temp1 = (HashMap<ArrayList<ArrayList<String>>, ArrayList<String>>) getOnlyKeyFromHashMap(map_latest);
					arr_temp1= hash_temp1.get(data_latest);
					main.put(arr_temp1, "e");
					System.out.println("main: "+ main);
					
					
					/*HashMap<String, Attribute> hmc =  new HashMap<String, Attribute>();
					attr_latest.setLeaf(leaf_main);
					hmc.put(child_key, attr_latest.getLeaf());
					attr_latest.setChild_map(hmc);
					*/
					//return "e";
				}
			}
			else 
			{
				//System.out.println("!!! Into !=0 calculation loop !!!!");
				
				//System.out.println("attr_next: "+attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0]);		
				String child_key =  get_unique_value(data_latest, attr_latest);
/*				HashMap<String, Attribute> hmc =  new HashMap<String, Attribute>();
				hmc.put(child_key, attr_next);*/
				attr_latest.addChild_map(child_key, attr_next);
;				/*System.out.println("Child of attribute_latest "+ attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]+
								" is "+attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0]+ " or "+
								attr_latest.getChild_map().get(attr_latest.getChild_map().keySet().toArray(new String[attr_latest.h.size()])[0]));
				*/
				//attr_next.setParent(attr_latest);
				child_parent = new HashMap<ArrayList<ArrayList<String>>, Attribute>();
				child_parent.put(data_latest, attr_latest);
				attr_next.setParent_map(child_parent);
				Attribute j2 =  attr_next.getParent_map().get(data_latest);
				/*System.out.println("=============== Parent of attribute_next "+ attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0]+
									" is "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]+ " or "+
									j2.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				*/
				key_final =temp.get(data_latest);
				/*System.out.println("---Printing key_final value: "+ key_final);
				System.out.println("attr_latest before: "+ attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				*/
				attr_latest = attr_next;
				//System.out.println("attr_latest after: "+ attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				attr_list_next = map_latest.get(temp).get(hash_temp).get(attr_list_latest);
				//System.out.println("attr_list_latest before: "+attr_list_latest);
				attr_list_latest = new ArrayList<Attribute>(attr_list_next);
				/*System.out.println("attr_list_latest after: "+attr_list_latest);
				System.out.println("data_latest at end: "+data_latest);
				System.out.println("attr_latest at end: "+attr_latest+"  "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				*/
				build_decision_tree_gain(data_latest);
			}
			
			if (stop>50)
			{
				System.out.println("--------- Breaking from while loop --------");
				break;
			}
		}
	}catch(Exception e){
        e.printStackTrace();
    }
	return null;	
	}

 	public static ArrayList<String> cal_decision_value(ArrayList<ArrayList<String>> data)
 	{

 		System.out.println("main values: "+main);
 		String value = null;
 		ArrayList<String> result = new ArrayList<String>();
		//System.out.println("Size of data testing: "+data.size());
		Attribute leaf_p = new Attribute();
		leaf_p.h.put("p", null);
		Attribute leaf_e = new Attribute();
		leaf_e.h.put("e", null);
		ArrayList<String> test = new ArrayList<String>();
 		for (int i=0; i<data.size();i++)
 		{
 			test = new ArrayList<String>();
 			Attribute r = root;
 			/*System.out.println("******** In "+i+" th record************");
 			System.out.println("Attribute r : "+ r.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
 			*/
 			//System.out.println("r's leaf_value: "+ r.getLeaf_value());
 			/*while( (!(r.getLeaf_value().equals("p")) || !(r.getLeaf_value().equals("e"))))
 			{ 
 				System.out.println("Into while loop");
 				value = data.get(i).get(Integer.parseInt(ha.get(r.h.keySet().toArray(new String[attr_latest.h.size()])[0])));
 				System.out.println("value in loop: "+value);
				r = r.getChild_map().get(value);
				System.out.println("Attribute r at end of loop: "+r.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
 			}
 			if ((r.getLeaf_value().equals("p") || (r.getLeaf_value().equals("e"))))
 			{
 				result.add(r.getLeaf_value());
 			}*/
 			while(true)
 			{ 
 				/*System.out.println("Into while loop");
 				System.out.println("data being observed: "+data.get(i));*/
 				value = data.get(i).get(Integer.parseInt(ha.get(r.h.keySet().toArray(new String[attr_latest.h.size()])[0])));
 				//System.out.println("value in loop: "+value);
 				test.add(value);
 				if (main.containsKey(test))
 				{
 					result.add(main.get(test));
 					break;
 				}
 				Attribute k = r.getChild_map().get(value);
 				
 				/*System.out.println("Attribute k: "+k.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
 				System.out.println("k.h values: "+k.h.get(k.h.keySet().toArray(new String[attr_latest.h.size()])[0]));
 				*/
 				/*if (k.h.containsKey("p") || k.h.containsKey("e"))
 				{
 					System.out.println("into 1st if");
 					if (((k.h.keySet().toArray(new String[attr_latest.h.size()])[0]).equals("p")) && ((k.h.get("p"))==null))
 					{
 						System.out.println("Into 2nd if");
 						result.add("p");
 						break;
 					}
 					if (((k.h.keySet().toArray(new String[attr_latest.h.size()])[0]).equals("e")) && ((k.h.get("e"))==null))
 					{
 						System.out.println("into 3rd if");
 						result.add("e");
 						break;
 					}
 				}*/
 				r = r.getChild_map().get(value);
 			}
 			//System.out.println("*********  result untill now:********* "+ result);
 		}
 		System.out.println("----Main ----: "+main);
	return result;
 	}
 	
	}
