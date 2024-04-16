//===================================================================
// SerializeUserSummary.java
// 	Description:
// 		Turns the UserSummary variable into an output var for 
// 		display.
//===================================================================

package com.socialvagrancy.vail.ui.display.serializers;

import com.socialvagrancy.vail.structures.OutputFormat;
import com.socialvagrancy.vail.structures.UserSummary;

import java.util.ArrayList;

public class SerializeUserSummary
{
	public static ArrayList<OutputFormat> forOutput(ArrayList<UserSummary> user_list)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line;

		int groupCount = maxGroupCount(user_list);
		int counter;

		for(int i=0; i < user_list.size(); i++)
		{
			line = new OutputFormat();
			line.header = user_list.get(i).type;
			line.value = null;
			output.add(line);

			line = new OutputFormat();
			line.header = user_list.get(i).type + ">username";
			line.value = user_list.get(i).name;
			output.add(line);

			line = new OutputFormat();
			line.header = user_list.get(i).type + ">account";
			line.value = user_list.get(i).account_name;
			output.add(line);

			line = new OutputFormat();
			line.header = user_list.get(i).type + ">account_id";
			line.value = user_list.get(i).account_id;
			output.add(line);

			// Add groups
			for(counter = 0; counter < user_list.get(i).groupCount(); counter++)
			{
				line = new OutputFormat();
				line.header = user_list.get(i).type + ">group";
				line.value = user_list.get(i).group(counter);
				output.add(line);
			}
			
			// Add blank groups to round out the table.
			for(int j = counter; j < groupCount; j++)
			{
				line = new OutputFormat();
				line.header = user_list.get(i).type + ">group";
				line.value = "";
				line.indents = -1; // mark as blank
				output.add(line);
			}

			line = new OutputFormat();
			line.header = user_list.get(i).type + ">status";
			line.value = user_list.get(i).status;
			output.add(line);

			line = new OutputFormat();
			line.header = user_list.get(i).type;
			line.value = null;
			output.add(line);

		}

		return output;
	}

	//=======================================
	// Private Functions
	//=======================================
	
	private static int maxGroupCount(ArrayList<UserSummary> user_list)
	{
		// Determine the maximum number of groups required for 
		// formatting tables and CSV outputs.

		int maxGroups = 0;

		for(int i=0; i<user_list.size(); i++)
		{
			if(user_list.get(i).groupCount() > maxGroups)
			{
				maxGroups = user_list.get(i).groupCount();
			}
		}

		return maxGroups;
	}
}
