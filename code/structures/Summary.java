package com.socialvagrancy.vail.structures;

import java.util.ArrayList;

public class Summary
{
	public String type;
	public String name;
	public String account_name;
	public String account_id;
	public String status = null;
	public ArrayList<String> groups;

	public void addGroup(String group)
	{
		if(groups == null)
		{
			groups = new ArrayList<String>();
		}

		groups.add(group);
	}
}
