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

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class Entropy extends Attribute {
   static HashMap<String, String> ha = new HashMap<String, String>();
   static ArrayList<Attribute> attr_all = new ArrayList<Attribute>();
   static ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> cla = new ArrayList<String>( Arrays.asList( new String[]{"p", "e"}));
		Attribute prime = new Attribute();
		prime.h.put("class",cla);                		
		attr_all.add(prime);
		
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
	                	for(String eachchar : charsArray){
	                		words.add(eachchar);
	                	}
	                	lines.add(words);                        	               	
                    }
                    }
            System.out.println(lines.size());
            System.out.println(cal_entropy(lines)); 
            buf.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        try{
            BufferedReader buf2 = new BufferedReader(new FileReader("C:/KC/UNM/Spring 2016/Machine Learning/Project1/data/attributes.txt"));
            ArrayList<String> words2 = null;
            String line1 = null;
            String line2 = null;
            String[] ch;
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
			//    System.out.println(words2.size());
            Attribute a =  attr_all.get(0);
            Set<String> st = a.h.keySet();
            String[] strings = a.h.keySet().toArray(new String[a.h.size()]);
            System.out.println(st);
            System.out.println(a.h.get(strings[0]));
            ArrayList<ArrayList<ArrayList<String>>> man = new ArrayList<ArrayList<ArrayList<String>>>();
            	man = split_data(lines, a);
            	int sum=0;
            	for (int z = 0; z<man.size();z++)
            	{
            System.out.println(man.get(z).size());
            sum = sum+ man.get(z).size();
            
            	}
            	System.out.println("Total: "+String.valueOf(sum));
            	System.out.println("MaxError Attribute: "+get_attr_maxerr(lines, attr_all).h.keySet());
                System.out.println("Miserror of "+attr_all.get(17).h.keySet()+" is "+String.valueOf(cal_miserr(lines, attr_all.get(17))));

            buf2.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
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
	
/*	public static String cal_index(String attr){
		return ha.get(attr);
		
	};
*/	
	public static ArrayList<ArrayList<ArrayList<String>>> split_data(ArrayList<ArrayList<String>> S, Attribute A){
		int unique = 0;
		ArrayList<String> un = null;
		ArrayList<ArrayList<String>> sub;
		un = A.h.get(A.h.keySet().toArray(new String[A.h.size()])[0]);
		unique = un.size();
		//System.out.println(unique);
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
	
	public static double cal_miserr(ArrayList<ArrayList<String>> data, Attribute A)
	{
		double err = 0;
		double p = 0, e = 0, p_prime =0 , e_prime = 0;;
		ArrayList<ArrayList<ArrayList<String>>> data_after_split= split_data(data,A);
		for (int i=0; i<data_after_split.size();i++)
		{
			ArrayList<ArrayList<String>> ar = data_after_split.get(i);
			p= 0;
			e= 0;
			for (int k=0;k<ar.size();k++)
			{
				if(ar.get(k).get(0).equals("p")){p++;}
				if(ar.get(k).get(0).equals("e")){e++;}
			}
			p_prime = p/ar.size();
			e_prime = e/ar.size();
			/*System.out.println("ar size after "+i+" th iteration is "+ar.size());
			System.out.println("p after "+i+" th iteration is "+p);
			System.out.println("p_prime after "+ i +"th position is " +p_prime);
			System.out.println("e after "+i+" th iteration is "+e);
			System.out.println("e_prime after "+ i +"th position is " +e_prime);
		*/	
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
		for (int p = 0; p<attr_current.size();p++)
		{	
			e = cal_miserr(S, attr_current.get(p));
			if (e>max_err){
				max_err = e;
				A = attr_current.get(p);
			}
		}
		System.out.println("Maximum error: " +String.valueOf(max_err));
		return A;
	}
	
	public static void build_decision_tree(ArrayList<ArrayList<String>> data)
	{
		ArrayList<ArrayList<String>> data_latest = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<ArrayList<String>>> data_split_latest= new ArrayList<ArrayList<ArrayList<String>>>();
		Attribute attr_latest = new Attribute();
		Attribute attr_next = new Attribute();
		ArrayList<Attribute> attr_list_latest = new ArrayList<Attribute>();
		HashMap<Attribute, String> map_inner = new HashMap<Attribute, String>();
		HashMap<HashMap<Attribute, String>, ArrayList<ArrayList<String>>> map = new HashMap<HashMap<Attribute,String>, ArrayList<ArrayList<String>>>();
		HashMap<HashMap<Attribute, String>, ArrayList<ArrayList<String>>> map_latest = new HashMap<HashMap<Attribute,String>, ArrayList<ArrayList<String>>>();
		boolean root_status = false;
		Stack<HashMap<HashMap<Attribute, String>, ArrayList<ArrayList<String>>>> stack = new Stack<HashMap<HashMap<Attribute,String>,ArrayList<ArrayList<String>>>>();

		ArrayList<String> unique_latest = new ArrayList<String>();
		
	try{
		if (!root_status){
			data_latest = data;
			attr_list_latest = attr_all;
			Attribute root = get_attr_maxerr(data_latest,attr_list_latest);
			attr_latest = root;
			attr_list_latest.remove(attr_latest);
			root_status = true;	
			}
/*		else{
			attr_latest = get_attr_maxerr(data_latest,attr_list_latest);
			data_latest = data;			
			}
		*/
		unique_latest = attr_latest.h.get(attr_latest.h.keySet().toArray(new String[attr_latest.h.size()])[0]);
		data_split_latest = split_data(data_latest,attr_latest);		
		for (int f = 0; f<unique_latest.size();f++)
		{
			data_latest = data_split_latest.get(f);
			map_inner.put(attr_latest,unique_latest.get(f));
			map.put(map_inner,data_latest);
			stack.add(map);
			//attr_latest.setParent(get_attr_maxerr(data_latest,attr_list_latest));
		}
		
		while (!stack.empty())
		{
			map_latest = stack.pop();
			data_latest = map_latest.get(map_inner);
			//attr_latest = attr_next;
			if(cal_miserr(data_latest, attr_latest)==0)
			{
				if(get_prime(data_latest, attr_latest)==1){attr_latest.setLeaf_value("p");}
				else {attr_latest.setLeaf_value("e");}
			}
			if (!(cal_miserr(data_latest, attr_latest)==0)) 
			{
				attr_next = get_attr_maxerr(data_latest,attr_list_latest);
				attr_latest.child_array.add(attr_next);
				attr_next.setParent(attr_latest);
				attr_latest = attr_next;
				build_decision_tree(data_latest);
			}
		}
	}catch(Exception e){
        e.printStackTrace();
    }
		
	}
	
	}
