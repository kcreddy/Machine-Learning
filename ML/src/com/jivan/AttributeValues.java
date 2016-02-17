package com.jivan;
import java.io.*;
import java.util.*;


public class AttributeValues{
	
		int number=0,numDistinctValue=0;
	  
	  List<String> values= new ArrayList<String>();
	  
	  
	  public List<String> getDistinctValues(int att_num) throws FileNotFoundException{
		  
		  int number=0,numDistinctValue=0;
		  String attributeName="no_name",currentLine;
		  List<String> values= new ArrayList<String>();
  
		  AttributeValues[] array_objects= new AttributeValues[22];
		  String[] dataset=new String[44];
		  int count=0;

		  BufferedReader br = new BufferedReader(new FileReader("attributes.txt"));
  

          try{
              while((currentLine=br.readLine())!=null)
                {
                  dataset[count]=currentLine;
                  count++;
                }
            }
          catch(Exception e){}



        for ( int i=0; i<22; i++) {
                  array_objects[i]=new AttributeValues();
                  array_objects[i].number=i*2;
                  array_objects[i].values =Arrays.asList(dataset[i*2+1].split(","));
                  array_objects[i].numDistinctValue=array_objects[i].values.size();
              }
      return new ArrayList(array_objects[att_num].values);
      
}}
