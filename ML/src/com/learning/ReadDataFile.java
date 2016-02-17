package com.learning;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ReadDataFile{
public ArrayList<ArrayList<String>> readDataFile(String fileName){
	ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();

  try{
          BufferedReader buf = new BufferedReader(new FileReader(fileName));
          ArrayList<String> words;
          String lineJustFetched = null;
          String[] wordsArray;
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
          //System.out.println(lines.size());
          //System.out.println(lines.get(0).size());
          buf.close();
      }catch(Exception e){
          e.printStackTrace();
      }
  return lines;
}

}
