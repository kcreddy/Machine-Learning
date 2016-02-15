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

import javax.smartcardio.ATR;

import org.w3c.dom.Attr;


public class Entropy extends Attribute {
   static HashMap<String, String> ha = new HashMap<String, String>();
   static ArrayList<Attribute> attr_all = new ArrayList<Attribute>();
   static ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
   static ArrayList<ArrayList<String>> lines_test = new ArrayList<ArrayList<String>>();
   static boolean root_status = false;
   
    static Attribute root = new Attribute();
    static ArrayList<ArrayList<String>> data_latest = new ArrayList<ArrayList<String>>();
	static ArrayList<ArrayList<ArrayList<String>>> data_split_latest= new ArrayList<ArrayList<ArrayList<String>>>();
	static Attribute attr_latest = new Attribute();
	static Attribute attr_next = new Attribute();
	static ArrayList<Attribute> attr_list_latest = null;
	static ArrayList<Attribute> attr_list_next = new ArrayList<Attribute>();
	static ArrayList<Attribute> attr_list_temp = new ArrayList<Attribute>();
	static HashMap<Attribute, ArrayList<ArrayList<String>>> child_parent = new HashMap<Attribute, ArrayList<ArrayList<String>>>();
	static HashMap<ArrayList<ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>> map_latest = 
			new HashMap<ArrayList<ArrayList<String>>, HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>(); 
	static Stack<HashMap<ArrayList<ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>>> stack = 
			new Stack<HashMap<ArrayList<ArrayList<String>>,HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>>();
	static ArrayList<String> unique_latest = new ArrayList<String>();
	//static ArrayList<Attribute> attr_all_minus_class = new ArrayList<Attribute>(attr_all);
	static ArrayList<Attribute> attr_all_minus_class = new ArrayList<Attribute>();
	static ArrayList<String> testing_main = new ArrayList<String>();
   

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> cla = new ArrayList<String>( Arrays.asList( new String[]{"p", "e"}));
		Attribute prime = new Attribute();
		prime.h.put("class",cla);                		
		attr_all.add(prime);
		
