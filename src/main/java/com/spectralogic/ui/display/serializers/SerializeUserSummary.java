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
import java.util.List;

public class SerializeUserSummary
{
	public static ArrayList<OutputFormat> forOutput(List<UserSummary> user_list)
	{
		ArrayList<OutputFormat> output = new ArrayList<OutputFormat>();
		OutputFormat line;

		int groupCount = maxGroupCount(user_list);
		int counter;

		for(UserSummary user : user_list)
		{
			line = new OutputFormat();
			line.setHeader(user.getType());
			line.setValue(null);
			output.add(line);

			line = new OutputFormat();
			line.setHeader(user.getType() + ">username");
			line.setValue(user.getName());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(user.getType() + ">account");
			line.setValue(user.getAccountName());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(user.getType() + ">account_id");
			line.setValue(user.getAccountId());
			output.add(line);

			// Add groups
			for(counter = 0; counter < user.groupCount(); counter++)
			{
				line = new OutputFormat();
				line.setHeader(user.getType() + ">group");
				line.setValue(user.group(counter));
				output.add(line);
			}
			
			// Add blank groups to round out the table.
			for(int j = counter; j < groupCount; j++)
			{
				line = new OutputFormat();
				line.setHeader(user.getType() + ">group");
				line.setValue("");
				line.setIndents(-1); // mark as blank
				output.add(line);
			}

			line = new OutputFormat();
			line.setHeader(user.getType() + ">status");
			line.setValue(user.getStatus());
			output.add(line);

			line = new OutputFormat();
			line.setHeader(user.getType());
			line.setValue(null);
			output.add(line);

		}

		return output;
	}

	//=======================================
	// Private Functions
	//=======================================
	
	private static int maxGroupCount(List<UserSummary> user_list)
	{
		// Determine the maximum number of groups required for 
		// formatting tables and CSV outputs.

		int maxGroups = 0;

		for(UserSummary user : user_list)
		{
			if(user.groupCount() > maxGroups)
			{
				maxGroups = user.groupCount();
			}
		}

		return maxGroups;
	}
}
