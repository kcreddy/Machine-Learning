package com.machine;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author KrishnaChaitanyaReddy
 *
 */
public class Attribute {
	HashMap<String, ArrayList<String>> h = new HashMap<String, ArrayList<String>>();
	 ArrayList<Attribute> child_array;
	 private Attribute parent = new Attribute();
	 private String leaf_value = null;

	public ArrayList<Attribute> getChild_array() {
		return child_array;
	}

	public String getLeaf_value() {
		return leaf_value;
	}

	public void setLeaf_value(String leaf_value) {
		this.leaf_value = leaf_value;
	}

	public void setChild_array(ArrayList<Attribute> child_array) {
		this.child_array = child_array;
	}

	public Attribute getParent() {
		return parent;
	}

	public void setParent(Attribute parent) {
		this.parent = parent;
	}

/*
	 public ArrayList<String> get_unique_attr_values()
	{
		return h.get(h.keySet().toArray(new String[h.size()])[0]);	
	}*/
}
