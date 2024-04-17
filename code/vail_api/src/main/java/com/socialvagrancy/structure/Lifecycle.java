package com.socialvagrancy.vail.structures;

public class Lifecycle
{
	public String id;
	public String name;
	public String modified;
	// Setting markers to true to be in-line with sphere defaults.
	public boolean markers = true;
	public LifecycleRule[] rules;
	public String description;
	// Setting uploads to 7 to be in-line with sphere defaults.
	public int uploads = 7;

	//=======================================
	// Getters
	//=======================================
	
	public int ruleCount() { return rules.length; }
    public LifecycleRule[] getRules() { return rules; }
	public int storageCount(int r) { return rules[r].storageCount(); }
	public String storageID(int r, int s) { return rules[r].storageID(s); }

	//=======================================
	// Setters
	//=======================================

	public void clearDestinationCount(int r) { rules[r].clearDestinationCount(); }	
    public void setRules(LifecycleRule[] rules) { this.rules = rules; }
	public void setStorage(int r, int s, String sid) { rules[r].setStorage(s, sid); }
	
	public void setTypeMarkers()
	{
		// Sets the deletion flag based on the rule type.
		// The structure of the json is what actually determines
		// the rule type, not the string type.
		for(int i=0; i<ruleCount(); i++)
		{
			if(rules[i].type().equals("clone"))
			{
				rules[i].setDeletion(false);
			}
			else if(rules[i].type().equals("move"))
			{
				rules[i].setDeletion(true);
			}
			else if(rules[i].type().equals("expire"))
			{
				rules[i].setExpiration(true);
				rules[i].destinations = null;
			}
		}
	}
}
