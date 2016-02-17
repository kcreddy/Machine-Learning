package com.machine;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author KrishnaChaitanyaReddy + Jivan Patil
 *
 */
public class Attribute {
	
	/* Each attribute is an object with these fields:
	* hashmap (h) -> for storing: key = "attribute_name" & value = [unique values]
	* hashmap (child_map) -> for storing the attribute's child: key = "unique value based on which split is made" 
	* 						& value = child attribute
	* leaf_value -> takes "p" or "e" only if the attribute is leaf. Used for checking if attribute is leaf.
	*/
	 HashMap<String, ArrayList<String>> h = new HashMap<String, ArrayList<String>>();
	 private HashMap<String, Attribute> child_map= new HashMap<String, Attribute>();
	 private HashMap< ArrayList<ArrayList<String>>, Attribute> parent_map = null;
	 private String leaf_value = "wrong";

	public String getLeaf_value() {
		return leaf_value;
	}

	public void setLeaf_value(String leaf_value) {
		this.leaf_value = leaf_value;
	}


	public HashMap<String, Attribute> getChild_map() {
		return child_map;
	}

	public void setChild_map(HashMap<String, Attribute> child_map) {
		this.child_map = child_map;
	}

	public HashMap<ArrayList<ArrayList<String>>, Attribute > getParent_map() {
		return parent_map;
	}

	public void setParent_map(
			HashMap< ArrayList<ArrayList<String>>, Attribute> parent_map) {
		this.parent_map = parent_map;
	}
	
	public void addChild_map(String s, Attribute a)
	{
		this.child_map.put(s, a);
	}

}
