package com.socialvagrancy.vail.structures;

public class Lifecycle
{
	public String id;
	public String name;
	public String modified;
	public boolean markers;
	public LifecycleRule[] rules;
	public String description;
	public int uploads;

	//=======================================
	// Getters
	//=======================================
	
	public int ruleCount() { return rules.length; }
	public int storageCount(int r) { return rules[r].storageCount(); }
	public String storageID(int r, int s) { return rules[r].storageID(s); }

	//=======================================
	// Setters
	//=======================================

	public void clearDestinationCount(int r) { rules[r].clearDestinationCount(); }	
	public void setStorage(int r, int s, String sid) { rules[r].setStorage(s, sid); }
}