		try{
            BufferedReader buf = new BufferedReader(new FileReader("C:/KC/UNM/Spring 2016/Machine Learning/Project1/data/training2.txt"));
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
            BufferedReader buf2 = new BufferedReader(new FileReader("C:/KC/UNM/Spring 2016/Machine Learning/Project1/data/attributes2.txt"));
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
            	//System.out.println("Total: "+String.valueOf(sum));
 /*           	ArrayList<Attribute> temp = new ArrayList<Attribute>(attr_all);
            	temp.remove(attr_all.get(0));
            	System.out.println("Attributes: ");
            	for (int l = 0; l<temp.size();l++)
            	{
            		System.out.println(temp.get(l).h.keySet().toArray(new String[temp.get(l).h.size()])[0]);
            	}*/
            	
            //	System.out.println("MaxError Attribute: "+get_attr_maxerr(lines, temp).h.keySet());
           //     System.out.println("Miserror of "+attr_all.get(17).h.keySet()+" is "+String.valueOf(cal_miserr(lines, attr_all.get(17))));

            buf2.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        try{
            BufferedReader buf = new BufferedReader(new FileReader("C:/KC/UNM/Spring 2016/Machine Learning/Project1/data/testing2.txt"));
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
        System.out.println("% of matches with training set "+ (double)((count/cal_decision_value.size())*100));
        /*String result = get_unique_value(lines,attr_all.get(1));
        System.out.println("value taken by attibute in dataset is: "+ result);*/   
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
		k1 = p/S.size();
		k2 = e/S.size();
		ent = ((k1)*(Math.log(k1)/Math.log(2))) + ((k2)*(Math.log(k2)/Math.log(2))); 
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
	
	public static double cal_miserr(ArrayList<ArrayList<String>> data, Attribute A)
	{
		double err = 0;
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
			/*System.out.println("ar size after "+i+" th iteration is "+ar.size());
			System.out.println("p after "+i+" th iteration is "+p);
			System.out.println("p_prime after "+ i +"th position is " +p_prime);
			System.out.println("e after "+i+" th iteration is "+e);
			System.out.println("e_prime after "+ i +"th position is " +e_prime);*/
			
			/*System.out.println(ar.size());
			System.out.println(data.size());
			System.out.println((double)ar.size()/data.size());
*/			err = err + ((1-Math.max(p_prime,e_prime))* (double)ar.size()/data.size());
			//System.out.println("err after "+i+" th iteration is "+err);
		}
		return err;
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
		System.out.println("get_attr_maxerr() %%%%%%% Maximum error: " +String.format("%.5g%n", max_err));
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

		
	try{
		if (!root_status){
			System.out.println("+++++   Building root    +++++ ");
			data_latest = data;
			attr_list_latest = new ArrayList<Attribute>(attr_all_minus_class);
			//attr_list_latest = attr_all;
			System.out.println("Before removing root, list of attrs: ");
			for (int y=0;y<attr_list_latest.size();y++)
			{
				System.out.println(attr_list_latest.get(y).h.keySet().toArray(new String[attr_latest.h.size()])[0]);
			}
			root = get_attr_maxerr(data_latest,attr_list_latest);
			attr_latest = root;
			System.out.println("Attr with max error: "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
			Attribute rem = attr_list_latest.remove(attr_list_latest.indexOf(attr_latest));
			System.out.println("remove: "+rem.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
			System.out.println("Size of attr_list_latest after removing root: "+attr_list_latest.size());
			System.out.println("After removing root, attrs list: ");
			for (int y=0;y<attr_list_latest.size();y++)
			{
				System.out.println(attr_list_latest.get(y).h.keySet().toArray(new String[attr_latest.h.size()])[0]);
			}
			root_status = true;	
			}
		else { System.out.println("------   Root built    ----- moving on   ----");}
		
		System.out.println("attr_latest at beginning: "+ (attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]));
		unique_latest = attr_latest.h.get(attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
		System.out.println("Unique elements: "+ unique_latest);
		System.out.println("Size of unique_latest: "+ unique_latest.size());
		data_split_latest = split_data(data_latest,attr_latest);
		System.out.println("Split data latest: "+ data_split_latest);

		for (int f = 0; f<unique_latest.size();f++)
		{
			data_latest = data_split_latest.get(f);
			if (!(data_latest.size()==0) )
			{
				attr_next = get_attr_maxerr(data_latest,attr_list_latest);
				System.out.println("Next, Attr with max error: "+attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				System.out.println("Data to be put into stack "+data_latest);
				/*System.out.println("After choosing root, attrs list: ");
				for (int y=0;y<attr_list_latest.size();y++)
				{
					
					System.out.println(attr_list_latest.get(y).h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				}*/
				if (!stack.isEmpty()){System.out.println("Peek into stack before declaration:  "+stack.peek());}

				HashMap<Attribute, Attribute> map_inner_left = new HashMap<Attribute, Attribute>();
				HashMap<ArrayList<Attribute>, ArrayList<Attribute>> map_inner_right = new HashMap<ArrayList<Attribute>, ArrayList<Attribute>>();
				HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>> map_inner = 
						new HashMap<HashMap<Attribute,Attribute>, HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>();
				HashMap<ArrayList<ArrayList<String>>, HashMap<HashMap<Attribute, Attribute>, HashMap<ArrayList<Attribute>, ArrayList<Attribute>>>> map = 
						new HashMap<ArrayList<ArrayList<String>>, HashMap<HashMap<Attribute,Attribute>,HashMap<ArrayList<Attribute>,ArrayList<Attribute>>>>(); 
				if (!stack.isEmpty()){System.out.println("Peek into stack after declaration:  "+stack.peek());}

				map_inner_left.put(attr_latest,attr_next);
				attr_list_temp = new ArrayList<Attribute>(attr_list_latest);
				attr_list_temp.remove(attr_next);
				attr_list_next = new ArrayList<Attribute>(attr_list_temp);
				map_inner_right.put(attr_list_latest,attr_list_next);
				map_inner.put(map_inner_left, map_inner_right);
				map.put(data_latest,map_inner);
/*				if ( map.containsKey(data_latest))
				{
					map_copy.put(data_latest,map_inner);
				}*/
				stack.push(map);
				System.out.println("Peek into stack after push:  "+stack.peek());
				map_inner_copy = map_inner;

			}
		//	attr_list_latest = (ArrayList<Attribute>) getKeyFromValue(map_inner_right,attr_list_next);
		}
		
		while (!stack.empty())
		{
			
			System.out.println("-------Into the stack-------");
			System.out.println("Peek into stack before pop:  "+stack.peek());
			map_latest = stack.pop();
/*			System.out.println("--Clearing maps-- ");
			map_inner_left.clear();
			System.out.println("map_inner_left is clear: "+ map_inner_left.isEmpty());
			map_inner_right.clear();
			System.out.println("map_inner_right is clear: "+ map_inner_right.isEmpty());
			map_inner.clear();
			System.out.println("map_inner is clear: "+ map_inner.isEmpty());
			map.clear();
			System.out.println("map is cleared after pop: "+ map.isEmpty());*/
			System.out.println("Testing empty stack after pop:   "+ stack.empty());
			System.out.println("testing value of map_latest: "+map_latest);
			//data_latest = (ArrayList<ArrayList<String>>) map_latest.keySet();
			System.out.println("Map_inner_copy: "+map_inner_copy);
			data_latest = (ArrayList<ArrayList<String>>) getOnlyKeyFromHashMap(map_latest);
			System.out.println("Data_latest before calculation: "+data_latest);
			//data_latest = (ArrayList<ArrayList<String>>) getKeyFromValue(map_latest,map_inner_copy);
			//attr_latest = attr_next;
			HashMap<Attribute, Attribute> hash_temp = new HashMap<Attribute, Attribute>();
			hash_temp = (HashMap<Attribute, Attribute>) getOnlyKeyFromHashMap(map_latest.get(data_latest));
			attr_latest = (Attribute) getOnlyKeyFromHashMap(hash_temp);
			attr_next = hash_temp.get(attr_latest);
			attr_list_latest = (ArrayList<Attribute>) getOnlyKeyFromHashMap(map_latest.get(data_latest).get(hash_temp));
			attr_list_next = map_latest.get(data_latest).get(hash_temp).get(attr_list_latest);
			System.out.println("Attribute latest before calculation: "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0] );
			def =  cal_miserr(data_latest, attr_latest);
			System.out.println("Calculation: "+def);
			if(def==0)
			{
				System.out.println("****Into 0 calculation loop***");
				System.out.println(get_prime(data_latest, attr_latest));
				if(get_prime(data_latest, attr_latest)==1)
				{
					System.out.println("into 'p' loop ");
					attr_latest.setLeaf_value("p");
					String child_key =  get_unique_value(data_latest, attr_latest);
					Attribute leaf_main = new Attribute();
					leaf_main.h.put("p",null);
					attr_latest.addChild_map(child_key, leaf_main);
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
				System.out.println("!!! Into !=0 calculation loop !!!!");
				
				System.out.println("attr_next: "+attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0]);		
				String child_key =  get_unique_value(data_latest, attr_latest);
/*				HashMap<String, Attribute> hmc =  new HashMap<String, Attribute>();
				hmc.put(child_key, attr_next);*/
				attr_latest.addChild_map(child_key, attr_next);
;				System.out.println("Child of attribute_latest "+ attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]+
								" is "+attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0]+ " or "+
								attr_latest.getChild_map().get(attr_latest.getChild_map().keySet().toArray(new String[attr_latest.h.size()])[0]));
				//attr_next.setParent(attr_latest);
				child_parent.put(attr_latest,data_latest);
				attr_next.setParent_map(child_parent);
				Attribute j2 =  attr_next.getParent_map().keySet().toArray(new Attribute[attr_latest.h.size()])[0];
				System.out.println("=============== Parent of attribute_next "+ attr_next.h.keySet().toArray(new String[attr_latest.h.size()])[0]+
									" is "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]+ " or "+
									j2.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				child_parent.clear();
				System.out.println("attr_latest before: "+ attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				attr_latest = attr_next;
				System.out.println("attr_latest after: "+ attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				attr_list_next = map_latest.get(data_latest).get(hash_temp).get(attr_list_latest);
				System.out.println("attr_list_latest before: "+attr_list_latest);
				attr_list_latest = new ArrayList<Attribute>(attr_list_next);
				System.out.println("attr_list_latest after: "+attr_list_latest);
				System.out.println("data_latest at end: "+data_latest);
				System.out.println("attr_latest at end: "+attr_latest+"  "+attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
				build_decision_tree(data_latest);
			}
		}
	}catch(Exception e){
        e.printStackTrace();
    }
	return null;	
	}
	
 	public static ArrayList<String> cal_decision_value(ArrayList<ArrayList<String>> data)
 	{

 		
 		String value = null;
 		ArrayList<String> result = new ArrayList<String>();
		System.out.println("Size of data testing: "+data.size());
		Attribute leaf_p = new Attribute();
		leaf_p.h.put("p", null);
		Attribute leaf_e = new Attribute();
		leaf_e.h.put("e", null);
 		for (int i=0; i<data.size();i++)
 		{
 			Attribute r = root;
 			System.out.println("******** In "+i+" th record************");
 			System.out.println("Attribute r : "+ r.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
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
 			int h=0;
 			while(true)
 			{ 
 				System.out.println("Into while loop");
 				value = data.get(i).get(Integer.parseInt(ha.get(r.h.keySet().toArray(new String[attr_latest.h.size()])[0])));
 				System.out.println("value in loop: "+value);
 				Attribute k = r.getChild_map().get(value);
 				System.out.println("Attribute k: "+k.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
 				System.out.println("k.h values: "+k.h.get(k.h.keySet().toArray(new String[attr_latest.h.size()])[0]));
 				if (k.h.containsKey("p") || k.h.containsKey("e"))
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
 				}
 				h++;
 				r = r.getChild_map().get(value);
 			} 		
 		}
	return result;
 	}
 	
 	
	}
