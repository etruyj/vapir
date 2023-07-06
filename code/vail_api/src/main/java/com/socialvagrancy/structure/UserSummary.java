package com.socialvagrancy.vail.structures;

import java.util.ArrayList;

public class UserSummary extends Summary
{
	ArrayList<String> groups;


	public void addGroup(String group)
	{
		if(groups == null)
		{
			groups = new ArrayList<String>();
		}

		groups.add(group);
	}

	public int groupCount() 
	{
		if(groups == null)
	       	{
			return 0;
	    	}	      
	  
		return groups.size(); 
	}

	public String group(int i) { return groups.get(i); }
}
