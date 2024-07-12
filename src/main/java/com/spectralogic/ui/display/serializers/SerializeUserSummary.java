//===================================================================
// SerializeUserSummary.java
// 	Description:
// 		Turns the UserSummary variable into an output var for 
// 		display.
//===================================================================

package com.spectralogic.vail.vapir.ui.display.serializers;

import com.spectralogic.vail.vapir.model.OutputFormat;
import com.spectralogic.vail.vapir.model.UserSummary;

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
			line.setHeader(user_list.get(i).getType());
			line.setValue(null);
			output.add(line);

			line = new OutputFormat();
			line.setHeader(user_list.get(i).getType() + ">username");
			line.setValue(user_list.get(i).getName());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(user_list.get(i).getType() + ">account");
			line.setValue(user_list.get(i).getAccountName());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(user_list.get(i).getType() + ">account_id");
			line.setValue(user_list.get(i).getAccountId());
			output.add(line);

			// Add groups
			for(counter = 0; counter < user_list.get(i).groupCount(); counter++)
			{
				line = new OutputFormat();
				line.setHeader(user_list.get(i).getType() + ">group");
				line.setValue(user_list.get(i).group(counter));
				output.add(line);
			}
			
			// Add blank groups to round out the table.
			for(int j = counter; j < groupCount; j++)
			{
				line = new OutputFormat();
				line.setHeader(user_list.get(i).getType() + ">group");
				line.setValue("");
				line.setIndents(-1); // mark as blank
				output.add(line);
			}

			line = new OutputFormat();
			line.setHeader(user_list.get(i).getType() + ">status");
			line.setValue(user_list.get(i).getStatus());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(user_list.get(i).getType());
			line.setValue(null);
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
